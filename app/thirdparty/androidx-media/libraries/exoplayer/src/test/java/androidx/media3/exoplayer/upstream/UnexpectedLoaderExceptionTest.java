/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.exoplayer.upstream.Loader.UnexpectedLoaderException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link UnexpectedLoaderException}. */
@RunWith(JUnit4.class)
public class UnexpectedLoaderExceptionTest {

  @Test
  public void causeWithMessage_messageAppended() {
    UnexpectedLoaderException unexpectedLoaderException =
        new UnexpectedLoaderException(new IllegalStateException("test message"));

    assertThat(unexpectedLoaderException)
        .hasMessageThat()
        .isEqualTo("Unexpected IllegalStateException: test message");
  }

  @Test
  public void causeWithoutMessage_noMessageAppended() {
    UnexpectedLoaderException unexpectedLoaderException =
        new UnexpectedLoaderException(new IllegalStateException());

    assertThat(unexpectedLoaderException)
        .hasMessageThat()
        .isEqualTo("Unexpected IllegalStateException");
  }
}
