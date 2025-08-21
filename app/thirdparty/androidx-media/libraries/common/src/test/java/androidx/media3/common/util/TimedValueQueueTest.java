/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link TimedValueQueue}. */
@RunWith(AndroidJUnit4.class)
public class TimedValueQueueTest {

  private TimedValueQueue<String> queue;

  @Before
  public void setUp() throws Exception {
    queue = new TimedValueQueue<>();
  }

  @Test
  public void bufferCapacityIncreasesAutomatically() {
    queue = new TimedValueQueue<>(1);
    for (int i = 0; i < 20; i++) {
      queue.add(i, "" + i);
      if ((i & 1) == 1) {
        assertThat(queue.pollFirst()).isEqualTo("" + (i / 2));
      }
    }
    assertThat(queue.size()).isEqualTo(10);
  }

  @Test
  public void timeDiscontinuityClearsValues() {
    queue.add(1, "b");
    queue.add(2, "c");
    queue.add(0, "a");
    assertThat(queue.size()).isEqualTo(1);
    assertThat(queue.pollFirst()).isEqualTo("a");
  }

  @Test
  public void timeDiscontinuityOnFullBufferClearsValues() {
    queue = new TimedValueQueue<>(2);
    queue.add(1, "b");
    queue.add(3, "c");
    queue.add(2, "a");
    assertThat(queue.size()).isEqualTo(1);
    assertThat(queue.pollFirst()).isEqualTo("a");
  }

  @Test
  public void pollFirstReturnsValues() {
    queue.add(0, "a");
    queue.add(1, "b");
    queue.add(2, "c");
    assertThat(queue.pollFirst()).isEqualTo("a");
    assertThat(queue.size()).isEqualTo(2);
    assertThat(queue.pollFirst()).isEqualTo("b");
    assertThat(queue.size()).isEqualTo(1);
    assertThat(queue.pollFirst()).isEqualTo("c");
    assertThat(queue.size()).isEqualTo(0);
    assertThat(queue.pollFirst()).isEqualTo(null);
    assertThat(queue.size()).isEqualTo(0);
  }

  @Test
  public void pollReturnsValues() {
    queue.add(0, "a");
    queue.add(1, "b");
    queue.add(2, "c");
    assertThat(queue.poll(0)).isEqualTo("a");
    assertThat(queue.size()).isEqualTo(2);
    assertThat(queue.poll(1)).isEqualTo("b");
    assertThat(queue.size()).isEqualTo(1);
    assertThat(queue.poll(2)).isEqualTo("c");
    assertThat(queue.size()).isEqualTo(0);
    assertThat(queue.pollFirst()).isEqualTo(null);
    assertThat(queue.size()).isEqualTo(0);
  }

  @Test
  public void pollReturnsClosestValue() {
    queue.add(0, "a");
    queue.add(3, "b");
    assertThat(queue.poll(2)).isEqualTo("b");
    assertThat(queue.size()).isEqualTo(0);
  }

  @Test
  public void pollRemovesPreviousValues() {
    queue.add(0, "a");
    queue.add(1, "b");
    queue.add(2, "c");
    assertThat(queue.poll(1)).isEqualTo("b");
    assertThat(queue.size()).isEqualTo(1);
  }

  @Test
  public void pollFloorReturnsClosestPreviousValue() {
    queue.add(0, "a");
    queue.add(3, "b");
    assertThat(queue.pollFloor(2)).isEqualTo("a");
    assertThat(queue.pollFloor(2)).isEqualTo(null);
    assertThat(queue.pollFloor(3)).isEqualTo("b");
    assertThat(queue.size()).isEqualTo(0);
  }

  @Test
  public void pollFloorRemovesPreviousValues() {
    queue.add(0, "a");
    queue.add(1, "b");
    queue.add(2, "c");
    assertThat(queue.pollFloor(1)).isEqualTo("b");
    assertThat(queue.size()).isEqualTo(1);
  }
}
