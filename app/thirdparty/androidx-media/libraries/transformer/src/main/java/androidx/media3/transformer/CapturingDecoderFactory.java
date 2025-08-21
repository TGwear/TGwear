/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import android.view.Surface;
import androidx.annotation.Nullable;
import androidx.media3.common.Format;

/** A forwarding {@link Codec.DecoderFactory} that captures details about the codecs created. */
/* package */ final class CapturingDecoderFactory implements Codec.DecoderFactory {
  private final Codec.DecoderFactory decoderFactory;

  @Nullable private String audioDecoderName;
  @Nullable private String videoDecoderName;

  public CapturingDecoderFactory(Codec.DecoderFactory decoderFactory) {
    this.decoderFactory = decoderFactory;
  }

  @Override
  public Codec createForAudioDecoding(Format format) throws ExportException {
    Codec audioDecoder = decoderFactory.createForAudioDecoding(format);
    audioDecoderName = audioDecoder.getName();
    return audioDecoder;
  }

  @Override
  public Codec createForVideoDecoding(
      Format format, Surface outputSurface, boolean requestSdrToneMapping) throws ExportException {
    Codec videoDecoder =
        decoderFactory.createForVideoDecoding(format, outputSurface, requestSdrToneMapping);
    videoDecoderName = videoDecoder.getName();
    return videoDecoder;
  }

  /**
   * Returns the name of the last audio {@linkplain Codec decoder} created, or {@code null} if none
   * were created.
   */
  @Nullable
  public String getAudioDecoderName() {
    return audioDecoderName;
  }

  /**
   * Returns the name of the last video {@linkplain Codec decoder} created, or {@code null} if none
   * were created.
   */
  @Nullable
  public String getVideoDecoderName() {
    return videoDecoderName;
  }
}
