/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import static org.junit.Assert.assertThrows;

import android.os.Bundle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link TestUtil}. */
@RunWith(AndroidJUnit4.class)
public class TestUtilTest {

  @Test
  public void getThrowingBundle_throwsWhenUsed() {
    Bundle bundle = TestUtil.getThrowingBundle();

    assertThrows(RuntimeException.class, () -> bundle.getInt("0"));
  }
}
