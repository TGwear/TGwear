/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata;

import androidx.annotation.Nullable;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import java.nio.ByteBuffer;

/** A {@link MetadataDecoder} base class that validates input buffers. */
@UnstableApi
public abstract class SimpleMetadataDecoder implements MetadataDecoder {

  @Override
  @Nullable
  public final Metadata decode(MetadataInputBuffer inputBuffer) {
    ByteBuffer buffer = Assertions.checkNotNull(inputBuffer.data);
    Assertions.checkArgument(
        buffer.position() == 0 && buffer.hasArray() && buffer.arrayOffset() == 0);
    return decode(inputBuffer, buffer);
  }

  /**
   * Called by {@link #decode(MetadataInputBuffer)} after input buffer validation has been
   * performed.
   *
   * @param inputBuffer The input buffer to decode.
   * @param buffer The input buffer's {@link MetadataInputBuffer#data data buffer}, for convenience.
   *     Validation by {@link #decode} guarantees that {@link ByteBuffer#hasArray()}, {@link
   *     ByteBuffer#position()} and {@link ByteBuffer#arrayOffset()} are {@code true}, {@code 0} and
   *     {@code 0} respectively.
   * @return The decoded metadata object, or {@code null} if the metadata could not be decoded.
   */
  @Nullable
  protected abstract Metadata decode(MetadataInputBuffer inputBuffer, ByteBuffer buffer);
}
