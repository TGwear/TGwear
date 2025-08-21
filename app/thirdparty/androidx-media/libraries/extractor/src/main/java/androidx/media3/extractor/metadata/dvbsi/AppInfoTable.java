/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.dvbsi;

import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;

/**
 * A representation of a DVB Application Information Table (AIT).
 *
 * <p>For more info on the AIT see section 5.3.4 of the <a
 * href="https://www.etsi.org/deliver/etsi_ts/102800_102899/102809/01.01.01_60/ts_102809v010101p.pdf">
 * DVB ETSI TS 102 809 v1.1.1 spec</a>.
 */
@UnstableApi
public final class AppInfoTable implements Metadata.Entry {
  /**
   * The application shall be started when the service is selected, unless the application is
   * already running.
   */
  public static final int CONTROL_CODE_AUTOSTART = 0x01;

  /**
   * The application is allowed to run while the service is selected, however it shall not start
   * automatically when the service becomes selected.
   */
  public static final int CONTROL_CODE_PRESENT = 0x02;

  public final int controlCode;
  public final String url;

  public AppInfoTable(int controlCode, String url) {
    this.controlCode = controlCode;
    this.url = url;
  }

  @Override
  public String toString() {
    return "Ait(controlCode=" + controlCode + ",url=" + url + ")";
  }
}
