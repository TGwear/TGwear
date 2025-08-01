/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.mp4;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;
import com.google.common.primitives.Floats;

/**
 * Stores metadata from the Samsung smta box.
 *
 * <p>See [Internal: b/150138465#comment76], [Internal: b/301273734#comment17].
 */
@UnstableApi
public final class SmtaMetadataEntry implements Metadata.Entry {

  /**
   * The capture frame rate, in fps, or {@link C#RATE_UNSET} if it is unknown.
   *
   * <p>If known, the capture frame rate should always be an integer value.
   */
  public final float captureFrameRate;

  /** The number of layers in the SVC extended frames. */
  public final int svcTemporalLayerCount;

  /** Creates an instance. */
  public SmtaMetadataEntry(float captureFrameRate, int svcTemporalLayerCount) {
    this.captureFrameRate = captureFrameRate;
    this.svcTemporalLayerCount = svcTemporalLayerCount;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    SmtaMetadataEntry other = (SmtaMetadataEntry) obj;
    return captureFrameRate == other.captureFrameRate
        && svcTemporalLayerCount == other.svcTemporalLayerCount;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + Floats.hashCode(captureFrameRate);
    result = 31 * result + svcTemporalLayerCount;
    return result;
  }

  @Override
  public String toString() {
    return "smta: captureFrameRate="
        + captureFrameRate
        + ", svcTemporalLayerCount="
        + svcTemporalLayerCount;
  }
}
