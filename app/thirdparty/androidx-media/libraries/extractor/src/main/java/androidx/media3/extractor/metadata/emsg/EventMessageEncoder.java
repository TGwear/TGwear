/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.emsg;

import androidx.media3.common.util.UnstableApi;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Encodes data that can be decoded by {@link EventMessageDecoder}. This class isn't thread safe.
 */
@UnstableApi
public final class EventMessageEncoder {

  private final ByteArrayOutputStream byteArrayOutputStream;
  private final DataOutputStream dataOutputStream;

  public EventMessageEncoder() {
    byteArrayOutputStream = new ByteArrayOutputStream(512);
    dataOutputStream = new DataOutputStream(byteArrayOutputStream);
  }

  /**
   * Encodes an {@link EventMessage} to a byte array that can be decoded by {@link
   * EventMessageDecoder}.
   *
   * @param eventMessage The event message to be encoded.
   * @return The serialized byte array.
   */
  public byte[] encode(EventMessage eventMessage) {
    byteArrayOutputStream.reset();
    try {
      writeNullTerminatedString(dataOutputStream, eventMessage.schemeIdUri);
      String nonNullValue = eventMessage.value != null ? eventMessage.value : "";
      writeNullTerminatedString(dataOutputStream, nonNullValue);
      dataOutputStream.writeLong(eventMessage.durationMs);
      dataOutputStream.writeLong(eventMessage.id);
      dataOutputStream.write(eventMessage.messageData);
      dataOutputStream.flush();
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      // Should never happen.
      throw new RuntimeException(e);
    }
  }

  private static void writeNullTerminatedString(DataOutputStream dataOutputStream, String value)
      throws IOException {
    dataOutputStream.writeBytes(value);
    dataOutputStream.writeByte(0);
  }
}
