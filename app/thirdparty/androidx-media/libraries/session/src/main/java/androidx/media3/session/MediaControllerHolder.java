/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.common.util.Util.postOrRun;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.google.common.util.concurrent.AbstractFuture;

/* package */ class MediaControllerHolder<T extends MediaController> extends AbstractFuture<T>
    implements MediaController.ConnectionCallback {

  private final Handler handler;
  @Nullable private T controller;
  private boolean accepted;

  public MediaControllerHolder(Looper looper) {
    handler = new Handler(looper);
  }

  public void setController(T controller) {
    this.controller = controller;
    maybeSetFutureResult();

    addListener(
        () -> {
          if (isCancelled()) {
            controller.release();
          }
        },
        runnable -> postOrRun(handler, runnable));
  }

  @Override
  public void onAccepted() {
    accepted = true;
    maybeSetFutureResult();
  }

  @Override
  public void onRejected() {
    maybeSetException();
  }

  private void maybeSetFutureResult() {
    if (controller != null && accepted) {
      set(controller);
    }
  }

  private void maybeSetException() {
    setException(new SecurityException("Session rejected the connection request."));
  }
}
