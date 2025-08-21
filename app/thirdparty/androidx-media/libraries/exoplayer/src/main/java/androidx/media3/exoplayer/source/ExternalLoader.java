/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import android.net.Uri;
import androidx.media3.common.util.UnstableApi;
import com.google.common.util.concurrent.ListenableFuture;
import org.checkerframework.checker.nullness.qual.Nullable;

/** An object for loading media outside of ExoPlayer's loading mechanism. */
@UnstableApi
public interface ExternalLoader {

  /** A data class providing information associated with the load event. */
  final class LoadRequest {

    /** The {@link Uri} stored in the load request object. */
    public final Uri uri;

    /** Creates an instance. */
    public LoadRequest(Uri uri) {
      this.uri = uri;
    }
  }

  /**
   * Loads the external media.
   *
   * @param loadRequest The load request.
   * @return The {@link ListenableFuture} tracking the completion of the loading work.
   */
  ListenableFuture<@Nullable ?> load(LoadRequest loadRequest);
}
