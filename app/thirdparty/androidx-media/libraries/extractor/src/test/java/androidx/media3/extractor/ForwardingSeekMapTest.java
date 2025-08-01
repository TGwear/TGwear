/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import androidx.media3.test.utils.TestUtil;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link ForwardingSeekMap}. */
@RunWith(AndroidJUnit4.class)
public class ForwardingSeekMapTest {

  @Test
  public void overridesAllMethods() throws Exception {
    TestUtil.assertSubclassOverridesAllMethods(SeekMap.class, ForwardingSeekMap.class);
  }

  @Test
  public void forwardsAllMethods() throws Exception {
    TestUtil.assertForwardingClassForwardsAllMethods(SeekMap.class, ForwardingSeekMap::new);
  }
}
