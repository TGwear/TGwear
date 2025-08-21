/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.midi;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.test.utils.FakeTrackOutput;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TrackChunkTest {
  /**
   * Tests that mid-note-event tempo changes are correctly accounted for in the event's duration.
   * Each duration affected by a tempo change is a segment calculated individually. The duration of
   * the sample is the sum of these segments.
   */
  @Test
  public void testMidNoteTempoChanges() throws IOException {
    FakeTrackOutput fakeTrackOutput = new FakeTrackOutput(false);
    fakeTrackOutput.format(new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_MIDI).build());
    // Chunk format:
    // Note ON at absolute ticks 0.
    // Note OFF at absolute ticks 1920.
    // End of track.
    ParsableByteArray trackData =
        new ParsableByteArray(new byte[] {0, -112, 72, 127, -113, 0, -128, 72, 127, 0, -1, 47, 0});
    TrackChunk trackChunk =
        new TrackChunk(
            /* fileFormat= */ 1,
            /* ticksPerQuarterNote= */ 480,
            /* trackEventsBytes= */ trackData,
            /* tempoListener= */ mock(TrackChunk.TempoChangedListener.class));

    trackChunk.populateFrontTrackEvent();
    trackChunk.outputFrontSample(fakeTrackOutput, /* skipNoteEvents= */ false);
    assertThat(fakeTrackOutput.getSampleTimeUs(/* index= */ 0)).isEqualTo(/* expected= */ 0);

    trackChunk.addTempoChange(/* tempoBpm= */ 180, /* ticks= */ 480);
    trackChunk.addTempoChange(/* tempoBpm= */ 240, /* ticks= */ 960);
    trackChunk.addTempoChange(/* tempoBpm= */ 300, /* ticks= */ 1440);

    trackChunk.populateFrontTrackEvent();
    trackChunk.outputFrontSample(fakeTrackOutput, /* skipNoteEvents= */ false);
    assertThat(fakeTrackOutput.getSampleTimeUs(/* index= */ 1)).isEqualTo(/* expected= */ 1283333);
  }
}
