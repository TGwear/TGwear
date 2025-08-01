/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.flac;

import androidx.media3.common.util.UnstableApi;

/** Defines constants used by the FLAC extractor. */
@UnstableApi
public final class FlacConstants {

  /** Size of the FLAC stream marker in bytes. */
  public static final int STREAM_MARKER_SIZE = 4;

  /** Size of the header of a FLAC metadata block in bytes. */
  public static final int METADATA_BLOCK_HEADER_SIZE = 4;

  /** Size of the FLAC stream info block (header included) in bytes. */
  public static final int STREAM_INFO_BLOCK_SIZE = 38;

  /** Minimum size of a FLAC frame header in bytes. */
  public static final int MIN_FRAME_HEADER_SIZE = 6;

  /** Maximum size of a FLAC frame header in bytes. */
  public static final int MAX_FRAME_HEADER_SIZE = 16;

  /** Stream info metadata block type. */
  public static final int METADATA_TYPE_STREAM_INFO = 0;

  /** Seek table metadata block type. */
  public static final int METADATA_TYPE_SEEK_TABLE = 3;

  /** Vorbis comment metadata block type. */
  public static final int METADATA_TYPE_VORBIS_COMMENT = 4;

  /** Picture metadata block type. */
  public static final int METADATA_TYPE_PICTURE = 6;

  private FlacConstants() {}
}
