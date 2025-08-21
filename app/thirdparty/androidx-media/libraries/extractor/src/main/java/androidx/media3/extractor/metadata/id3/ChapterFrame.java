/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.id3;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import java.util.Arrays;
import java.util.Objects;

/** Chapter information ID3 frame. */
@UnstableApi
public final class ChapterFrame extends Id3Frame {

  public static final String ID = "CHAP";

  public final String chapterId;
  public final int startTimeMs;
  public final int endTimeMs;

  /** The byte offset of the start of the chapter, or {@link C#INDEX_UNSET} if not set. */
  public final long startOffset;

  /** The byte offset of the end of the chapter, or {@link C#INDEX_UNSET} if not set. */
  public final long endOffset;

  private final Id3Frame[] subFrames;

  public ChapterFrame(
      String chapterId,
      int startTimeMs,
      int endTimeMs,
      long startOffset,
      long endOffset,
      Id3Frame[] subFrames) {
    super(ID);
    this.chapterId = chapterId;
    this.startTimeMs = startTimeMs;
    this.endTimeMs = endTimeMs;
    this.startOffset = startOffset;
    this.endOffset = endOffset;
    this.subFrames = subFrames;
  }

  /** Returns the number of sub-frames. */
  public int getSubFrameCount() {
    return subFrames.length;
  }

  /** Returns the sub-frame at {@code index}. */
  public Id3Frame getSubFrame(int index) {
    return subFrames[index];
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ChapterFrame other = (ChapterFrame) obj;
    return startTimeMs == other.startTimeMs
        && endTimeMs == other.endTimeMs
        && startOffset == other.startOffset
        && endOffset == other.endOffset
        && Objects.equals(chapterId, other.chapterId)
        && Arrays.equals(subFrames, other.subFrames);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + startTimeMs;
    result = 31 * result + endTimeMs;
    result = 31 * result + (int) startOffset;
    result = 31 * result + (int) endOffset;
    result = 31 * result + (chapterId != null ? chapterId.hashCode() : 0);
    return result;
  }
}
