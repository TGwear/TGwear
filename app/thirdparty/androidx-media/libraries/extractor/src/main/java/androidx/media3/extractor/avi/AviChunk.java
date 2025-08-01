/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.avi;

/**
 * A chunk, as defined in the AVI spec.
 *
 * <p>See https://docs.microsoft.com/en-us/windows/win32/directshow/avi-riff-file-reference.
 */
/* package */ interface AviChunk {

  /** Returns the chunk type fourcc. */
  int getType();
}
