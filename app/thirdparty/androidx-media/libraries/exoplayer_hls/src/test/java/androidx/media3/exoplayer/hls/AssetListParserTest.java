/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.datasource.ByteArrayDataSource;
import androidx.media3.exoplayer.hls.AssetListParser.Asset;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AssetListParserTest {

  @Test
  public void load() throws IOException {
    byte[] assetListBytes =
        ("{\"ASSETS\": [ "
                + "{\"URI\": \"http://1\", \"DURATION\":1.23},"
                + "{\"URI\": \"http://2\", \"DURATION\":2.34}"
                + "] }")
            .getBytes(Charset.defaultCharset());
    ParsingLoadable<AssetListParser.AssetList> parsingLoadable =
        new ParsingLoadable<>(
            new ByteArrayDataSource(assetListBytes),
            Uri.EMPTY,
            C.DATA_TYPE_AD,
            new AssetListParser());

    parsingLoadable.load();

    assertThat(parsingLoadable.getResult().assets)
        .containsExactly(
            new Asset(Uri.parse("http://1"), /* durationUs= */ 1_230_000L),
            new Asset(Uri.parse("http://2"), /* durationUs= */ 2_340_000L))
        .inOrder();
  }

  @Test
  public void load_fileWithDisturbingJsonJunk_parsesCorrectly() throws IOException {
    byte[] assetListBytes =
        TestUtil.getByteArray(
            ApplicationProvider.getApplicationContext(),
            "media/hls/interstitials/x_asset_list_mixed_elements.json");
    ParsingLoadable<AssetListParser.AssetList> parsingLoadable =
        new ParsingLoadable<>(
            new ByteArrayDataSource(assetListBytes),
            Uri.EMPTY,
            C.DATA_TYPE_AD,
            new AssetListParser());

    parsingLoadable.load();

    assertThat(parsingLoadable.getResult().assets)
        .containsExactly(
            new Asset(Uri.parse("http://1"), 12_123_000L),
            new Asset(Uri.parse("http://2"), 22_123_000L),
            new Asset(Uri.parse("http://3"), 32_122_999L),
            new Asset(Uri.parse("http://4"), 42_123_000L))
        .inOrder();
    assertThat(parsingLoadable.getResult().stringAttributes)
        .containsExactly(
            new AssetListParser.StringAttribute("foo", "foo"),
            new AssetListParser.StringAttribute("fooBar", "fooBar"),
            new AssetListParser.StringAttribute("ASSETS", "stringValue"))
        .inOrder();
  }

  @Test
  public void load_withJsonArrayAsRoot_emptyResult() throws IOException {
    byte[] assetListBytes = "[]".getBytes(Charset.defaultCharset());
    ParsingLoadable<AssetListParser.AssetList> parsingLoadable =
        new ParsingLoadable<>(
            new ByteArrayDataSource(assetListBytes),
            Uri.EMPTY,
            C.DATA_TYPE_AD,
            new AssetListParser());

    parsingLoadable.load();

    assertThat(parsingLoadable.getResult().assets).isEmpty();
    assertThat(parsingLoadable.getResult().stringAttributes).isEmpty();
  }

  @Test
  public void load_emptyInputStream_throwsEOFException() throws IOException {
    ParsingLoadable<AssetListParser.AssetList> parsingLoadable =
        new ParsingLoadable<>(
            new ByteArrayDataSource(" ".getBytes(Charset.defaultCharset())),
            Uri.EMPTY,
            C.DATA_TYPE_AD,
            new AssetListParser());

    assertThrows(EOFException.class, parsingLoadable::load);
  }
}
