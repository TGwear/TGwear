/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.database;

import android.database.SQLException;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/** An {@link IOException} whose cause is an {@link SQLException}. */
@UnstableApi
public final class DatabaseIOException extends IOException {

  public DatabaseIOException(SQLException cause) {
    super(cause);
  }

  public DatabaseIOException(SQLException cause, String message) {
    super(message, cause);
  }
}
