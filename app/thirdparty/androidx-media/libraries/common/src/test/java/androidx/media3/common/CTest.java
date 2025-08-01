/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static com.google.common.truth.Truth.assertThat;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.MediaCodec;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link C}. */
@RunWith(AndroidJUnit4.class)
public class CTest {

  @SuppressLint("InlinedApi")
  @Test
  public void bufferFlagConstants_equalToMediaCodecConstants() {
    // Check that constant values match those defined by the platform.
    assertThat(C.BUFFER_FLAG_KEY_FRAME).isEqualTo(MediaCodec.BUFFER_FLAG_KEY_FRAME);
    assertThat(C.BUFFER_FLAG_END_OF_STREAM).isEqualTo(MediaCodec.BUFFER_FLAG_END_OF_STREAM);
    assertThat(C.CRYPTO_MODE_AES_CTR).isEqualTo(MediaCodec.CRYPTO_MODE_AES_CTR);
  }

  @SuppressLint("InlinedApi")
  @Test
  public void encodingConstants_equalToAudioFormatConstants() {
    // Check that encoding constant values match those defined by the platform.
    assertThat(C.ENCODING_PCM_16BIT).isEqualTo(AudioFormat.ENCODING_PCM_16BIT);
    assertThat(C.ENCODING_MP3).isEqualTo(AudioFormat.ENCODING_MP3);
    assertThat(C.ENCODING_PCM_FLOAT).isEqualTo(AudioFormat.ENCODING_PCM_FLOAT);
  }
}
