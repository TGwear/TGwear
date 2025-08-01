/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text;

import androidx.annotation.Nullable;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.SimpleDecoder;
import java.nio.ByteBuffer;

/** Base class for subtitle parsers that use their own decode thread. */
@UnstableApi
public abstract class SimpleSubtitleDecoder
    extends SimpleDecoder<SubtitleInputBuffer, SubtitleOutputBuffer, SubtitleDecoderException>
    implements SubtitleDecoder {

  private final String name;

  /**
   * @param name The name of the decoder.
   */
  @SuppressWarnings("nullness:method.invocation")
  protected SimpleSubtitleDecoder(String name) {
    super(new SubtitleInputBuffer[2], new SubtitleOutputBuffer[2]);
    this.name = name;
    setInitialInputBufferSize(1024);
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public void setPositionUs(long positionUs) {
    // Do nothing
  }

  @Override
  protected final SubtitleInputBuffer createInputBuffer() {
    return new SubtitleInputBuffer();
  }

  @Override
  protected final SubtitleOutputBuffer createOutputBuffer() {
    return new SubtitleOutputBuffer() {
      @Override
      public void release() {
        SimpleSubtitleDecoder.this.releaseOutputBuffer(this);
      }
    };
  }

  @Override
  protected final SubtitleDecoderException createUnexpectedDecodeException(Throwable error) {
    return new SubtitleDecoderException("Unexpected decode error", error);
  }

  @SuppressWarnings("ByteBufferBackingArray")
  @Override
  @Nullable
  protected final SubtitleDecoderException decode(
      SubtitleInputBuffer inputBuffer, SubtitleOutputBuffer outputBuffer, boolean reset) {
    try {
      ByteBuffer inputData = Assertions.checkNotNull(inputBuffer.data);
      Subtitle subtitle = decode(inputData.array(), inputData.limit(), reset);
      outputBuffer.setContent(inputBuffer.timeUs, subtitle, inputBuffer.subsampleOffsetUs);
      outputBuffer.shouldBeSkipped = false; // Skipping is handled by TextRenderer
      return null;
    } catch (SubtitleDecoderException e) {
      return e;
    }
  }

  /**
   * Decodes data into a {@link Subtitle}.
   *
   * @param data An array holding the data to be decoded, starting at position 0.
   * @param length The number of bytes from {@code data} to be decoded.
   * @param reset Whether the decoder must be reset before decoding.
   * @return The decoded {@link Subtitle}.
   * @throws SubtitleDecoderException If a decoding error occurs.
   */
  protected abstract Subtitle decode(byte[] data, int length, boolean reset)
      throws SubtitleDecoderException;
}
