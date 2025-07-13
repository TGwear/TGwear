/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.annotation.Nullable;

/**
 * PeerConnectionDependencies holds all PeerConnection dependencies that are
 * applied per PeerConnection. A dependency is distinct from a configuration
 * as it defines significant executable code that can be provided by a user of
 * the API.
 */
public final class PeerConnectionDependencies {
  // Mandatory dependencies.
  private final PeerConnection.Observer observer;

  // Optional fields.
  private final SSLCertificateVerifier sslCertificateVerifier;

  public static class Builder {
    private PeerConnection.Observer observer;
    private SSLCertificateVerifier sslCertificateVerifier;

    private Builder(PeerConnection.Observer observer) {
      this.observer = observer;
    }

    public Builder setSSLCertificateVerifier(SSLCertificateVerifier sslCertificateVerifier) {
      this.sslCertificateVerifier = sslCertificateVerifier;
      return this;
    }

    // Observer is a required dependency and so is forced in the construction of the object.
    public PeerConnectionDependencies createPeerConnectionDependencies() {
      return new PeerConnectionDependencies(observer, sslCertificateVerifier);
    }
  }

  public static Builder builder(PeerConnection.Observer observer) {
    return new Builder(observer);
  }

  PeerConnection.Observer getObserver() {
    return observer;
  }

  @Nullable
  SSLCertificateVerifier getSSLCertificateVerifier() {
    return sslCertificateVerifier;
  }

  private PeerConnectionDependencies(
      PeerConnection.Observer observer, SSLCertificateVerifier sslCertificateVerifier) {
    this.observer = observer;
    this.sslCertificateVerifier = sslCertificateVerifier;
  }
}
