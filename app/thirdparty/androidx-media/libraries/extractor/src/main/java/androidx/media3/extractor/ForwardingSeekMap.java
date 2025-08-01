/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import androidx.media3.common.util.UnstableApi;

/** A forwarding class for {@link SeekMap} */
@UnstableApi
public class ForwardingSeekMap implements SeekMap {
  private final SeekMap seekMap;

  /**
   * Creates a instance.
   *
   * @param seekMap The original {@link SeekMap}.
   */
  public ForwardingSeekMap(SeekMap seekMap) {
    this.seekMap = seekMap;
  }

  @Override
  public boolean isSeekable() {
    return seekMap.isSeekable();
  }

  @Override
  public long getDurationUs() {
    return seekMap.getDurationUs();
  }

  @Override
  public SeekPoints getSeekPoints(long timeUs) {
    return seekMap.getSeekPoints(timeUs);
  }
}
