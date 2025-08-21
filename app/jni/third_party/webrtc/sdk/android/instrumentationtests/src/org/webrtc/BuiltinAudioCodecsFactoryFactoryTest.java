/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.filters.SmallTest;
import org.junit.Before;
import org.junit.Test;

public final class BuiltinAudioCodecsFactoryFactoryTest {
  @Before
  public void setUp() {
    System.loadLibrary(TestConstants.NATIVE_LIBRARY);
  }

  @Test
  @SmallTest
  public void testAudioEncoderFactoryFactoryTest() throws Exception {
    BuiltinAudioEncoderFactoryFactory factory = new BuiltinAudioEncoderFactoryFactory();
    long aef = 0;
    try {
      aef = factory.createNativeAudioEncoderFactory();
      assertThat(aef).isNotEqualTo(0);
    } finally {
      if (aef != 0) {
        JniCommon.nativeReleaseRef(aef);
      }
    }
  }

  @Test
  @SmallTest
  public void testAudioDecoderFactoryFactoryTest() throws Exception {
    BuiltinAudioDecoderFactoryFactory factory = new BuiltinAudioDecoderFactoryFactory();
    long adf = 0;
    try {
      adf = factory.createNativeAudioDecoderFactory();
      assertThat(adf).isNotEqualTo(0);
    } finally {
      if (adf != 0) {
        JniCommon.nativeReleaseRef(adf);
      }
    }
  }
}
