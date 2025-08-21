/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.image;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.robolectric.annotation.GraphicsMode.Mode.NATIVE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.nio.ByteBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.GraphicsMode;

/** Unit tests for {@link BitmapFactoryImageDecoder}. */
@RunWith(AndroidJUnit4.class)
@GraphicsMode(value = NATIVE)
public class BitmapFactoryImageDecoderTest {

  private static final String PNG_TEST_IMAGE_PATH = "media/png/non-motion-photo-shortened.png";
  private static final String JPEG_TEST_IMAGE_PATH = "media/jpeg/non-motion-photo-shortened.jpg";

  private BitmapFactoryImageDecoder decoder;
  private DecoderInputBuffer inputBuffer;
  private ImageOutputBuffer outputBuffer;

  @Before
  public void setUp() {
    decoder = new BitmapFactoryImageDecoder.Factory().createImageDecoder();
    inputBuffer = decoder.createInputBuffer();
    outputBuffer = decoder.createOutputBuffer();
  }

  @After
  public void tearDown() {
    decoder.release();
  }

  @Test
  public void decode_png_loadsCorrectData() throws Exception {
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), PNG_TEST_IMAGE_PATH);

    Bitmap bitmap = decode(imageData);

    assertThat(
            bitmap.sameAs(
                BitmapFactory.decodeByteArray(imageData, /* offset= */ 0, imageData.length)))
        .isTrue();
  }

  @Test
  public void decode_jpegWithExifRotation_loadsCorrectData() throws Exception {
    byte[] imageData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), JPEG_TEST_IMAGE_PATH);
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

    Bitmap actualBitmap = decode(imageData);

    assertThat(actualBitmap.sameAs(expectedBitmap)).isTrue();
  }

  @Test
  public void decodeBitmap_withInvalidData_throws() throws ImageDecoderException {
    assertThrows(ImageDecoderException.class, () -> decode(new byte[1]));
  }

  private Bitmap decode(byte[] data) throws Exception {
    inputBuffer.data = ByteBuffer.wrap(data);
    Exception e = decoder.decode(inputBuffer, outputBuffer, /* reset= */ false);
    if (e != null) {
      throw e;
    }
    return checkNotNull(outputBuffer.bitmap);
  }
}
