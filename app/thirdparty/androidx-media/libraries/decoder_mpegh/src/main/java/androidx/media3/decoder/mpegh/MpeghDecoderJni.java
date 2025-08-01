/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.mpegh;

import java.nio.ByteBuffer;

/** JNI wrapper for the libmpegh MPEG-H decoder. */
public class MpeghDecoderJni {

  private long decoderHandle; // used by JNI only to hold the native context.

  public MpeghDecoderJni() {}

  /**
   * Initializes the native MPEG-H decoder.
   *
   * @param cicpIndex The desired target layout CICP index.
   * @param mhaConfig The byte array holding the audio specific configuration for MHA content.
   * @param mhaConfigLength Length of audio specific configuration.
   * @throws MpeghDecoderException If initialization fails.
   */
  public native void init(int cicpIndex, byte[] mhaConfig, int mhaConfigLength)
      throws MpeghDecoderException;

  /** Destroys the native MPEG-H decoder. */
  public native void destroy();

  /**
   * Processes data (access units) and corresponding PTS inside of the native MPEG-H decoder.
   *
   * @param inputBuffer The direct byte buffer holding the access unit.
   * @param inputLength The length of the direct byte buffer.
   * @param timestampUs The presentation timestamp of the access unit, in microseconds.
   * @throws MpeghDecoderException If processing fails.
   */
  public native void process(ByteBuffer inputBuffer, int inputLength, long timestampUs)
      throws MpeghDecoderException;

  /**
   * Obtains decoded samples from the native MPEG-H decoder and writes them into {@code buffer} at
   * position {@code writePos}.
   *
   * <p>NOTE: The decoder returns the samples as 16bit values.
   *
   * @param buffer The direct byte buffer to write the decoded samples to.
   * @param writePos The start position in the byte buffer to write the decoded samples to.
   * @return The number of bytes written to buffer.
   * @throws MpeghDecoderException If obtaining samples fails.
   */
  public native int getSamples(ByteBuffer buffer, int writePos) throws MpeghDecoderException;

  /**
   * Flushes the native MPEG-H decoder and writes available output samples into a sample queue.
   *
   * @throws MpeghDecoderException If flushing fails.
   */
  public native void flushAndGet() throws MpeghDecoderException;

  /**
   * Gets the number of output channels from the native MPEG-H decoder.
   *
   * <p>NOTE: This information belongs to the last audio frame obtained from {@link
   * #getSamples(ByteBuffer, int)} or {@link #flushAndGet()}.
   *
   * @return The number of output channels.
   */
  public native int getNumChannels();

  /**
   * Gets the output sample rate from the native MPEG-H decoder.
   *
   * <p>NOTE: This information belongs to the last audio frame obtained from {@link
   * #getSamples(ByteBuffer, int)} or {@link #flushAndGet()}.
   *
   * @return The output sample rate.
   */
  public native int getSamplerate();

  /**
   * Gets the PTS from the native MPEG-H decoder, in microseconds.
   *
   * <p>NOTE: This information belongs to the last audio frame obtained from {@link
   * #getSamples(ByteBuffer, int)} or {@link #flushAndGet()}.
   *
   * @return The output presentation timestamp.
   */
  public native long getPts();

  /**
   * Flushes the native MPEG-H decoder.
   *
   * @throws MpeghDecoderException If flushing fails.
   */
  public native void flush() throws MpeghDecoderException;
}
