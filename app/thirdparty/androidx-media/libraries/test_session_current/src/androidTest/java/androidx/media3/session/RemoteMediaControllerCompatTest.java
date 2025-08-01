/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.test.session.common.CommonConstants.DEFAULT_TEST_NAME;
import static androidx.media3.test.session.common.TestUtils.TIMEOUT_MS;
import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.media3.test.session.common.HandlerThreadTestRule;
import androidx.media3.test.session.common.MainLooperTestRule;
import androidx.media3.test.session.common.TestHandler;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import java.util.concurrent.CountDownLatch;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link RemoteMediaControllerCompat}. */
@RunWith(AndroidJUnit4.class)
public class RemoteMediaControllerCompatTest {

  @ClassRule public static MainLooperTestRule mainLooperTestRule = new MainLooperTestRule();

  @Rule
  public final HandlerThreadTestRule threadTestRule =
      new HandlerThreadTestRule("RemoteMediaControllerCompatTest");

  private TestHandler handler;
  private MediaSessionCompat sessionCompat;
  private RemoteMediaControllerCompat remoteControllerCompat;

  @Before
  public void setUp() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    handler = threadTestRule.getHandler();
    handler.postAndSync(
        () -> {
          sessionCompat = new MediaSessionCompat(context, DEFAULT_TEST_NAME);
          sessionCompat.setActive(true);
        });
    remoteControllerCompat =
        new RemoteMediaControllerCompat(
            context, sessionCompat.getSessionToken(), /* waitForConnection= */ true);
  }

  @After
  public void cleanUp() {
    sessionCompat.release();
    remoteControllerCompat.cleanUp();
  }

  @Test
  @SmallTest
  public void play() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    sessionCompat.setCallback(
        new MediaSessionCompat.Callback() {
          @Override
          public void onPlay() {
            latch.countDown();
          }
        },
        handler);

    remoteControllerCompat.getTransportControls().play();
    assertThat(latch.await(TIMEOUT_MS, MILLISECONDS)).isTrue();
  }
}
