/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import android.net.Uri;
import android.os.Looper;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.ListenerSet;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowLooper;

/** Unit tests for {@link FallbackListener}. */
@RunWith(AndroidJUnit4.class)
public class FallbackListenerTest {

  private static final Composition PLACEHOLDER_COMPOSITION =
      new Composition.Builder(
              new EditedMediaItemSequence.Builder(
                      new EditedMediaItem.Builder(MediaItem.fromUri(Uri.EMPTY)).build())
                  .build())
          .build();

  @Test
  public void onTransformationRequestFinalized_withoutTrackCountSet_throwsException() {
    TransformationRequest transformationRequest = new TransformationRequest.Builder().build();
    FallbackListener fallbackListener =
        new FallbackListener(
            PLACEHOLDER_COMPOSITION, createListenerSet(), createHandler(), transformationRequest);

    assertThrows(
        IllegalStateException.class,
        () -> fallbackListener.onTransformationRequestFinalized(transformationRequest));
  }

  @Test
  public void onTransformationRequestFinalized_afterTrackCountSet_completesSuccessfully() {
    TransformationRequest transformationRequest = new TransformationRequest.Builder().build();
    FallbackListener fallbackListener =
        new FallbackListener(
            PLACEHOLDER_COMPOSITION, createListenerSet(), createHandler(), transformationRequest);

    fallbackListener.setTrackCount(1);
    fallbackListener.onTransformationRequestFinalized(transformationRequest);
    ShadowLooper.idleMainLooper();
  }

  @Test
  public void onTransformationRequestFinalized_withUnchangedRequest_doesNotCallback() {
    TransformationRequest originalRequest =
        new TransformationRequest.Builder().setAudioMimeType(MimeTypes.AUDIO_AAC).build();
    TransformationRequest unchangedRequest = originalRequest.buildUpon().build();
    Transformer.Listener mockListener = mock(Transformer.Listener.class);
    FallbackListener fallbackListener =
        new FallbackListener(
            PLACEHOLDER_COMPOSITION,
            createListenerSet(mockListener),
            createHandler(),
            originalRequest);

    fallbackListener.setTrackCount(1);
    fallbackListener.onTransformationRequestFinalized(unchangedRequest);
    ShadowLooper.idleMainLooper();

    verify(mockListener, never()).onFallbackApplied(any(Composition.class), any(), any());
  }

  @Test
  public void onTransformationRequestFinalized_withDifferentRequest_callsCallback() {
    TransformationRequest originalRequest =
        new TransformationRequest.Builder().setAudioMimeType(MimeTypes.AUDIO_AAC).build();
    TransformationRequest audioFallbackRequest =
        new TransformationRequest.Builder().setAudioMimeType(MimeTypes.AUDIO_AMR_WB).build();
    Transformer.Listener mockListener = mock(Transformer.Listener.class);
    FallbackListener fallbackListener =
        new FallbackListener(
            PLACEHOLDER_COMPOSITION,
            createListenerSet(mockListener),
            createHandler(),
            originalRequest);

    fallbackListener.setTrackCount(1);
    fallbackListener.onTransformationRequestFinalized(audioFallbackRequest);
    ShadowLooper.idleMainLooper();

    verify(mockListener)
        .onFallbackApplied(PLACEHOLDER_COMPOSITION, originalRequest, audioFallbackRequest);
  }

  @Test
  public void
      onTransformationRequestFinalized_forMultipleTracks_callsCallbackOnceWithMergedRequest() {
    TransformationRequest originalRequest =
        new TransformationRequest.Builder().setAudioMimeType(MimeTypes.AUDIO_AAC).build();
    TransformationRequest audioFallbackRequest =
        originalRequest.buildUpon().setAudioMimeType(MimeTypes.AUDIO_AMR_WB).build();
    TransformationRequest videoFallbackRequest =
        originalRequest.buildUpon().setVideoMimeType(MimeTypes.VIDEO_H264).build();
    TransformationRequest mergedFallbackRequest =
        new TransformationRequest.Builder()
            .setAudioMimeType(MimeTypes.AUDIO_AMR_WB)
            .setVideoMimeType(MimeTypes.VIDEO_H264)
            .build();
    Transformer.Listener mockListener = mock(Transformer.Listener.class);
    FallbackListener fallbackListener =
        new FallbackListener(
            PLACEHOLDER_COMPOSITION,
            createListenerSet(mockListener),
            createHandler(),
            originalRequest);

    fallbackListener.setTrackCount(2);
    fallbackListener.onTransformationRequestFinalized(audioFallbackRequest);
    fallbackListener.onTransformationRequestFinalized(videoFallbackRequest);
    ShadowLooper.idleMainLooper();

    verify(mockListener)
        .onFallbackApplied(PLACEHOLDER_COMPOSITION, originalRequest, mergedFallbackRequest);
  }

  private static ListenerSet<Transformer.Listener> createListenerSet(
      Transformer.Listener transformerListener) {
    ListenerSet<Transformer.Listener> listenerSet = createListenerSet();
    listenerSet.add(transformerListener);
    return listenerSet;
  }

  private static ListenerSet<Transformer.Listener> createListenerSet() {
    return new ListenerSet<>(Looper.myLooper(), Clock.DEFAULT, (listener, flags) -> {});
  }

  private static HandlerWrapper createHandler() {
    return Clock.DEFAULT.createHandler(Looper.myLooper(), /* callback= */ null);
  }
}
