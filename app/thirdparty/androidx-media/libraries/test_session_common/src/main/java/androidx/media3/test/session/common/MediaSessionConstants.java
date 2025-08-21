/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.session.common;

/** Constants for calling MediaSession methods. */
public class MediaSessionConstants {

  // Test method names
  public static final String TEST_GET_SESSION_ACTIVITY = "testGetSessionActivity";
  public static final String TEST_GET_CUSTOM_LAYOUT = "testGetCustomLayout";
  public static final String TEST_GET_COMMAND_BUTTONS_FOR_MEDIA_ITEMS =
      "testGetCommandButtonsForMediaItems";
  public static final String TEST_GET_COMMAND_BUTTONS_FOR_MEDIA_ITEMS_COMMANDS_NOT_AVAILABLE =
      "testGetCommandButtonsForMediaItemsCommandsNotAvailable";
  public static final String TEST_WITH_CUSTOM_COMMANDS = "testWithCustomCommands";
  public static final String TEST_CONTROLLER_LISTENER_SESSION_REJECTS = "connection_sessionRejects";
  public static final String TEST_IS_SESSION_COMMAND_AVAILABLE = "testIsSessionCommandAvailable";
  public static final String TEST_COMMAND_GET_TRACKS = "testCommandGetTracksUnavailable";
  public static final String TEST_ON_VIDEO_SIZE_CHANGED = "onVideoSizeChanged";
  public static final String TEST_ON_TRACKS_CHANGED_VIDEO_TO_AUDIO_TRANSITION =
      "onTracksChanged_videoToAudioTransition";
  public static final String TEST_SET_SHOW_PLAY_BUTTON_IF_SUPPRESSED_TO_FALSE =
      "testSetShowPlayButtonIfSuppressedToFalse";
  public static final String TEST_MEDIA_CONTROLLER_COMPAT_CALLBACK_WITH_MEDIA_SESSION_TEST =
      "MediaControllerCompatCallbackWithMediaSessionTest";
  // Bundle keys
  public static final String KEY_AVAILABLE_SESSION_COMMANDS = "availableSessionCommands";
  public static final String KEY_CONTROLLER = "controllerKey";
  public static final String KEY_COMMAND_GET_TASKS_UNAVAILABLE = "commandGetTasksUnavailable";

  /**
   * The key used to identify the notification controller in test-only methods like {@code
   * RemoteMediaSession#setSessionExtras(String controllerKey, Bundle extras)}.
   */
  public static final String NOTIFICATION_CONTROLLER_KEY = "notificationController";

  private MediaSessionConstants() {}
}
