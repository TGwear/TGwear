/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import android.graphics.Gainmap;
import androidx.media3.common.util.GlUtil.GlException;

/** Interface for a {@link GlShaderProgram} that samples from a gainmap. */
/* package */ interface GainmapShaderProgram extends GlShaderProgram {

  /**
   * Sets the {@link Gainmap} that is applied to the output frame.
   *
   * @param gainmap The {@link Gainmap}.
   */
  void setGainmap(Gainmap gainmap) throws GlException;
}
