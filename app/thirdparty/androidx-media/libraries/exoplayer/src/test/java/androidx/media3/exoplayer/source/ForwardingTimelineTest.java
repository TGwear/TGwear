/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import static androidx.media3.test.utils.TestUtil.assertForwardingClassForwardsAllMethodsExcept;
import static androidx.media3.test.utils.TestUtil.assertSubclassOverridesAllMethods;

import androidx.media3.common.Timeline;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link ForwardingTimeline}. */
@RunWith(AndroidJUnit4.class)
public class ForwardingTimelineTest {

  @Test
  public void overridesAllMethods() throws Exception {
    assertSubclassOverridesAllMethods(Timeline.class, ForwardingTimeline.class);
  }

  @Test
  public void forwardsAllMethods() throws Exception {
    // ForwardingTimeline equals, hashCode, and getPeriodByUid implementations deliberately call
    // through to super rather than the delegate instance. This is because these methods are already
    // correctly implemented on Timeline in terms of the publicly visible parts of Timeline.
    assertForwardingClassForwardsAllMethodsExcept(
        Timeline.class,
        delegate -> new ForwardingTimeline(delegate) {},
        ImmutableSet.of("equals", "hashCode", "getPeriodByUid"));
  }
}
