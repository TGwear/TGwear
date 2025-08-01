/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import androidx.annotation.Nullable;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.Format;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;

/** Wraps a {@link Format} to allow dumping it. */
@UnstableApi
public final class DumpableFormat implements Dumper.Dumpable {
  private final Format format;
  private final String tag;

  private static final Format DEFAULT_FORMAT = new Format.Builder().build();
  private static final ColorInfo DEFAULT_COLOR_INFO = new ColorInfo.Builder().build();

  public DumpableFormat(Format format, int index) {
    this(format, Integer.toString(index));
  }

  public DumpableFormat(Format format, String tag) {
    this.format = format;
    this.tag = tag;
  }

  @Override
  public void dump(Dumper dumper) {
    dumper.startBlock("format " + tag);
    dumper.addIfNonDefault(
        "averageBitrate", format, DEFAULT_FORMAT, format -> format.averageBitrate);
    dumper.addIfNonDefault("peakBitrate", format, DEFAULT_FORMAT, format -> format.peakBitrate);
    dumper.addIfNonDefault("id", format, DEFAULT_FORMAT, format -> format.id);
    dumper.addIfNonDefault(
        "containerMimeType", format, DEFAULT_FORMAT, format -> format.containerMimeType);
    dumper.addIfNonDefault(
        "sampleMimeType", format, DEFAULT_FORMAT, format -> format.sampleMimeType);
    dumper.addIfNonDefault("codecs", format, DEFAULT_FORMAT, format -> format.codecs);
    dumper.addIfNonDefault("maxInputSize", format, DEFAULT_FORMAT, format -> format.maxInputSize);
    dumper.addIfNonDefault(
        "maxNumReorderSamples", format, DEFAULT_FORMAT, format -> format.maxNumReorderSamples);
    dumper.addIfNonDefault("width", format, DEFAULT_FORMAT, format -> format.width);
    dumper.addIfNonDefault("height", format, DEFAULT_FORMAT, format -> format.height);
    dumper.addIfNonDefault(
        "frameRate",
        format,
        DEFAULT_FORMAT,
        format -> Util.formatInvariant("%.2f", format.frameRate));
    dumper.addIfNonDefault(
        "rotationDegrees", format, DEFAULT_FORMAT, format -> format.rotationDegrees);
    dumper.addIfNonDefault(
        "pixelWidthHeightRatio", format, DEFAULT_FORMAT, format -> format.pixelWidthHeightRatio);
    dumper.addIfNonDefault("maxSubLayers", format, DEFAULT_FORMAT, format -> format.maxSubLayers);
    @Nullable ColorInfo colorInfo = format.colorInfo;
    if (colorInfo != null) {
      dumper.startBlock("colorInfo");
      dumper.addIfNonDefault("colorSpace", colorInfo, DEFAULT_COLOR_INFO, c -> c.colorSpace);
      dumper.addIfNonDefault("colorRange", colorInfo, DEFAULT_COLOR_INFO, c -> c.colorRange);
      dumper.addIfNonDefault("colorTransfer", colorInfo, DEFAULT_COLOR_INFO, c -> c.colorTransfer);
      if (colorInfo.hdrStaticInfo != null) {
        dumper.add("hdrStaticInfo", colorInfo.hdrStaticInfo);
      }
      dumper.addIfNonDefault("lumaBitdepth", colorInfo, DEFAULT_COLOR_INFO, c -> c.lumaBitdepth);
      dumper.addIfNonDefault(
          "chromaBitdepth", colorInfo, DEFAULT_COLOR_INFO, c -> c.chromaBitdepth);
      dumper.endBlock();
    }
    dumper.addIfNonDefault("channelCount", format, DEFAULT_FORMAT, format -> format.channelCount);
    dumper.addIfNonDefault("sampleRate", format, DEFAULT_FORMAT, format -> format.sampleRate);
    dumper.addIfNonDefault("pcmEncoding", format, DEFAULT_FORMAT, format -> format.pcmEncoding);
    dumper.addIfNonDefault("encoderDelay", format, DEFAULT_FORMAT, format -> format.encoderDelay);
    dumper.addIfNonDefault(
        "encoderPadding", format, DEFAULT_FORMAT, format -> format.encoderPadding);
    dumper.addIfNonDefault(
        "subsampleOffsetUs", format, DEFAULT_FORMAT, format -> format.subsampleOffsetUs);
    dumper.addIfNonDefault(
        "selectionFlags",
        format,
        DEFAULT_FORMAT,
        format -> Util.getSelectionFlagStrings(format.selectionFlags));
    dumper.addIfNonDefault(
        "roleFlags", format, DEFAULT_FORMAT, format -> Util.getRoleFlagStrings(format.roleFlags));
    dumper.addIfNonDefault(
        "auxiliaryTrackType",
        format,
        DEFAULT_FORMAT,
        format -> Util.getAuxiliaryTrackTypeString(format.auxiliaryTrackType));
    dumper.addIfNonDefault("language", format, DEFAULT_FORMAT, format -> format.language);
    dumper.addIfNonDefault("label", format, DEFAULT_FORMAT, format -> format.label);
    if (!format.labels.isEmpty()) {
      dumper.startBlock("labels");
      for (int i = 0; i < format.labels.size(); i++) {
        String lang = format.labels.get(i).language;
        if (lang != null) {
          dumper.add("lang", lang);
        }
        dumper.add("value", format.labels.get(i).value);
      }
      dumper.endBlock();
    }
    if (format.drmInitData != null) {
      dumper.add("drmInitData", format.drmInitData.hashCode());
    }
    dumper.addIfNonDefault("metadata", format, DEFAULT_FORMAT, format -> format.metadata);
    if (!format.initializationData.isEmpty()) {
      dumper.startBlock("initializationData");
      for (int i = 0; i < format.initializationData.size(); i++) {
        dumper.add("data", format.initializationData.get(i));
      }
      dumper.endBlock();
    }
    dumper.endBlock();
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DumpableFormat that = (DumpableFormat) o;
    return tag.equals(that.tag) && format.equals(that.format);
  }

  @Override
  public int hashCode() {
    int result = format.hashCode();
    result = 31 * result + tag.hashCode();
    return result;
  }
}
