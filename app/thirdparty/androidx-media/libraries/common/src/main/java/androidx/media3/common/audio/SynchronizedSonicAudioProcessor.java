/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.audio;

import java.nio.ByteBuffer;

/**
 * A thread safe version {@link SonicAudioProcessor} that synchronizes calls before forwarding them
 * to {@link SonicAudioProcessor}.
 */
/* package */ class SynchronizedSonicAudioProcessor implements AudioProcessor {

  private final Object lock;
  private final SonicAudioProcessor sonicAudioProcessor;

  public SynchronizedSonicAudioProcessor(Object lock, boolean keepActiveWithDefaultParameters) {
    this.lock = lock;
    sonicAudioProcessor = new SonicAudioProcessor(keepActiveWithDefaultParameters);
  }

  public final void setSpeed(float speed) {
    synchronized (lock) {
      sonicAudioProcessor.setSpeed(speed);
    }
  }

  public final void setPitch(float pitch) {
    synchronized (lock) {
      sonicAudioProcessor.setPitch(pitch);
    }
  }

  public final void setOutputSampleRateHz(int sampleRateHz) {
    synchronized (lock) {
      sonicAudioProcessor.setOutputSampleRateHz(sampleRateHz);
    }
  }

  public final long getMediaDuration(long playoutDuration) {
    synchronized (lock) {
      return sonicAudioProcessor.getMediaDuration(playoutDuration);
    }
  }

  public final long getPlayoutDuration(long mediaDuration) {
    synchronized (lock) {
      return sonicAudioProcessor.getPlayoutDuration(mediaDuration);
    }
  }

  public final long getProcessedInputBytes() {
    synchronized (lock) {
      return sonicAudioProcessor.getProcessedInputBytes();
    }
  }

  @Override
  public long getDurationAfterProcessorApplied(long durationUs) {
    return getPlayoutDuration(durationUs);
  }

  @Override
  public final AudioFormat configure(AudioFormat inputAudioFormat)
      throws UnhandledAudioFormatException {
    synchronized (lock) {
      return sonicAudioProcessor.configure(inputAudioFormat);
    }
  }

  @Override
  public final boolean isActive() {
    synchronized (lock) {
      return sonicAudioProcessor.isActive();
    }
  }

  @Override
  public final void queueInput(ByteBuffer inputBuffer) {
    synchronized (lock) {
      sonicAudioProcessor.queueInput(inputBuffer);
    }
  }

  @Override
  public final void queueEndOfStream() {
    synchronized (lock) {
      sonicAudioProcessor.queueEndOfStream();
    }
  }

  @Override
  public final ByteBuffer getOutput() {
    synchronized (lock) {
      return sonicAudioProcessor.getOutput();
    }
  }

  @Override
  public final boolean isEnded() {
    synchronized (lock) {
      return sonicAudioProcessor.isEnded();
    }
  }

  @Override
  public final void flush() {
    synchronized (lock) {
      sonicAudioProcessor.flush();
    }
  }

  @Override
  public final void reset() {
    synchronized (lock) {
      sonicAudioProcessor.reset();
    }
  }
}
