/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.audio;

import static org.mockito.Mockito.verify;

import androidx.media3.common.C;
import androidx.media3.common.audio.AudioProcessor.AudioFormat;
import androidx.media3.exoplayer.audio.TeeAudioProcessor.AudioBufferSink;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/** Unit tests for {@link TeeAudioProcessorTest}. */
@RunWith(AndroidJUnit4.class)
public final class TeeAudioProcessorTest {

  private static final AudioFormat AUDIO_FORMAT =
      new AudioFormat(/* sampleRate= */ 44100, /* channelCount= */ 2, C.ENCODING_PCM_16BIT);

  @Rule public final MockitoRule mockito = MockitoJUnit.rule();

  private TeeAudioProcessor teeAudioProcessor;

  @Mock private AudioBufferSink mockAudioBufferSink;

  @Before
  public void setUp() {
    teeAudioProcessor = new TeeAudioProcessor(mockAudioBufferSink);
  }

  @Test
  public void initialFlush_flushesSink() throws Exception {
    teeAudioProcessor.configure(AUDIO_FORMAT);
    teeAudioProcessor.flush();

    verify(mockAudioBufferSink)
        .flush(AUDIO_FORMAT.sampleRate, AUDIO_FORMAT.channelCount, AUDIO_FORMAT.encoding);
  }
}
