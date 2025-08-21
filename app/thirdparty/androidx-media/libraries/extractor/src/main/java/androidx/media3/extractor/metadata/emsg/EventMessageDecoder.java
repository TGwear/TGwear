/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.emsg;

import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.media3.extractor.metadata.SimpleMetadataDecoder;
import java.nio.ByteBuffer;
import java.util.Arrays;

/** Decodes data encoded by {@link EventMessageEncoder}. */
@UnstableApi
public final class EventMessageDecoder extends SimpleMetadataDecoder {

  @Override
  @SuppressWarnings("ByteBufferBackingArray") // Buffer validated by SimpleMetadataDecoder.decode
  protected Metadata decode(MetadataInputBuffer inputBuffer, ByteBuffer buffer) {
    return new Metadata(decode(new ParsableByteArray(buffer.array(), buffer.limit())));
  }

  public EventMessage decode(ParsableByteArray emsgData) {
    String schemeIdUri = Assertions.checkNotNull(emsgData.readNullTerminatedString());
    String value = Assertions.checkNotNull(emsgData.readNullTerminatedString());
    long durationMs = emsgData.readLong();
    long id = emsgData.readLong();
    byte[] messageData =
        Arrays.copyOfRange(emsgData.getData(), emsgData.getPosition(), emsgData.limit());
    return new EventMessage(schemeIdUri, value, durationMs, id, messageData);
  }
}
