/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/** Unit tests for {@link LeastRecentlyUsedCacheEvictor}. */
@RunWith(AndroidJUnit4.class)
public class LeastRecentlyUsedCacheEvictorTest {

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void contentBiggerThanMaxSizeDoesNotThrowException() throws Exception {
    int maxBytes = 100;
    LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(maxBytes);
    evictor.onCacheInitialized();
    evictor.onStartFile(Mockito.mock(Cache.class), "key", 0, maxBytes + 1);
  }
}
