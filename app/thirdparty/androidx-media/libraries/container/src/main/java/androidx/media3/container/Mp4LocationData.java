/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.container;

import static androidx.media3.common.util.Assertions.checkArgument;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;
import com.google.common.primitives.Floats;

/**
 * Stores MP4 location data.
 *
 * <p>The location data is typically read/written in the "udta" box (user data box, defined in
 * ISO/IEC 14496-12).
 */
@UnstableApi
public final class Mp4LocationData implements Metadata.Entry {

  /** The latitude, in degrees. */
  public final float latitude;

  /** The longitude, in degrees. */
  public final float longitude;

  /**
   * Creates an instance.
   *
   * @param latitude The latitude, in degrees. Its value must be in the range [-90, 90].
   * @param longitude The longitude, in degrees. Its value must be in the range [-180, 180].
   */
  public Mp4LocationData(
      @FloatRange(from = -90.0, to = 90.0) float latitude,
      @FloatRange(from = -180.0, to = 180.0) float longitude) {
    checkArgument(
        latitude >= -90.0f && latitude <= 90.0f && longitude >= -180.0f && longitude <= 180.0f,
        "Invalid latitude or longitude");
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Mp4LocationData other = (Mp4LocationData) obj;
    return latitude == other.latitude && longitude == other.longitude;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + Floats.hashCode(latitude);
    result = 31 * result + Floats.hashCode(longitude);
    return result;
  }

  @Override
  public String toString() {
    return "xyz: latitude=" + latitude + ", longitude=" + longitude;
  }
}
