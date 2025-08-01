/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import android.media.MediaCodec.BufferInfo;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.muxer.MuxerException;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;

/** A default {@link Muxer} implementation. */
@UnstableApi
public final class DefaultMuxer implements Muxer {

  /** A {@link Muxer.Factory} for {@link DefaultMuxer}. */
  public static final class Factory implements Muxer.Factory {
    private final FrameworkMuxer.Factory muxerFactory;

    /** Creates an instance. */
    public Factory() {
      this.muxerFactory = new FrameworkMuxer.Factory();
    }

    /**
     * @deprecated Use {@link #setVideoDurationUs(long)} instead. Note that a conversion from
     *     milliseconds to microseconds is required to migrate to {@link #setVideoDurationUs(long)}.
     */
    @Deprecated
    public Factory(long videoDurationMs) {
      this.muxerFactory =
          new FrameworkMuxer.Factory().setVideoDurationUs(Util.msToUs(videoDurationMs));
    }

    /**
     * Sets the duration of the video track (in microseconds) to enforce in the output.
     *
     * <p>The default is {@link C#TIME_UNSET}.
     *
     * @param videoDurationUs The duration of the video track (in microseconds) to enforce in the
     *     output, or {@link C#TIME_UNSET} to not enforce. Only applicable when a video track is
     *     {@linkplain #addTrack(Format) added}.
     * @return This factory.
     */
    @CanIgnoreReturnValue
    public Factory setVideoDurationUs(long videoDurationUs) {
      muxerFactory.setVideoDurationUs(videoDurationUs);
      return this;
    }

    @Override
    public Muxer create(String path) throws MuxerException {
      return new DefaultMuxer(muxerFactory.create(path));
    }

    @Override
    public ImmutableList<String> getSupportedSampleMimeTypes(@C.TrackType int trackType) {
      return muxerFactory.getSupportedSampleMimeTypes(trackType);
    }
  }

  public static final String MUXER_NAME = FrameworkMuxer.MUXER_NAME;
  private final Muxer muxer;

  private DefaultMuxer(Muxer muxer) {
    this.muxer = muxer;
  }

  @Override
  public int addTrack(Format format) throws MuxerException {
    return muxer.addTrack(format);
  }

  @Override
  public void writeSampleData(int trackId, ByteBuffer byteBuffer, BufferInfo bufferInfo)
      throws MuxerException {
    muxer.writeSampleData(trackId, byteBuffer, bufferInfo);
  }

  @Override
  public void addMetadataEntry(Metadata.Entry metadataEntry) {
    muxer.addMetadataEntry(metadataEntry);
  }

  @Override
  public void close() throws MuxerException {
    muxer.close();
  }
}
