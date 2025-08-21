/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.exoplayer.image;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link BitmapFactoryImageDecoder.Factory}. */
@RunWith(AndroidJUnit4.class)
public class BitmapFactoryImageDecoderFactoryTest {

  private final BitmapFactoryImageDecoder.Factory imageDecoderFactory =
      new BitmapFactoryImageDecoder.Factory();

  @Test
  public void supportsFormat_validFormat_returnsFormatSupported() throws Exception {
    Format.Builder format = new Format.Builder().setSampleMimeType(MimeTypes.IMAGE_JPEG);

    assertThat(imageDecoderFactory.supportsFormat(format.build()))
        .isEqualTo(RendererCapabilities.create(C.FORMAT_HANDLED));
  }

  @Test
  public void supportsFormat_noContainerMimeType_returnsUnsupportedType() throws Exception {
    Format.Builder format = new Format.Builder();

    assertThat(imageDecoderFactory.supportsFormat(format.build()))
        .isEqualTo(RendererCapabilities.create(C.FORMAT_UNSUPPORTED_TYPE));
  }

  @Test
  public void supportsFormat_nonImageMimeType_returnsUnsupportedType() throws Exception {
    Format.Builder format = new Format.Builder();

    format.setSampleMimeType(MimeTypes.VIDEO_AV1);

    assertThat(imageDecoderFactory.supportsFormat(format.build()))
        .isEqualTo(RendererCapabilities.create(C.FORMAT_UNSUPPORTED_TYPE));
  }

  @Test
  public void supportsFormat_unsupportedImageMimeType_returnsUnsupportedSubType() throws Exception {
    Format.Builder format = new Format.Builder();

    format.setSampleMimeType("image/custom");

    assertThat(imageDecoderFactory.supportsFormat(format.build()))
        .isEqualTo(RendererCapabilities.create(C.FORMAT_UNSUPPORTED_SUBTYPE));
  }
}
