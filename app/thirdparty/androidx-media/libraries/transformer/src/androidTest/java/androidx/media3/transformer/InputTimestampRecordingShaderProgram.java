/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.effect.PassthroughShaderProgram;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

/** A {@link PassthroughShaderProgram} that records the input timestamps. */
public class InputTimestampRecordingShaderProgram extends PassthroughShaderProgram {
  private final List<Long> inputTimestampsUs;

  /** Creates an instance. */
  public InputTimestampRecordingShaderProgram() {
    inputTimestampsUs = new ArrayList<>();
  }

  /** Returns the captured timestamps, in microseconds. */
  public ImmutableList<Long> getInputTimestampsUs() {
    return ImmutableList.copyOf(inputTimestampsUs);
  }

  @Override
  public void queueInputFrame(
      GlObjectsProvider glObjectsProvider, GlTextureInfo inputTexture, long presentationTimeUs) {
    super.queueInputFrame(glObjectsProvider, inputTexture, presentationTimeUs);
    inputTimestampsUs.add(presentationTimeUs);
  }
}
