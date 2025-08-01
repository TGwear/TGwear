/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.muxer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Utilities for dealing with MP4 boxes. */
/* package */ final class BoxUtils {
  private static final int BOX_TYPE_BYTES = 4;
  private static final int BOX_SIZE_BYTES = 4;

  private BoxUtils() {}

  /** Wraps content into a box, prefixing it with a length and a box type. */
  public static ByteBuffer wrapIntoBox(String boxType, ByteBuffer contents) {
    byte[] typeByteArray = boxType.getBytes(StandardCharsets.UTF_8);
    return wrapIntoBox(typeByteArray, contents);
  }

  /**
   * Wraps content into a box, prefixing it with a length and a box type.
   *
   * <p>Use this method for box types with special characters. For example location box, which has a
   * copyright symbol in the beginning.
   */
  public static ByteBuffer wrapIntoBox(byte[] boxType, ByteBuffer contents) {
    ByteBuffer box = ByteBuffer.allocate(contents.remaining() + BOX_TYPE_BYTES + BOX_SIZE_BYTES);
    box.putInt(contents.remaining() + BOX_TYPE_BYTES + BOX_SIZE_BYTES);
    box.put(boxType, /* offset= */ 0, BOX_TYPE_BYTES);
    box.put(contents);
    box.flip();
    return box;
  }

  /** Concatenate multiple boxes into a box, prefixing it with a length and a box type. */
  public static ByteBuffer wrapBoxesIntoBox(String boxType, List<ByteBuffer> boxes) {
    int totalSize = BOX_TYPE_BYTES + BOX_SIZE_BYTES;
    for (int i = 0; i < boxes.size(); i++) {
      totalSize += boxes.get(i).remaining();
    }

    ByteBuffer result = ByteBuffer.allocate(totalSize);
    result.putInt(totalSize);
    result.put(boxType.getBytes(StandardCharsets.UTF_8), 0, BOX_TYPE_BYTES);

    for (int i = 0; i < boxes.size(); i++) {
      result.put(boxes.get(i));
    }

    result.flip();
    return result;
  }

  /**
   * Concatenates multiple {@linkplain ByteBuffer byte buffers} into a single {@link ByteBuffer}.
   */
  public static ByteBuffer concatenateBuffers(ByteBuffer... buffers) {
    int totalSize = 0;
    for (ByteBuffer buffer : buffers) {
      totalSize += buffer.remaining();
    }

    ByteBuffer result = ByteBuffer.allocate(totalSize);
    for (ByteBuffer buffer : buffers) {
      result.put(buffer);
    }

    result.flip();
    return result;
  }
}
