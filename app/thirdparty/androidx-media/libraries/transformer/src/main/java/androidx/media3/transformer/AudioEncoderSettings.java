/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import android.media.MediaCodecInfo;
import androidx.media3.common.Format;
import androidx.media3.common.util.UnstableApi;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

/** Represents the audio encoder settings. */
@UnstableApi
public final class AudioEncoderSettings {
  /** Builds {@link AudioEncoderSettings} instances. */
  public static final class Builder {
    private int profile;
    private int bitrate;

    /** Creates a new instance. */
    public Builder() {
      profile = NO_VALUE;
      bitrate = NO_VALUE;
    }

    /**
     * Sets the {@link AudioEncoderSettings#profile}.
     *
     * <p>The default value is {@link #NO_VALUE} and the appropriate profile will be used
     * automatically.
     *
     * <p>The requested profile must be one of the values defined in {@link
     * MediaCodecInfo.CodecProfileLevel} and must be compatible with the requested {@linkplain
     * Transformer.Builder#setAudioMimeType(String) audio MIME type}. When using the {@link
     * DefaultEncoderFactory}, if the encoder does not support the requested profile, then it will
     * be ignored to avoid any encoder configuration failures.
     *
     * @param profile The profile.
     * @return This builder.
     */
    @CanIgnoreReturnValue
    public Builder setProfile(int profile) {
      this.profile = profile;
      return this;
    }

    /**
     * Sets the {@link AudioEncoderSettings#bitrate}.
     *
     * <p>The default value is {@link #NO_VALUE}.
     *
     * <p>The encoder may ignore the requested bitrate to improve the encoding quality.
     *
     * @param bitrate The bitrate in bits per second.
     * @return This builder.
     */
    @CanIgnoreReturnValue
    public Builder setBitrate(int bitrate) {
      this.bitrate = bitrate;
      return this;
    }

    /** Builds the instance. */
    public AudioEncoderSettings build() {
      return new AudioEncoderSettings(profile, bitrate);
    }
  }

  /** A value for various fields to indicate that the field's value is unknown or not applicable. */
  public static final int NO_VALUE = Format.NO_VALUE;

  /** A default {@link AudioEncoderSettings}. */
  public static final AudioEncoderSettings DEFAULT = new Builder().build();

  /** The encoding profile. */
  public final int profile;

  /** The encoding bitrate in bits per second. */
  public final int bitrate;

  private AudioEncoderSettings(int profile, int bitrate) {
    this.profile = profile;
    this.bitrate = bitrate;
  }
}
