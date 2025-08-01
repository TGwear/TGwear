/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.ui;

import androidx.media3.common.Format;
import androidx.media3.common.util.UnstableApi;

/** Converts {@link Format}s to user readable track names. */
@UnstableApi
public interface TrackNameProvider {

  /** Returns a user readable track name for the given {@link Format}. */
  String getTrackName(Format format);
}
