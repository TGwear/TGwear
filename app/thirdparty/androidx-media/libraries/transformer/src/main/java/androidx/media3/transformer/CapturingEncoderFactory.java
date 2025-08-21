/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import androidx.annotation.Nullable;
import androidx.media3.common.Format;

/** A forwarding {@link Codec.EncoderFactory} that captures details about the codecs created. */
/* package */ final class CapturingEncoderFactory implements Codec.EncoderFactory {
  private final Codec.EncoderFactory encoderFactory;

  @Nullable private String audioEncoderName;
  @Nullable private String videoEncoderName;

  public CapturingEncoderFactory(Codec.EncoderFactory encoderFactory) {
    this.encoderFactory = encoderFactory;
  }

  @Override
  public Codec createForAudioEncoding(Format format) throws ExportException {
    Codec audioEncoder = encoderFactory.createForAudioEncoding(format);
    audioEncoderName = audioEncoder.getName();
    return audioEncoder;
  }

  @Override
  public Codec createForVideoEncoding(Format format) throws ExportException {
    Codec videoEncoder = encoderFactory.createForVideoEncoding(format);
    videoEncoderName = videoEncoder.getName();
    return videoEncoder;
  }

  @Override
  public boolean audioNeedsEncoding() {
    return encoderFactory.audioNeedsEncoding();
  }

  @Override
  public boolean videoNeedsEncoding() {
    return encoderFactory.videoNeedsEncoding();
  }

  /**
   * Returns the name of the last audio {@linkplain Codec encoder} created, or {@code null} if none
   * were created.
   */
  @Nullable
  public String getAudioEncoderName() {
    return audioEncoderName;
  }

  /**
   * Returns the name of the last video {@linkplain Codec encoder} created, or {@code null} if none
   * were created.
   */
  @Nullable
  public String getVideoEncoderName() {
    return videoEncoderName;
  }
}
