/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static android.opengl.GLES20.GL_FALSE;
import static android.opengl.GLES20.GL_TRUE;
import static java.lang.Math.log;

import android.graphics.Bitmap;
import android.graphics.Gainmap;
import androidx.annotation.RequiresApi;
import androidx.media3.common.C;
import androidx.media3.common.util.GlProgram;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.UnstableApi;

/** Utilities for Gainmaps. */
@UnstableApi
/* package */ class GainmapUtil {
  private GainmapUtil() {}

  /** Checks whether the contents and fields relevant to effects processing are equal. */
  @RequiresApi(34)
  public static boolean equals(Gainmap g1, Gainmap g2) {
    return g1.getGamma() == g2.getGamma()
        && g1.getRatioMax() == g2.getRatioMax()
        && g1.getRatioMin() == g2.getRatioMin()
        && g1.getEpsilonHdr() == g2.getEpsilonHdr()
        && g1.getEpsilonSdr() == g2.getEpsilonSdr()
        && g1.getDisplayRatioForFullHdr() == g2.getDisplayRatioForFullHdr()
        && g1.getMinDisplayRatioForHdrTransition() == g2.getMinDisplayRatioForHdrTransition()
        && g1.getGainmapContents() == g2.getGainmapContents()
        && g1.getGainmapContents().getGenerationId() == g2.getGainmapContents().getGenerationId();
  }

  /**
   * Sets the uniforms for applying a gainmap to a base image.
   *
   * @param glProgram The {@link GlProgram}.
   * @param gainmap The {@link Gainmap}.
   * @param index The index to add to the end of the uniforms, or {@link C#INDEX_UNSET}, is no index
   *     is to be added.
   */
  @RequiresApi(34)
  public static void setGainmapUniforms(GlProgram glProgram, Gainmap gainmap, int index)
      throws GlUtil.GlException {
    boolean gainmapIsAlpha = gainmap.getGainmapContents().getConfig() == Bitmap.Config.ALPHA_8;
    float[] gainmapGamma = gainmap.getGamma();
    boolean noGamma = gainmapGamma[0] == 1f && gainmapGamma[1] == 1f && gainmapGamma[2] == 1f;
    boolean singleChannel =
        areAllChannelsEqual(gainmapGamma)
            && areAllChannelsEqual(gainmap.getRatioMax())
            && areAllChannelsEqual(gainmap.getRatioMin());

    glProgram.setIntUniform(
        addIndex("uGainmapIsAlpha", index), gainmapIsAlpha ? GL_TRUE : GL_FALSE);
    glProgram.setIntUniform(addIndex("uNoGamma", index), noGamma ? GL_TRUE : GL_FALSE);
    glProgram.setIntUniform(addIndex("uSingleChannel", index), singleChannel ? GL_TRUE : GL_FALSE);
    glProgram.setFloatsUniform(addIndex("uLogRatioMin", index), logRgb(gainmap.getRatioMin()));
    glProgram.setFloatsUniform(addIndex("uLogRatioMax", index), logRgb(gainmap.getRatioMax()));
    glProgram.setFloatsUniform(addIndex("uEpsilonSdr", index), gainmap.getEpsilonSdr());
    glProgram.setFloatsUniform(addIndex("uEpsilonHdr", index), gainmap.getEpsilonHdr());
    glProgram.setFloatsUniform(addIndex("uGainmapGamma", index), gainmapGamma);
    glProgram.setFloatUniform(
        addIndex("uDisplayRatioHdr", index), gainmap.getDisplayRatioForFullHdr());
    glProgram.setFloatUniform(
        addIndex("uDisplayRatioSdr", index), gainmap.getMinDisplayRatioForHdrTransition());
    GlUtil.checkGlError();
  }

  private static boolean areAllChannelsEqual(float[] channels) {
    return channels[0] == channels[1] && channels[1] == channels[2];
  }

  private static String addIndex(String s, int index) {
    return index == C.INDEX_UNSET ? s : s + index;
  }

  private static float[] logRgb(float[] values) {
    return new float[] {(float) log(values[0]), (float) log(values[1]), (float) log(values[2])};
  }
}
