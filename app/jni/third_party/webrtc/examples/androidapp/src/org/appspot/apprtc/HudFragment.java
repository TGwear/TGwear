/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.appspot.apprtc;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import org.webrtc.RTCStats;
import org.webrtc.RTCStatsReport;

/**
 * Fragment for HUD statistics display.
 */
public class HudFragment extends Fragment {
  private TextView statView;
  private ImageButton toggleDebugButton;
  private boolean displayHud;
  private volatile boolean isRunning;
  private CpuMonitor cpuMonitor;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View controlView = inflater.inflate(R.layout.fragment_hud, container, false);

    // Create UI controls.
    statView = controlView.findViewById(R.id.hud_stat_call);
    toggleDebugButton = controlView.findViewById(R.id.button_toggle_debug);

    toggleDebugButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (displayHud) {
          statView.setVisibility(
              statView.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        }
      }
    });

    return controlView;
  }

  @Override
  public void onStart() {
    super.onStart();

    Bundle args = getArguments();
    if (args != null) {
      displayHud = args.getBoolean(CallActivity.EXTRA_DISPLAY_HUD, false);
    }
    int visibility = displayHud ? View.VISIBLE : View.INVISIBLE;
    statView.setVisibility(View.INVISIBLE);
    toggleDebugButton.setVisibility(visibility);
    isRunning = true;
  }

  @Override
  public void onStop() {
    isRunning = false;
    super.onStop();
  }

  public void setCpuMonitor(CpuMonitor cpuMonitor) {
    this.cpuMonitor = cpuMonitor;
  }

  public void updateEncoderStatistics(final RTCStatsReport report) {
    if (!isRunning || !displayHud) {
      return;
    }

    StringBuilder sb = new StringBuilder();

    if (cpuMonitor != null) {
      sb.append("CPU%: ")
          .append(cpuMonitor.getCpuUsageCurrent())
          .append("/")
          .append(cpuMonitor.getCpuUsageAverage())
          .append(". Freq: ")
          .append(cpuMonitor.getFrequencyScaleAverage())
          .append("\n");
    }

    for (RTCStats stat : report.getStatsMap().values()) {
      sb.append(stat.toString()).append("\n");
    }

    statView.setText(sb.toString());
  }
}
