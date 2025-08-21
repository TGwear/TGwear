/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import androidx.annotation.Nullable;
import androidx.media3.common.Timeline;
import androidx.media3.common.Timeline.Period;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.upstream.Allocator;

/**
 * Fake {@link MediaSource} that provides a given timeline. Creating the period returns a {@link
 * FakeAdaptiveMediaPeriod} from the given {@link TrackGroupArray}.
 */
@UnstableApi
public class FakeAdaptiveMediaSource extends FakeMediaSource {

  private final FakeChunkSource.Factory chunkSourceFactory;

  public FakeAdaptiveMediaSource(
      Timeline timeline,
      TrackGroupArray trackGroupArray,
      FakeChunkSource.Factory chunkSourceFactory) {
    super(
        timeline,
        DrmSessionManager.DRM_UNSUPPORTED,
        /* trackDataFactory= */ (unusedFormat, unusedMediaPeriodId) -> {
          throw new RuntimeException("Unused TrackDataFactory");
        },
        trackGroupArray);
    this.chunkSourceFactory = chunkSourceFactory;
  }

  @Override
  protected MediaPeriod createMediaPeriod(
      MediaPeriodId id,
      TrackGroupArray trackGroupArray,
      Allocator allocator,
      MediaSourceEventListener.EventDispatcher mediaSourceEventDispatcher,
      DrmSessionManager drmSessionManager,
      DrmSessionEventListener.EventDispatcher drmEventDispatcher,
      @Nullable TransferListener transferListener) {
    Period period = Util.castNonNull(getTimeline()).getPeriodByUid(id.periodUid, new Period());
    return new FakeAdaptiveMediaPeriod(
        trackGroupArray,
        mediaSourceEventDispatcher,
        allocator,
        chunkSourceFactory,
        period.durationUs,
        transferListener);
  }

  @Override
  public void releaseMediaPeriod(MediaPeriod mediaPeriod) {
    ((FakeAdaptiveMediaPeriod) mediaPeriod).release();
  }
}
