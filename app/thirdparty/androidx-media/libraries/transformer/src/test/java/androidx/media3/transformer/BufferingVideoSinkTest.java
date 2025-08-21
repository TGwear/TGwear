/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import android.content.Context;
import androidx.media3.exoplayer.video.VideoSink;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

/** Tests for {@link BufferingVideoSink} */
@RunWith(AndroidJUnit4.class)
public class BufferingVideoSinkTest {

  private final Context context = ApplicationProvider.getApplicationContext();

  @Test
  public void executeOperation_withVideoSinkSet_callsVideoSink() {
    BufferingVideoSink bufferingVideoSink = new BufferingVideoSink(context);
    VideoSink videoSinkMock = mock(VideoSink.class);

    bufferingVideoSink.setVideoSink(videoSinkMock);
    bufferingVideoSink.onRendererEnabled(/* mayRenderStartOfStream= */ true);
    bufferingVideoSink.onRendererStarted();

    InOrder inOrder = Mockito.inOrder(videoSinkMock);
    inOrder.verify(videoSinkMock).onRendererEnabled(/* mayRenderStartOfStream= */ true);
    inOrder.verify(videoSinkMock).onRendererStarted();
  }

  @Test
  public void setVideoSink_executesPendingOperations() {
    BufferingVideoSink bufferingVideoSink = new BufferingVideoSink(context);
    VideoSink videoSinkMock = mock(VideoSink.class);

    bufferingVideoSink.onRendererEnabled(/* mayRenderStartOfStream= */ true);
    bufferingVideoSink.onRendererStarted();
    bufferingVideoSink.setVideoSink(videoSinkMock);

    InOrder inOrder = Mockito.inOrder(videoSinkMock);
    inOrder.verify(videoSinkMock).onRendererEnabled(/* mayRenderStartOfStream= */ true);
    inOrder.verify(videoSinkMock).onRendererStarted();
  }

  @Test
  public void setNullVideoSink_thenExecuteOperations_doesNotCallVideoSink() {
    BufferingVideoSink bufferingVideoSink = new BufferingVideoSink(context);
    VideoSink videoSinkMock = mock(VideoSink.class);
    bufferingVideoSink.setVideoSink(videoSinkMock);

    bufferingVideoSink.setVideoSink(null);
    bufferingVideoSink.onRendererEnabled(/* mayRenderStartOfStream= */ true);
    bufferingVideoSink.onRendererStarted();

    verify(videoSinkMock, never()).onRendererEnabled(/* mayRenderStartOfStream= */ true);
    verify(videoSinkMock, never()).onRendererStarted();
  }

  @Test
  public void clearPendingOperations_clearsPendingOperations() {
    BufferingVideoSink bufferingVideoSink = new BufferingVideoSink(context);
    VideoSink videoSinkMock = mock(VideoSink.class);

    bufferingVideoSink.onRendererEnabled(/* mayRenderStartOfStream= */ true);
    bufferingVideoSink.onRendererStarted();
    bufferingVideoSink.clearPendingOperations();
    bufferingVideoSink.setVideoSink(videoSinkMock);

    verify(videoSinkMock, never()).onRendererEnabled(/* mayRenderStartOfStream= */ true);
    verify(videoSinkMock, never()).onRendererStarted();
  }
}
