/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * CryptoOptions defines advanced cryptographic settings for native WebRTC.
 * These settings must be passed into RTCConfiguration. WebRTC is secur by
 * default and you should not need to set any of these options unless you are
 * specifically looking for an additional crypto feature such as AES_GCM
 * support. This class is the Java binding of native api/crypto/cryptooptions.h
 */
public final class CryptoOptions {
  /**
   * SRTP Related Peer Connection Options.
   */
  public final class Srtp {
    /**
     * Enable GCM crypto suites from RFC 7714 for SRTP. GCM will only be used
     * if both sides enable it
     */
    private final boolean enableGcmCryptoSuites;
    /**
     * If set to true, the (potentially insecure) crypto cipher
     * kSrtpAes128CmSha1_32 will be included in the list of supported ciphers
     * during negotiation. It will only be used if both peers support it and no
     * other ciphers get preferred.
     */
    private final boolean enableAes128Sha1_32CryptoCipher;
    /**
     * If set to true, encrypted RTP header extensions as defined in RFC 6904
     * will be negotiated. They will only be used if both peers support them.
     */
    private final boolean enableEncryptedRtpHeaderExtensions;

    private Srtp(boolean enableGcmCryptoSuites, boolean enableAes128Sha1_32CryptoCipher,
        boolean enableEncryptedRtpHeaderExtensions) {
      this.enableGcmCryptoSuites = enableGcmCryptoSuites;
      this.enableAes128Sha1_32CryptoCipher = enableAes128Sha1_32CryptoCipher;
      this.enableEncryptedRtpHeaderExtensions = enableEncryptedRtpHeaderExtensions;
    }

    @CalledByNative("Srtp")
    public boolean getEnableGcmCryptoSuites() {
      return enableGcmCryptoSuites;
    }

    @CalledByNative("Srtp")
    public boolean getEnableAes128Sha1_32CryptoCipher() {
      return enableAes128Sha1_32CryptoCipher;
    }

    @CalledByNative("Srtp")
    public boolean getEnableEncryptedRtpHeaderExtensions() {
      return enableEncryptedRtpHeaderExtensions;
    }
  }

  /**
   * Options to be used when the FrameEncryptor / FrameDecryptor APIs are used.
   */
  public final class SFrame {
    /**
     * If set all RtpSenders must have an FrameEncryptor attached to them before
     * they are allowed to send packets. All RtpReceivers must have a
     * FrameDecryptor attached to them before they are able to receive packets.
     */
    private final boolean requireFrameEncryption;

    private SFrame(boolean requireFrameEncryption) {
      this.requireFrameEncryption = requireFrameEncryption;
    }

    @CalledByNative("SFrame")
    public boolean getRequireFrameEncryption() {
      return requireFrameEncryption;
    }
  }

  private final Srtp srtp;
  private final SFrame sframe;

  private CryptoOptions(boolean enableGcmCryptoSuites, boolean enableAes128Sha1_32CryptoCipher,
      boolean enableEncryptedRtpHeaderExtensions, boolean requireFrameEncryption) {
    this.srtp = new Srtp(
        enableGcmCryptoSuites, enableAes128Sha1_32CryptoCipher, enableEncryptedRtpHeaderExtensions);
    this.sframe = new SFrame(requireFrameEncryption);
  }

  public static Builder builder() {
    return new Builder();
  }

  @CalledByNative
  public Srtp getSrtp() {
    return srtp;
  }

  @CalledByNative
  public SFrame getSFrame() {
    return sframe;
  }

  public static class Builder {
    private boolean enableGcmCryptoSuites;
    private boolean enableAes128Sha1_32CryptoCipher;
    private boolean enableEncryptedRtpHeaderExtensions;
    private boolean requireFrameEncryption;

    private Builder() {}

    public Builder setEnableGcmCryptoSuites(boolean enableGcmCryptoSuites) {
      this.enableGcmCryptoSuites = enableGcmCryptoSuites;
      return this;
    }

    public Builder setEnableAes128Sha1_32CryptoCipher(boolean enableAes128Sha1_32CryptoCipher) {
      this.enableAes128Sha1_32CryptoCipher = enableAes128Sha1_32CryptoCipher;
      return this;
    }

    public Builder setEnableEncryptedRtpHeaderExtensions(
        boolean enableEncryptedRtpHeaderExtensions) {
      this.enableEncryptedRtpHeaderExtensions = enableEncryptedRtpHeaderExtensions;
      return this;
    }

    public Builder setRequireFrameEncryption(boolean requireFrameEncryption) {
      this.requireFrameEncryption = requireFrameEncryption;
      return this;
    }

    public CryptoOptions createCryptoOptions() {
      return new CryptoOptions(enableGcmCryptoSuites, enableAes128Sha1_32CryptoCipher,
          enableEncryptedRtpHeaderExtensions, requireFrameEncryption);
    }
  }
}
