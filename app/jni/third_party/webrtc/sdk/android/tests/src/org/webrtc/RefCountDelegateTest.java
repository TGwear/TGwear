/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import androidx.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

@RunWith(AndroidJUnit4.class)
@Config(manifest = Config.NONE)
public class RefCountDelegateTest {
  @Mock Runnable mockReleaseCallback;
  private RefCountDelegate refCountDelegate;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    refCountDelegate = new RefCountDelegate(mockReleaseCallback);
  }

  @Test
  public void testReleaseRunsReleaseCallback() {
    refCountDelegate.release();
    verify(mockReleaseCallback).run();
  }

  @Test
  public void testRetainIncreasesRefCount() {
    refCountDelegate.retain();

    refCountDelegate.release();
    verify(mockReleaseCallback, never()).run();

    refCountDelegate.release();
    verify(mockReleaseCallback).run();
  }

  @Test(expected = IllegalStateException.class)
  public void testReleaseAfterFreeThrowsIllegalStateException() {
    refCountDelegate.release();
    refCountDelegate.release();
  }

  @Test(expected = IllegalStateException.class)
  public void testRetainAfterFreeThrowsIllegalStateException() {
    refCountDelegate.release();
    refCountDelegate.retain();
  }

  @Test
  public void testSafeRetainBeforeFreeReturnsTrueAndIncreasesRefCount() {
    assertThat(refCountDelegate.safeRetain()).isTrue();

    refCountDelegate.release();
    verify(mockReleaseCallback, never()).run();

    refCountDelegate.release();
    verify(mockReleaseCallback).run();
  }

  @Test
  public void testSafeRetainAfterFreeReturnsFalse() {
    refCountDelegate.release();
    assertThat(refCountDelegate.safeRetain()).isFalse();
  }
}
