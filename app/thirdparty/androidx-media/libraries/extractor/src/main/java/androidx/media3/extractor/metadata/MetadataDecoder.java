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
import androidx.media3.common.util.UnstableApi;
import java.nio.ByteBuffer;

/** Decodes metadata from binary data. */
@UnstableApi
public interface MetadataDecoder {

  /**
   * Decodes a {@link Metadata} element from the provided input buffer.
   *
   * <p>Respects {@link ByteBuffer#limit()} of {@code inputBuffer.data}, but assumes {@link
   * ByteBuffer#position()} and {@link ByteBuffer#arrayOffset()} are both zero and {@link
   * ByteBuffer#hasArray()} is true.
   *
   * @param inputBuffer The input buffer to decode.
   * @return The decoded metadata object, or {@code null} if the metadata could not be decoded.
   */
  @Nullable
  Metadata decode(MetadataInputBuffer inputBuffer);
}
