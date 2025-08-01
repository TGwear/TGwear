/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.base.Supplier;

/**
 * A {@link SubtitleParser.Factory} for {@link SubtitleParser} instances that throw an exception on
 * every call to {@link SubtitleParser#parse}.
 *
 * <p>It claims support for all subtitle formats (returns the result of {@link MimeTypes#isText}
 * from {@link SubtitleParser.Factory#supportsFormat}).
 */
@UnstableApi
public class ThrowingSubtitleParserFactory implements SubtitleParser.Factory {

  public static final @Format.CueReplacementBehavior int REPLACEMENT_BEHAVIOR =
      Format.CUE_REPLACEMENT_BEHAVIOR_REPLACE;
  private final Supplier<RuntimeException> exceptionSupplier;

  public ThrowingSubtitleParserFactory(Supplier<RuntimeException> exceptionSupplier) {
    this.exceptionSupplier = exceptionSupplier;
  }

  @Override
  public boolean supportsFormat(Format format) {
    return MimeTypes.isText(format.sampleMimeType);
  }

  @Override
  public @Format.CueReplacementBehavior int getCueReplacementBehavior(Format format) {
    return REPLACEMENT_BEHAVIOR;
  }

  @Override
  public SubtitleParser create(Format format) {
    return new SubtitleParser() {
      @Override
      public void parse(
          byte[] data,
          int offset,
          int length,
          OutputOptions outputOptions,
          Consumer<CuesWithTiming> output) {
        throw exceptionSupplier.get();
      }

      @Override
      public @Format.CueReplacementBehavior int getCueReplacementBehavior() {
        return REPLACEMENT_BEHAVIOR;
      }
    };
  }
}
