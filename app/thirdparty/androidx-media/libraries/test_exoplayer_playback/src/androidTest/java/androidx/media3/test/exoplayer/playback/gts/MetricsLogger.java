/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.exoplayer.playback.gts;

import android.app.Instrumentation;
import androidx.annotation.Size;

/** Metric logging interface for playback tests. */
/* package */ interface MetricsLogger {

  interface Factory {
    MetricsLogger create(
        Instrumentation instrumentation, @Size(max = 23) String tag, String streamName);
  }

  Factory DEFAULT_FACTORY = LogcatMetricsLogger.FACTORY;

  String KEY_FRAMES_DROPPED_COUNT = "frames_dropped_count";
  String KEY_FRAMES_RENDERED_COUNT = "frames_rendered_count";
  String KEY_FRAMES_SKIPPED_COUNT = "frames_skipped_count";
  String KEY_MAX_CONSECUTIVE_FRAMES_DROPPED_COUNT = "maximum_consecutive_frames_dropped_count";
  String KEY_TEST_NAME = "test_name";
  String KEY_IS_CDD_LIMITED_RETRY = "is_cdd_limited_retry";

  /**
   * Logs an int metric provided from a test.
   *
   * @param key The key of the metric to be logged.
   * @param value The value of the metric to be logged.
   */
  void logMetric(String key, int value);

  /**
   * Logs a string metric provided from a test.
   *
   * @param key The key of the metric to be logged.
   * @param value The value of the metric to be logged.
   */
  void logMetric(String key, String value);

  /**
   * Logs a boolean metric provided from a test.
   *
   * @param key The key of the metric to be logged.
   * @param value The value of the metric to be logged.
   */
  void logMetric(String key, boolean value);

  /** Closes the logger. */
  void close();
}
