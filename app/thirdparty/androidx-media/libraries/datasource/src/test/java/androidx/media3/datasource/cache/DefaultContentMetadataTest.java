/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests {@link DefaultContentMetadata}. */
@RunWith(AndroidJUnit4.class)
public class DefaultContentMetadataTest {

  private DefaultContentMetadata contentMetadata;

  @Before
  public void setUp() throws Exception {
    contentMetadata = createContentMetadata();
  }

  @Test
  public void containsReturnsFalseWhenEmpty() throws Exception {
    assertThat(contentMetadata.contains("test metadata")).isFalse();
  }

  @Test
  public void containsReturnsTrueForInitialValue() throws Exception {
    contentMetadata = createContentMetadata("metadata name", "value");
    assertThat(contentMetadata.contains("metadata name")).isTrue();
  }

  @Test
  public void getReturnsDefaultValueWhenValueIsNotAvailable() throws Exception {
    assertThat(contentMetadata.get("metadata name", "default value")).isEqualTo("default value");
  }

  @Test
  public void getReturnsInitialValue() throws Exception {
    contentMetadata = createContentMetadata("metadata name", "value");
    assertThat(contentMetadata.get("metadata name", "default value")).isEqualTo("value");
  }

  @Test
  public void emptyMutationDoesNotFail() throws Exception {
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    DefaultContentMetadata.EMPTY.copyWithMutationsApplied(mutations);
  }

  @Test
  public void addNewMetadata() throws Exception {
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    mutations.set("metadata name", "value");
    contentMetadata = contentMetadata.copyWithMutationsApplied(mutations);
    assertThat(contentMetadata.get("metadata name", "default value")).isEqualTo("value");
  }

  @Test
  public void addNewIntMetadata() throws Exception {
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    mutations.set("metadata name", 5);
    contentMetadata = contentMetadata.copyWithMutationsApplied(mutations);
    assertThat(contentMetadata.get("metadata name", 0)).isEqualTo(5);
  }

  @Test
  public void addNewByteArrayMetadata() throws Exception {
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    byte[] value = {1, 2, 3};
    mutations.set("metadata name", value);
    contentMetadata = contentMetadata.copyWithMutationsApplied(mutations);
    assertThat(contentMetadata.get("metadata name", new byte[] {})).isEqualTo(value);
  }

  @Test
  public void newMetadataNotWrittenBeforeCommitted() throws Exception {
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    mutations.set("metadata name", "value");
    assertThat(contentMetadata.get("metadata name", "default value")).isEqualTo("default value");
  }

  @Test
  public void editMetadata() throws Exception {
    contentMetadata = createContentMetadata("metadata name", "value");
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    mutations.set("metadata name", "edited value");
    contentMetadata = contentMetadata.copyWithMutationsApplied(mutations);
    assertThat(contentMetadata.get("metadata name", "default value")).isEqualTo("edited value");
  }

  @Test
  public void removeMetadata() throws Exception {
    contentMetadata = createContentMetadata("metadata name", "value");
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    mutations.remove("metadata name");
    contentMetadata = contentMetadata.copyWithMutationsApplied(mutations);
    assertThat(contentMetadata.get("metadata name", "default value")).isEqualTo("default value");
  }

  @Test
  public void addAndRemoveMetadata() throws Exception {
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    mutations.set("metadata name", "value");
    mutations.remove("metadata name");
    contentMetadata = contentMetadata.copyWithMutationsApplied(mutations);
    assertThat(contentMetadata.get("metadata name", "default value")).isEqualTo("default value");
  }

  @Test
  public void removeAndAddMetadata() throws Exception {
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    mutations.remove("metadata name");
    mutations.set("metadata name", "value");
    contentMetadata = contentMetadata.copyWithMutationsApplied(mutations);
    assertThat(contentMetadata.get("metadata name", "default value")).isEqualTo("value");
  }

  @Test
  public void equalsStringValues() throws Exception {
    DefaultContentMetadata metadata1 = createContentMetadata("metadata1", "value");
    DefaultContentMetadata metadata2 = createContentMetadata("metadata1", "value");
    assertThat(metadata1).isEqualTo(metadata2);
  }

  @Test
  public void equals() throws Exception {
    DefaultContentMetadata metadata1 =
        createContentMetadata(
            "metadata1", "value", "metadata2", 12345, "metadata3", new byte[] {1, 2, 3});
    DefaultContentMetadata metadata2 =
        createContentMetadata(
            "metadata2", 12345, "metadata3", new byte[] {1, 2, 3}, "metadata1", "value");
    assertThat(metadata1).isEqualTo(metadata2);
    assertThat(metadata1.hashCode()).isEqualTo(metadata2.hashCode());
  }

  @Test
  public void notEquals() throws Exception {
    DefaultContentMetadata metadata1 = createContentMetadata("metadata1", new byte[] {1, 2, 3});
    DefaultContentMetadata metadata2 = createContentMetadata("metadata1", new byte[] {3, 2, 1});
    assertThat(metadata1).isNotEqualTo(metadata2);
    assertThat(metadata1.hashCode()).isNotEqualTo(metadata2.hashCode());
  }

  private DefaultContentMetadata createContentMetadata(Object... pairs) {
    assertThat(pairs.length % 2).isEqualTo(0);
    ContentMetadataMutations mutations = new ContentMetadataMutations();
    for (int i = 0; i < pairs.length; i += 2) {
      String name = (String) pairs[i];
      Object value = pairs[i + 1];
      if (value instanceof String) {
        mutations.set(name, (String) value);
      } else if (value instanceof byte[]) {
        mutations.set(name, (byte[]) value);
      } else if (value instanceof Number) {
        mutations.set(name, ((Number) value).longValue());
      } else {
        throw new IllegalArgumentException();
      }
    }
    return DefaultContentMetadata.EMPTY.copyWithMutationsApplied(mutations);
  }
}
