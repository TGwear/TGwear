/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static androidx.media3.common.util.Assertions.checkArgument;
import static androidx.media3.common.util.Assertions.checkNotNull;
import static androidx.media3.transformer.ExportException.ERROR_CODE_UNSPECIFIED;
import static androidx.media3.transformer.SampleConsumer.INPUT_RESULT_END_OF_STREAM;
import static androidx.media3.transformer.SampleConsumer.INPUT_RESULT_TRY_AGAIN_LATER;
import static androidx.media3.transformer.Transformer.PROGRESS_STATE_AVAILABLE;
import static androidx.media3.transformer.Transformer.PROGRESS_STATE_NOT_STARTED;
import static androidx.media3.transformer.TransformerUtil.getValidColor;
import static java.lang.Math.round;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.OnInputFrameProcessedListener;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.transformer.SampleConsumer.InputResult;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/**
 * An {@link AssetLoader} implementation that loads videos from {@linkplain
 * android.opengl.GLES10#GL_TEXTURE_2D traditional GLES texture} instances.
 *
 * <p>Typically instantiated in a custom {@link AssetLoader.Factory} saving a reference to the
 * created {@link TextureAssetLoader}. Provide video frames as input by calling {@link
 * #queueInputTexture}, then {@link #signalEndOfVideoInput() signal the end of input} when finished.
 * Those methods must be called from the same thread, which can be any thread.
 *
 * @deprecated Use {@link RawAssetLoader}.
 */
@Deprecated
@UnstableApi
public final class TextureAssetLoader implements AssetLoader {
  private final EditedMediaItem editedMediaItem;
  private final Listener assetLoaderListener;
  private final Format format;
  private final OnInputFrameProcessedListener frameProcessedListener;

  private @MonotonicNonNull SampleConsumer sampleConsumer;
  private @Transformer.ProgressState int progressState;
  private boolean isTrackAdded;
  private boolean isEndOfStreamSignaled;

  private volatile boolean isStarted;
  private volatile long lastQueuedPresentationTimeUs;

  /**
   * Creates an instance.
   *
   * <p>The {@link EditedMediaItem#durationUs}, {@link Format#width} and {@link Format#height} must
   * be set.
   *
   * @param editedMediaItem Information about the media item for which frames are provided.
   * @param assetLoaderListener Listener for asset loading events.
   * @param format Information about the format of video frames.
   * @param frameProcessedListener Listener for the event when a frame has been processed. The
   *     listener receives a GL sync object (if supported) to allow reusing the texture after it's
   *     no longer in use.
   */
  public TextureAssetLoader(
      EditedMediaItem editedMediaItem,
      Listener assetLoaderListener,
      Format format,
      OnInputFrameProcessedListener frameProcessedListener) {
    checkArgument(editedMediaItem.durationUs != C.TIME_UNSET);
    checkArgument(format.height != Format.NO_VALUE && format.width != Format.NO_VALUE);
    this.editedMediaItem = editedMediaItem;
    this.assetLoaderListener = assetLoaderListener;
    this.format =
        format
            .buildUpon()
            .setColorInfo(getValidColor(format.colorInfo))
            .setSampleMimeType(MimeTypes.VIDEO_RAW)
            .build();
    this.frameProcessedListener = frameProcessedListener;
    progressState = PROGRESS_STATE_NOT_STARTED;
  }

  @Override
  public void start() {
    progressState = PROGRESS_STATE_AVAILABLE;
    assetLoaderListener.onDurationUs(editedMediaItem.durationUs);
    assetLoaderListener.onTrackCount(1);
    isStarted = true;
  }

  @Override
  public @Transformer.ProgressState int getProgress(ProgressHolder progressHolder) {
    if (progressState == PROGRESS_STATE_AVAILABLE) {
      progressHolder.progress =
          round((lastQueuedPresentationTimeUs / (float) editedMediaItem.durationUs) * 100);
    }
    return progressState;
  }

  @Override
  public ImmutableMap<Integer, String> getDecoderNames() {
    return ImmutableMap.of();
  }

  @Override
  public void release() {
    progressState = PROGRESS_STATE_NOT_STARTED;
  }

  /**
   * Attempts to provide an input texture.
   *
   * <p>Must be called on the same thread as {@link #signalEndOfVideoInput}.
   *
   * @param texId The ID of the texture to queue.
   * @param presentationTimeUs The presentation time for the texture, in microseconds.
   * @return Whether the texture was successfully queued. If {@code false}, the caller should try
   *     again later.
   */
  public boolean queueInputTexture(int texId, long presentationTimeUs) {
    try {
      if (!isTrackAdded) {
        if (!isStarted) {
          return false;
        }
        assetLoaderListener.onTrackAdded(format, SUPPORTED_OUTPUT_TYPE_DECODED);
        isTrackAdded = true;
      }
      if (sampleConsumer == null) {
        @Nullable SampleConsumer sampleConsumer = assetLoaderListener.onOutputFormat(format);
        if (sampleConsumer == null) {
          return false;
        } else {
          this.sampleConsumer = sampleConsumer;
          sampleConsumer.setOnInputFrameProcessedListener(frameProcessedListener);
        }
      }
      @InputResult int result = sampleConsumer.queueInputTexture(texId, presentationTimeUs);
      if (result == INPUT_RESULT_TRY_AGAIN_LATER) {
        return false;
      }
      if (result == INPUT_RESULT_END_OF_STREAM) {
        isEndOfStreamSignaled = true;
      }
      lastQueuedPresentationTimeUs = presentationTimeUs;
      return true;
    } catch (ExportException e) {
      assetLoaderListener.onError(e);
    } catch (RuntimeException e) {
      assetLoaderListener.onError(ExportException.createForAssetLoader(e, ERROR_CODE_UNSPECIFIED));
    }
    return false;
  }

  /**
   * Signals that no further input frames will be rendered.
   *
   * <p>Must be called on the same thread as {@link #queueInputTexture}.
   */
  public void signalEndOfVideoInput() {
    try {
      if (!isEndOfStreamSignaled) {
        isEndOfStreamSignaled = true;
        checkNotNull(sampleConsumer).signalEndOfVideoInput();
      }
    } catch (RuntimeException e) {
      assetLoaderListener.onError(ExportException.createForAssetLoader(e, ERROR_CODE_UNSPECIFIED));
    }
  }
}
