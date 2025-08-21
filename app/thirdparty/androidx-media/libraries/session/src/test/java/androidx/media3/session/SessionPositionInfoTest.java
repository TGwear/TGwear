/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static com.google.common.truth.Truth.assertThat;

import android.os.Bundle;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player.PositionInfo;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link SessionPositionInfo}. */
@RunWith(AndroidJUnit4.class)
public class SessionPositionInfoTest {

  @Test
  public void roundTripViaBundle_yieldsEqualInstance() {
    SessionPositionInfo testSessionPositionInfo =
        new SessionPositionInfo(
            new PositionInfo(
                /* windowUid= */ null,
                /* mediaItemIndex= */ 33,
                new MediaItem.Builder().setMediaId("1234").build(),
                /* periodUid= */ null,
                /* periodIndex= */ 44,
                /* positionMs= */ 233L,
                /* contentPositionMs= */ 333L,
                /* adGroupIndex= */ 2,
                /* adIndexInAdGroup= */ 8),
            /* isPlayingAd= */ true,
            /* eventTimeMs= */ 103L,
            /* durationMs= */ 400L,
            /* bufferedPositionMs= */ 200L,
            /* bufferedPercentage= */ 50,
            /* totalBufferedDurationMs= */ 500L,
            /* currentLiveOffsetMs= */ 20L,
            /* contentDurationMs= */ 400L,
            /* contentBufferedPositionMs= */ 223L);
    Bundle sessionPositionInfoBundle =
        testSessionPositionInfo.toBundle(MediaControllerStub.VERSION_INT);
    SessionPositionInfo sessionPositionInfo =
        SessionPositionInfo.fromBundle(sessionPositionInfoBundle);
    assertThat(sessionPositionInfo).isEqualTo(testSessionPositionInfo);
  }

  @Test
  public void constructor_invalidIsPlayingAd_throwsIllegalArgumentException() {
    Assert.assertThrows(
        IllegalArgumentException.class,
        () ->
            new SessionPositionInfo(
                new PositionInfo(
                    /* windowUid= */ null,
                    /* mediaItemIndex= */ 33,
                    MediaItem.EMPTY,
                    /* periodUid= */ null,
                    /* periodIndex= */ 44,
                    /* positionMs= */ 233L,
                    /* contentPositionMs= */ 333L,
                    /* adGroupIndex= */ 2,
                    /* adIndexInAdGroup= */ C.INDEX_UNSET),
                /* isPlayingAd= */ false,
                /* eventTimeMs= */ 103L,
                /* durationMs= */ 400L,
                /* bufferedPositionMs= */ 200L,
                /* bufferedPercentage= */ 50,
                /* totalBufferedDurationMs= */ 500L,
                /* currentLiveOffsetMs= */ 20L,
                /* contentDurationMs= */ 400L,
                /* contentBufferedPositionMs= */ 223L));
  }

  @Test
  public void roundTripViaBundle_withDefaultValues_yieldsEqualInstance() {
    SessionPositionInfo roundTripValue =
        SessionPositionInfo.fromBundle(
            SessionPositionInfo.DEFAULT.toBundle(MediaControllerStub.VERSION_INT));

    assertThat(roundTripValue).isEqualTo(SessionPositionInfo.DEFAULT);
  }

  @Test
  public void toBundle_withDefaultValues_omitsAllData() {
    Bundle bundle =
        SessionPositionInfo.DEFAULT.toBundle(/* controllerInterfaceVersion= */ Integer.MAX_VALUE);

    assertThat(bundle.isEmpty()).isTrue();
  }

  @Test
  public void
      toBundle_withDefaultValuesForControllerInterfaceBefore3_includesPositionInfoAndBufferedValues() {
    // Controller before version 3 uses invalid default values for indices in PositionInfo and for
    // the buffered positions. The Bundle should always include these fields to avoid using the
    // invalid defaults.
    Bundle bundle = SessionPositionInfo.DEFAULT.toBundle(/* controllerInterfaceVersion= */ 2);

    assertThat(bundle.keySet())
        .containsAtLeast(
            SessionPositionInfo.FIELD_BUFFERED_POSITION_MS,
            SessionPositionInfo.FIELD_CONTENT_BUFFERED_POSITION_MS,
            SessionPositionInfo.FIELD_POSITION_INFO);
  }
}
