/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import android.view.SurfaceView;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;

/**
 * Provider for views to show diagnostic information during an export, for debugging.
 *
 * <p>This is not intended for production use-cases.
 */
@UnstableApi
public interface DebugViewProvider {

  /** Debug view provider that doesn't show any debug info. */
  DebugViewProvider NONE = (int width, int height) -> null;

  /**
   * Returns a new surface view to show a preview of transformer output with the given width/height
   * in pixels, or {@code null} if no debug information should be shown.
   *
   * <p>This method may be called on an arbitrary thread.
   */
  @Nullable
  SurfaceView getDebugPreviewSurfaceView(int width, int height);
}
