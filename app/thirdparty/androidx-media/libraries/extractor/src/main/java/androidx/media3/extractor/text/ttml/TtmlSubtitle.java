/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text.ttml;

import androidx.annotation.VisibleForTesting;
import androidx.media3.common.C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.Subtitle;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** A representation of a TTML subtitle. */
/* package */ final class TtmlSubtitle implements Subtitle {

  private final TtmlNode root;
  private final long[] eventTimesUs;
  private final Map<String, TtmlStyle> globalStyles;
  private final Map<String, TtmlRegion> regionMap;
  private final Map<String, String> imageMap;

  public TtmlSubtitle(
      TtmlNode root,
      Map<String, TtmlStyle> globalStyles,
      Map<String, TtmlRegion> regionMap,
      Map<String, String> imageMap) {
    this.root = root;
    this.regionMap = regionMap;
    this.imageMap = imageMap;
    this.globalStyles =
        globalStyles != null ? Collections.unmodifiableMap(globalStyles) : Collections.emptyMap();
    this.eventTimesUs = root.getEventTimesUs();
  }

  @Override
  public int getNextEventTimeIndex(long timeUs) {
    int index = Util.binarySearchCeil(eventTimesUs, timeUs, false, false);
    return index < eventTimesUs.length ? index : C.INDEX_UNSET;
  }

  @Override
  public int getEventTimeCount() {
    return eventTimesUs.length;
  }

  @Override
  public long getEventTime(int index) {
    return eventTimesUs[index];
  }

  @VisibleForTesting
  /* package */ TtmlNode getRoot() {
    return root;
  }

  @Override
  public List<Cue> getCues(long timeUs) {
    return root.getCues(timeUs, globalStyles, regionMap, imageMap);
  }

  @VisibleForTesting
  /* package */ Map<String, TtmlStyle> getGlobalStyles() {
    return globalStyles;
  }
}
