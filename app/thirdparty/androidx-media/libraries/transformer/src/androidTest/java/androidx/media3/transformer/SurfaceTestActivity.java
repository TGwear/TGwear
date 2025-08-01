/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;
import androidx.media3.common.util.Util;
import androidx.media3.transformer.test.R;

/** An activity with surfaces for testing purposes. */
public final class SurfaceTestActivity extends Activity {

  private SurfaceView surfaceView;
  private TextureView textureView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setKeepScreenOn(this);
    setContentView(R.layout.surface_test_activity);
    surfaceView = findViewById(R.id.surface_view);
    textureView = findViewById(R.id.texture_view);
  }

  /** Gets this activity's {@link SurfaceView}. */
  public SurfaceView getSurfaceView() {
    return surfaceView;
  }

  /** Gets this activity's {@link TextureView}. */
  public TextureView getTextureView() {
    return textureView;
  }

  private static void setKeepScreenOn(Activity activity) {
    if (Util.SDK_INT >= 27) {
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      activity.setTurnScreenOn(true);
      activity.setShowWhenLocked(true);
      KeyguardManager keyguardManager =
          (KeyguardManager) activity.getSystemService(KEYGUARD_SERVICE);
      keyguardManager.requestDismissKeyguard(activity, /* callback= */ null);
    } else {
      activity
          .getWindow()
          .addFlags(
              WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                  | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                  | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                  | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
  }
}
