/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.extractor.text;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/**
 * @deprecated Use {@link SubtitleTranscodingExtractorOutput} directly from within an existing
 *     {@link Extractor} implementation instead.
 */
@Deprecated
@UnstableApi
public class SubtitleTranscodingExtractor implements Extractor {

  private final Extractor delegate;
  private final SubtitleParser.Factory subtitleParserFactory;

  private @MonotonicNonNull SubtitleTranscodingExtractorOutput transcodingExtractorOutput;

  public SubtitleTranscodingExtractor(
      Extractor delegate, SubtitleParser.Factory subtitleParserFactory) {
    this.delegate = delegate;
    this.subtitleParserFactory = subtitleParserFactory;
  }

  @Override
  public boolean sniff(ExtractorInput input) throws IOException {
    return delegate.sniff(input);
  }

  @Override
  public void init(ExtractorOutput output) {
    transcodingExtractorOutput =
        new SubtitleTranscodingExtractorOutput(output, subtitleParserFactory);
    delegate.init(transcodingExtractorOutput);
  }

  @Override
  public @ReadResult int read(ExtractorInput input, PositionHolder seekPosition)
      throws IOException {
    return delegate.read(input, seekPosition);
  }

  @Override
  public void seek(long position, long timeUs) {
    if (transcodingExtractorOutput != null) {
      transcodingExtractorOutput.resetSubtitleParsers();
    }
    delegate.seek(position, timeUs);
  }

  @Override
  public void release() {
    delegate.release();
  }

  @Override
  public Extractor getUnderlyingImplementation() {
    return delegate;
  }
}
