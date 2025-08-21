/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.common;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.lang.reflect.Method;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link Player}. */
@RunWith(AndroidJUnit4.class)
public class PlayerTest {

  /**
   * This test picks a method on the {@link Player} interface that is known will never be
   * stabilised, and asserts that it is required to be implemented (therefore enforcing that {@link
   * Player} is unstable-for-implementors). If this test fails because the {@link Player#next()}
   * method is removed, it should be replaced with an equivalent unstable, unimplemented method.
   */
  @Test
  public void testAtLeastOneUnstableUnimplementedMethodExists() throws Exception {
    Method nextMethod = Player.class.getMethod("next");
    assertThat(nextMethod.isDefault()).isFalse();
  }
}
