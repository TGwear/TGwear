/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import static androidx.media3.common.util.Assertions.checkState;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/**
 * Rechunks TrueHD sample data into groups of {@link Ac3Util#TRUEHD_RECHUNK_SAMPLE_COUNT} samples.
 */
@UnstableApi
public final class TrueHdSampleRechunker {

  private final byte[] syncframePrefix;

  private boolean foundSyncframe;
  private int chunkSampleCount;
  private long chunkTimeUs;
  private @C.BufferFlags int chunkFlags;
  private int chunkSize;
  private int chunkOffset;

  public TrueHdSampleRechunker() {
    syncframePrefix = new byte[Ac3Util.TRUEHD_SYNCFRAME_PREFIX_LENGTH];
  }

  public void reset() {
    foundSyncframe = false;
    chunkSampleCount = 0;
  }

  public void startSample(ExtractorInput input) throws IOException {
    if (foundSyncframe) {
      return;
    }
    input.peekFully(syncframePrefix, 0, Ac3Util.TRUEHD_SYNCFRAME_PREFIX_LENGTH);
    input.resetPeekPosition();
    if (Ac3Util.parseTrueHdSyncframeAudioSampleCount(syncframePrefix) == 0) {
      return;
    }
    foundSyncframe = true;
  }

  public void sampleMetadata(
      TrackOutput trackOutput,
      long timeUs,
      @C.BufferFlags int flags,
      int size,
      int offset,
      @Nullable TrackOutput.CryptoData cryptoData) {
    checkState(
        chunkOffset <= size + offset,
        "TrueHD chunk samples must be contiguous in the sample queue.");
    if (!foundSyncframe) {
      return;
    }
    if (chunkSampleCount++ == 0) {
      // This is the first sample in the chunk.
      chunkTimeUs = timeUs;
      chunkFlags = flags;
      chunkSize = 0;
    }
    chunkSize += size;
    chunkOffset = offset; // The offset is to the end of the sample.
    if (chunkSampleCount >= Ac3Util.TRUEHD_RECHUNK_SAMPLE_COUNT) {
      outputPendingSampleMetadata(trackOutput, cryptoData);
    }
  }

  public void outputPendingSampleMetadata(
      TrackOutput trackOutput, @Nullable TrackOutput.CryptoData cryptoData) {
    if (chunkSampleCount > 0) {
      trackOutput.sampleMetadata(chunkTimeUs, chunkFlags, chunkSize, chunkOffset, cryptoData);
      chunkSampleCount = 0;
    }
  }
}
