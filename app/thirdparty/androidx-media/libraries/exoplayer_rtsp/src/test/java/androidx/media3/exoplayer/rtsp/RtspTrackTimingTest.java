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

import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.common.ParserException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link RtspTrackTiming}. */
@RunWith(AndroidJUnit4.class)
public class RtspTrackTimingTest {
  @Test
  public void parseTiming_withSeqNumberAndRtpTime() throws Exception {
    String rtpInfoString =
        "url=rtsp://video.example.com/twister/video;seq=12312232;rtptime=78712811";

    ImmutableList<RtspTrackTiming> trackTimingList =
        RtspTrackTiming.parseTrackTiming(rtpInfoString, Uri.parse("rtsp://video.example.com"));

    assertThat(trackTimingList).hasSize(1);
    RtspTrackTiming trackTiming = trackTimingList.get(0);
    assertThat(trackTiming.uri).isEqualTo(Uri.parse("rtsp://video.example.com/twister/video"));
    assertThat(trackTiming.sequenceNumber).isEqualTo(12312232);
    assertThat(trackTiming.rtpTimestamp).isEqualTo(78712811);
  }

  @Test
  public void parseTiming_withSeqNumberOnly() throws Exception {
    String rtpInfoString =
        "url=rtsp://foo.com/bar.avi/streamid=0;seq=45102,url=rtsp://foo.com/bar.avi/streamid=1;seq=30211";

    ImmutableList<RtspTrackTiming> trackTimingList =
        RtspTrackTiming.parseTrackTiming(rtpInfoString, Uri.parse("rtsp://foo.com"));

    assertThat(trackTimingList).hasSize(2);
    RtspTrackTiming trackTiming = trackTimingList.get(0);
    assertThat(trackTiming.uri).isEqualTo(Uri.parse("rtsp://foo.com/bar.avi/streamid=0"));
    assertThat(trackTiming.sequenceNumber).isEqualTo(45102);
    assertThat(trackTiming.rtpTimestamp).isEqualTo(C.TIME_UNSET);
    trackTiming = trackTimingList.get(1);
    assertThat(trackTiming.uri).isEqualTo(Uri.parse("rtsp://foo.com/bar.avi/streamid=1"));
    assertThat(trackTiming.sequenceNumber).isEqualTo(30211);
    assertThat(trackTiming.rtpTimestamp).isEqualTo(C.TIME_UNSET);
  }

  @Test
  public void parseTiming_withInvalidParameter_throws() {
    String rtpInfoString = "url=rtsp://video.example.com/twister/video;seq=123abc";

    assertThrows(
        ParserException.class,
        () ->
            RtspTrackTiming.parseTrackTiming(
                rtpInfoString, Uri.parse("rtsp://video.example.com/twister")));
  }

  @Test
  public void parseTiming_withNoParameter_throws() {
    String rtpInfoString = "url=rtsp://video.example.com/twister/video";

    assertThrows(
        ParserException.class,
        () ->
            RtspTrackTiming.parseTrackTiming(
                rtpInfoString, Uri.parse("rtsp://video.example.com/twister")));
  }

  @Test
  public void parseTiming_withNoUrl_throws() {
    String rtpInfoString = "seq=35421887";

    assertThrows(
        ParserException.class,
        () ->
            RtspTrackTiming.parseTrackTiming(
                rtpInfoString, Uri.parse("rtsp://video.example.com/twister")));
  }

  @Test
  public void resolveUri_withAbsoluteUri_succeeds() {
    Uri uri =
        RtspTrackTiming.resolveUri(
            "rtsp://video.example.com/twister/video=1?a2bfc09887ce",
            Uri.parse("rtsp://video.example.com/twister"));

    assertThat(uri).isEqualTo(Uri.parse("rtsp://video.example.com/twister/video=1?a2bfc09887ce"));
  }

  @Test
  public void resolveUri_withCompleteUriMissingScheme_succeeds() {
    Uri uri =
        RtspTrackTiming.resolveUri(
            "video.example.com/twister/video=1", Uri.parse("rtsp://video.example.com/twister"));

    assertThat(uri).isEqualTo(Uri.parse("rtsp://video.example.com/twister/video=1"));
  }

  @Test
  public void resolveUri_withPartialUriMissingScheme_succeeds() {
    Uri uri = RtspTrackTiming.resolveUri("video=1", Uri.parse("rtsp://video.example.com/twister"));

    assertThat(uri).isEqualTo(Uri.parse("rtsp://video.example.com/twister/video=1"));
  }

  @Test
  public void resolveUri_withMultipartPartialUriMissingScheme_succeeds() {
    Uri uri =
        RtspTrackTiming.resolveUri(
            "container/video=1", Uri.parse("rtsp://video.example.com/twister"));

    assertThat(uri).isEqualTo(Uri.parse("rtsp://video.example.com/twister/container/video=1"));
  }

  @Test
  public void resolveUri_withPartialUriMissingSchemeWithIpBaseUri_succeeds() {
    Uri uri = RtspTrackTiming.resolveUri("video=1", Uri.parse("rtsp://127.0.0.1:18888/test"));

    assertThat(uri).isEqualTo(Uri.parse("rtsp://127.0.0.1:18888/test/video=1"));
  }

  @Test
  public void resolveUri_withPartialUriMissingSchemeWithIpBaseUriWithSlash_succeeds() {
    Uri uri = RtspTrackTiming.resolveUri("video=1", Uri.parse("rtsp://127.0.0.1:18888/test/"));

    assertThat(uri).isEqualTo(Uri.parse("rtsp://127.0.0.1:18888/test/video=1"));
  }

  @Test
  public void resolveUri_withSessionUriMissingScheme_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> RtspTrackTiming.resolveUri("video=1", Uri.parse("127.0.0.1:18888/test")));
  }
}
