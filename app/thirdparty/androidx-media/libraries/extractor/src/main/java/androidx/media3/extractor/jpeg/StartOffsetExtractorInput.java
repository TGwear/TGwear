/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.jpeg;

import static androidx.media3.common.util.Assertions.checkArgument;

import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ForwardingExtractorInput;

/**
 * An extractor input that wraps another extractor input and exposes data starting at a given start
 * byte offset.
 *
 * <p>This is useful for reading data from a container that's concatenated after some prefix data
 * but where the container's extractor doesn't handle a non-zero start offset (for example, because
 * it seeks to absolute positions read from the container data).
 */
/* package */ final class StartOffsetExtractorInput extends ForwardingExtractorInput {

  private final long startOffset;

  /**
   * Creates a new wrapper reading from the given start byte offset.
   *
   * @param input The extractor input to wrap. The reading position must be at or after the start
   *     offset, otherwise data could be read from before the start offset.
   * @param startOffset The offset from which this extractor input provides data, in bytes.
   * @throws IllegalArgumentException Thrown if the start offset is before the current reading
   *     position.
   */
  public StartOffsetExtractorInput(ExtractorInput input, long startOffset) {
    super(input);
    checkArgument(input.getPosition() >= startOffset);
    this.startOffset = startOffset;
  }

  @Override
  public long getPosition() {
    return super.getPosition() - startOffset;
  }

  @Override
  public long getPeekPosition() {
    return super.getPeekPosition() - startOffset;
  }

  @Override
  public long getLength() {
    return super.getLength() - startOffset;
  }

  @Override
  public <E extends Throwable> void setRetryPosition(long position, E e) throws E {
    super.setRetryPosition(position + startOffset, e);
  }
}
