/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.database;

import static androidx.media3.database.VersionTable.FEATURE_CACHE_FILE_METADATA;
import static androidx.media3.database.VersionTable.FEATURE_OFFLINE;
import static com.google.common.truth.Truth.assertThat;

import android.database.sqlite.SQLiteDatabase;
import androidx.media3.test.utils.TestUtil;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link VersionTable}. */
@RunWith(AndroidJUnit4.class)
public class VersionTableTest {

  private static final String INSTANCE_1 = "1";
  private static final String INSTANCE_2 = "2";

  private DatabaseProvider databaseProvider;
  private SQLiteDatabase database;

  @Before
  public void setUp() {
    databaseProvider = TestUtil.getInMemoryDatabaseProvider();
    database = databaseProvider.getWritableDatabase();
  }

  @Test
  public void getVersion_unsetFeature_returnsVersionUnset() throws DatabaseIOException {
    int version = VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_1);
    assertThat(version).isEqualTo(VersionTable.VERSION_UNSET);
  }

  @Test
  public void getVersion_unsetVersion_returnsVersionUnset() throws DatabaseIOException {
    VersionTable.setVersion(database, FEATURE_OFFLINE, INSTANCE_1, 1);
    int version = VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_2);
    assertThat(version).isEqualTo(VersionTable.VERSION_UNSET);
  }

  @Test
  public void getVersion_returnsSetVersion() throws DatabaseIOException {
    VersionTable.setVersion(database, FEATURE_OFFLINE, INSTANCE_1, 1);
    assertThat(VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_1)).isEqualTo(1);

    VersionTable.setVersion(database, FEATURE_OFFLINE, INSTANCE_1, 2);
    assertThat(VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_1)).isEqualTo(2);

    VersionTable.setVersion(database, FEATURE_CACHE_FILE_METADATA, INSTANCE_1, 3);
    assertThat(VersionTable.getVersion(database, FEATURE_CACHE_FILE_METADATA, INSTANCE_1))
        .isEqualTo(3);
    assertThat(VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_1)).isEqualTo(2);

    VersionTable.setVersion(database, FEATURE_CACHE_FILE_METADATA, INSTANCE_2, 4);
    assertThat(VersionTable.getVersion(database, FEATURE_CACHE_FILE_METADATA, INSTANCE_2))
        .isEqualTo(4);
    assertThat(VersionTable.getVersion(database, FEATURE_CACHE_FILE_METADATA, INSTANCE_1))
        .isEqualTo(3);
    assertThat(VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_1)).isEqualTo(2);
  }

  @Test
  public void removeVersion_removesSetVersion() throws DatabaseIOException {
    VersionTable.setVersion(database, FEATURE_OFFLINE, INSTANCE_1, 1);
    VersionTable.setVersion(database, FEATURE_OFFLINE, INSTANCE_2, 2);
    assertThat(VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_1)).isEqualTo(1);
    assertThat(VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_2)).isEqualTo(2);

    VersionTable.removeVersion(database, FEATURE_OFFLINE, INSTANCE_1);
    assertThat(VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_1))
        .isEqualTo(VersionTable.VERSION_UNSET);
    assertThat(VersionTable.getVersion(database, FEATURE_OFFLINE, INSTANCE_2)).isEqualTo(2);
  }
}
