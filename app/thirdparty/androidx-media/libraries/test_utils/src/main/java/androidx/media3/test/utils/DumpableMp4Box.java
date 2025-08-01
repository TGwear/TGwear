/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import com.google.common.collect.ImmutableSet;
import java.nio.ByteBuffer;

/** Wraps an Mp4 box to allow dumping it. */
@UnstableApi
public final class DumpableMp4Box implements Dumper.Dumpable {
  private static final ImmutableSet<String> CONTAINER_BOXES =
      ImmutableSet.of(
          "moov", "trak", "mdia", "minf", "stbl", "edts", "meta", "mvex", "moof", "traf", "axte");
  private final ParsableByteArray box;

  /***
   * Creates an instance.
   *
   * @param box The Mp4 box to wrap.
   */
  public DumpableMp4Box(ByteBuffer box) {
    byte[] boxArray = new byte[box.remaining()];
    box.get(boxArray);
    this.box = new ParsableByteArray(boxArray);
  }

  @Override
  public void dump(Dumper dumper) {
    dumpBoxRecursively(box.limit(), dumper);
  }

  private void dumpBoxRecursively(long endPosition, Dumper dumper) {
    while (box.getPosition() < endPosition) {
      long size = box.readInt();
      String name = box.readString(/* length= */ 4);
      long payloadSize = size - 8;
      // When the box needs 64-bit box size (typically used for mdat box), then the original 32-bit
      // box size is set to 1.
      if (size == 1) {
        size = box.readUnsignedLongToLong();
        // Parsing is not supported for box having size > Integer.MAX_VALUE.
        Assertions.checkState(size <= Integer.MAX_VALUE);
        // Subtract 4 bytes (32-bit box size) + 4 bytes (box name) + 8 bytes (64-bit box size).
        payloadSize = size - 16;
      }
      dumper.startBlock(name + " (" + size + " bytes)");
      if (CONTAINER_BOXES.contains(name)) {
        dumpBoxRecursively(box.getPosition() + payloadSize, dumper);
      } else {
        byte[] data = new byte[(int) payloadSize];
        box.readBytes(data, /* offset= */ 0, (int) payloadSize);
        dumper.add("Data", data);
      }
      dumper.endBlock();
    }
  }
}
