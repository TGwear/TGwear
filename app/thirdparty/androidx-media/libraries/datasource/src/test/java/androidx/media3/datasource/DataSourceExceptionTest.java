/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.PlaybackException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link DataSourceException}. */
@RunWith(AndroidJUnit4.class)
public class DataSourceExceptionTest {

  @Test
  public void isCausedByPositionOutOfRange_reasonIsPositionOutOfRange_returnsTrue() {
    DataSourceException e =
        new DataSourceException(PlaybackException.ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE);
    assertThat(DataSourceException.isCausedByPositionOutOfRange(e)).isTrue();
  }

  @Test
  public void isCausedByPositionOutOfRange_reasonIsOther_returnsFalse() {
    DataSourceException e = new DataSourceException(PlaybackException.ERROR_CODE_IO_UNSPECIFIED);
    assertThat(DataSourceException.isCausedByPositionOutOfRange(e)).isFalse();
  }

  @Test
  public void isCausedByPositionOutOfRange_indirectCauseReasonIsPositionOutOfRange_returnsTrue() {
    DataSourceException cause =
        new DataSourceException(PlaybackException.ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE);
    IOException e = new IOException(new IOException(cause));
    assertThat(DataSourceException.isCausedByPositionOutOfRange(e)).isTrue();
  }

  @Test
  public void isCausedByPositionOutOfRange_causeReasonIsOther_returnsFalse() {
    DataSourceException cause =
        new DataSourceException(PlaybackException.ERROR_CODE_IO_UNSPECIFIED);
    IOException e = new IOException(new IOException(cause));
    assertThat(DataSourceException.isCausedByPositionOutOfRange(e)).isFalse();
  }
}
