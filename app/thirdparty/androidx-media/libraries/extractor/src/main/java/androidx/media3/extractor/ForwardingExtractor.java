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
import java.util.List;

/**
 * An overridable {@link Extractor} implementation which forwards all methods to another {@link
 * Extractor}.
 */
@UnstableApi
public class ForwardingExtractor implements Extractor {
  private final Extractor delegate;

  public ForwardingExtractor(Extractor delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean sniff(ExtractorInput input) throws IOException {
    return delegate.sniff(input);
  }

  @Override
  public List<SniffFailure> getSniffFailureDetails() {
    return delegate.getSniffFailureDetails();
  }

  @Override
  public void init(ExtractorOutput output) {
    delegate.init(output);
  }

  @Override
  public @ReadResult int read(ExtractorInput input, PositionHolder seekPosition)
      throws IOException {
    return delegate.read(input, seekPosition);
  }

  @Override
  public void seek(long position, long timeUs) {
    delegate.seek(position, timeUs);
  }

  @Override
  public void release() {
    delegate.release();
  }

  @Override
  public Extractor getUnderlyingImplementation() {
    return delegate.getUnderlyingImplementation();
  }
}
