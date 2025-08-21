/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.drm;

import android.net.Uri;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSpec;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Thrown when an error occurs while executing a DRM {@link MediaDrmCallback#executeKeyRequest key}
 * or {@link MediaDrmCallback#executeProvisionRequest provisioning} request.
 */
@UnstableApi
public final class MediaDrmCallbackException extends IOException {

  /** The {@link DataSpec} associated with the request. */
  public final DataSpec dataSpec;

  /**
   * The {@link Uri} after redirections, or {@link #dataSpec dataSpec.uri} if no redirection
   * occurred.
   */
  public final Uri uriAfterRedirects;

  /** The HTTP request headers included in the response. */
  public final Map<String, List<String>> responseHeaders;

  /** The number of bytes obtained from the server. */
  public final long bytesLoaded;

  /**
   * Creates a new instance with the given values.
   *
   * @param dataSpec See {@link #dataSpec}.
   * @param uriAfterRedirects See {@link #uriAfterRedirects}.
   * @param responseHeaders See {@link #responseHeaders}.
   * @param bytesLoaded See {@link #bytesLoaded}.
   * @param cause The cause of the exception.
   */
  public MediaDrmCallbackException(
      DataSpec dataSpec,
      Uri uriAfterRedirects,
      Map<String, List<String>> responseHeaders,
      long bytesLoaded,
      Throwable cause) {
    super(cause);
    this.dataSpec = dataSpec;
    this.uriAfterRedirects = uriAfterRedirects;
    this.responseHeaders = responseHeaders;
    this.bytesLoaded = bytesLoaded;
  }
}
