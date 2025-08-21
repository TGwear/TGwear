/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.id3;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import java.util.Arrays;

/** MPEG location lookup table frame. */
@UnstableApi
public final class MlltFrame extends Id3Frame {

  public static final String ID = "MLLT";

  public final int mpegFramesBetweenReference;
  public final int bytesBetweenReference;
  public final int millisecondsBetweenReference;
  public final int[] bytesDeviations;
  public final int[] millisecondsDeviations;

  public MlltFrame(
      int mpegFramesBetweenReference,
      int bytesBetweenReference,
      int millisecondsBetweenReference,
      int[] bytesDeviations,
      int[] millisecondsDeviations) {
    super(ID);
    this.mpegFramesBetweenReference = mpegFramesBetweenReference;
    this.bytesBetweenReference = bytesBetweenReference;
    this.millisecondsBetweenReference = millisecondsBetweenReference;
    this.bytesDeviations = bytesDeviations;
    this.millisecondsDeviations = millisecondsDeviations;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    MlltFrame other = (MlltFrame) obj;
    return mpegFramesBetweenReference == other.mpegFramesBetweenReference
        && bytesBetweenReference == other.bytesBetweenReference
        && millisecondsBetweenReference == other.millisecondsBetweenReference
        && Arrays.equals(bytesDeviations, other.bytesDeviations)
        && Arrays.equals(millisecondsDeviations, other.millisecondsDeviations);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + mpegFramesBetweenReference;
    result = 31 * result + bytesBetweenReference;
    result = 31 * result + millisecondsBetweenReference;
    result = 31 * result + Arrays.hashCode(bytesDeviations);
    result = 31 * result + Arrays.hashCode(millisecondsDeviations);
    return result;
  }
}
