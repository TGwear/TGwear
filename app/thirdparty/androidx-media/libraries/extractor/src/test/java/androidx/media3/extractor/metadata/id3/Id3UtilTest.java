/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.id3;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public final class Id3UtilTest {

  @Test
  public void expectedNumberOfV1Genres() {
    for (int i = 0; i < 192; i++) {
      assertThat(Id3Util.resolveV1Genre(i)).isNotNull();
    }
  }

  @Test
  public void unrecognizedV1Genre_returnsNull() {
    assertThat(Id3Util.resolveV1Genre(-1)).isNull();
    assertThat(Id3Util.resolveV1Genre(200)).isNull();
  }
}
