/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.text;

import androidx.media3.extractor.text.SimpleSubtitleDecoder;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleParser;

/**
 * Wrapper around a {@link SubtitleParser} that can be used instead of any current {@link
 * SimpleSubtitleDecoder} subclass. The main {@link #decode(byte[], int, boolean)} method will be
 * delegating the parsing of the data to the underlying {@link SubtitleParser} instance and its
 * {@link SubtitleParser#parseToLegacySubtitle(byte[], int, int)} implementation.
 *
 * <p>Functionally, once each XXXDecoder class is refactored to be a XXXParser that implements
 * {@link SubtitleParser}, the following should be equivalent:
 *
 * <ul>
 *   <li>DelegatingSubtitleDecoder("XXX", new XXXParser())
 *   <li>XXXDecoder()
 * </ul>
 *
 * <p>Or in the case with initialization data:
 *
 * <ul>
 *   <li>DelegatingSubtitleDecoder("XXX", new XXXParser(initializationData))
 *   <li>XXXDecoder(initializationData)
 * </ul>
 */
// TODO(b/289983417): this will only be used in the old decoding flow (Decoder after SampleQueue)
// while we maintain dual architecture. Once we fully migrate to the pre-SampleQueue flow, it can be
// deprecated and later deleted.
/* package */ final class DelegatingSubtitleDecoder extends SimpleSubtitleDecoder {

  private final SubtitleParser subtitleParser;

  public DelegatingSubtitleDecoder(String name, SubtitleParser subtitleParser) {
    super(name);
    this.subtitleParser = subtitleParser;
  }

  @Override
  protected Subtitle decode(byte[] data, int length, boolean reset) {
    if (reset) {
      subtitleParser.reset();
    }
    return subtitleParser.parseToLegacySubtitle(data, /* offset= */ 0, length);
  }
}
