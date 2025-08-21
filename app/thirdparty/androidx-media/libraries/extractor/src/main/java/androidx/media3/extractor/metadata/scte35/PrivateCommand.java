/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.scte35;

import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;

/** Represents a private command as defined in SCTE35, Section 9.3.6. */
@UnstableApi
public final class PrivateCommand extends SpliceCommand {

  /** The {@code pts_adjustment} as defined in SCTE35, Section 9.2. */
  public final long ptsAdjustment;

  /** The identifier as defined in SCTE35, Section 9.3.6. */
  public final long identifier;

  /** The private bytes as defined in SCTE35, Section 9.3.6. */
  public final byte[] commandBytes;

  private PrivateCommand(long identifier, byte[] commandBytes, long ptsAdjustment) {
    this.ptsAdjustment = ptsAdjustment;
    this.identifier = identifier;
    this.commandBytes = commandBytes;
  }

  /* package */ static PrivateCommand parseFromSection(
      ParsableByteArray sectionData, int commandLength, long ptsAdjustment) {
    long identifier = sectionData.readUnsignedInt();
    byte[] privateBytes = new byte[commandLength - 4 /* identifier size */];
    sectionData.readBytes(privateBytes, 0, privateBytes.length);
    return new PrivateCommand(identifier, privateBytes, ptsAdjustment);
  }

  @Override
  public String toString() {
    return "SCTE-35 PrivateCommand { ptsAdjustment="
        + ptsAdjustment
        + ", identifier= "
        + identifier
        + " }";
  }
}
