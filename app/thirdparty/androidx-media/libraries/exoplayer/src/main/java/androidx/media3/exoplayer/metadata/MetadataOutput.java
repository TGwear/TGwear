/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.metadata;

import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;

/** Receives metadata output. */
@UnstableApi
public interface MetadataOutput {

  /**
   * Called when there is metadata associated with current playback time.
   *
   * @param metadata The metadata.
   */
  void onMetadata(Metadata metadata);
}
