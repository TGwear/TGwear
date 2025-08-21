/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.mp4;

/* package */ final class DefaultSampleValues {

  public final int sampleDescriptionIndex;
  public final int duration;
  public final int size;
  public final int flags;

  public DefaultSampleValues(int sampleDescriptionIndex, int duration, int size, int flags) {
    this.sampleDescriptionIndex = sampleDescriptionIndex;
    this.duration = duration;
    this.size = size;
    this.flags = flags;
  }
}
