/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.ParserException;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.io.Files;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import java.io.File;
import java.util.concurrent.ExecutionException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DataSourceBitmapLoader}.
 *
 * <p>This test needs to run as an androidTest because robolectric's BitmapFactory is not fully
 * functional.
 */
@RunWith(AndroidJUnit4.class)
public class DataSourceBitmapLoaderTest {

  @Rule public final TemporaryFolder tempFolder = new TemporaryFolder();

  private static final String TEST_IMAGE_FOLDER = "media/jpeg/";
  private static final String TEST_IMAGE_PATH =
      TEST_IMAGE_FOLDER + "non-motion-photo-shortened-no-exif.jpg";

  private DataSource.Factory dataSourceFactory;

  @Before
  public void setUp() {
    dataSourceFactory = new DefaultDataSource.Factory(ApplicationProvider.getApplicationContext());
  }

  @Test
  public void decodeBitmap_withValidData_loadsCorrectData() throws Exception {
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), TEST_IMAGE_PATH);

    Bitmap bitmap = bitmapLoader.decodeBitmap(imageData).get();

    assertThat(
            bitmap.sameAs(
                BitmapFactory.decodeByteArray(imageData, /* offset= */ 0, imageData.length)))
        .isTrue();
  }

  @Test
  public void decodeBitmap_withExifRotation_loadsCorrectData() throws Exception {
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);
    byte[] imageData =
        TestUtil.getByteArray(
            ApplicationProvider.getApplicationContext(),
            TEST_IMAGE_FOLDER + "non-motion-photo-shortened.jpg");
    Bitmap bitmapWithoutRotation =
        BitmapFactory.decodeByteArray(imageData, /* offset= */ 0, imageData.length);
    Matrix rotationMatrix = new Matrix();
    rotationMatrix.postRotate(/* degrees= */ 90);
    Bitmap expectedBitmap =
        Bitmap.createBitmap(
            bitmapWithoutRotation,
            /* x= */ 0,
            /* y= */ 0,
            bitmapWithoutRotation.getWidth(),
            bitmapWithoutRotation.getHeight(),
            rotationMatrix,
            /* filter= */ false);

    Bitmap actualBitmap = bitmapLoader.decodeBitmap(imageData).get();

    assertThat(actualBitmap.sameAs(expectedBitmap)).isTrue();
  }

  @Test
  public void decodeBitmap_withInvalidData_throws() {
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);

    ListenableFuture<Bitmap> future = bitmapLoader.decodeBitmap(new byte[0]);

    assertException(
        future::get, ParserException.class, /* messagePart= */ "Could not decode image data");
  }

  @Test
  public void loadBitmap_withHttpUri_loadsCorrectData() throws Exception {
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), TEST_IMAGE_PATH);
    Buffer responseBody = new Buffer().write(imageData);
    Bitmap bitmap;
    try (MockWebServer mockWebServer = new MockWebServer()) {
      mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(responseBody));

      bitmap = bitmapLoader.loadBitmap(Uri.parse(mockWebServer.url("test_path").toString())).get();
    }

    assertThat(
            bitmap.sameAs(
                BitmapFactory.decodeByteArray(imageData, /* offset= */ 0, imageData.length)))
        .isTrue();
  }

  @Test
  public void loadBitmap_httpUriAndServerError_throws() throws Exception {
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);
    ListenableFuture<Bitmap> future;
    try (MockWebServer mockWebServer = new MockWebServer()) {
      mockWebServer.enqueue(new MockResponse().setResponseCode(404));

      future = bitmapLoader.loadBitmap(Uri.parse(mockWebServer.url("test_path").toString()));
    }

    assertException(
        future::get, HttpDataSource.InvalidResponseCodeException.class, /* messagePart= */ "404");
  }

  @Test
  public void loadBitmap_assetUri_loadsCorrectData() throws Exception {
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), TEST_IMAGE_PATH);

    Bitmap bitmap = bitmapLoader.loadBitmap(Uri.parse("asset:///" + TEST_IMAGE_PATH)).get();

    assertThat(
            bitmap.sameAs(
                BitmapFactory.decodeByteArray(imageData, /* offset= */ 0, imageData.length)))
        .isTrue();
  }

  @Test
  public void loadBitmap_assetUriWithAssetNotExisting_throws() {
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);

    assertException(
        () -> bitmapLoader.loadBitmap(Uri.parse("asset:///not_valid/path/image.bmp")).get(),
        AssetDataSource.AssetDataSourceException.class,
        /* messagePart= */ "");
  }

  @Test
  public void loadBitmap_withFileUri_loadsCorrectData() throws Exception {
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), TEST_IMAGE_PATH);
    File file = tempFolder.newFile();
    Files.write(imageData, file);
    Uri uri = Uri.fromFile(file);
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);

    Bitmap bitmap = bitmapLoader.loadBitmap(uri).get();

    assertThat(
            bitmap.sameAs(
                BitmapFactory.decodeByteArray(imageData, /* offset= */ 0, imageData.length)))
        .isTrue();
  }

  @Test
  public void loadBitmap_withFileUriAndOptions_loadsDataWithRespectToOptions() throws Exception {
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), TEST_IMAGE_PATH);
    File file = tempFolder.newFile();
    Files.write(imageData, file);
    Uri uri = Uri.fromFile(file);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inMutable = true;
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(
            MoreExecutors.newDirectExecutorService(), dataSourceFactory, options);

    Bitmap bitmap = bitmapLoader.loadBitmap(uri).get();

    assertThat(bitmap.isMutable()).isTrue();
  }

  @Test
  public void loadBitmap_withFileUriAndMaxOutputDimension_loadsDataWithSmallerSize()
      throws Exception {
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), TEST_IMAGE_PATH);
    File file = tempFolder.newFile();
    Files.write(imageData, file);
    Uri uri = Uri.fromFile(file);
    int maximumOutputDimension = 2000;
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(
            MoreExecutors.newDirectExecutorService(),
            dataSourceFactory,
            /* options= */ null,
            maximumOutputDimension);

    Bitmap bitmap = bitmapLoader.loadBitmap(uri).get();

    assertThat(bitmap.getWidth()).isAtMost(maximumOutputDimension);
    assertThat(bitmap.getHeight()).isAtMost(maximumOutputDimension);
  }

  @Test
  public void loadBitmap_fileUriWithFileNotExisting_throws() {
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);

    assertException(
        () -> bitmapLoader.loadBitmap(Uri.parse("file:///not_valid/path/image.bmp")).get(),
        FileDataSource.FileDataSourceException.class,
        /* messagePart= */ "No such file or directory");
  }

  @Test
  public void loadBitmapFromMetadata_withArtworkDataAndArtworkUriSet_decodeFromArtworkData()
      throws Exception {
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), TEST_IMAGE_PATH);
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);
    try (MockWebServer mockWebServer = new MockWebServer()) {
      Uri uri = Uri.parse(mockWebServer.url("test_path").toString());
      MediaMetadata metadata =
          new MediaMetadata.Builder()
              .setArtworkData(imageData, MediaMetadata.PICTURE_TYPE_FRONT_COVER)
              .setArtworkUri(uri)
              .build();

      Bitmap bitmap = bitmapLoader.loadBitmapFromMetadata(metadata).get();

      assertThat(
              bitmap.sameAs(
                  BitmapFactory.decodeByteArray(imageData, /* offset= */ 0, imageData.length)))
          .isTrue();
      assertThat(mockWebServer.getRequestCount()).isEqualTo(0);
    }
  }

  @Test
  public void loadBitmapFromMetadata_withArtworkUriSet_loadFromArtworkUri() throws Exception {
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), TEST_IMAGE_PATH);
    Buffer responseBody = new Buffer().write(imageData);
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);
    try (MockWebServer mockWebServer = new MockWebServer()) {
      mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(responseBody));
      Uri uri = Uri.parse(mockWebServer.url("test_path").toString());
      MediaMetadata metadata = new MediaMetadata.Builder().setArtworkUri(uri).build();

      Bitmap bitmap = bitmapLoader.loadBitmapFromMetadata(metadata).get();

      assertThat(
              bitmap.sameAs(
                  BitmapFactory.decodeByteArray(imageData, /* offset= */ 0, imageData.length)))
          .isTrue();
      assertThat(mockWebServer.getRequestCount()).isEqualTo(1);
    }
  }

  @Test
  public void loadBitmapFromMetadata_withArtworkDataAndArtworkUriUnset_returnNull() {
    MediaMetadata metadata = new MediaMetadata.Builder().build();
    DataSourceBitmapLoader bitmapLoader =
        new DataSourceBitmapLoader(MoreExecutors.newDirectExecutorService(), dataSourceFactory);

    ListenableFuture<Bitmap> bitmapFuture = bitmapLoader.loadBitmapFromMetadata(metadata);

    assertThat(bitmapFuture).isNull();
  }

  private static void assertException(
      ThrowingRunnable runnable, Class<? extends Exception> clazz, String messagePart) {
    ExecutionException executionException = assertThrows(ExecutionException.class, runnable);
    assertThat(executionException).hasCauseThat().isInstanceOf(clazz);
    assertThat(executionException).hasMessageThat().contains(messagePart);
  }
}
