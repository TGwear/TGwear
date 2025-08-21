/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.extractor.text;

import android.util.SparseArray;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;

/**
 * A wrapping {@link ExtractorOutput} that transcodes {@linkplain C#TRACK_TYPE_TEXT text samples}
 * from supported subtitle formats to {@link MimeTypes#APPLICATION_MEDIA3_CUES}.
 *
 * <p>The resulting {@link MimeTypes#APPLICATION_MEDIA3_CUES} samples are emitted to the underlying
 * {@link TrackOutput}.
 *
 * <p>For non-text tracks (i.e. where {@link C.TrackType} is not {@code C.TRACK_TYPE_TEXT}), samples
 * are passed through to the underlying {@link TrackOutput} without modification.
 *
 * <p>Support for subtitle formats is determined by {@link
 * SubtitleParser.Factory#supportsFormat(Format)} on the {@link SubtitleParser.Factory} passed to
 * the constructor of this class.
 */
@UnstableApi
public final class SubtitleTranscodingExtractorOutput implements ExtractorOutput {

  private final ExtractorOutput delegate;
  private final SubtitleParser.Factory subtitleParserFactory;
  private final SparseArray<SubtitleTranscodingTrackOutput> textTrackOutputs;

  private boolean hasNonTextTracks;

  public SubtitleTranscodingExtractorOutput(
      ExtractorOutput delegate, SubtitleParser.Factory subtitleParserFactory) {
    this.delegate = delegate;
    this.subtitleParserFactory = subtitleParserFactory;
    textTrackOutputs = new SparseArray<>();
  }

  public void resetSubtitleParsers() {
    for (int i = 0; i < textTrackOutputs.size(); i++) {
      textTrackOutputs.valueAt(i).resetSubtitleParser();
    }
  }

  // ExtractorOutput implementation

  @Override
  public TrackOutput track(int id, @C.TrackType int type) {
    if (type != C.TRACK_TYPE_TEXT) {
      hasNonTextTracks = true;
      return delegate.track(id, type);
    }
    SubtitleTranscodingTrackOutput existingTrackOutput = textTrackOutputs.get(id);
    if (existingTrackOutput != null) {
      return existingTrackOutput;
    }
    SubtitleTranscodingTrackOutput trackOutput =
        new SubtitleTranscodingTrackOutput(delegate.track(id, type), subtitleParserFactory);
    textTrackOutputs.put(id, trackOutput);
    return trackOutput;
  }

  @Override
  public void endTracks() {
    delegate.endTracks();
    if (hasNonTextTracks) {
      for (int i = 0; i < textTrackOutputs.size(); i++) {
        textTrackOutputs.valueAt(i).shouldSuppressParsingErrors(true);
      }
    }
  }

  @Override
  public void seekMap(SeekMap seekMap) {
    delegate.seekMap(seekMap);
  }
}
