/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static com.google.common.truth.Truth.assertThat;

import android.os.Bundle;
import androidx.media3.common.Player.PositionInfo;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link Player.PositionInfo}. */
@RunWith(AndroidJUnit4.class)
public class PositionInfoTest {

  @Test
  public void roundTripViaBundle_ofPositionInfoWithoutObjectFields_yieldsEqualInstance() {
    PositionInfo positionInfo =
        new PositionInfo(
            /* windowUid= */ null,
            /* mediaItemIndex= */ 23,
            new MediaItem.Builder().setMediaId("1234").build(),
            /* periodUid= */ null,
            /* periodIndex= */ 11,
            /* positionMs= */ 8787L,
            /* contentPositionMs= */ 12L,
            /* adGroupIndex= */ 2,
            /* adIndexInAdGroup= */ 444);

    assertThat(PositionInfo.fromBundle(positionInfo.toBundle())).isEqualTo(positionInfo);
  }

  @Test
  public void roundTripViaBundle_ofPositionInfoWithWindowUid_yieldsNullWindowUid() {
    PositionInfo positionInfo =
        new PositionInfo(
            /* windowUid= */ new Object(),
            /* mediaItemIndex= */ 23,
            MediaItem.fromUri("https://exoplayer.dev"),
            /* periodUid= */ null,
            /* periodIndex= */ 11,
            /* positionMs= */ 8787L,
            /* contentPositionMs= */ 12L,
            /* adGroupIndex= */ 2,
            /* adIndexInAdGroup= */ 444);

    PositionInfo positionInfoFromBundle = PositionInfo.fromBundle(positionInfo.toBundle());
    assertThat(positionInfoFromBundle.windowUid).isNull();
  }

  @Test
  public void roundTripViaBundle_ofPositionInfoWithPeriodUid_yieldsNullPeriodUid() {
    PositionInfo positionInfo =
        new PositionInfo(
            /* windowUid= */ null,
            /* mediaItemIndex= */ 23,
            MediaItem.fromUri("https://exoplayer.dev"),
            /* periodUid= */ new Object(),
            /* periodIndex= */ 11,
            /* positionMs= */ 8787L,
            /* contentPositionMs= */ 12L,
            /* adGroupIndex= */ 2,
            /* adIndexInAdGroup= */ 444);

    PositionInfo positionInfoFromBundle = PositionInfo.fromBundle(positionInfo.toBundle());
    assertThat(positionInfoFromBundle.periodUid).isNull();
  }

  @Test
  public void roundTripViaBundle_withDefaultValues_yieldsEqualInstance() {
    PositionInfo defaultPositionInfo =
        new PositionInfo(
            /* windowUid= */ null,
            /* mediaItemIndex= */ 0,
            /* mediaItem= */ null,
            /* periodUid= */ null,
            /* periodIndex= */ 0,
            /* positionMs= */ 0,
            /* contentPositionMs= */ 0,
            /* adGroupIndex= */ C.INDEX_UNSET,
            /* adIndexInAdGroup= */ C.INDEX_UNSET);

    PositionInfo roundTripValue = PositionInfo.fromBundle(defaultPositionInfo.toBundle());

    assertThat(roundTripValue).isEqualTo(defaultPositionInfo);
  }

  @Test
  public void toBundle_withDefaultValues_omitsAllData() {
    PositionInfo defaultPositionInfo =
        new PositionInfo(
            /* windowUid= */ null,
            /* mediaItemIndex= */ 0,
            /* mediaItem= */ null,
            /* periodUid= */ null,
            /* periodIndex= */ 0,
            /* positionMs= */ 0,
            /* contentPositionMs= */ 0,
            /* adGroupIndex= */ C.INDEX_UNSET,
            /* adIndexInAdGroup= */ C.INDEX_UNSET);

    Bundle bundle =
        defaultPositionInfo.toBundle(/* controllerInterfaceVersion= */ Integer.MAX_VALUE);

    assertThat(bundle.isEmpty()).isTrue();
  }

  @Test
  public void toBundle_withDefaultValuesForControllerInterfaceBefore3_includesDefaultValues() {
    // Controller before version 3 uses invalid default values for indices and the Bundle should
    // always include them to avoid using the default values in the controller code.
    PositionInfo defaultPositionInfo =
        new PositionInfo(
            /* windowUid= */ null,
            /* mediaItemIndex= */ 0,
            /* mediaItem= */ null,
            /* periodUid= */ null,
            /* periodIndex= */ 0,
            /* positionMs= */ 0,
            /* contentPositionMs= */ 0,
            /* adGroupIndex= */ C.INDEX_UNSET,
            /* adIndexInAdGroup= */ C.INDEX_UNSET);

    Bundle bundle = defaultPositionInfo.toBundle(/* controllerInterfaceVersion= */ 2);

    assertThat(bundle.keySet())
        .containsAtLeast(
            PositionInfo.FIELD_MEDIA_ITEM_INDEX,
            PositionInfo.FIELD_CONTENT_POSITION_MS,
            PositionInfo.FIELD_PERIOD_INDEX,
            PositionInfo.FIELD_POSITION_MS);
  }
}
