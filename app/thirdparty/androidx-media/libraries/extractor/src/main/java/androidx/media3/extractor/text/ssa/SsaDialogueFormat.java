/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text.ssa;

import static androidx.media3.extractor.text.ssa.SsaParser.FORMAT_LINE_PREFIX;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.Assertions;
import com.google.common.base.Ascii;

/**
 * Represents a {@code Format:} line from the {@code [Events]} section
 *
 * <p>The indices are used to determine the location of particular properties in each {@code
 * Dialogue:} line.
 */
/* package */ final class SsaDialogueFormat {

  public final int startTimeIndex;
  public final int endTimeIndex;
  public final int styleIndex;
  public final int textIndex;
  public final int length;

  private SsaDialogueFormat(
      int startTimeIndex, int endTimeIndex, int styleIndex, int textIndex, int length) {
    this.startTimeIndex = startTimeIndex;
    this.endTimeIndex = endTimeIndex;
    this.styleIndex = styleIndex;
    this.textIndex = textIndex;
    this.length = length;
  }

  /**
   * Parses the format info from a 'Format:' line in the [Events] section.
   *
   * @return the parsed info, or null if {@code formatLine} doesn't contain both 'start' and 'end'.
   */
  @Nullable
  public static SsaDialogueFormat fromFormatLine(String formatLine) {
    int startTimeIndex = C.INDEX_UNSET;
    int endTimeIndex = C.INDEX_UNSET;
    int styleIndex = C.INDEX_UNSET;
    int textIndex = C.INDEX_UNSET;
    Assertions.checkArgument(formatLine.startsWith(FORMAT_LINE_PREFIX));
    String[] keys = TextUtils.split(formatLine.substring(FORMAT_LINE_PREFIX.length()), ",");
    for (int i = 0; i < keys.length; i++) {
      switch (Ascii.toLowerCase(keys[i].trim())) {
        case "start":
          startTimeIndex = i;
          break;
        case "end":
          endTimeIndex = i;
          break;
        case "style":
          styleIndex = i;
          break;
        case "text":
          textIndex = i;
          break;
      }
    }
    return (startTimeIndex != C.INDEX_UNSET
            && endTimeIndex != C.INDEX_UNSET
            && textIndex != C.INDEX_UNSET)
        ? new SsaDialogueFormat(startTimeIndex, endTimeIndex, styleIndex, textIndex, keys.length)
        : null;
  }
}
