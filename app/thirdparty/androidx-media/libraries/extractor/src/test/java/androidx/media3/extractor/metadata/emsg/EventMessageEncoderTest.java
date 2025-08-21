/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.emsg;

import static androidx.media3.test.utils.TestUtil.createByteArray;
import static androidx.media3.test.utils.TestUtil.createMetadataInputBuffer;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.primitives.Bytes;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link EventMessageEncoder}. */
@RunWith(AndroidJUnit4.class)
public final class EventMessageEncoderTest {

  private static final EventMessage DECODED_MESSAGE =
      new EventMessage("urn:test", "123", 3000, 1000403, new byte[] {0, 1, 2, 3, 4});

  private static final byte[] ENCODED_MESSAGE =
      Bytes.concat(
          createByteArray(117, 114, 110, 58, 116, 101, 115, 116, 0), // scheme_id_uri = "urn:test"
          createByteArray(49, 50, 51, 0), // value = "123"
          createByteArray(0, 0, 0, 0, 0, 0, 11, 184), // event_duration_ms = 3000
          createByteArray(0, 0, 0, 0, 0, 15, 67, 211), // id = 1000403
          createByteArray(0, 1, 2, 3, 4)); // message_data = {0, 1, 2, 3, 4}

  @Test
  public void encodeEventStream() throws IOException {
    byte[] encodedByteArray = new EventMessageEncoder().encode(DECODED_MESSAGE);
    assertThat(encodedByteArray).isEqualTo(ENCODED_MESSAGE);
  }

  @Test
  public void encodeDecodeEventStream() throws IOException {
    byte[] encodedByteArray = new EventMessageEncoder().encode(DECODED_MESSAGE);
    MetadataInputBuffer buffer = createMetadataInputBuffer(encodedByteArray);

    EventMessageDecoder decoder = new EventMessageDecoder();
    Metadata metadata = decoder.decode(buffer);
    assertThat(metadata.length()).isEqualTo(1);
    assertThat(metadata.get(0)).isEqualTo(DECODED_MESSAGE);
  }

  @Test
  public void encodeEventStreamMultipleTimesWorkingCorrectly() throws IOException {
    EventMessage eventMessage1 =
        new EventMessage("urn:test", "123", 3000, 1000402, new byte[] {4, 3, 2, 1, 0});
    byte[] expectedEmsgBody1 =
        Bytes.concat(
            createByteArray(117, 114, 110, 58, 116, 101, 115, 116, 0), // scheme_id_uri = "urn:test"
            createByteArray(49, 50, 51, 0), // value = "123"
            createByteArray(0, 0, 0, 0, 0, 0, 11, 184), // event_duration_ms = 3000
            createByteArray(0, 0, 0, 0, 0, 15, 67, 210), // id = 1000402
            createByteArray(4, 3, 2, 1, 0)); // message_data = {4, 3, 2, 1, 0}

    EventMessageEncoder eventMessageEncoder = new EventMessageEncoder();
    byte[] encodedByteArray = eventMessageEncoder.encode(DECODED_MESSAGE);
    assertThat(encodedByteArray).isEqualTo(ENCODED_MESSAGE);
    byte[] encodedByteArray1 = eventMessageEncoder.encode(eventMessage1);
    assertThat(encodedByteArray1).isEqualTo(expectedEmsgBody1);
  }

  // https://github.com/google/ExoPlayer/issues/9123
  @Test
  public void encodeDecodeEventMessage_durationNotSet() {
    EventMessage originalMessage =
        new EventMessage("urn:test", "456", C.TIME_UNSET, 99, new byte[] {7, 8, 9});
    byte[] encodedMessage = new EventMessageEncoder().encode(originalMessage);
    EventMessage decodedMessage =
        new EventMessageDecoder().decode(new ParsableByteArray(encodedMessage));

    assertThat(decodedMessage).isEqualTo(originalMessage);
  }
}
