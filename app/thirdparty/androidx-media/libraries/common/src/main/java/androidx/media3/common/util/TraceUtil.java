/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import android.os.Trace;
import androidx.media3.common.MediaLibraryInfo;

/**
 * Calls through to {@link Trace} methods if {@link MediaLibraryInfo#TRACE_ENABLED} is {@code true}.
 */
@UnstableApi
public final class TraceUtil {

  private TraceUtil() {}

  /**
   * Writes a trace message to indicate that a given section of code has begun.
   *
   * @see Trace#beginSection(String)
   * @param sectionName The name of the code section to appear in the trace. This may be at most 127
   *     Unicode code units long.
   */
  public static void beginSection(String sectionName) {
    if (MediaLibraryInfo.TRACE_ENABLED) {
      Trace.beginSection(sectionName);
    }
  }

  /**
   * Writes a trace message to indicate that a given section of code has ended.
   *
   * @see Trace#endSection()
   */
  public static void endSection() {
    if (MediaLibraryInfo.TRACE_ENABLED) {
      Trace.endSection();
    }
  }
}
