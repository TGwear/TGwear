/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import java.lang.reflect.Method;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link TestMediaBrowserListener}. */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestMediaBrowserListenerTest {

  /**
   * Test if the {@link TestMediaBrowserListener} wraps the listener proxy without missing any
   * method.
   */
  @Test
  public void methods_overridden() {
    Method[] methods = TestMediaBrowserListener.class.getMethods();
    assertThat(methods).isNotNull();
    for (Method method : methods) {
      // For any methods in the controller listener, TestBrowserListener should override the method
      // and call the matching API in the listener proxy.
      assertWithMessage(
              "TestBrowserListener should override " + method + " and call listener proxy")
          .that(method.getDeclaringClass())
          .isNotEqualTo(MediaBrowser.Listener.class);
      assertWithMessage(
              "TestBrowserListener should override " + method + " and call listener proxy")
          .that(method.getDeclaringClass())
          .isNotEqualTo(MediaController.Listener.class);
    }
  }
}
