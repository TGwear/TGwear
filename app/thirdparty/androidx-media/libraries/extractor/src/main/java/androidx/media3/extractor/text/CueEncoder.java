/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text;

import android.os.Bundle;
import android.os.Parcel;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.UnstableApi;
import java.util.ArrayList;
import java.util.List;

/** Encodes data that can be decoded by {@link CueDecoder}. */
@UnstableApi
public final class CueEncoder {

  /**
   * Encodes a {@link Cue} list and duration to a byte array that can be decoded by {@link
   * CueDecoder#decode}.
   *
   * @param cues Cues to be encoded.
   * @param durationUs Duration to be encoded, in microseconds.
   * @return The serialized byte array.
   */
  public byte[] encode(List<Cue> cues, long durationUs) {
    ArrayList<Bundle> bundledCues =
        BundleCollectionUtil.toBundleArrayList(cues, Cue::toSerializableBundle);
    Bundle allCuesBundle = new Bundle();
    allCuesBundle.putParcelableArrayList(CueDecoder.BUNDLE_FIELD_CUES, bundledCues);
    allCuesBundle.putLong(CueDecoder.BUNDLE_FIELD_DURATION_US, durationUs);
    Parcel parcel = Parcel.obtain();
    parcel.writeBundle(allCuesBundle);
    byte[] bytes = parcel.marshall();
    parcel.recycle();

    return bytes;
  }
}
