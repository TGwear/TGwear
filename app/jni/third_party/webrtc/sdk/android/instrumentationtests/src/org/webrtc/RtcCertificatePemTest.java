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
import org.webrtc.PeerConnection;
import org.webrtc.RtcCertificatePem;

/** Tests for RtcCertificatePem.java. */
public class RtcCertificatePemTest {
  @Before
  public void setUp() {
    System.loadLibrary(TestConstants.NATIVE_LIBRARY);
  }

  @Test
  @SmallTest
  public void testConstructor() {
    RtcCertificatePem original = RtcCertificatePem.generateCertificate();
    RtcCertificatePem recreated = new RtcCertificatePem(original.privateKey, original.certificate);
    assertThat(original.privateKey).isEqualTo(recreated.privateKey);
    assertThat(original.certificate).isEqualTo(recreated.certificate);
  }

  @Test
  @SmallTest
  public void testGenerateCertificateDefaults() {
    RtcCertificatePem rtcCertificate = RtcCertificatePem.generateCertificate();
    assertThat(rtcCertificate.privateKey).isNotEmpty();
    assertThat(rtcCertificate.certificate).isNotEmpty();
  }

  @Test
  @SmallTest
  public void testGenerateCertificateCustomKeyTypeDefaultExpires() {
    RtcCertificatePem rtcCertificate =
        RtcCertificatePem.generateCertificate(PeerConnection.KeyType.RSA);
    assertThat(rtcCertificate.privateKey).isNotEmpty();
    assertThat(rtcCertificate.certificate).isNotEmpty();
  }

  @Test
  @SmallTest
  public void testGenerateCertificateCustomExpiresDefaultKeyType() {
    RtcCertificatePem rtcCertificate = RtcCertificatePem.generateCertificate(60 * 60 * 24);
    assertThat(rtcCertificate.privateKey).isNotEmpty();
    assertThat(rtcCertificate.certificate).isNotEmpty();
  }

  @Test
  @SmallTest
  public void testGenerateCertificateCustomKeyTypeAndExpires() {
    RtcCertificatePem rtcCertificate =
        RtcCertificatePem.generateCertificate(PeerConnection.KeyType.RSA, 60 * 60 * 24);
    assertThat(rtcCertificate.privateKey).isNotEmpty();
    assertThat(rtcCertificate.certificate).isNotEmpty();
  }
}
