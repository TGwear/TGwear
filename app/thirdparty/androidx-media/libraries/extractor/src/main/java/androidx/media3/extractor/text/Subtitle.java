/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text;

import androidx.media3.common.C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.UnstableApi;
import java.util.List;

/** A subtitle consisting of timed {@link Cue}s. */
@UnstableApi
public interface Subtitle {

  /**
   * Returns the index of the first event that occurs after a given time (exclusive).
   *
   * @param timeUs The time in microseconds.
   * @return The index of the next event, or {@link C#INDEX_UNSET} if there are no events after the
   *     specified time.
   */
  int getNextEventTimeIndex(long timeUs);

  /**
   * Returns the number of event times, where events are defined as points in time at which the cues
   * returned by {@link #getCues(long)} changes.
   *
   * @return The number of event times.
   */
  int getEventTimeCount();

  /**
   * Returns the event time at a specified index.
   *
   * @param index The index of the event time to obtain.
   * @return The event time in microseconds.
   */
  long getEventTime(int index);

  /**
   * Retrieve the cues that should be displayed at a given time.
   *
   * @param timeUs The time in microseconds.
   * @return A list of cues that should be displayed, possibly empty.
   */
  List<Cue> getCues(long timeUs);
}
