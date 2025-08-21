/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream;

import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.TransferListener;
import java.util.concurrent.CopyOnWriteArrayList;

/** Provides estimates of the currently available bandwidth. */
@UnstableApi
public interface BandwidthMeter {

  /** A listener of {@link BandwidthMeter} events. */
  interface EventListener {

    /**
     * Called periodically to indicate that bytes have been transferred or the estimated bitrate has
     * changed.
     *
     * <p>Note: The estimated bitrate is typically derived from more information than just {@code
     * bytesTransferred} and {@code elapsedMs}.
     *
     * @param elapsedMs The time taken to transfer {@code bytesTransferred}, in milliseconds. This
     *     is at most the elapsed time since the last callback, but may be less if there were
     *     periods during which data was not being transferred.
     * @param bytesTransferred The number of bytes transferred since the last callback.
     * @param bitrateEstimate The estimated bitrate in bits/sec.
     */
    void onBandwidthSample(int elapsedMs, long bytesTransferred, long bitrateEstimate);

    /** Event dispatcher which allows listener registration. */
    final class EventDispatcher {

      private final CopyOnWriteArrayList<HandlerAndListener> listeners;

      /** Creates an event dispatcher. */
      public EventDispatcher() {
        listeners = new CopyOnWriteArrayList<>();
      }

      /** Adds a listener to the event dispatcher. */
      public void addListener(Handler eventHandler, BandwidthMeter.EventListener eventListener) {
        Assertions.checkNotNull(eventHandler);
        Assertions.checkNotNull(eventListener);
        removeListener(eventListener);
        listeners.add(new HandlerAndListener(eventHandler, eventListener));
      }

      /** Removes a listener from the event dispatcher. */
      public void removeListener(BandwidthMeter.EventListener eventListener) {
        for (HandlerAndListener handlerAndListener : listeners) {
          if (handlerAndListener.listener == eventListener) {
            handlerAndListener.release();
            listeners.remove(handlerAndListener);
          }
        }
      }

      public void bandwidthSample(int elapsedMs, long bytesTransferred, long bitrateEstimate) {
        for (HandlerAndListener handlerAndListener : listeners) {
          if (!handlerAndListener.released) {
            handlerAndListener.handler.post(
                () ->
                    handlerAndListener.listener.onBandwidthSample(
                        elapsedMs, bytesTransferred, bitrateEstimate));
          }
        }
      }

      private static final class HandlerAndListener {

        private final Handler handler;
        private final BandwidthMeter.EventListener listener;

        private boolean released;

        public HandlerAndListener(Handler handler, BandwidthMeter.EventListener eventListener) {
          this.handler = handler;
          this.listener = eventListener;
        }

        public void release() {
          released = true;
        }
      }
    }
  }

  /** Returns the estimated bitrate. */
  long getBitrateEstimate();

  /**
   * Returns the estimated time to first byte, in microseconds, or {@link C#TIME_UNSET} if no
   * estimate is available.
   */
  default long getTimeToFirstByteEstimateUs() {
    return C.TIME_UNSET;
  }

  /**
   * Returns the {@link TransferListener} that this instance uses to gather bandwidth information
   * from data transfers. May be null if the implementation does not listen to data transfers.
   */
  @Nullable
  TransferListener getTransferListener();

  /**
   * Adds an {@link EventListener}.
   *
   * @param eventHandler A handler for events.
   * @param eventListener A listener of events.
   */
  void addEventListener(Handler eventHandler, EventListener eventListener);

  /**
   * Removes an {@link EventListener}.
   *
   * @param eventListener The listener to be removed.
   */
  void removeEventListener(EventListener eventListener);
}
