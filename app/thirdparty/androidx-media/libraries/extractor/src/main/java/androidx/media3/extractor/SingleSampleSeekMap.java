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
 * A {@link SeekMap} implementation that maps the given point back onto itself.
 *
 * <p>Used for single sample media.
 */
@UnstableApi
public final class SingleSampleSeekMap implements SeekMap {
  private final long durationUs;
  private final long startPosition;

  /**
   * Creates an instance with {@code startPosition} set to 0.
   *
   * @param durationUs The duration of the stream in microseconds, or {@link C#TIME_UNSET} if the
   *     duration is unknown.
   */
  public SingleSampleSeekMap(long durationUs) {
    this(durationUs, /* startPosition= */ 0);
  }

  /**
   * Creates an instance.
   *
   * @param durationUs The duration of the stream in microseconds, or {@link C#TIME_UNSET} if the
   *     duration is unknown.
   * @param startPosition The position (byte offset) of the start of the media.
   */
  public SingleSampleSeekMap(long durationUs, long startPosition) {
    this.durationUs = durationUs;
    this.startPosition = startPosition;
  }

  @Override
  public boolean isSeekable() {
    return true;
  }

  @Override
  public long getDurationUs() {
    return durationUs;
  }

  @Override
  public SeekPoints getSeekPoints(long timeUs) {
    return new SeekPoints(new SeekPoint(timeUs, startPosition));
  }
}
