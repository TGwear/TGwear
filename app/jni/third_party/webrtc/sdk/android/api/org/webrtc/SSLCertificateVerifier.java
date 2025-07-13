/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * The SSLCertificateVerifier interface allows API users to provide custom
 * logic to verify certificates.
 */
public interface SSLCertificateVerifier {
  /**
   * Implementations of verify allow applications to provide custom logic for
   * verifying certificates. This is not required by default and should be used
   * with care.
   *
   * @param certificate A byte array containing a DER encoded X509 certificate.
   * @return True if the certificate is verified and trusted else false.
   */
  @CalledByNative boolean verify(byte[] certificate);
}
