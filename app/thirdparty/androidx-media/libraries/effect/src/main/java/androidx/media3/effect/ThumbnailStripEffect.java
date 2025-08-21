/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import android.content.Context;
import androidx.media3.common.C;
import androidx.media3.common.VideoFrameProcessingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate a thumbnail strip (i.e. tile frames horizontally) containing frames at given {@link
 * #setTimestampsMs timestamps}.
 */
/* package */ final class ThumbnailStripEffect implements GlEffect {

  /* package */ final int stripWidth;
  /* package */ final int stripHeight;
  private final List<Long> timestampsMs;
  private int currentThumbnailIndex;

  /**
   * Creates a new instance with the given size. No thumbnails are drawn by default, call {@link
   * #setTimestampsMs} to change how many to draw and their timestamp.
   *
   * @param stripWidth The width of the thumbnail strip.
   * @param stripHeight The height of the thumbnail strip.
   */
  public ThumbnailStripEffect(int stripWidth, int stripHeight) {
    this.stripWidth = stripWidth;
    this.stripHeight = stripHeight;
    timestampsMs = new ArrayList<>();
  }

  @Override
  public ThumbnailStripShaderProgram toGlShaderProgram(Context context, boolean useHdr)
      throws VideoFrameProcessingException {
    return new ThumbnailStripShaderProgram(context, useHdr, this);
  }

  /**
   * Sets the timestamps of the frames to draw, in milliseconds.
   *
   * <p>The timestamp represents the minimum presentation time of the next frame added to the strip.
   * For example, if the timestamp is 10, a frame with a time of 100 will be drawn but one with a
   * time of 9 will be ignored.
   */
  public void setTimestampsMs(List<Long> timestampsMs) {
    this.timestampsMs.clear();
    this.timestampsMs.addAll(timestampsMs);
    currentThumbnailIndex = 0;
  }

  /** Returns whether all the thumbnails have already been drawn. */
  public boolean isDone() {
    return currentThumbnailIndex >= timestampsMs.size();
  }

  /** Returns the index of the next thumbnail to draw. */
  public int getNextThumbnailIndex() {
    return currentThumbnailIndex;
  }

  /** Returns the timestamp in milliseconds of the next thumbnail to draw. */
  public long getNextTimestampMs() {
    return isDone() ? C.TIME_END_OF_SOURCE : timestampsMs.get(currentThumbnailIndex);
  }

  /** Returns the total number of thumbnails to be drawn in the strip. */
  public int getNumberOfThumbnails() {
    return timestampsMs.size();
  }

  /* package */ void onThumbnailDrawn() {
    currentThumbnailIndex++;
  }
}
