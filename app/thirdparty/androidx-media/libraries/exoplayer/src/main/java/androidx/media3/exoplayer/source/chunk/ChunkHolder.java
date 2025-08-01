/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.chunk;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;

/** Holds a chunk or an indication that the end of the stream has been reached. */
@UnstableApi
public final class ChunkHolder {

  /** The chunk. */
  @Nullable public Chunk chunk;

  /** Indicates that the end of the stream has been reached. */
  public boolean endOfStream;

  /** Clears the holder. */
  public void clear() {
    chunk = null;
    endOfStream = false;
  }
}
