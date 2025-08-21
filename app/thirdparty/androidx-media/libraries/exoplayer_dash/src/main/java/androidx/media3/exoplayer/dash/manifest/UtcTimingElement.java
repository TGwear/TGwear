/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.dash.manifest;

import androidx.media3.common.util.UnstableApi;

/** Represents a UTCTiming element. */
@UnstableApi
public final class UtcTimingElement {

  public final String schemeIdUri;
  public final String value;

  public UtcTimingElement(String schemeIdUri, String value) {
    this.schemeIdUri = schemeIdUri;
    this.value = value;
  }

  @Override
  public String toString() {
    return schemeIdUri + ", " + value;
  }
}
