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
import android.os.Parcel;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link StreamKey}. */
@RunWith(AndroidJUnit4.class)
public class StreamKeyTest {

  @Test
  public void parcelable() {
    StreamKey streamKeyToParcel =
        new StreamKey(/* periodIndex= */ 1, /* groupIndex= */ 2, /* streamIndex= */ 3);
    Parcel parcel = Parcel.obtain();
    streamKeyToParcel.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);

    StreamKey streamKeyFromParcel = StreamKey.CREATOR.createFromParcel(parcel);
    assertThat(streamKeyFromParcel).isEqualTo(streamKeyToParcel);

    parcel.recycle();
  }

  @Test
  public void roundTripViaBundle_withDefaultPeriodIndex_yieldsEqualInstance() {
    StreamKey originalStreamKey = new StreamKey(/* groupIndex= */ 1, /* streamIndex= */ 2);

    StreamKey streamKeyFromBundle = StreamKey.fromBundle(originalStreamKey.toBundle());

    assertThat(originalStreamKey).isEqualTo(streamKeyFromBundle);
  }

  @Test
  public void roundTripViaBundle_toBundleSkipsDefaultValues_fromBundleRestoresThem() {
    StreamKey originalStreamKey = new StreamKey(/* groupIndex= */ 0, /* streamIndex= */ 0);

    Bundle streamKeyBundle = originalStreamKey.toBundle();

    assertThat(streamKeyBundle.keySet()).isEmpty();

    StreamKey streamKeyFromBundle = StreamKey.fromBundle(streamKeyBundle);

    assertThat(originalStreamKey).isEqualTo(streamKeyFromBundle);
  }

  @Test
  public void roundTripViaBundle_yieldsEqualInstance() {
    StreamKey originalStreamKey =
        new StreamKey(/* periodIndex= */ 10, /* groupIndex= */ 11, /* streamIndex= */ 12);

    StreamKey streamKeyFromBundle = StreamKey.fromBundle(originalStreamKey.toBundle());

    assertThat(originalStreamKey).isEqualTo(streamKeyFromBundle);
  }
}
