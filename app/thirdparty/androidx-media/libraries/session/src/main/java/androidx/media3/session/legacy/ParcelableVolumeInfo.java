/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session.legacy;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.RestrictTo;
import androidx.media3.common.util.UnstableApi;

/**
 * Convenience class for passing information about the audio configuration of a {@link
 * MediaSessionCompat}.
 */
@UnstableApi
@RestrictTo(LIBRARY)
@SuppressLint("BanParcelableUsage")
public class ParcelableVolumeInfo implements Parcelable {
  public int volumeType;
  public int audioStream;
  public int controlType;
  public int maxVolume;
  public int currentVolume;

  public ParcelableVolumeInfo(
      int volumeType, int audioStream, int controlType, int maxVolume, int currentVolume) {
    this.volumeType = volumeType;
    this.audioStream = audioStream;
    this.controlType = controlType;
    this.maxVolume = maxVolume;
    this.currentVolume = currentVolume;
  }

  public ParcelableVolumeInfo(Parcel from) {
    volumeType = from.readInt();
    controlType = from.readInt();
    maxVolume = from.readInt();
    currentVolume = from.readInt();
    audioStream = from.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(volumeType);
    dest.writeInt(controlType);
    dest.writeInt(maxVolume);
    dest.writeInt(currentVolume);
    dest.writeInt(audioStream);
  }

  public static final Parcelable.Creator<ParcelableVolumeInfo> CREATOR =
      new Parcelable.Creator<ParcelableVolumeInfo>() {
        @Override
        public ParcelableVolumeInfo createFromParcel(Parcel in) {
          return new ParcelableVolumeInfo(in);
        }

        @Override
        public ParcelableVolumeInfo[] newArray(int size) {
          return new ParcelableVolumeInfo[size];
        }
      };
}
