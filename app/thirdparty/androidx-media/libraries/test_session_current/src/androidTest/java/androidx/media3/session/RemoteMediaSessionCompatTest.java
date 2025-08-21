/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.test.session.common.CommonConstants.DEFAULT_TEST_NAME;
import static androidx.media3.test.session.common.CommonConstants.SUPPORT_APP_PACKAGE_NAME;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link RemoteMediaSessionCompat}. */
@RunWith(AndroidJUnit4.class)
public class RemoteMediaSessionCompatTest {

  private Context context;
  private RemoteMediaSessionCompat remoteSessionCompat;

  @Before
  public void setUp() throws Exception {
    context = ApplicationProvider.getApplicationContext();
    remoteSessionCompat = new RemoteMediaSessionCompat(DEFAULT_TEST_NAME, context);
  }

  @After
  public void cleanUp() throws Exception {
    remoteSessionCompat.cleanUp();
  }

  @Test
  @SmallTest
  public void gettingToken() throws Exception {
    MediaSessionCompat.Token token = remoteSessionCompat.getSessionToken();
    assertThat(token).isNotNull();
  }

  @Test
  @SmallTest
  public void creatingControllerCompat() throws Exception {
    MediaSessionCompat.Token token = remoteSessionCompat.getSessionToken();
    assertThat(token).isNotNull();
    MediaControllerCompat controller = new MediaControllerCompat(context, token);
    assertThat(controller.getPackageName()).isEqualTo(SUPPORT_APP_PACKAGE_NAME);
  }
}
