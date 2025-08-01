/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.icy;

import static androidx.media3.test.utils.TestUtil.createByteArray;
import static androidx.media3.test.utils.TestUtil.createMetadataInputBuffer;
import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertThrows;

import androidx.media3.common.Metadata;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.primitives.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test for {@link IcyDecoder}. */
@RunWith(AndroidJUnit4.class)
public final class IcyDecoderTest {

  private final IcyDecoder decoder = new IcyDecoder();

  @Test
  public void decode() {
    byte[] icyContent = "StreamTitle='test title';StreamURL='test_url';".getBytes(UTF_8);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isEqualTo("test title");
    assertThat(streamInfo.url).isEqualTo("test_url");
  }

  @Test
  // Check the decoder is reading MetadataInputBuffer.data.limit() correctly.
  public void decode_respectsLimit() {
    byte[] icyTitle = "StreamTitle='test title';".getBytes(UTF_8);
    byte[] icyUrl = "StreamURL='test_url';".getBytes(UTF_8);
    byte[] paddedRawBytes = Bytes.concat(icyTitle, icyUrl);
    MetadataInputBuffer metadataBuffer = createMetadataInputBuffer(paddedRawBytes);
    // Stop before the stream URL.
    metadataBuffer.data.limit(icyTitle.length);
    Metadata metadata = decoder.decode(metadataBuffer);

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyTitle);
    assertThat(streamInfo.title).isEqualTo("test title");
    assertThat(streamInfo.url).isNull();
  }

  @Test
  public void decode_titleOnly() {
    byte[] icyContent = "StreamTitle='test title';".getBytes(UTF_8);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isEqualTo("test title");
    assertThat(streamInfo.url).isNull();
  }

  @Test
  public void decode_extraTags() {
    byte[] icyContent =
        "StreamTitle='test title';StreamURL='test_url';CustomTag|withWeirdSeparator"
            .getBytes(UTF_8);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isEqualTo("test title");
    assertThat(streamInfo.url).isEqualTo("test_url");
  }

  @Test
  public void decode_emptyTitle() {
    byte[] icyContent = "StreamTitle='';StreamURL='test_url';".getBytes(UTF_8);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isEmpty();
    assertThat(streamInfo.url).isEqualTo("test_url");
  }

  @Test
  public void decode_semiColonInTitle() {
    byte[] icyContent = "StreamTitle='test; title';StreamURL='test_url';".getBytes(UTF_8);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isEqualTo("test; title");
    assertThat(streamInfo.url).isEqualTo("test_url");
  }

  @Test
  public void decode_quoteInTitle() {
    byte[] icyContent = "StreamTitle='test' title';StreamURL='test_url';".getBytes(UTF_8);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isEqualTo("test' title");
    assertThat(streamInfo.url).isEqualTo("test_url");
  }

  @Test
  public void decode_lineTerminatorInTitle() {
    byte[] icyContent = "StreamTitle='test\r\ntitle';StreamURL='test_url';".getBytes(UTF_8);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isEqualTo("test\r\ntitle");
    assertThat(streamInfo.url).isEqualTo("test_url");
  }

  @Test
  public void decode_iso885911() {
    // Create an invalid UTF-8 string by using 'é'.
    byte[] icyContent = "StreamTitle='tést';StreamURL='tést_url';".getBytes(ISO_8859_1);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isEqualTo("tést");
    assertThat(streamInfo.url).isEqualTo("tést_url");
  }

  @Test
  public void decode_unrecognisedEncoding() {
    // Create an invalid UTF-8 and ISO-88591-1 string by using 'é'.
    byte[] icyContent = "StreamTitle='tést';StreamURL='tést_url';".getBytes(UTF_16);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isNull();
    assertThat(streamInfo.url).isNull();
  }

  @Test
  public void decode_noRecognisedHeaders() {
    byte[] icyContent = "NotIcyData".getBytes(UTF_8);

    Metadata metadata = decoder.decode(createMetadataInputBuffer(icyContent));

    assertThat(metadata.length()).isEqualTo(1);
    IcyInfo streamInfo = (IcyInfo) metadata.get(0);
    assertThat(streamInfo.rawMetadata).isEqualTo(icyContent);
    assertThat(streamInfo.title).isNull();
    assertThat(streamInfo.url).isNull();
  }

  @Test
  public void decode_failsIfPositionNonZero() {
    MetadataInputBuffer buffer = createMetadataInputBuffer(createByteArray(1, 2, 3));
    buffer.data.position(1);

    assertThrows(IllegalArgumentException.class, () -> decoder.decode(buffer));
  }

  @Test
  public void decode_failsIfBufferHasNoArray() {
    MetadataInputBuffer buffer = createMetadataInputBuffer(createByteArray(1, 2, 3));
    buffer.data = Util.createReadOnlyByteBuffer(buffer.data);

    assertThrows(IllegalArgumentException.class, () -> decoder.decode(buffer));
  }

  @Test
  public void decode_failsIfArrayOffsetNonZero() {
    MetadataInputBuffer buffer = createMetadataInputBuffer(createByteArray(1, 2, 3));
    buffer.data.position(1);
    buffer.data = buffer.data.slice();

    assertThrows(IllegalArgumentException.class, () -> decoder.decode(buffer));
  }
}
