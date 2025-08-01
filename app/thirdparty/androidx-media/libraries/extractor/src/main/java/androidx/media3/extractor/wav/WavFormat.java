/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.wav;

/** Format information for a WAV file. */
/* package */ final class WavFormat {

  /**
   * The format type. Standard format types are the "WAVE form Registration Number" constants
   * defined in RFC 2361 Appendix A.
   */
  public final int formatType;

  /** The number of channels. */
  public final int numChannels;

  /** The sample rate in Hertz. */
  public final int frameRateHz;

  /** The average bytes per second for the sample data. */
  public final int averageBytesPerSecond;

  /** The block size in bytes. */
  public final int blockSize;

  /** Bits per sample for a single channel. */
  public final int bitsPerSample;

  /** Extra data appended to the format chunk. */
  public final byte[] extraData;

  public WavFormat(
      int formatType,
      int numChannels,
      int frameRateHz,
      int averageBytesPerSecond,
      int blockSize,
      int bitsPerSample,
      byte[] extraData) {
    this.formatType = formatType;
    this.numChannels = numChannels;
    this.frameRateHz = frameRateHz;
    this.averageBytesPerSecond = averageBytesPerSecond;
    this.blockSize = blockSize;
    this.bitsPerSample = bitsPerSample;
    this.extraData = extraData;
  }
}
