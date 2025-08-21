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
import android.os.Bundle;
import androidx.media3.test.session.common.TestUtils;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link RemoteMediaSession}. */
@RunWith(AndroidJUnit4.class)
public class RemoteMediaSessionTest {

  private Context context;
  private RemoteMediaSession remoteSession;
  private Bundle tokenExtras;

  @Before
  public void setUp() throws Exception {
    context = ApplicationProvider.getApplicationContext();
    tokenExtras = TestUtils.createTestBundle();
    remoteSession = new RemoteMediaSession(DEFAULT_TEST_NAME, context, tokenExtras);
  }

  @After
  public void cleanUp() throws Exception {
    if (remoteSession != null) {
      remoteSession.cleanUp();
    }
  }

  @Test
  @SmallTest
  public void gettingToken() throws Exception {
    SessionToken token = remoteSession.getToken();
    assertThat(token).isNotNull();
    assertThat(token.getPackageName()).isEqualTo(SUPPORT_APP_PACKAGE_NAME);
    assertThat(TestUtils.equals(tokenExtras, token.getExtras())).isTrue();
  }
}
