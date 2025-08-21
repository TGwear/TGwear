/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.database.DatabaseIOException;
import androidx.media3.test.utils.TestUtil;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.HashSet;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests {@link CacheFileMetadataIndex}. */
@RunWith(AndroidJUnit4.class)
public class CacheFileMetadataIndexTest {

  @Test
  public void initiallyEmpty() throws DatabaseIOException {
    CacheFileMetadataIndex index = newInitializedIndex();
    assertThat(index.getAll()).isEmpty();
  }

  @Test
  public void insert() throws DatabaseIOException {
    CacheFileMetadataIndex index = newInitializedIndex();

    index.set("name1", /* length= */ 123, /* lastTouchTimestamp= */ 456);
    index.set("name2", /* length= */ 789, /* lastTouchTimestamp= */ 123);

    Map<String, CacheFileMetadata> all = index.getAll();
    assertThat(all.size()).isEqualTo(2);

    CacheFileMetadata metadata = all.get("name1");
    assertThat(metadata).isNotNull();
    assertThat(metadata.length).isEqualTo(123);
    assertThat(metadata.lastTouchTimestamp).isEqualTo(456);

    metadata = all.get("name2");
    assertThat(metadata).isNotNull();
    assertThat(metadata.length).isEqualTo(789);
    assertThat(metadata.lastTouchTimestamp).isEqualTo(123);

    metadata = all.get("name3");
    assertThat(metadata).isNull();
  }

  @Test
  public void insertAndRemove() throws DatabaseIOException {
    CacheFileMetadataIndex index = newInitializedIndex();

    index.set("name1", /* length= */ 123, /* lastTouchTimestamp= */ 456);
    index.set("name2", /* length= */ 789, /* lastTouchTimestamp= */ 123);

    index.remove("name1");

    Map<String, CacheFileMetadata> all = index.getAll();
    assertThat(all.size()).isEqualTo(1);

    CacheFileMetadata metadata = all.get("name1");
    assertThat(metadata).isNull();

    metadata = all.get("name2");
    assertThat(metadata).isNotNull();
    assertThat(metadata.length).isEqualTo(789);
    assertThat(metadata.lastTouchTimestamp).isEqualTo(123);

    index.remove("name2");

    all = index.getAll();
    assertThat(all).isEmpty();

    metadata = all.get("name2");
    assertThat(metadata).isNull();
  }

  @Test
  public void insertAndRemoveAll() throws DatabaseIOException {
    CacheFileMetadataIndex index = newInitializedIndex();

    index.set("name1", /* length= */ 123, /* lastTouchTimestamp= */ 456);
    index.set("name2", /* length= */ 789, /* lastTouchTimestamp= */ 123);

    HashSet<String> namesToRemove = new HashSet<>();
    namesToRemove.add("name1");
    namesToRemove.add("name2");
    index.removeAll(namesToRemove);

    Map<String, CacheFileMetadata> all = index.getAll();
    assertThat(all.isEmpty()).isTrue();

    CacheFileMetadata metadata = all.get("name1");
    assertThat(metadata).isNull();

    metadata = all.get("name2");
    assertThat(metadata).isNull();
  }

  @Test
  public void insertAndReplace() throws DatabaseIOException {
    CacheFileMetadataIndex index = newInitializedIndex();

    index.set("name1", /* length= */ 123, /* lastTouchTimestamp= */ 456);
    index.set("name1", /* length= */ 789, /* lastTouchTimestamp= */ 123);

    Map<String, CacheFileMetadata> all = index.getAll();
    assertThat(all.size()).isEqualTo(1);

    CacheFileMetadata metadata = all.get("name1");
    assertThat(metadata).isNotNull();
    assertThat(metadata.length).isEqualTo(789);
    assertThat(metadata.lastTouchTimestamp).isEqualTo(123);
  }

  private static CacheFileMetadataIndex newInitializedIndex() throws DatabaseIOException {
    CacheFileMetadataIndex index =
        new CacheFileMetadataIndex(TestUtil.getInMemoryDatabaseProvider());
    index.initialize(/* uid= */ 1234);
    return index;
  }
}
