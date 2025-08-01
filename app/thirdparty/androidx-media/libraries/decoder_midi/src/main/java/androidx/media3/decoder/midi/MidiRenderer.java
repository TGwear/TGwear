/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.midi;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.CryptoConfig;
import androidx.media3.exoplayer.audio.AudioRendererEventListener;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.audio.DecoderAudioRenderer;

/** Decodes and renders MIDI audio. */
@UnstableApi
public final class MidiRenderer extends DecoderAudioRenderer<MidiDecoder> {

  private final Context context;

  /**
   * @deprecated Use {@link #MidiRenderer(Context, Handler, AudioRendererEventListener, AudioSink)}
   *     instead.
   */
  @Deprecated
  public MidiRenderer(Context context) {
    this.context = context.getApplicationContext();
  }

  /** Creates the renderer instance. */
  public MidiRenderer(
      Context context,
      @Nullable Handler eventHandler,
      @Nullable AudioRendererEventListener eventListener,
      AudioSink audioSink) {
    super(eventHandler, eventListener, audioSink);
    this.context = context.getApplicationContext();
  }

  @Override
  public String getName() {
    return "MidiRenderer";
  }

  @Override
  protected @C.FormatSupport int supportsFormatInternal(Format format) {
    if (!MimeTypes.AUDIO_EXOPLAYER_MIDI.equals(format.sampleMimeType)) {
      return C.FORMAT_UNSUPPORTED_TYPE;
    }

    if (!sinkSupportsFormat(MidiDecoder.getDecoderOutputFormat())) {
      return C.FORMAT_UNSUPPORTED_SUBTYPE;
    }

    return C.FORMAT_HANDLED;
  }

  /**
   * {@inheritDoc}
   *
   * @hide
   */
  @Override
  protected MidiDecoder createDecoder(Format format, @Nullable CryptoConfig cryptoConfig)
      throws MidiDecoderException {
    return new MidiDecoder(context);
  }

  /**
   * {@inheritDoc}
   *
   * @hide
   */
  @Override
  protected Format getOutputFormat(MidiDecoder decoder) {
    return MidiDecoder.getDecoderOutputFormat();
  }
}
