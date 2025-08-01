/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import androidx.annotation.Nullable;
import androidx.media3.common.Metadata;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.nio.ByteBuffer;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link SimpleMetadataDecoder}. */
@RunWith(AndroidJUnit4.class)
public class SimpleMetadataDecoderTest {

  @Test
  public void decode_nullDataInputBuffer_throwsNullPointerException() {
    TestSimpleMetadataDecoder decoder = new TestSimpleMetadataDecoder();
    MetadataInputBuffer nullDataInputBuffer = new MetadataInputBuffer();
    nullDataInputBuffer.data = null;

    assertThrows(NullPointerException.class, () -> decoder.decode(nullDataInputBuffer));
    assertThat(decoder.decodeWasCalled).isFalse();
  }

  @Test
  public void decode_directDataInputBuffer_throwsIllegalArgumentException() {
    TestSimpleMetadataDecoder decoder = new TestSimpleMetadataDecoder();
    MetadataInputBuffer directDataInputBuffer = new MetadataInputBuffer();
    directDataInputBuffer.data = ByteBuffer.allocateDirect(8);

    assertThrows(IllegalArgumentException.class, () -> decoder.decode(directDataInputBuffer));
    assertThat(decoder.decodeWasCalled).isFalse();
  }

  @Test
  public void decode_nonZeroPositionDataInputBuffer_throwsIllegalArgumentException() {
    TestSimpleMetadataDecoder decoder = new TestSimpleMetadataDecoder();
    MetadataInputBuffer nonZeroPositionDataInputBuffer = new MetadataInputBuffer();
    nonZeroPositionDataInputBuffer.data = ByteBuffer.wrap(new byte[8]);
    nonZeroPositionDataInputBuffer.data.position(1);

    assertThrows(
        IllegalArgumentException.class, () -> decoder.decode(nonZeroPositionDataInputBuffer));
    assertThat(decoder.decodeWasCalled).isFalse();
  }

  @Test
  public void decode_nonZeroOffsetDataInputBuffer_throwsIllegalArgumentException() {
    TestSimpleMetadataDecoder decoder = new TestSimpleMetadataDecoder();
    MetadataInputBuffer directDataInputBuffer = new MetadataInputBuffer();
    directDataInputBuffer.data = ByteBuffer.wrap(new byte[8], /* offset= */ 4, /* length= */ 4);

    assertThrows(IllegalArgumentException.class, () -> decoder.decode(directDataInputBuffer));
    assertThat(decoder.decodeWasCalled).isFalse();
  }

  @Test
  public void decode_returnsDecodeInternalResult() {
    TestSimpleMetadataDecoder decoder = new TestSimpleMetadataDecoder();
    MetadataInputBuffer buffer = new MetadataInputBuffer();
    buffer.data = ByteBuffer.wrap(new byte[8]);

    assertThat(decoder.decode(buffer)).isSameInstanceAs(decoder.result);
    assertThat(decoder.decodeWasCalled).isTrue();
  }

  private static final class TestSimpleMetadataDecoder extends SimpleMetadataDecoder {

    public final Metadata result;

    public boolean decodeWasCalled;

    public TestSimpleMetadataDecoder() {
      result = new Metadata();
    }

    @Nullable
    @Override
    protected Metadata decode(MetadataInputBuffer inputBuffer, ByteBuffer buffer) {
      decodeWasCalled = true;
      return result;
    }
  }
}
