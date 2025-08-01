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
 * A {@link SniffFailure} indicating an atom declares a size that is too small for the header fields
 * that must present for the given type.
 */
@UnstableApi
public final class AtomSizeTooSmallSniffFailure implements SniffFailure {
  public final int atomType;
  public final long atomSize;
  public final int minimumHeaderSize;

  public AtomSizeTooSmallSniffFailure(int atomType, long atomSize, int minimumHeaderSize) {
    this.atomType = atomType;
    this.atomSize = atomSize;
    this.minimumHeaderSize = minimumHeaderSize;
  }
}
