/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.mp4;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.SniffFailure;

/** {@link SniffFailure} indicating the MP4 file didn't declare any brands. */
@UnstableApi
public final class NoDeclaredBrandSniffFailure implements SniffFailure {

  public static final NoDeclaredBrandSniffFailure INSTANCE = new NoDeclaredBrandSniffFailure();

  private NoDeclaredBrandSniffFailure() {}
}
