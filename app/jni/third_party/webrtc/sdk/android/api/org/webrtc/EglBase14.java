/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;

/** EGL 1.4 implementation of EglBase. */
public interface EglBase14 extends EglBase {
  interface Context extends EglBase.Context {
    EGLContext getRawContext();
  }

  interface EglConnection extends EglBase.EglConnection {
    EGLContext getContext();

    EGLDisplay getDisplay();

    EGLConfig getConfig();
  }
}
