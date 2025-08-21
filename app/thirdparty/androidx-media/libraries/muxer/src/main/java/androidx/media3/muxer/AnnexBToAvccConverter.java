/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.muxer;

import androidx.media3.common.util.UnstableApi;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;

/**
 * Converts a buffer containing H.264/H.265 NAL units from the Annex-B format (ISO/IEC 14496-14
 * Annex B, which uses start codes to delineate NAL units) to the avcC format (ISO/IEC 14496-15,
 * which uses length prefixes).
 */
@UnstableApi
public interface AnnexBToAvccConverter {
  /** Default implementation for {@link AnnexBToAvccConverter}. */
  AnnexBToAvccConverter DEFAULT =
      new AnnexBToAvccConverter() {
        @Override
        public ByteBuffer process(ByteBuffer inputBuffer) {
          return process(inputBuffer, ByteBufferAllocator.DEFAULT);
        }

        @Override
        public ByteBuffer process(ByteBuffer inputBuffer, ByteBufferAllocator byteBufferAllocator) {
          if (!inputBuffer.hasRemaining()) {
            return inputBuffer;
          }

          ImmutableList<ByteBuffer> nalUnitList = AnnexBUtils.findNalUnits(inputBuffer);

          int totalBytesNeeded = 0;

          for (int i = 0; i < nalUnitList.size(); i++) {
            // 4 bytes to store NAL unit length.
            totalBytesNeeded += 4 + nalUnitList.get(i).remaining();
          }

          ByteBuffer outputBuffer = byteBufferAllocator.allocate(totalBytesNeeded);

          for (int i = 0; i < nalUnitList.size(); i++) {
            ByteBuffer currentNalUnit = nalUnitList.get(i);
            int currentNalUnitLength = currentNalUnit.remaining();

            // Rewrite NAL units with NAL unit length in place of start code.
            outputBuffer.putInt(currentNalUnitLength);
            outputBuffer.put(currentNalUnit);
          }
          outputBuffer.rewind();
          return outputBuffer;
        }
      };

  /**
   * Returns the processed {@link ByteBuffer}.
   *
   * <p>Expects a {@link ByteBuffer} input with a zero offset.
   *
   * @param inputBuffer The buffer to be converted.
   */
  ByteBuffer process(ByteBuffer inputBuffer);

  /**
   * Returns the processed {@link ByteBuffer}.
   *
   * <p>Expects a {@link ByteBuffer} input with a zero offset.
   *
   * @param inputBuffer The buffer to be converted.
   * @param allocator An allocator for {@link ByteBuffer} instances that enables memory reuse.
   */
  default ByteBuffer process(ByteBuffer inputBuffer, ByteBufferAllocator allocator) {
    return process(inputBuffer);
  }
}
