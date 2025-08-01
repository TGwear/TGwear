/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.session.common;

import android.os.HandlerThread;
import org.junit.rules.ExternalResource;

/** TestRule for providing a handler and an executor for {@link HandlerThread}. */
public final class HandlerThreadTestRule extends ExternalResource {

  private final String threadName;
  private TestHandler handler;

  public HandlerThreadTestRule(String threadName) {
    this.threadName = threadName;
  }

  @Override
  protected void before() {
    HandlerThread handlerThread = new HandlerThread(threadName);
    handlerThread.start();

    handler = new TestHandler(handlerThread.getLooper());
  }

  @Override
  protected void after() {
    try {
      handler.getLooper().quitSafely();
    } finally {
      handler = null;
    }
  }

  /** Gets the handler for the thread. */
  public TestHandler getHandler() {
    if (handler == null) {
      throw new IllegalStateException("It should be called between before() and after()");
    }
    return handler;
  }
}
