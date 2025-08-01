/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.session.common;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/** Custom Parcelable class to test sending/receiving user parcelables between processes. */
@SuppressLint("BanParcelableUsage")
public class CustomParcelable implements Parcelable {

  private int value;

  public CustomParcelable(int value) {
    this.value = value;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @SuppressLint("UnknownNullness") // Parcel dest
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(value);
  }

  public static final Parcelable.Creator<CustomParcelable> CREATOR =
      new Parcelable.Creator<CustomParcelable>() {
        @Override
        public CustomParcelable createFromParcel(Parcel in) {
          int value = in.readInt();
          return new CustomParcelable(value);
        }

        @Override
        public CustomParcelable[] newArray(int size) {
          return new CustomParcelable[size];
        }
      };
}
