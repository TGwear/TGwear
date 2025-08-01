/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;
import static androidx.media3.common.util.Assertions.checkStateNotNull;

import android.content.Context;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.SuitableOutputChecker;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

/** Fake implementation for {@link SuitableOutputChecker}. */
@RestrictTo(LIBRARY_GROUP)
@UnstableApi
public final class FakeSuitableOutputChecker implements SuitableOutputChecker {

  /** Builder for {@link FakeSuitableOutputChecker} instance. */
  public static final class Builder {

    private boolean isSuitableOutputAvailable;

    /**
     * Sets the initial value to be returned from {@link
     * SuitableOutputChecker#isSelectedOutputSuitableForPlayback()}. The default value is false.
     */
    @CanIgnoreReturnValue
    public Builder setIsSuitableExternalOutputAvailable(boolean isSuitableOutputAvailable) {
      this.isSuitableOutputAvailable = isSuitableOutputAvailable;
      return this;
    }

    /**
     * Builds a {@link FakeSuitableOutputChecker} with the builder's current values.
     *
     * @return The built {@link FakeSuitableOutputChecker}.
     */
    public FakeSuitableOutputChecker build() {
      return new FakeSuitableOutputChecker(isSuitableOutputAvailable);
    }
  }

  private boolean isSelectedOutputSuitableForPlayback;
  private boolean previousSelectedOutputSuitableForPlayback;
  @Nullable private Callback callback;

  public FakeSuitableOutputChecker(boolean isSelectedOutputSuitableForPlayback) {
    this.isSelectedOutputSuitableForPlayback = isSelectedOutputSuitableForPlayback;
    this.previousSelectedOutputSuitableForPlayback = isSelectedOutputSuitableForPlayback;
  }

  @Override
  public void enable(
      Callback callback,
      Context context,
      Looper callbackLooper,
      Looper backgroundLooper,
      Clock clock) {
    this.callback = callback;
  }

  @Override
  public void disable() {
    this.callback = null;
  }

  @Override
  public boolean isSelectedOutputSuitableForPlayback() {
    checkStateNotNull(callback, "SuitableOutputChecker is not enabled");
    return isSelectedOutputSuitableForPlayback;
  }

  /**
   * Updates the value to be returned by {@link
   * SuitableOutputChecker#isSelectedOutputSuitableForPlayback()} and send callbacks to registered
   * callers via {@link Callback#onSelectedOutputSuitabilityChanged(boolean)}.
   */
  public void updateIsSelectedSuitableOutputAvailableAndNotify(
      boolean isSelectedOutputSuitableForPlayback) {
    this.isSelectedOutputSuitableForPlayback = isSelectedOutputSuitableForPlayback;
    if (callback != null
        && previousSelectedOutputSuitableForPlayback != isSelectedOutputSuitableForPlayback) {
      callback.onSelectedOutputSuitabilityChanged(isSelectedOutputSuitableForPlayback);
      previousSelectedOutputSuitableForPlayback = isSelectedOutputSuitableForPlayback;
    }
  }
}
