/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.drm;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.drm.ExoMediaDrm.KeyRequest;
import androidx.media3.exoplayer.drm.ExoMediaDrm.ProvisionRequest;
import java.util.UUID;

/** Performs {@link ExoMediaDrm} key and provisioning requests. */
@UnstableApi
public interface MediaDrmCallback {

  /**
   * Executes a provisioning request.
   *
   * @param uuid The UUID of the content protection scheme.
   * @param request The request.
   * @return The response data.
   * @throws MediaDrmCallbackException If an error occurred executing the request.
   */
  byte[] executeProvisionRequest(UUID uuid, ProvisionRequest request)
      throws MediaDrmCallbackException;

  /**
   * Executes a key request.
   *
   * @param uuid The UUID of the content protection scheme.
   * @param request The request.
   * @return The response data.
   * @throws MediaDrmCallbackException If an error occurred executing the request.
   */
  byte[] executeKeyRequest(UUID uuid, KeyRequest request) throws MediaDrmCallbackException;
}
