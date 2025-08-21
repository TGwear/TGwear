/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.mp4;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.SniffFailure;
import com.google.common.primitives.ImmutableIntArray;

/**
 * A {@link SniffFailure} indicating none of the brands declared in the {@code ftyp} box of the MP4
 * file are supported (see ISO 14496-12:2012 section 4.3).
 */
@UnstableApi
public final class UnsupportedBrandsSniffFailure implements SniffFailure {

  /** The {@code major_brand} from the {@code ftyp} box. */
  public final int majorBrand;

  /** The {@code compatible_brands} list from the {@code ftyp} box. */
  public final ImmutableIntArray compatibleBrands;

  public UnsupportedBrandsSniffFailure(int majorBrand, @Nullable int[] compatibleBrands) {
    this.majorBrand = majorBrand;
    this.compatibleBrands =
        compatibleBrands != null
            ? ImmutableIntArray.copyOf(compatibleBrands)
            : ImmutableIntArray.of();
  }
}
