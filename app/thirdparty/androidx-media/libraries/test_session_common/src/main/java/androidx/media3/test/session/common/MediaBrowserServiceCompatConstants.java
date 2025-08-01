/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.session.common;

/** Constants for calling MediaBrowserServiceCompat methods. */
public class MediaBrowserServiceCompatConstants {

  public static final String TEST_CONNECT_REJECTED = "testConnect_rejected";
  public static final String TEST_ON_CHILDREN_CHANGED_SUBSCRIBE_AND_UNSUBSCRIBE =
      "testOnChildrenChanged_subscribeAndUnsubscribe";
  public static final String TEST_GET_LIBRARY_ROOT = "getLibraryRoot_correctExtraKeyAndValue";
  public static final String TEST_GET_CHILDREN = "getChildren_correctMetadataExtras";
  public static final String TEST_GET_CHILDREN_WITH_NULL_LIST =
      "onChildrenChanged_withNullChildrenListInLegacyService_convertedToSessionError";
  public static final String TEST_GET_CHILDREN_INCREASE_NUMBER_OF_CHILDREN_WITH_EACH_CALL =
      "onChildrenChanged_cacheChildrenOfSubscribeCall_serviceCalledOnceOnly";
  public static final String TEST_GET_CHILDREN_FATAL_AUTHENTICATION_ERROR =
      "getLibraryRoot_fatalAuthenticationError_receivesPlaybackException";
  public static final String TEST_GET_CHILDREN_NON_FATAL_AUTHENTICATION_ERROR =
      "getLibraryRoot_nonFatalAuthenticationError_receivesPlaybackException";
  public static final String TEST_SEND_CUSTOM_COMMAND = "sendCustomCommand";
  public static final String TEST_MEDIA_ITEMS_WITH_BROWSE_ACTIONS =
      "getLibraryRoot_withBrowseActions";
  public static final String TEST_SUBSCRIBE_THEN_REJECT_ON_LOAD_CHILDREN =
      "subscribe_thenRejectOnLoadChildren";

  private MediaBrowserServiceCompatConstants() {}
}
