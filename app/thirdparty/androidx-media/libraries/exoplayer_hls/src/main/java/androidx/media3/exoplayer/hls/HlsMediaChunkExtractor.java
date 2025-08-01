/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls;

import androidx.media3.common.Format;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import java.io.IOException;

/** Extracts samples and track {@link Format Formats} from {@link HlsMediaChunk HlsMediaChunks}. */
@UnstableApi
public interface HlsMediaChunkExtractor {

  /**
   * Initializes the extractor with an {@link ExtractorOutput}. Called at most once.
   *
   * @param extractorOutput An {@link ExtractorOutput} to receive extracted data.
   */
  void init(ExtractorOutput extractorOutput);

  /**
   * Extracts data read from a provided {@link ExtractorInput}. Must not be called before {@link
   * #init(ExtractorOutput)}.
   *
   * <p>A single call to this method will block until some progress has been made, but will not
   * block for longer than this. Hence each call will consume only a small amount of input data.
   *
   * <p>When this method throws an {@link IOException}, extraction may continue by providing an
   * {@link ExtractorInput} with an unchanged {@link ExtractorInput#getPosition() read position} to
   * a subsequent call to this method.
   *
   * @param extractorInput The input to read from.
   * @return Whether there is any data left to extract. Returns false if the end of input has been
   *     reached.
   * @throws IOException If an error occurred reading from or parsing the input.
   */
  boolean read(ExtractorInput extractorInput) throws IOException;

  /** Returns whether this is a packed audio extractor, as defined in RFC 8216, Section 3.4. */
  boolean isPackedAudioExtractor();

  /** Returns whether this instance can be used for extracting multiple continuous segments. */
  boolean isReusable();

  /**
   * Returns a new instance for extracting the same type of media as this one. Can only be called on
   * instances that are not {@link #isReusable() reusable}.
   */
  HlsMediaChunkExtractor recreate();

  /**
   * Resets the sample parsing state.
   *
   * <p>Resetting the parsing state allows support for Fragmented MP4 EXT-X-I-FRAME-STREAM-INF
   * segments. EXT-X-I-FRAME-STREAM-INF segments are truncated to include only a leading key frame.
   * After parsing said keyframe, an extractor may reach an unexpected end of file. By resetting its
   * state, we can continue feeding samples from the following segments to the extractor. See <a
   * href="https://github.com/google/ExoPlayer/issues/7512">#7512</a> for context.
   */
  void onTruncatedSegmentParsed();
}
