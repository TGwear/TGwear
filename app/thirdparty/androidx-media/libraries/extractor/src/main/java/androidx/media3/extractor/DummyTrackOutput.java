/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/**
 * @deprecated Use {@link DiscardingTrackOutput} instead.
 */
@UnstableApi
@Deprecated
public final class DummyTrackOutput implements TrackOutput {

  private final DiscardingTrackOutput discardingTrackOutput;

  public DummyTrackOutput() {
    discardingTrackOutput = new DiscardingTrackOutput();
  }

  @Override
  public void format(Format format) {
    discardingTrackOutput.format(format);
  }

  @Override
  public int sampleData(DataReader input, int length, boolean allowEndOfInput) throws IOException {
    return discardingTrackOutput.sampleData(input, length, allowEndOfInput);
  }

  @Override
  public void sampleData(ParsableByteArray data, int length) {
    discardingTrackOutput.sampleData(data, length);
  }

  @Override
  public int sampleData(
      DataReader input, int length, boolean allowEndOfInput, @SampleDataPart int sampleDataPart)
      throws IOException {
    return discardingTrackOutput.sampleData(input, length, allowEndOfInput, sampleDataPart);
  }

  @Override
  public void sampleData(ParsableByteArray data, int length, @SampleDataPart int sampleDataPart) {
    discardingTrackOutput.sampleData(data, length, sampleDataPart);
  }

  @Override
  public void sampleMetadata(
      long timeUs,
      @C.BufferFlags int flags,
      int size,
      int offset,
      @Nullable CryptoData cryptoData) {
    discardingTrackOutput.sampleMetadata(timeUs, flags, size, offset, cryptoData);
  }
}
