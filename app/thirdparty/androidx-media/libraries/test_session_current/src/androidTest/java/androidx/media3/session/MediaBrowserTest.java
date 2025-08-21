/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Before;
import org.junit.runner.RunWith;

/** Tests for {@link MediaBrowser}. */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MediaBrowserTest extends MediaControllerTest {

  @Before
  public void setControllerType() {
    controllerTestRule.setControllerType(MediaBrowser.class);
  }
}
