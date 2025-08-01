/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.scheduler;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import androidx.media3.common.util.UnstableApi;

/** Schedules a service to be started in the foreground when some {@link Requirements} are met. */
@UnstableApi
public interface Scheduler {

  /**
   * Schedules a service to be started in the foreground when some {@link Requirements} are met.
   * Anything that was previously scheduled will be canceled.
   *
   * <p>The service to be started must be declared in the manifest of {@code servicePackage} with an
   * intent filter containing {@code serviceAction}. Note that when started with {@code
   * serviceAction}, the service must call {@link Service#startForeground(int, Notification)} to
   * make itself a foreground service, as documented by {@link
   * Service#startForegroundService(Intent)}.
   *
   * @param requirements The requirements.
   * @param servicePackage The package name.
   * @param serviceAction The action with which the service will be started.
   * @return Whether scheduling was successful.
   */
  boolean schedule(Requirements requirements, String servicePackage, String serviceAction);

  /**
   * Cancels anything that was previously scheduled, or else does nothing.
   *
   * @return Whether cancellation was successful.
   */
  boolean cancel();

  /**
   * Checks whether this {@link Scheduler} supports the provided {@link Requirements}. If all of the
   * requirements are supported then the same {@link Requirements} instance is returned. If not then
   * a new instance is returned containing the subset of the requirements that are supported.
   *
   * @param requirements The requirements to check.
   * @return The supported requirements.
   */
  Requirements getSupportedRequirements(Requirements requirements);
}
