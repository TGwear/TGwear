/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Class for holding the native pointer of a histogram. Since there is no way to destroy a
 * histogram, please don't create unnecessary instances of this object. This class is thread safe.
 *
 * Usage example:
 * private static final Histogram someMetricHistogram =
 *     Histogram.createCounts("WebRTC.Video.SomeMetric", 1, 10000, 50);
 * someMetricHistogram.addSample(someVariable);
 */
class Histogram {
  private final long handle;

  private Histogram(long handle) {
    this.handle = handle;
  }

  static public Histogram createCounts(String name, int min, int max, int bucketCount) {
    return new Histogram(nativeCreateCounts(name, min, max, bucketCount));
  }

  static public Histogram createEnumeration(String name, int max) {
    return new Histogram(nativeCreateEnumeration(name, max));
  }

  public void addSample(int sample) {
    nativeAddSample(handle, sample);
  }

  private static native long nativeCreateCounts(String name, int min, int max, int bucketCount);
  private static native long nativeCreateEnumeration(String name, int max);
  private static native void nativeAddSample(long handle, int sample);
}
