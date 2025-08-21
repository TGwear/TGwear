/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.session.common;

import android.content.Context;
import android.media.AudioManager;
import android.os.Looper;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/** TestRule for preparing main looper. */
public final class MainLooperTestRule implements TestRule {

  @Override
  public Statement apply(Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        prepare();
        base.evaluate();
      }
    };
  }

  private void prepare() {
    InstrumentationRegistry.getInstrumentation()
        .runOnMainSync(
            () -> {
              // Prepare the main looper if it hasn't.
              // Some framework APIs always run on the main looper.
              if (Looper.getMainLooper() == null) {
                Looper.prepareMainLooper();
              }

              // Initialize AudioManager on the main thread to workaround b/78617702 that
              // audio focus listener is called on the thread where the AudioManager was
              // originally initialized.
              // Without posting this, audio focus listeners wouldn't be called because the
              // listeners would be posted to the test thread (here) where it waits until the
              // tests are finished.
              Context context = ApplicationProvider.getApplicationContext();
              AudioManager unusedManager =
                  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            });
  }

  public static void runOnMainSync(Runnable runnable) throws Exception {
    InstrumentationRegistry.getInstrumentation().runOnMainSync(runnable);
  }
}
