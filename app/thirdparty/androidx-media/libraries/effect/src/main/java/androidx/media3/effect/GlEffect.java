/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import android.content.Context;
import androidx.media3.common.Effect;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.UnstableApi;

/**
 * Interface for a video frame effect with a {@link GlShaderProgram} implementation.
 *
 * <p>Implementations contain information specifying the effect and can be {@linkplain
 * #toGlShaderProgram(Context, boolean) converted} to a {@link GlShaderProgram} which applies the
 * effect.
 */
@UnstableApi
public interface GlEffect extends Effect {

  /**
   * Returns a {@link GlShaderProgram} that applies the effect.
   *
   * @param context A {@link Context}.
   * @param useHdr Whether input textures come from an HDR source. If {@code true}, colors will be
   *     in linear RGB BT.2020. If {@code false}, colors will be in linear RGB BT.709.
   * @throws VideoFrameProcessingException If an error occurs while creating the {@link
   *     GlShaderProgram}.
   */
  GlShaderProgram toGlShaderProgram(Context context, boolean useHdr)
      throws VideoFrameProcessingException;

  /**
   * Returns whether a {@link GlEffect} applies no change at every timestamp.
   *
   * <p>This can be used as a hint to skip this instance.
   *
   * @param inputWidth The input frame width, in pixels.
   * @param inputHeight The input frame height, in pixels.
   */
  default boolean isNoOp(int inputWidth, int inputHeight) {
    return false;
  }
}
