/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.muxer;

import static androidx.media3.container.Mp4TimestampData.unixTimeToMp4TimeSeconds;

import androidx.media3.common.Metadata;
import androidx.media3.container.MdtaMetadataEntry;
import androidx.media3.container.Mp4LocationData;
import androidx.media3.container.Mp4OrientationData;
import androidx.media3.container.Mp4TimestampData;
import androidx.media3.container.XmpData;
import java.util.HashSet;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/** Collects and provides metadata: location, FPS, XMP data, etc. */
/* package */ final class MetadataCollector {
  public Mp4OrientationData orientationData;
  public @MonotonicNonNull Mp4LocationData locationData;
  public Set<MdtaMetadataEntry> metadataEntries;
  public Mp4TimestampData timestampData;
  public @MonotonicNonNull XmpData xmpData;

  /** Creates an instance. */
  public MetadataCollector() {
    orientationData = new Mp4OrientationData(/* orientation= */ 0);
    metadataEntries = new HashSet<>();
    long currentTimeInMp4TimeSeconds = unixTimeToMp4TimeSeconds(System.currentTimeMillis());
    timestampData =
        new Mp4TimestampData(
            /* creationTimestampSeconds= */ currentTimeInMp4TimeSeconds,
            /* modificationTimestampSeconds= */ currentTimeInMp4TimeSeconds);
  }

  /** Adds metadata for the output file. */
  public void addMetadata(Metadata.Entry metadata) {
    if (metadata instanceof Mp4OrientationData) {
      orientationData = (Mp4OrientationData) metadata;
    } else if (metadata instanceof Mp4LocationData) {
      locationData = (Mp4LocationData) metadata;
    } else if (metadata instanceof Mp4TimestampData) {
      timestampData = (Mp4TimestampData) metadata;
    } else if (metadata instanceof MdtaMetadataEntry) {
      metadataEntries.add((MdtaMetadataEntry) metadata);
    } else if (metadata instanceof XmpData) {
      xmpData = (XmpData) metadata;
    } else {
      throw new IllegalArgumentException("Unsupported metadata");
    }
  }

  /** Removes a previously added {@link MdtaMetadataEntry}. */
  public void removeMdtaMetadataEntry(MdtaMetadataEntry mdtaMetadataEntry) {
    metadataEntries.remove(mdtaMetadataEntry);
  }
}
