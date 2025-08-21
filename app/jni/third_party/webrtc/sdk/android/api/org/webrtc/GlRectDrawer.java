/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/** Simplest possible GL shader that just draws frames as opaque quads. */
public class GlRectDrawer extends GlGenericDrawer {
  private static final String FRAGMENT_SHADER = "void main() {\n"
      + "  gl_FragColor = sample(tc);\n"
      + "}\n";

  private static class ShaderCallbacks implements GlGenericDrawer.ShaderCallbacks {
    @Override
    public void onNewShader(GlShader shader) {}

    @Override
    public void onPrepareShader(GlShader shader, float[] texMatrix, int frameWidth, int frameHeight,
        int viewportWidth, int viewportHeight) {}
  }

  public GlRectDrawer() {
    super(FRAGMENT_SHADER, new ShaderCallbacks());
  }
}
