/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.test.session.common.CommonConstants.MOCK_MEDIA3_LIBRARY_SERVICE;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.test.session.common.MainLooperTestRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link RemoteMediaBrowserCompat}. */
@RunWith(AndroidJUnit4.class)
public class RemoteMediaBrowserCompatTest {

  @ClassRule public static MainLooperTestRule mainLooperTestRule = new MainLooperTestRule();

  private RemoteMediaBrowserCompat remoteBrowserCompat;

  @Before
  public void setUp() throws Exception {
    remoteBrowserCompat =
        new RemoteMediaBrowserCompat(
            ApplicationProvider.getApplicationContext(), MOCK_MEDIA3_LIBRARY_SERVICE);
  }

  @After
  public void cleanUp() throws Exception {
    if (remoteBrowserCompat != null) {
      remoteBrowserCompat.cleanUp();
    }
  }

  @Test
  @SmallTest
  public void connect() throws Exception {
    remoteBrowserCompat.connect(/* waitForConnection= */ true);
    assertThat(remoteBrowserCompat.isConnected()).isTrue();
  }
}
