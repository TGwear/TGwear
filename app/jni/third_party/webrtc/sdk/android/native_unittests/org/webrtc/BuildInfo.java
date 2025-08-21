/*
 * Copyright (c) 2015-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.os.Build;
import org.webrtc.CalledByNative;

public final class BuildInfo {
  public static String getDevice() {
    return Build.DEVICE;
  }

  @CalledByNative
  public static String getDeviceModel() {
    return Build.MODEL;
  }

  public static String getProduct() {
    return Build.PRODUCT;
  }

  @CalledByNative
  public static String getBrand() {
    return Build.BRAND;
  }

  @CalledByNative
  public static String getDeviceManufacturer() {
    return Build.MANUFACTURER;
  }

  @CalledByNative
  public static String getAndroidBuildId() {
    return Build.ID;
  }

  @CalledByNative
  public static String getBuildType() {
    return Build.TYPE;
  }

  @CalledByNative
  public static String getBuildRelease() {
    return Build.VERSION.RELEASE;
  }

  @CalledByNative
  public static int getSdkVersion() {
    return Build.VERSION.SDK_INT;
  }
}
