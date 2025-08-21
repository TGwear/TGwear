/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.test.utils;

import android.os.SystemClock;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.audio.AudioRendererEventListener;

/** A {@link FakeRenderer} that supports {@link C#TRACK_TYPE_AUDIO}. */
@UnstableApi
public class FakeAudioRenderer extends FakeRenderer {

  private final HandlerWrapper handler;
  private final AudioRendererEventListener eventListener;
  private final DecoderCounters decoderCounters;
  private boolean notifiedPositionAdvancing;

  public FakeAudioRenderer(HandlerWrapper handler, AudioRendererEventListener eventListener) {
    super(C.TRACK_TYPE_AUDIO);
    this.handler = handler;
    this.eventListener = eventListener;
    decoderCounters = new DecoderCounters();
  }

  @Override
  protected void onEnabled(boolean joining, boolean mayRenderStartOfStream)
      throws ExoPlaybackException {
    super.onEnabled(joining, mayRenderStartOfStream);
    handler.post(() -> eventListener.onAudioEnabled(decoderCounters));
    notifiedPositionAdvancing = false;
  }

  @Override
  protected void onDisabled() {
    super.onDisabled();
    handler.post(() -> eventListener.onAudioDisabled(decoderCounters));
  }

  @Override
  protected void onFormatChanged(Format format) {
    handler.post(
        () -> eventListener.onAudioInputFormatChanged(format, /* decoderReuseEvaluation= */ null));
    handler.post(
        () ->
            eventListener.onAudioDecoderInitialized(
                /* decoderName= */ "fake.audio.decoder",
                /* initializedTimestampMs= */ SystemClock.elapsedRealtime(),
                /* initializationDurationMs= */ 0));
  }

  @Override
  protected boolean shouldProcessBuffer(long bufferTimeUs, long playbackPositionUs) {
    boolean shouldProcess = super.shouldProcessBuffer(bufferTimeUs, playbackPositionUs);
    if (shouldProcess && !notifiedPositionAdvancing) {
      handler.post(() -> eventListener.onAudioPositionAdvancing(System.currentTimeMillis()));
      notifiedPositionAdvancing = true;
    }
    return shouldProcess;
  }
}
