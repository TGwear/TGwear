/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;
import java.util.ArrayList;
import org.junit.Test;
import org.webrtc.Loggable;
import org.webrtc.Logging.Severity;
import org.webrtc.PeerConnectionFactory;

public class LoggableTest {
  private static String TAG = "LoggableTest";
  private static String NATIVE_FILENAME_TAG = "loggable_test.cc";

  private static class MockLoggable implements Loggable {
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<Severity> sevs = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();

    @Override
    public void onLogMessage(String message, Severity sev, String tag) {
      messages.add(message);
      sevs.add(sev);
      tags.add(tag);
    }

    public boolean isMessageReceived(String message) {
      for (int i = 0; i < messages.size(); i++) {
        if (messages.get(i).contains(message)) {
          return true;
        }
      }
      return false;
    }

    public boolean isMessageReceived(String message, Severity sev, String tag) {
      for (int i = 0; i < messages.size(); i++) {
        if (messages.get(i).contains(message) && sevs.get(i) == sev && tags.get(i).equals(tag)) {
          return true;
        }
      }
      return false;
    }
  }

  private final MockLoggable mockLoggable = new MockLoggable();

  @Test
  @SmallTest
  public void testLoggableSetWithoutError() throws InterruptedException {
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setInjectableLogger(mockLoggable, Severity.LS_INFO)
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
  }

  @Test
  @SmallTest
  public void testMessageIsLoggedCorrectly() throws InterruptedException {
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setInjectableLogger(mockLoggable, Severity.LS_INFO)
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
    String msg = "Message that should be logged";
    Logging.d(TAG, msg);
    assertTrue(mockLoggable.isMessageReceived(msg, Severity.LS_INFO, TAG));
  }

  @Test
  @SmallTest
  public void testLowSeverityIsFiltered() throws InterruptedException {
    // Set severity to LS_WARNING to filter out LS_INFO and below.
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setInjectableLogger(mockLoggable, Severity.LS_WARNING)
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
    String msg = "Message that should NOT be logged";
    Logging.d(TAG, msg);
    assertFalse(mockLoggable.isMessageReceived(msg));
  }

  @Test
  @SmallTest
  public void testLoggableDoesNotReceiveMessagesAfterUnsetting() {
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setInjectableLogger(mockLoggable, Severity.LS_INFO)
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
    // Reinitialize without Loggable
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
    String msg = "Message that should NOT be logged";
    Logging.d(TAG, msg);
    assertFalse(mockLoggable.isMessageReceived(msg));
  }

  @Test
  @SmallTest
  public void testNativeMessageIsLoggedCorrectly() throws InterruptedException {
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setInjectableLogger(mockLoggable, Severity.LS_INFO)
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
    String msg = "Message that should be logged";
    nativeLogInfoTestMessage(msg);
    assertTrue(mockLoggable.isMessageReceived(msg, Severity.LS_INFO, NATIVE_FILENAME_TAG));
  }

  @Test
  @SmallTest
  public void testNativeLowSeverityIsFiltered() throws InterruptedException {
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setInjectableLogger(mockLoggable, Severity.LS_WARNING)
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
    String msg = "Message that should NOT be logged";
    nativeLogInfoTestMessage(msg);
    assertFalse(mockLoggable.isMessageReceived(msg));
  }

  @Test
  @SmallTest
  public void testNativeLoggableDoesNotReceiveMessagesAfterUnsetting() {
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setInjectableLogger(mockLoggable, Severity.LS_INFO)
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
    // Reinitialize without Loggable
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
    String msg = "Message that should NOT be logged";
    nativeLogInfoTestMessage(msg);
    assertFalse(mockLoggable.isMessageReceived(msg));
  }

  private static native void nativeLogInfoTestMessage(String message);
}
