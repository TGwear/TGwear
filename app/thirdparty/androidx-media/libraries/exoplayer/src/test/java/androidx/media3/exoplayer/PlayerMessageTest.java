/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import android.os.HandlerThread;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Clock;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

/** Unit test for {@link PlayerMessage}. */
@RunWith(AndroidJUnit4.class)
public class PlayerMessageTest {

  private static final long TIMEOUT_MS = 10;

  @Mock Clock clock;
  private HandlerThread handlerThread;
  private PlayerMessage message;

  @Before
  public void setUp() {
    initMocks(this);
    PlayerMessage.Sender sender = (message) -> {};
    PlayerMessage.Target target = (messageType, payload) -> {};
    handlerThread = new HandlerThread("TestHandler");
    handlerThread.start();
    message =
        new PlayerMessage(
            sender,
            target,
            Timeline.EMPTY,
            /* defaultWindowIndex= */ 0,
            clock,
            handlerThread.getLooper());
  }

  @After
  public void tearDown() {
    handlerThread.quit();
  }

  @Test
  public void blockUntilDelivered_timesOut() throws Exception {
    when(clock.elapsedRealtime()).thenReturn(0L).thenReturn(TIMEOUT_MS * 2);

    assertThrows(TimeoutException.class, () -> message.send().blockUntilDelivered(TIMEOUT_MS));

    // Ensure blockUntilDelivered() entered the blocking loop.
    verify(clock, Mockito.times(2)).elapsedRealtime();
  }

  @Test
  public void blockUntilDelivered_onAlreadyProcessed_succeeds() throws Exception {
    when(clock.elapsedRealtime()).thenReturn(0L);

    message.send().markAsProcessed(/* isDelivered= */ true);

    assertThat(message.blockUntilDelivered(TIMEOUT_MS)).isTrue();
  }

  @Test
  public void blockUntilDelivered_markAsProcessedWhileBlocked_succeeds() throws Exception {
    message.send();

    // Use a separate Thread to mark the message as processed.
    CountDownLatch prepareLatch = new CountDownLatch(1);
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<Boolean> future =
        executorService.submit(
            () -> {
              prepareLatch.await();
              message.markAsProcessed(true);
              return true;
            });

    when(clock.elapsedRealtime())
        .thenReturn(0L)
        .then(
            (invocation) -> {
              // Signal the background thread to call PlayerMessage#markAsProcessed.
              prepareLatch.countDown();
              return TIMEOUT_MS - 1;
            });

    try {
      assertThat(message.blockUntilDelivered(TIMEOUT_MS)).isTrue();
      // Ensure blockUntilDelivered() entered the blocking loop.
      verify(clock, Mockito.atLeast(2)).elapsedRealtime();
      future.get(1, SECONDS);
    } finally {
      executorService.shutdown();
    }
  }
}
