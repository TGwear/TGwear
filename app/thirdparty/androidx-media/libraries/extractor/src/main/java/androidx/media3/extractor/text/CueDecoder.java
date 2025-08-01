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
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.UnstableApi;
import java.util.ArrayList;
import java.util.List;

/** Decodes data encoded by {@link CueEncoder}. */
@UnstableApi
public final class CueDecoder {

  /** Key under which the list of cues is saved in the {@link Bundle}. */
  /* package */ static final String BUNDLE_FIELD_CUES = "c";

  /** Key under which the duration is saved in the {@link Bundle}. */
  /* package */ static final String BUNDLE_FIELD_DURATION_US = "d";

  /**
   * Decodes a byte array into a {@link CuesWithTiming} instance.
   *
   * @param startTimeUs The value for {@link CuesWithTiming#startTimeUs} (this is not encoded in
   *     {@code bytes}).
   * @param bytes Byte array containing data produced by {@link CueEncoder#encode(List, long)}
   * @param offset The start index of cue data in {@code bytes}.
   * @param length The length of cue data in {@code bytes}.
   * @return Decoded {@link CuesWithTiming} instance.
   */
  public CuesWithTiming decode(long startTimeUs, byte[] bytes, int offset, int length) {
    Parcel parcel = Parcel.obtain();
    parcel.unmarshall(bytes, offset, length);
    parcel.setDataPosition(0);
    Bundle bundle = parcel.readBundle(Bundle.class.getClassLoader());
    parcel.recycle();
    ArrayList<Bundle> bundledCues =
        Assertions.checkNotNull(bundle.getParcelableArrayList(BUNDLE_FIELD_CUES));
    return new CuesWithTiming(
        BundleCollectionUtil.fromBundleList(Cue::fromBundle, bundledCues),
        startTimeUs,
        bundle.getLong(BUNDLE_FIELD_DURATION_US));
  }
}
