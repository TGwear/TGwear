/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.cast;

import android.content.Context;
import androidx.media3.common.util.UnstableApi;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;
import java.util.Collections;
import java.util.List;

/** A convenience {@link OptionsProvider} to target the default cast receiver app. */
@UnstableApi
public final class DefaultCastOptionsProvider implements OptionsProvider {

  /**
   * App id that points to the Default Media Receiver app with basic DRM support.
   *
   * <p>Applications that require more complex DRM authentication should <a
   * href="https://developers.google.com/cast/docs/web_receiver/streaming_protocols#drm">create a
   * custom receiver application</a>.
   */
  public static final String APP_ID_DEFAULT_RECEIVER_WITH_DRM = "A12D4273";

  @Override
  public CastOptions getCastOptions(Context context) {
    return new CastOptions.Builder()
        .setResumeSavedSession(false)
        .setEnableReconnectionService(false)
        .setReceiverApplicationId(APP_ID_DEFAULT_RECEIVER_WITH_DRM)
        .setStopReceiverApplicationWhenEndingSession(true)
        .build();
  }

  @Override
  public List<SessionProvider> getAdditionalSessionProviders(Context context) {
    return Collections.emptyList();
  }
}
