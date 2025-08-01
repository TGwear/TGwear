/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.common.util.Assertions.checkArgument;
import static androidx.media3.common.util.Assertions.checkNotNull;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.util.Util;

/**
 * Created by {@link MediaController} to send its state to the {@link MediaSession} to request to
 * connect.
 */
/* package */ class ConnectionRequest {

  public final int libraryVersion;

  public final int controllerInterfaceVersion;

  public final String packageName;

  public final int pid;

  public final Bundle connectionHints;

  public final int maxCommandsForMediaItems;

  public ConnectionRequest(
      String packageName, int pid, Bundle connectionHints, int maxCommandsForMediaItems) {
    this(
        MediaLibraryInfo.VERSION_INT,
        MediaControllerStub.VERSION_INT,
        packageName,
        pid,
        new Bundle(connectionHints),
        maxCommandsForMediaItems);
  }

  private ConnectionRequest(
      int libraryVersion,
      int controllerInterfaceVersion,
      String packageName,
      int pid,
      Bundle connectionHints,
      int maxCommandsForMediaItems) {
    this.libraryVersion = libraryVersion;
    this.controllerInterfaceVersion = controllerInterfaceVersion;
    this.packageName = packageName;
    this.pid = pid;
    this.connectionHints = connectionHints;
    this.maxCommandsForMediaItems = maxCommandsForMediaItems;
  }

  private static final String FIELD_LIBRARY_VERSION = Util.intToStringMaxRadix(0);
  private static final String FIELD_PACKAGE_NAME = Util.intToStringMaxRadix(1);
  private static final String FIELD_PID = Util.intToStringMaxRadix(2);
  private static final String FIELD_CONNECTION_HINTS = Util.intToStringMaxRadix(3);
  private static final String FIELD_CONTROLLER_INTERFACE_VERSION = Util.intToStringMaxRadix(4);
  private static final String FIELD_MAX_COMMANDS_FOR_MEDIA_ITEM = Util.intToStringMaxRadix(5);

  // Next id: 6

  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putInt(FIELD_LIBRARY_VERSION, libraryVersion);
    bundle.putString(FIELD_PACKAGE_NAME, packageName);
    bundle.putInt(FIELD_PID, pid);
    bundle.putBundle(FIELD_CONNECTION_HINTS, connectionHints);
    bundle.putInt(FIELD_CONTROLLER_INTERFACE_VERSION, controllerInterfaceVersion);
    bundle.putInt(FIELD_MAX_COMMANDS_FOR_MEDIA_ITEM, maxCommandsForMediaItems);
    return bundle;
  }

  /** Restores a {@code ConnectionRequest} from a {@link Bundle}. */
  public static ConnectionRequest fromBundle(Bundle bundle) {
    int libraryVersion = bundle.getInt(FIELD_LIBRARY_VERSION, /* defaultValue= */ 0);
    int controllerInterfaceVersion =
        bundle.getInt(FIELD_CONTROLLER_INTERFACE_VERSION, /* defaultValue= */ 0);
    String packageName = checkNotNull(bundle.getString(FIELD_PACKAGE_NAME));
    checkArgument(bundle.containsKey(FIELD_PID));
    int pid = bundle.getInt(FIELD_PID);
    @Nullable Bundle connectionHints = bundle.getBundle(FIELD_CONNECTION_HINTS);
    int maxCommandsForMediaItems =
        bundle.getInt(FIELD_MAX_COMMANDS_FOR_MEDIA_ITEM, /* defaultValue= */ 0);
    return new ConnectionRequest(
        libraryVersion,
        controllerInterfaceVersion,
        packageName,
        pid,
        connectionHints == null ? Bundle.EMPTY : connectionHints,
        maxCommandsForMediaItems);
  }
}
