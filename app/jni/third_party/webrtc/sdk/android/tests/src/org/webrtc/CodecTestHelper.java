/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Helper methods for {@link HardwareVideoEncoderTest} and {@link AndroidVideoDecoderTest}.
 */
class CodecTestHelper {
  static void assertEqualContents(byte[] expected, ByteBuffer actual, int offset, int size) {
    assertThat(size).isEqualTo(expected.length);
    assertThat(actual.capacity()).isAtLeast(offset + size);
    for (int i = 0; i < expected.length; i++) {
      assertWithMessage("At index: " + i).that(actual.get(offset + i)).isEqualTo(expected[i]);
    }
  }

  static byte[] generateRandomData(int length) {
    Random random = new Random();
    byte[] data = new byte[length];
    random.nextBytes(data);
    return data;
  }

  static VideoFrame.I420Buffer wrapI420(int width, int height, byte[] data) {
    final int posY = 0;
    final int posU = width * height;
    final int posV = posU + width * height / 4;
    final int endV = posV + width * height / 4;

    ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
    buffer.put(data);

    buffer.limit(posU);
    buffer.position(posY);
    ByteBuffer dataY = buffer.slice();

    buffer.limit(posV);
    buffer.position(posU);
    ByteBuffer dataU = buffer.slice();

    buffer.limit(endV);
    buffer.position(posV);
    ByteBuffer dataV = buffer.slice();

    return JavaI420Buffer.wrap(width, height, dataY, width, dataU, width / 2, dataV, width / 2,
        /* releaseCallback= */ null);
  }
}
