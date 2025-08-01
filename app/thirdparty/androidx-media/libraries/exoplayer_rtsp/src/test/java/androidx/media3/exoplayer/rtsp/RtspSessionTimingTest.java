/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import androidx.media3.common.C;
import androidx.media3.common.ParserException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link RtspSessionTiming}. */
@RunWith(AndroidJUnit4.class)
public class RtspSessionTimingTest {
  @Test
  public void parseTiming_withNowLiveTiming() throws Exception {
    RtspSessionTiming sessionTiming = RtspSessionTiming.parseTiming("npt=now-");
    assertThat(sessionTiming.getDurationMs()).isEqualTo(C.TIME_UNSET);
    assertThat(sessionTiming.isLive()).isTrue();
  }

  @Test
  public void parseTiming_withZeroLiveTiming() throws Exception {
    RtspSessionTiming sessionTiming = RtspSessionTiming.parseTiming("npt=0-");
    assertThat(sessionTiming.getDurationMs()).isEqualTo(C.TIME_UNSET);
    assertThat(sessionTiming.isLive()).isTrue();
  }

  @Test
  public void parseTiming_withDecimalZeroLiveTiming() throws Exception {
    RtspSessionTiming sessionTiming = RtspSessionTiming.parseTiming("npt=0.000-");
    assertThat(sessionTiming.getDurationMs()).isEqualTo(C.TIME_UNSET);
    assertThat(sessionTiming.isLive()).isTrue();
  }

  @Test
  public void parseTiming_withRangeTiming() throws Exception {
    RtspSessionTiming sessionTiming = RtspSessionTiming.parseTiming("npt=0.000-32.054");
    assertThat(sessionTiming.getDurationMs()).isEqualTo(32054);
    assertThat(sessionTiming.isLive()).isFalse();
  }

  @Test
  public void parseTiming_withRangeTimingAndColonSeparator() throws Exception {
    RtspSessionTiming sessionTiming = RtspSessionTiming.parseTiming("npt:0.000-32.054");
    assertThat(sessionTiming.getDurationMs()).isEqualTo(32054);
    assertThat(sessionTiming.isLive()).isFalse();
  }

  @Test
  public void parseTiming_withInvalidRangeTiming_throwsParserException() {
    assertThrows(ParserException.class, () -> RtspSessionTiming.parseTiming("npt=10.000-2.054"));
  }
}
