/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.session.SessionError.ERROR_BAD_VALUE;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import android.os.Bundle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link SessionError}. */
@RunWith(AndroidJUnit4.class)
public class SessionErrorTest {

  @Test
  public void constructor_twoArguments_usesEmptyBundle() {
    SessionError error = new SessionError(ERROR_BAD_VALUE, "error message");

    assertThat(error.extras.size()).isEqualTo(0);
  }

  @Test
  @SuppressWarnings("WrongConstant") // Deliberately testing an unrecognized error code.
  public void constructor_withNonErrorCode_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new SessionError(SessionResult.RESULT_SUCCESS, "error message"));
  }

  @Test
  public void equals_differentBundles_bundleIgnored() {
    Bundle errorBundle1 = new Bundle();
    errorBundle1.putString("key", "value");
    SessionError error1 = new SessionError(ERROR_BAD_VALUE, "error message", errorBundle1);
    SessionError error2 = new SessionError(ERROR_BAD_VALUE, "error message");

    assertThat(error1).isEqualTo(error2);
  }

  @Test
  public void toBundle_roundTrip_resultsInEqualObjectWithSameBundle() {
    Bundle errorBundle = new Bundle();
    errorBundle.putString("key", "value");
    SessionError error = new SessionError(ERROR_BAD_VALUE, "error message", errorBundle);

    SessionError sessionErrorFromBundle = SessionError.fromBundle(error.toBundle());

    assertThat(sessionErrorFromBundle).isEqualTo(error);
    assertThat(sessionErrorFromBundle.extras.size()).isEqualTo(1);
    assertThat(sessionErrorFromBundle.extras.getString("key")).isEqualTo("value");
  }
}
