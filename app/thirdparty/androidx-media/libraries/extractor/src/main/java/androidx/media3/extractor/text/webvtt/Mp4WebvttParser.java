/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text.webvtt;

import static androidx.media3.common.util.Assertions.checkArgument;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.Format.CueReplacementBehavior;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.SubtitleParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A {@link SubtitleParser} for Webvtt embedded in a Mp4 container file. */
@SuppressWarnings("ConstantField")
@UnstableApi
public final class Mp4WebvttParser implements SubtitleParser {

  /**
   * The {@link CueReplacementBehavior} for consecutive {@link CuesWithTiming} emitted by this
   * implementation.
   */
  public static final @CueReplacementBehavior int CUE_REPLACEMENT_BEHAVIOR =
      Format.CUE_REPLACEMENT_BEHAVIOR_REPLACE;

  private static final int BOX_HEADER_SIZE = 8;

  @SuppressWarnings("ConstantCaseForConstants")
  private static final int TYPE_payl = 0x7061796c;

  @SuppressWarnings("ConstantCaseForConstants")
  private static final int TYPE_sttg = 0x73747467;

  @SuppressWarnings("ConstantCaseForConstants")
  private static final int TYPE_vttc = 0x76747463;

  private final ParsableByteArray parsableByteArray;

  public Mp4WebvttParser() {
    parsableByteArray = new ParsableByteArray();
  }

  @Override
  public @CueReplacementBehavior int getCueReplacementBehavior() {
    return CUE_REPLACEMENT_BEHAVIOR;
  }

  @Override
  public void parse(
      byte[] data,
      int offset,
      int length,
      OutputOptions outputOptions,
      Consumer<CuesWithTiming> output) {
    parsableByteArray.reset(data, /* limit= */ offset + length);
    parsableByteArray.setPosition(offset);
    List<Cue> cues = new ArrayList<>();
    while (parsableByteArray.bytesLeft() > 0) {
      // Webvtt in Mp4 samples have boxes inside of them, so we have to do a traditional box
      // parsing: first 4 bytes size and then 4 bytes type.
      checkArgument(
          parsableByteArray.bytesLeft() >= BOX_HEADER_SIZE,
          "Incomplete Mp4Webvtt Top Level box header found.");
      int boxSize = parsableByteArray.readInt();
      int boxType = parsableByteArray.readInt();
      if (boxType == TYPE_vttc) {
        cues.add(parseVttCueBox(parsableByteArray, boxSize - BOX_HEADER_SIZE));
      } else {
        // Peers of the VTTCueBox are still not supported and are skipped.
        parsableByteArray.skipBytes(boxSize - BOX_HEADER_SIZE);
      }
    }
    output.accept(
        new CuesWithTiming(cues, /* startTimeUs= */ C.TIME_UNSET, /* durationUs= */ C.TIME_UNSET));
  }

  private static Cue parseVttCueBox(ParsableByteArray sampleData, int remainingCueBoxBytes) {
    @Nullable Cue.Builder cueBuilder = null;
    @Nullable CharSequence cueText = null;
    while (remainingCueBoxBytes > 0) {
      checkArgument(
          remainingCueBoxBytes >= BOX_HEADER_SIZE, "Incomplete vtt cue box header found.");
      int boxSize = sampleData.readInt();
      int boxType = sampleData.readInt();
      remainingCueBoxBytes -= BOX_HEADER_SIZE;
      int payloadLength = boxSize - BOX_HEADER_SIZE;
      String boxPayload =
          Util.fromUtf8Bytes(sampleData.getData(), sampleData.getPosition(), payloadLength);
      sampleData.skipBytes(payloadLength);
      remainingCueBoxBytes -= payloadLength;
      if (boxType == TYPE_sttg) {
        cueBuilder = WebvttCueParser.parseCueSettingsList(boxPayload);
      } else if (boxType == TYPE_payl) {
        cueText =
            WebvttCueParser.parseCueText(
                /* id= */ null, boxPayload.trim(), /* styles= */ Collections.emptyList());
      } else {
        // Other VTTCueBox children are still not supported and are ignored.
      }
    }
    if (cueText == null) {
      cueText = "";
    }
    return cueBuilder != null
        ? cueBuilder.setText(cueText).build()
        : WebvttCueParser.newCueForText(cueText);
  }
}
