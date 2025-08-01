/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.mp4;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.TrackOutput;

/**
 * Encapsulates information parsed from a track encryption (tenc) box or sample group description
 * (sgpd) box in an MP4 stream.
 */
@UnstableApi
public final class TrackEncryptionBox {

  private static final String TAG = "TrackEncryptionBox";

  /** Indicates the encryption state of the samples in the sample group. */
  public final boolean isEncrypted;

  /** The protection scheme type, as defined by the 'schm' box, or null if unknown. */
  @Nullable public final String schemeType;

  /**
   * A {@link TrackOutput.CryptoData} instance containing the encryption information from this
   * {@link TrackEncryptionBox}.
   */
  public final TrackOutput.CryptoData cryptoData;

  /** The initialization vector size in bytes for the samples in the corresponding sample group. */
  public final int perSampleIvSize;

  /**
   * If {@link #perSampleIvSize} is 0, holds the default initialization vector as defined in the
   * track encryption box or sample group description box. Null otherwise.
   */
  @Nullable public final byte[] defaultInitializationVector;

  /**
   * @param isEncrypted See {@link #isEncrypted}.
   * @param schemeType See {@link #schemeType}.
   * @param perSampleIvSize See {@link #perSampleIvSize}.
   * @param keyId See {@link TrackOutput.CryptoData#encryptionKey}.
   * @param defaultEncryptedBlocks See {@link TrackOutput.CryptoData#encryptedBlocks}.
   * @param defaultClearBlocks See {@link TrackOutput.CryptoData#clearBlocks}.
   * @param defaultInitializationVector See {@link #defaultInitializationVector}.
   */
  public TrackEncryptionBox(
      boolean isEncrypted,
      @Nullable String schemeType,
      int perSampleIvSize,
      byte[] keyId,
      int defaultEncryptedBlocks,
      int defaultClearBlocks,
      @Nullable byte[] defaultInitializationVector) {
    Assertions.checkArgument(perSampleIvSize == 0 ^ defaultInitializationVector == null);
    this.isEncrypted = isEncrypted;
    this.schemeType = schemeType;
    this.perSampleIvSize = perSampleIvSize;
    this.defaultInitializationVector = defaultInitializationVector;
    cryptoData =
        new TrackOutput.CryptoData(
            schemeToCryptoMode(schemeType), keyId, defaultEncryptedBlocks, defaultClearBlocks);
  }

  private static @C.CryptoMode int schemeToCryptoMode(@Nullable String schemeType) {
    if (schemeType == null) {
      // If unknown, assume cenc.
      return C.CRYPTO_MODE_AES_CTR;
    }
    switch (schemeType) {
      case C.CENC_TYPE_cenc:
      case C.CENC_TYPE_cens:
        return C.CRYPTO_MODE_AES_CTR;
      case C.CENC_TYPE_cbc1:
      case C.CENC_TYPE_cbcs:
        return C.CRYPTO_MODE_AES_CBC;
      default:
        Log.w(
            TAG,
            "Unsupported protection scheme type '"
                + schemeType
                + "'. Assuming AES-CTR crypto mode.");
        return C.CRYPTO_MODE_AES_CTR;
    }
  }
}
