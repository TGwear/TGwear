/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.exoplayer.playback.gts;

import static androidx.media3.test.exoplayer.playback.gts.GtsTestUtil.shouldSkipWidevineTest;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import androidx.media3.common.MimeTypes;
import androidx.media3.common.Player;
import androidx.media3.common.util.Util;
import androidx.media3.test.utils.ActionSchedule;
import androidx.media3.test.utils.HostActivity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test playback of encrypted DASH streams using different CENC scheme types. */
@RunWith(AndroidJUnit4.class)
public final class CommonEncryptionDrmTest {

  private static final String TAG = "CencDrmTest";

  private static final String ID_AUDIO = "0";
  private static final String[] IDS_VIDEO = new String[] {"1", "2"};

  // Seeks help reproduce playback issues in certain devices.
  private static final ActionSchedule ACTION_SCHEDULE_WITH_SEEKS =
      new ActionSchedule.Builder(TAG)
          .waitForPlaybackState(Player.STATE_READY)
          .delay(30000)
          .seekAndWait(300000)
          .delay(10000)
          .seekAndWait(270000)
          .delay(10000)
          .seekAndWait(200000)
          .delay(10000)
          .seekAndWait(732000)
          .build();

  @Rule public ActivityTestRule<HostActivity> testRule = new ActivityTestRule<>(HostActivity.class);

  private DashTestRunner testRunner;

  @Before
  public void setUp() {
    assumeFalse(shouldSkipWidevineTest(testRule.getActivity()));

    testRunner =
        new DashTestRunner(TAG, testRule.getActivity())
            .setWidevineInfo(MimeTypes.VIDEO_H264, false)
            .setActionSchedule(ACTION_SCHEDULE_WITH_SEEKS)
            .setAudioVideoFormats(ID_AUDIO, IDS_VIDEO)
            .setCanIncludeAdditionalVideoFormats(true);
  }

  @After
  public void tearDown() {
    testRunner = null;
  }

  @Test
  public void cencSchemeTypeV18() {
    testRunner
        .setStreamName("test_widevine_h264_scheme_cenc")
        .setManifestUrl(DashTestData.WIDEVINE_SCHEME_CENC)
        .run();
  }

  @Test
  public void cbcsSchemeTypeV25() {
    // cbcs support was added in API 24, but it is stable from API 25 onwards.
    // See [internal: b/65634809].
    assumeTrue(Util.SDK_INT >= 25);
    testRunner
        .setStreamName("test_widevine_h264_scheme_cbcs")
        .setManifestUrl(DashTestData.WIDEVINE_SCHEME_CBCS)
        .run();
  }
}
