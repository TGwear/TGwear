/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.text;

import static com.google.common.truth.Truth.assertThat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.text.SpannedString;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link CueGroup}. */
@RunWith(AndroidJUnit4.class)
public class CueGroupTest {

  @Test
  public void bundleAndUnBundleCueGroup() {
    Cue textCue = new Cue.Builder().setText(SpannedString.valueOf("text")).build();
    Cue bitmapCue =
        new Cue.Builder().setBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)).build();
    ImmutableList<Cue> cues = ImmutableList.of(textCue, bitmapCue);
    CueGroup cueGroup = new CueGroup(cues, /* presentationTimeUs= */ 1_230_000);

    Parcel parcel = Parcel.obtain();
    try {
      parcel.writeBundle(cueGroup.toBundle());
      parcel.setDataPosition(0);

      Bundle bundle = parcel.readBundle();
      CueGroup filteredCueGroup = CueGroup.fromBundle(bundle);

      assertThat(filteredCueGroup.cues).containsExactly(textCue);
    } finally {
      parcel.recycle();
    }
  }
}
