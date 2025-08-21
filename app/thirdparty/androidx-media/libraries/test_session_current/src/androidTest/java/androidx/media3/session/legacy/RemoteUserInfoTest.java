/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session.legacy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test of {@link MediaSessionManager.RemoteUserInfo} methods. */
@RunWith(AndroidJUnit4.class)
public class RemoteUserInfoTest {
  @Test
  public void testConstructor() {
    String testPackageName = "com.media.test";
    int testPid = 1000;
    int testUid = 2000;
    MediaSessionManager.RemoteUserInfo remoteUserInfo =
        new MediaSessionManager.RemoteUserInfo(testPackageName, testPid, testUid);
    assertEquals(testPackageName, remoteUserInfo.getPackageName());
    assertEquals(testPid, remoteUserInfo.getPid());
    assertEquals(testUid, remoteUserInfo.getUid());
  }

  @Test
  public void testConstructor_withNullPackageName_throwsNPE() {
    try {
      MediaSessionManager.RemoteUserInfo remoteUserInfo =
          new MediaSessionManager.RemoteUserInfo(null, 1000, 2000);
      fail("null package name shouldn't be allowed");
    } catch (NullPointerException e) {
      // expected
    } catch (Exception e) {
      fail("unexpected exception " + e);
    }
  }

  @Test
  public void testConstructor_withEmptyPackageName_throwsIAE() {
    try {
      MediaSessionManager.RemoteUserInfo remoteUserInfo =
          new MediaSessionManager.RemoteUserInfo("", 1000, 2000);
      fail("empty package name shouldn't be allowed");
    } catch (IllegalArgumentException e) {
      // expected
    } catch (Exception e) {
      fail("unexpected exception " + e);
    }
  }
}
