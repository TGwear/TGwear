/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.drm;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.drm.ExoMediaDrm.KeyRequest;
import androidx.media3.exoplayer.drm.ExoMediaDrm.ProvisionRequest;
import java.util.UUID;

/**
 * A {@link MediaDrmCallback} that provides a fixed response to key requests. Provisioning is not
 * supported. This implementation is primarily useful for providing locally stored keys to decrypt
 * ClearKey protected content. It is not suitable for use with Widevine or PlayReady protected
 * content.
 */
@UnstableApi
public final class LocalMediaDrmCallback implements MediaDrmCallback {

  private final byte[] keyResponse;

  /**
   * @param keyResponse The fixed response for all key requests.
   */
  public LocalMediaDrmCallback(byte[] keyResponse) {
    this.keyResponse = Assertions.checkNotNull(keyResponse);
  }

  @Override
  public byte[] executeProvisionRequest(UUID uuid, ProvisionRequest request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public byte[] executeKeyRequest(UUID uuid, KeyRequest request) {
    return keyResponse;
  }
}
