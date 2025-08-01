/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link Rating} and its subclasses. */
@RunWith(AndroidJUnit4.class)
public class RatingTest {

  @Test
  public void unratedHeartRating() {
    HeartRating rating = new HeartRating();
    assertThat(rating.isRated()).isFalse();
    assertThat(roundTripViaBundle(rating)).isEqualTo(rating);
  }

  @Test
  public void ratedHeartRating() {
    boolean hasHeart = true;
    HeartRating rating = new HeartRating(hasHeart);
    assertThat(rating.isRated()).isTrue();
    assertThat(rating.isHeart()).isEqualTo(hasHeart);
    assertThat(roundTripViaBundle(rating)).isEqualTo(rating);
  }

  @Test
  public void unratedPercentageRating() {
    PercentageRating rating = new PercentageRating();
    assertThat(rating.isRated()).isFalse();
    assertThat(roundTripViaBundle(rating)).isEqualTo(rating);
  }

  @Test
  public void ratedPercentageRating() {
    float percentage = 20.5f;
    PercentageRating rating = new PercentageRating(percentage);
    assertThat(rating.isRated()).isTrue();
    assertThat(rating.getPercent()).isEqualTo(percentage);
    assertThat(roundTripViaBundle(rating)).isEqualTo(rating);
  }

  @Test
  public void unratedThumbRating() {
    ThumbRating rating = new ThumbRating();
    assertThat(rating.isRated()).isFalse();
    assertThat(roundTripViaBundle(rating)).isEqualTo(rating);
  }

  @Test
  public void ratedThumbRating() {
    boolean isThumbUp = true;
    ThumbRating rating = new ThumbRating(isThumbUp);
    assertThat(rating.isRated()).isTrue();
    assertThat(rating.isThumbsUp()).isEqualTo(isThumbUp);
    assertThat(roundTripViaBundle(rating)).isEqualTo(rating);
  }

  @Test
  public void unratedStarRating() {
    int maxStars = 5;
    StarRating rating = new StarRating(maxStars);
    assertThat(rating.isRated()).isFalse();
    assertThat(rating.getMaxStars()).isEqualTo(maxStars);
    assertThat(roundTripViaBundle(rating)).isEqualTo(rating);
  }

  @Test
  public void ratedStarRating() {
    int maxStars = 5;
    float starRating = 3.1f;
    StarRating rating = new StarRating(maxStars, starRating);
    assertThat(rating.isRated()).isTrue();
    assertThat(rating.getMaxStars()).isEqualTo(maxStars);
    assertThat(rating.getStarRating()).isEqualTo(starRating);
    assertThat(roundTripViaBundle(rating)).isEqualTo(rating);
  }

  private static Rating roundTripViaBundle(Rating rating) {
    return Rating.fromBundle(rating.toBundle());
  }
}
