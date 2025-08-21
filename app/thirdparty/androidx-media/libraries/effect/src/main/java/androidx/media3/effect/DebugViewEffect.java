/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import android.content.Context;
import android.view.SurfaceView;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.DebugViewProvider;
import androidx.media3.common.util.UnstableApi;

/** {@link GlEffect} that renders to a {@link SurfaceView} provided by {@link DebugViewProvider}. */
@UnstableApi
public final class DebugViewEffect implements GlEffect {

  private final DebugViewProvider debugViewProvider;
  private final ColorInfo outputColorInfo;

  /**
   * Creates a new instance.
   *
   * @param debugViewProvider The class that provides the {@link SurfaceView} that the debug preview
   *     will be rendered to.
   * @param outputColorInfo The {@link ColorInfo} of the output preview.
   */
  public DebugViewEffect(DebugViewProvider debugViewProvider, ColorInfo outputColorInfo) {
    this.debugViewProvider = debugViewProvider;
    this.outputColorInfo = outputColorInfo;
  }

  @Override
  public GlShaderProgram toGlShaderProgram(Context context, boolean useHdr) {
    return new DebugViewShaderProgram(context, debugViewProvider, outputColorInfo);
  }
}
