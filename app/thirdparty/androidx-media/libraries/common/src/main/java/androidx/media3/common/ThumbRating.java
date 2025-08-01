/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static androidx.media3.common.util.Assertions.checkArgument;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import java.util.Objects;

/** A rating expressed as "thumbs up" or "thumbs down". */
public final class ThumbRating extends Rating {

  private final boolean rated;
  private final boolean isThumbsUp;

  /** Creates a unrated instance. */
  public ThumbRating() {
    rated = false;
    isThumbsUp = false;
  }

  /**
   * Creates a rated instance.
   *
   * @param isThumbsUp {@code true} for "thumbs up", {@code false} for "thumbs down".
   */
  public ThumbRating(boolean isThumbsUp) {
    rated = true;
    this.isThumbsUp = isThumbsUp;
  }

  @Override
  public boolean isRated() {
    return rated;
  }

  /** Returns whether the rating is "thumbs up". */
  public boolean isThumbsUp() {
    return isThumbsUp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(rated, isThumbsUp);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (!(obj instanceof ThumbRating)) {
      return false;
    }
    ThumbRating other = (ThumbRating) obj;
    return isThumbsUp == other.isThumbsUp && rated == other.rated;
  }

  private static final @RatingType int TYPE = RATING_TYPE_THUMB;

  private static final String FIELD_RATED = Util.intToStringMaxRadix(1);
  private static final String FIELD_IS_THUMBS_UP = Util.intToStringMaxRadix(2);

  @UnstableApi
  @Override
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putInt(FIELD_RATING_TYPE, TYPE);
    bundle.putBoolean(FIELD_RATED, rated);
    bundle.putBoolean(FIELD_IS_THUMBS_UP, isThumbsUp);
    return bundle;
  }

  /** Restores a {@code ThumbRating} from a {@link Bundle}. */
  @UnstableApi
  public static ThumbRating fromBundle(Bundle bundle) {
    checkArgument(bundle.getInt(FIELD_RATING_TYPE, /* defaultValue= */ RATING_TYPE_UNSET) == TYPE);
    boolean rated = bundle.getBoolean(FIELD_RATED, /* defaultValue= */ false);
    return rated
        ? new ThumbRating(bundle.getBoolean(FIELD_IS_THUMBS_UP, /* defaultValue= */ false))
        : new ThumbRating();
  }
}
