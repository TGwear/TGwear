/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import java.util.List;

/** Provides information about views for the ad playback UI. */
public interface AdViewProvider {

  /**
   * Returns the {@link ViewGroup} on top of the player that will show any ad UI, or {@code null} if
   * playing audio-only ads. Any views on top of the returned view group must be described by {@link
   * AdOverlayInfo AdOverlayInfos} returned by {@link #getAdOverlayInfos()}, for accurate
   * viewability measurement.
   */
  @Nullable
  ViewGroup getAdViewGroup();

  /**
   * Returns a list of {@link AdOverlayInfo} instances describing views that are on top of the ad
   * view group, but that are essential for controlling playback and should be excluded from ad
   * viewability measurements.
   *
   * <p>Each view must be either a fully transparent overlay (for capturing touch events), or a
   * small piece of transient UI that is essential to the user experience of playback (such as a
   * button to pause/resume playback or a transient full-screen or cast button). For more
   * information see the documentation for your ads loader.
   */
  default List<AdOverlayInfo> getAdOverlayInfos() {
    return ImmutableList.of();
  }
}
