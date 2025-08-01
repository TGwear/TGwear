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

/**
 * {@link SniffFailure} indicating the file's fragmented flag is incompatible with this {@link
 * androidx.media3.extractor.Extractor}.
 */
@UnstableApi
public final class IncorrectFragmentationSniffFailure implements SniffFailure {

  public static final IncorrectFragmentationSniffFailure FILE_FRAGMENTED =
      new IncorrectFragmentationSniffFailure(/* fileIsFragmented= */ true);

  public static final IncorrectFragmentationSniffFailure FILE_NOT_FRAGMENTED =
      new IncorrectFragmentationSniffFailure(/* fileIsFragmented= */ false);

  public final boolean fileIsFragmented;

  private IncorrectFragmentationSniffFailure(boolean fileIsFragmented) {
    this.fileIsFragmented = fileIsFragmented;
  }
}
