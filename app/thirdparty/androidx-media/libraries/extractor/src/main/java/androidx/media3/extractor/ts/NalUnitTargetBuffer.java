/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ts;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import java.util.Arrays;

/**
 * A buffer that fills itself with data corresponding to a specific NAL unit, as it is encountered
 * in the stream.
 */
@UnstableApi
public final class NalUnitTargetBuffer {

  private final int targetType;

  private boolean isFilling;
  private boolean isCompleted;

  public byte[] nalData;
  public int nalLength;

  /**
   * Creates the buffer.
   *
   * @param targetType The type of the NAL unit.
   * @param initialCapacity The initial capacity of the buffer in bytes.
   */
  public NalUnitTargetBuffer(int targetType, int initialCapacity) {
    this.targetType = targetType;

    // Initialize data with a start code in the first three bytes.
    nalData = new byte[3 + initialCapacity];
    nalData[2] = 1;
  }

  /** Resets the buffer, clearing any data that it holds. */
  public void reset() {
    isFilling = false;
    isCompleted = false;
  }

  /** Returns whether the buffer currently holds a complete NAL unit of the target type. */
  public boolean isCompleted() {
    return isCompleted;
  }

  /**
   * Called to indicate that a NAL unit has started.
   *
   * @param type The type of the NAL unit.
   */
  public void startNalUnit(int type) {
    Assertions.checkState(!isFilling);
    isFilling = type == targetType;
    if (isFilling) {
      // Skip the three byte start code when writing data.
      nalLength = 3;
      isCompleted = false;
    }
  }

  /**
   * Called to pass stream data. The data passed should not include the 3 byte start code.
   *
   * @param data Holds the data being passed.
   * @param offset The offset of the data in {@code data}.
   * @param limit The limit (exclusive) of the data in {@code data}.
   */
  public void appendToNalUnit(byte[] data, int offset, int limit) {
    if (!isFilling) {
      return;
    }
    int readLength = limit - offset;
    if (nalData.length < nalLength + readLength) {
      nalData = Arrays.copyOf(nalData, (nalLength + readLength) * 2);
    }
    System.arraycopy(data, offset, nalData, nalLength, readLength);
    nalLength += readLength;
  }

  /**
   * Called to indicate that a NAL unit has ended.
   *
   * @param discardPadding The number of excess bytes that were passed to {@link
   *     #appendToNalUnit(byte[], int, int)}, which should be discarded.
   * @return Whether the ended NAL unit is of the target type.
   */
  public boolean endNalUnit(int discardPadding) {
    if (!isFilling) {
      return false;
    }
    nalLength -= discardPadding;
    isFilling = false;
    isCompleted = true;
    return true;
  }
}
