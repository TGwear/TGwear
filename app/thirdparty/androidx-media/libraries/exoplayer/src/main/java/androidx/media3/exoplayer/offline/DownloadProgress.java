/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.offline;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;

/** Mutable {@link Download} progress. */
@UnstableApi
public class DownloadProgress {

  /** The number of bytes that have been downloaded. */
  public volatile long bytesDownloaded;

  /** The percentage that has been downloaded, or {@link C#PERCENTAGE_UNSET} if unknown. */
  public volatile float percentDownloaded;
}
