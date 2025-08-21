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

/**
 * An overridable {@link ExtractorOutput} implementation which forwards all methods to another
 * {@link ExtractorOutput}.
 */
@UnstableApi
public class ForwardingExtractorOutput implements ExtractorOutput {
  private final ExtractorOutput output;

  public ForwardingExtractorOutput(ExtractorOutput output) {
    this.output = output;
  }

  @Override
  public TrackOutput track(int id, @C.TrackType int type) {
    return output.track(id, type);
  }

  @Override
  public void endTracks() {
    output.endTracks();
  }

  @Override
  public void seekMap(SeekMap seekMap) {
    output.seekMap(seekMap);
  }
}
