/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;

/** Receives stream level data extracted by an {@link Extractor}. */
@UnstableApi
public interface ExtractorOutput {

  /**
   * Placeholder {@link ExtractorOutput} implementation throwing an {@link
   * UnsupportedOperationException} in each method.
   */
  ExtractorOutput PLACEHOLDER =
      new ExtractorOutput() {

        @Override
        public TrackOutput track(int id, int type) {
          throw new UnsupportedOperationException();
        }

        @Override
        public void endTracks() {
          throw new UnsupportedOperationException();
        }

        @Override
        public void seekMap(SeekMap seekMap) {
          throw new UnsupportedOperationException();
        }
      };

  /**
   * Called by the {@link Extractor} to get the {@link TrackOutput} for a specific track.
   *
   * <p>The same {@link TrackOutput} is returned if multiple calls are made with the same {@code
   * id}.
   *
   * @param id A track identifier.
   * @param type The {@link C.TrackType track type}.
   * @return The {@link TrackOutput} for the given track identifier.
   */
  TrackOutput track(int id, @C.TrackType int type);

  /**
   * Called when all tracks have been identified, meaning no new {@code trackId} values will be
   * passed to {@link #track(int, int)}.
   */
  void endTracks();

  /**
   * Called when a {@link SeekMap} has been extracted from the stream.
   *
   * @param seekMap The extracted {@link SeekMap}.
   */
  void seekMap(SeekMap seekMap);
}
