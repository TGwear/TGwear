/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer.mh;

import android.content.Context;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.effect.DefaultVideoFrameProcessor;
import androidx.media3.effect.GlEffect;
import androidx.media3.effect.GlShaderProgram;
import androidx.media3.effect.ScaleAndRotateTransformation;

/**
 * Wraps a {@link GlEffect} to prevent the {@link DefaultVideoFrameProcessor} from detecting its
 * class and optimizing it.
 *
 * <p>This ensures that {@link DefaultVideoFrameProcessor} uses a separate {@link GlShaderProgram}
 * for the wrapped {@link GlEffect} rather than merging it with preceding or subsequent {@link
 * GlEffect} instances and applying them in one combined {@link GlShaderProgram}.
 */
// TODO: b/263395272 - Move this to effects/mh tests.
public final class UnoptimizedGlEffect implements GlEffect {
  // A passthrough effect allows for testing having an intermediate effect injected, which uses
  // different OpenGL shaders from having no effects.
  public static final GlEffect NO_OP_EFFECT =
      new UnoptimizedGlEffect(new ScaleAndRotateTransformation.Builder().build());

  private final GlEffect effect;

  public UnoptimizedGlEffect(GlEffect effect) {
    this.effect = effect;
  }

  @Override
  public GlShaderProgram toGlShaderProgram(Context context, boolean useHdr)
      throws VideoFrameProcessingException {
    return effect.toGlShaderProgram(context, useHdr);
  }
}
