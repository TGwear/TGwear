/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import android.content.Context;
import android.os.Looper;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.test.utils.FakeClock;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/**
 * A builder of {@link Transformer} instances for testing with Robolectric.
 *
 * <p>To transcode audio, add the required codecs using {@link TestUtil#addAudioDecoders} and {@link
 * TestUtil#addAudioEncoders}.
 *
 * <p>Transcoding video is unsupported in Robolectric tests. In order for a {@link Transformer} test
 * instance to succeed with video input, make sure to configure the export in such a way that video
 * samples are transmuxed (for example by not adding any video effects).
 *
 * <p>Images are unsupported in Robolectric tests.
 */
@UnstableApi
public final class TestTransformerBuilder {

  private final Context context;
  private final List<Transformer.Listener> listeners;
  private final Clock clock;

  private @MonotonicNonNull String audioMimeType;
  private boolean trimOptimizationEnabled;
  private long maxDelayBetweenMuxerSamplesMs;
  private AssetLoader.Factory assetLoaderFactory;
  private Muxer.Factory muxerFactory;
  private boolean fallbackEnabled;
  private Looper looper;

  /** Creates a new instance. */
  public TestTransformerBuilder(Context context) {
    this.context = context;
    listeners = new ArrayList<>();
    clock = new FakeClock(/* isAutoAdvancing= */ true);
    maxDelayBetweenMuxerSamplesMs = Transformer.DEFAULT_MAX_DELAY_BETWEEN_MUXER_SAMPLES_MS;
    assetLoaderFactory =
        new DefaultAssetLoaderFactory(
            context, new DefaultDecoderFactory.Builder(context).build(), clock);
    muxerFactory = new DefaultMuxer.Factory();
    looper = Util.getCurrentOrMainLooper();
  }

  /**
   * Sets the audio {@linkplain MimeTypes MIME type} of the output.
   *
   * @param audioMimeType The audio MIME type of the output.
   * @return This builder.
   * @see Transformer.Builder#setAudioMimeType(String)
   */
  @CanIgnoreReturnValue
  public TestTransformerBuilder setAudioMimeType(String audioMimeType) {
    this.audioMimeType = audioMimeType;
    return this;
  }

  /**
   * Sets whether to enable the trim optimization.
   *
   * @param trimOptimizationEnabled Whether to enable the trim optimization.
   * @return This builder.
   * @see Transformer.Builder#experimentalSetTrimOptimizationEnabled(boolean)
   */
  @CanIgnoreReturnValue
  public TestTransformerBuilder experimentalSetTrimOptimizationEnabled(
      boolean trimOptimizationEnabled) {
    this.trimOptimizationEnabled = trimOptimizationEnabled;
    return this;
  }

  /**
   * Sets the maximum delay allowed between output samples.
   *
   * @param maxDelayBetweenMuxerSamplesMs The maximum delay allowed between output samples, in
   *     milliseconds.
   * @return This builder.
   * @see Transformer.Builder#setMaxDelayBetweenMuxerSamplesMs(long)
   */
  @CanIgnoreReturnValue
  public TestTransformerBuilder setMaxDelayBetweenMuxerSamplesMs(
      long maxDelayBetweenMuxerSamplesMs) {
    this.maxDelayBetweenMuxerSamplesMs = maxDelayBetweenMuxerSamplesMs;
    return this;
  }

  /**
   * Adds a {@link Transformer.Listener}.
   *
   * @param listener A @link Transformer.Listener}.
   * @return This builder.
   * @see Transformer.Builder#addListener(Transformer.Listener)
   */
  @CanIgnoreReturnValue
  public TestTransformerBuilder addListener(Transformer.Listener listener) {
    listeners.add(listener);
    return this;
  }

  /**
   * Sets the {@link AssetLoader.Factory} to use.
   *
   * @param assetLoaderFactory The {@link AssetLoader.Factory} to use.
   * @return This builder.
   * @see Transformer.Builder#setAssetLoaderFactory(AssetLoader.Factory)
   */
  @CanIgnoreReturnValue
  public TestTransformerBuilder setAssetLoaderFactory(AssetLoader.Factory assetLoaderFactory) {
    this.assetLoaderFactory = assetLoaderFactory;
    return this;
  }

  /**
   * Sets the {@link Muxer.Factory} to use.
   *
   * @param muxerFactory The {@link Muxer.Factory} to use.
   * @return This builder.
   * @see Transformer.Builder#setMuxerFactory(Muxer.Factory)
   */
  @CanIgnoreReturnValue
  public TestTransformerBuilder setMuxerFactory(Muxer.Factory muxerFactory) {
    this.muxerFactory = muxerFactory;
    return this;
  }

  /**
   * Sets whether to enable {@linkplain DefaultEncoderFactory.Builder#setEnableFallback(boolean)
   * fallback}.
   *
   * <p>The default value is {@code false}.
   *
   * @param fallbackEnabled Whether to enable fallback.
   * @return This builder.
   * @see DefaultEncoderFactory.Builder#setEnableFallback(boolean)
   */
  @CanIgnoreReturnValue
  public TestTransformerBuilder setFallbackEnabled(boolean fallbackEnabled) {
    this.fallbackEnabled = fallbackEnabled;
    return this;
  }

  /**
   * Sets the {@link Looper} that must be used for all calls to the transformer and that is used to
   * call listeners on.
   *
   * @param looper The {@link Looper} to use.
   * @return This builder.
   * @see Transformer.Builder#setLooper(Looper)
   */
  @CanIgnoreReturnValue
  public TestTransformerBuilder setLooper(Looper looper) {
    this.looper = looper;
    return this;
  }

  /** Builds a {@link Transformer} instance for testing with Robolectric. */
  public Transformer build() {
    Codec.EncoderFactory encoderFactory =
        new DefaultEncoderFactory.Builder(context).setEnableFallback(fallbackEnabled).build();
    Transformer.Builder transformerBuilder =
        new Transformer.Builder(context)
            .experimentalSetTrimOptimizationEnabled(trimOptimizationEnabled)
            .setMaxDelayBetweenMuxerSamplesMs(maxDelayBetweenMuxerSamplesMs)
            .setAssetLoaderFactory(assetLoaderFactory)
            .setMuxerFactory(muxerFactory)
            .setEncoderFactory(encoderFactory)
            .setLooper(looper)
            .setClock(clock);
    if (audioMimeType != null) {
      transformerBuilder.setAudioMimeType(audioMimeType);
    }
    for (Transformer.Listener listener : listeners) {
      transformerBuilder.addListener(listener);
    }
    return transformerBuilder.build();
  }
}
