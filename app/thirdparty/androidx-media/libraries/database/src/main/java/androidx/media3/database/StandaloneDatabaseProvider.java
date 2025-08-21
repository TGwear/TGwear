/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

/**
 * An {@link SQLiteOpenHelper} that provides instances of a standalone database.
 *
 * <p>Suitable for use by applications that do not already have their own database, or that would
 * prefer to keep tables used by media library components isolated in their own database. Other
 * applications should prefer to use {@link DefaultDatabaseProvider} with their own {@link
 * SQLiteOpenHelper}.
 */
// TODO: Make this class final when ExoDatabaseProvider is removed.
@UnstableApi
public class StandaloneDatabaseProvider extends SQLiteOpenHelper implements DatabaseProvider {

  /** The file name used for the standalone database. */
  public static final String DATABASE_NAME = "exoplayer_internal.db";

  private static final int VERSION = 1;
  private static final String TAG = "SADatabaseProvider";

  /**
   * Provides instances of the database located by passing {@link #DATABASE_NAME} to {@link
   * Context#getDatabasePath(String)}.
   *
   * @param context Any context.
   */
  public StandaloneDatabaseProvider(Context context) {
    super(context.getApplicationContext(), DATABASE_NAME, /* factory= */ null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // Features create their own tables.
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Features handle their own upgrades.
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    wipeDatabase(db);
  }

  /**
   * Makes a best effort to wipe the existing database. The wipe may be incomplete if the database
   * contains foreign key constraints.
   */
  private static void wipeDatabase(SQLiteDatabase db) {
    String[] columns = {"type", "name"};
    try (Cursor cursor =
        db.query(
            "sqlite_master",
            columns,
            /* selection= */ null,
            /* selectionArgs= */ null,
            /* groupBy= */ null,
            /* having= */ null,
            /* orderBy= */ null)) {
      while (cursor.moveToNext()) {
        String type = cursor.getString(0);
        String name = cursor.getString(1);
        if (!"sqlite_sequence".equals(name)) {
          // If it's not an SQL-controlled entity, drop it
          String sql = "DROP " + type + " IF EXISTS " + name;
          try {
            db.execSQL(sql);
          } catch (SQLException e) {
            Log.e(TAG, "Error executing " + sql, e);
          }
        }
      }
    }
  }
}
