/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils.robolectric;

import static androidx.media3.test.utils.robolectric.RobolectricUtil.createRobolectricConditionVariable;
import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.fail;

import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.offline.Download;
import androidx.media3.exoplayer.offline.DownloadManager;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Allows tests to block for, and assert properties of, calls from a {@link DownloadManager} to its
 * {@link DownloadManager.Listener}.
 */
@UnstableApi
public final class TestDownloadManagerListener implements DownloadManager.Listener {

  private static final int TIMEOUT_MS = 10_000;
  private static final int STATE_REMOVED = -1;

  private final DownloadManager downloadManager;
  private final HashMap<String, LinkedBlockingQueue<Integer>> downloadStates;
  private final ConditionVariable initializedCondition;
  private final ConditionVariable idleCondition;

  private @Download.FailureReason int failureReason;

  public TestDownloadManagerListener(DownloadManager downloadManager) {
    this.downloadManager = downloadManager;
    downloadStates = new HashMap<>();
    initializedCondition = createRobolectricConditionVariable();
    idleCondition = createRobolectricConditionVariable();
    downloadManager.addListener(this);
  }

  /** Blocks until the manager is initialized. */
  public void blockUntilInitialized() throws InterruptedException {
    assertThat(initializedCondition.block(TIMEOUT_MS)).isTrue();
  }

  /** Blocks until the manager is idle. */
  public void blockUntilIdle() throws InterruptedException {
    idleCondition.close();
    // If the manager is already idle the condition will be opened by the code immediately below.
    // Else it will be opened by onIdle().
    ConditionVariable checkedOnMainThread = createRobolectricConditionVariable();
    new Handler(downloadManager.getApplicationLooper())
        .post(
            () -> {
              if (downloadManager.isIdle()) {
                idleCondition.open();
              }
              checkedOnMainThread.open();
            });
    assertThat(checkedOnMainThread.block(TIMEOUT_MS)).isTrue();
    assertThat(idleCondition.block(TIMEOUT_MS)).isTrue();
  }

  /** Blocks until the manager is idle and throws if any of the downloads failed. */
  public void blockUntilIdleAndThrowAnyFailure() throws Exception {
    blockUntilIdle();
    if (failureReason != Download.FAILURE_REASON_NONE) {
      throw new Exception("Failure reason: " + failureReason);
    }
  }

  /** Asserts that the specified download transitions to the specified state. */
  public void assertState(String id, @Download.State int state) {
    assertStateInternal(id, state);
  }

  /** Asserts that the specified download is removed. */
  public void assertRemoved(String id) {
    assertStateInternal(id, STATE_REMOVED);
  }

  // DownloadManager.Listener implementation.

  @Override
  public void onInitialized(DownloadManager downloadManager) {
    initializedCondition.open();
  }

  @Override
  public void onDownloadChanged(
      DownloadManager downloadManager, Download download, @Nullable Exception finalException) {
    if (download.state == Download.STATE_FAILED) {
      failureReason = download.failureReason;
    }
    getStateQueue(download.request.id).add(download.state);
  }

  @Override
  public void onDownloadRemoved(DownloadManager downloadManager, Download download) {
    getStateQueue(download.request.id).add(STATE_REMOVED);
  }

  @Override
  public void onIdle(DownloadManager downloadManager) {
    idleCondition.open();
  }

  // Internal logic.

  private void assertStateInternal(String id, int expectedState) {
    while (true) {
      @Nullable Integer state = null;
      try {
        state = getStateQueue(id).poll(TIMEOUT_MS, MILLISECONDS);
      } catch (InterruptedException e) {
        fail("Interrupted: " + e.getMessage());
      }
      if (state != null) {
        if (expectedState == state) {
          return;
        }
      } else {
        fail("Didn't receive expected state: " + expectedState);
      }
    }
  }

  private LinkedBlockingQueue<Integer> getStateQueue(String id) {
    synchronized (downloadStates) {
      @Nullable LinkedBlockingQueue<Integer> stateQueue = downloadStates.get(id);
      if (stateQueue == null) {
        stateQueue = new LinkedBlockingQueue<>();
        downloadStates.put(id, stateQueue);
      }
      return stateQueue;
    }
  }
}
