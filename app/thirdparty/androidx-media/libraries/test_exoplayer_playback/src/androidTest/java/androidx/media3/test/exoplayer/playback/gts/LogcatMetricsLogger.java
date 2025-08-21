/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.exoplayer.playback.gts;

import androidx.annotation.Size;
import androidx.media3.common.util.Log;

/** Implementation of {@link MetricsLogger} that prints the metrics to logcat. */
/* package */ final class LogcatMetricsLogger implements MetricsLogger {

  public static final Factory FACTORY =
      (instrumentation, tag, streamName) -> new LogcatMetricsLogger(tag);

  @Size(max = 23)
  private final String tag;

  public LogcatMetricsLogger(@Size(max = 23) String tag) {
    this.tag = tag;
  }

  @Override
  public void logMetric(String key, int value) {
    Log.d(tag, key + ": " + value);
  }

  @Override
  public void logMetric(String key, String value) {
    Log.d(tag, key + ": " + value);
  }

  @Override
  public void logMetric(String key, boolean value) {
    Log.d(tag, key + ": " + value);
  }

  @Override
  public void close() {
    // Do nothing.
  }
}
