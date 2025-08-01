/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder;

import androidx.annotation.Nullable;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/** Buffer for {@link SimpleDecoder} output. */
@UnstableApi
public class SimpleDecoderOutputBuffer extends DecoderOutputBuffer {

  private final Owner<SimpleDecoderOutputBuffer> owner;

  @Nullable public ByteBuffer data;

  public SimpleDecoderOutputBuffer(Owner<SimpleDecoderOutputBuffer> owner) {
    this.owner = owner;
  }

  /**
   * Initializes the buffer.
   *
   * @param timeUs The presentation timestamp for the buffer, in microseconds.
   * @param size An upper bound on the size of the data that will be written to the buffer.
   * @return The {@link #data} buffer, for convenience.
   */
  public ByteBuffer init(long timeUs, int size) {
    this.timeUs = timeUs;
    if (data == null || data.capacity() < size) {
      data = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
    }
    data.position(0);
    data.limit(size);
    return data;
  }

  /**
   * Grows the buffer to a new size.
   *
   * <p>Existing data is copied to the new buffer, and {@link ByteBuffer#position} is preserved.
   *
   * @param newSize New size of the buffer.
   * @return The {@link #data} buffer, for convenience.
   */
  public ByteBuffer grow(int newSize) {
    ByteBuffer oldData = Assertions.checkNotNull(this.data);
    Assertions.checkArgument(newSize >= oldData.limit());
    ByteBuffer newData = ByteBuffer.allocateDirect(newSize).order(ByteOrder.nativeOrder());
    int restorePosition = oldData.position();
    oldData.position(0);
    newData.put(oldData);
    newData.position(restorePosition);
    newData.limit(newSize);
    this.data = newData;
    return newData;
  }

  @Override
  public void clear() {
    super.clear();
    if (data != null) {
      data.clear();
    }
  }

  @Override
  public void release() {
    owner.releaseOutputBuffer(this);
  }
}
