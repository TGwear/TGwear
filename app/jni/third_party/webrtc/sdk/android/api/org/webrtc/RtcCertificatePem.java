/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import org.webrtc.PeerConnection;

/**
 * Easily storable/serializable version of a native C++ RTCCertificatePEM.
 */
public class RtcCertificatePem {
  /** PEM string representation of the private key. */
  public final String privateKey;
  /** PEM string representation of the certificate. */
  public final String certificate;
  /** Default expiration time of 30 days. */
  private static final long DEFAULT_EXPIRY = 60 * 60 * 24 * 30;

  /** Instantiate an RtcCertificatePem object from stored strings. */
  @CalledByNative
  public RtcCertificatePem(String privateKey, String certificate) {
    this.privateKey = privateKey;
    this.certificate = certificate;
  }

  @CalledByNative
  String getPrivateKey() {
    return privateKey;
  }

  @CalledByNative
  String getCertificate() {
    return certificate;
  }

  /**
   * Generate a new RtcCertificatePem with the default settings of KeyType = ECDSA and
   * expires = 30 days.
   */
  public static RtcCertificatePem generateCertificate() {
    return nativeGenerateCertificate(PeerConnection.KeyType.ECDSA, DEFAULT_EXPIRY);
  }

  /**
   * Generate a new RtcCertificatePem with a custom KeyType and the default setting of
   * expires = 30 days.
   */
  public static RtcCertificatePem generateCertificate(PeerConnection.KeyType keyType) {
    return nativeGenerateCertificate(keyType, DEFAULT_EXPIRY);
  }

  /**
   * Generate a new RtcCertificatePem with a custom expires and the default setting of
   * KeyType = ECDSA.
   */
  public static RtcCertificatePem generateCertificate(long expires) {
    return nativeGenerateCertificate(PeerConnection.KeyType.ECDSA, expires);
  }

  /** Generate a new RtcCertificatePem with a custom KeyType and a custom expires. */
  public static RtcCertificatePem generateCertificate(
      PeerConnection.KeyType keyType, long expires) {
    return nativeGenerateCertificate(keyType, expires);
  }

  private static native RtcCertificatePem nativeGenerateCertificate(
      PeerConnection.KeyType keyType, long expires);
}
