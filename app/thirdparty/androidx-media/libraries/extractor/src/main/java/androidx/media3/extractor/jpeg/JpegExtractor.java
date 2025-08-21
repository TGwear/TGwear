/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.jpeg;

import static java.lang.annotation.ElementType.TYPE_USE;

import androidx.annotation.IntDef;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SingleSampleExtractor;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Extracts data from the JPEG container format. */
@UnstableApi
public final class JpegExtractor implements Extractor {
  /**
   * Flags controlling the behavior of the extractor. Possible flag value is {@link
   * #FLAG_READ_IMAGE}.
   */
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @Target(TYPE_USE)
  @IntDef(
      flag = true,
      value = {
        FLAG_READ_IMAGE,
      })
  public @interface Flags {}

  /** Flag to load the image track instead of the video and metadata track. */
  public static final int FLAG_READ_IMAGE = 1;

  // Specification reference: ITU-T.81 (1992) subsection B.1.1.3
  private static final int JPEG_FILE_SIGNATURE = 0xFFD8; // Start of image marker
  private static final int JPEG_FILE_SIGNATURE_LENGTH = 2;

  private final Extractor extractor;

  /** Creates an instance reading the video and metadata track. */
  public JpegExtractor() {
    this(/* flags= */ 0);
  }

  /**
   * Creates an instance.
   *
   * @param flags The {@link JpegExtractor.Flags} to control extractor behavior.
   */
  public JpegExtractor(@JpegExtractor.Flags int flags) {
    if ((flags & FLAG_READ_IMAGE) != 0) {
      extractor =
          new SingleSampleExtractor(
              JPEG_FILE_SIGNATURE, JPEG_FILE_SIGNATURE_LENGTH, MimeTypes.IMAGE_JPEG);
    } else {
      extractor = new JpegMotionPhotoExtractor();
    }
  }

  @Override
  public boolean sniff(ExtractorInput input) throws IOException {
    return extractor.sniff(input);
  }

  @Override
  public void init(ExtractorOutput output) {
    extractor.init(output);
  }

  @Override
  public @ReadResult int read(ExtractorInput input, PositionHolder seekPosition)
      throws IOException {
    return extractor.read(input, seekPosition);
  }

  @Override
  public void seek(long position, long timeUs) {
    extractor.seek(position, timeUs);
  }

  @Override
  public void release() {
    extractor.release();
  }
}
