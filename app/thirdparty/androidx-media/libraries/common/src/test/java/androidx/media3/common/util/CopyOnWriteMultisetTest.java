/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.Iterator;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link CopyOnWriteMultiset}. */
@RunWith(AndroidJUnit4.class)
public final class CopyOnWriteMultisetTest {

  @Test
  public void multipleEqualObjectsCountedAsExpected() {
    String item1 = "a string";
    String item2 = "a string";
    String item3 = "different string";

    CopyOnWriteMultiset<String> multiset = new CopyOnWriteMultiset<>();

    multiset.add(item1);
    multiset.add(item2);
    multiset.add(item3);

    assertThat(multiset).containsExactly("a string", "a string", "different string");
    assertThat(multiset.elementSet()).containsExactly("a string", "different string");
  }

  @Test
  public void removingObjectDecrementsCount() {
    String item1 = "a string";
    String item2 = "a string";
    String item3 = "different string";

    CopyOnWriteMultiset<String> multiset = new CopyOnWriteMultiset<>();

    multiset.add(item1);
    multiset.add(item2);
    multiset.add(item3);

    multiset.remove("a string");

    assertThat(multiset).containsExactly("a string", "different string");
    assertThat(multiset.elementSet()).containsExactly("a string", "different string");
  }

  @Test
  public void removingLastObjectRemovesCompletely() {
    String item1 = "a string";
    String item2 = "a string";
    String item3 = "different string";

    CopyOnWriteMultiset<String> multiset = new CopyOnWriteMultiset<>();

    multiset.add(item1);
    multiset.add(item2);
    multiset.add(item3);

    multiset.remove("different string");

    assertThat(multiset).containsExactly("a string", "a string");
    assertThat(multiset.elementSet()).containsExactly("a string");
  }

  @Test
  public void removingNonexistentElementSucceeds() {
    CopyOnWriteMultiset<String> multiset = new CopyOnWriteMultiset<>();

    multiset.remove("a string");
  }

  @Test
  public void modifyingIteratorFails() {
    CopyOnWriteMultiset<String> multiset = new CopyOnWriteMultiset<>();
    multiset.add("a string");

    Iterator<String> iterator = multiset.iterator();

    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }

  @Test
  public void modifyingElementSetFails() {
    CopyOnWriteMultiset<String> multiset = new CopyOnWriteMultiset<>();
    multiset.add("a string");

    Set<String> elementSet = multiset.elementSet();

    assertThrows(UnsupportedOperationException.class, () -> elementSet.remove("a string"));
  }

  @Test
  public void count() {
    CopyOnWriteMultiset<String> multiset = new CopyOnWriteMultiset<>();
    multiset.add("a string");
    multiset.add("a string");

    assertThat(multiset.count("a string")).isEqualTo(2);
    assertThat(multiset.count("another string")).isEqualTo(0);
  }

  @Test
  public void modifyingWhileIteratingElements_succeeds() {
    CopyOnWriteMultiset<String> multiset = new CopyOnWriteMultiset<>();
    multiset.add("a string");
    multiset.add("a string");
    multiset.add("another string");

    // A traditional collection would throw a ConcurrentModificationException here.
    for (String element : multiset) {
      multiset.remove(element);
    }

    assertThat(multiset).isEmpty();
  }

  @Test
  public void modifyingWhileIteratingElementSet_succeeds() {
    CopyOnWriteMultiset<String> multiset = new CopyOnWriteMultiset<>();
    multiset.add("a string");
    multiset.add("a string");
    multiset.add("another string");

    // A traditional collection would throw a ConcurrentModificationException here.
    for (String element : multiset.elementSet()) {
      multiset.remove(element);
    }

    assertThat(multiset).containsExactly("a string");
  }
}
