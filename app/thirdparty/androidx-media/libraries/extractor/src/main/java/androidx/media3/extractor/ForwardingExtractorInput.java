/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/** An overridable {@link ExtractorInput} implementation forwarding all methods to another input. */
@UnstableApi
public class ForwardingExtractorInput implements ExtractorInput {

  private final ExtractorInput input;

  public ForwardingExtractorInput(ExtractorInput input) {
    this.input = input;
  }

  @Override
  public int read(byte[] buffer, int offset, int length) throws IOException {
    return input.read(buffer, offset, length);
  }

  @Override
  public boolean readFully(byte[] target, int offset, int length, boolean allowEndOfInput)
      throws IOException {
    return input.readFully(target, offset, length, allowEndOfInput);
  }

  @Override
  public void readFully(byte[] target, int offset, int length) throws IOException {
    input.readFully(target, offset, length);
  }

  @Override
  public int skip(int length) throws IOException {
    return input.skip(length);
  }

  @Override
  public boolean skipFully(int length, boolean allowEndOfInput) throws IOException {
    return input.skipFully(length, allowEndOfInput);
  }

  @Override
  public void skipFully(int length) throws IOException {
    input.skipFully(length);
  }

  @Override
  public int peek(byte[] target, int offset, int length) throws IOException {
    return input.peek(target, offset, length);
  }

  @Override
  public boolean peekFully(byte[] target, int offset, int length, boolean allowEndOfInput)
      throws IOException {
    return input.peekFully(target, offset, length, allowEndOfInput);
  }

  @Override
  public void peekFully(byte[] target, int offset, int length) throws IOException {
    input.peekFully(target, offset, length);
  }

  @Override
  public boolean advancePeekPosition(int length, boolean allowEndOfInput) throws IOException {
    return input.advancePeekPosition(length, allowEndOfInput);
  }

  @Override
  public void advancePeekPosition(int length) throws IOException {
    input.advancePeekPosition(length);
  }

  @Override
  public void resetPeekPosition() {
    input.resetPeekPosition();
  }

  @Override
  public long getPeekPosition() {
    return input.getPeekPosition();
  }

  @Override
  public long getPosition() {
    return input.getPosition();
  }

  @Override
  public long getLength() {
    return input.getLength();
  }

  @Override
  public <E extends Throwable> void setRetryPosition(long position, E e) throws E {
    input.setRetryPosition(position, e);
  }
}
