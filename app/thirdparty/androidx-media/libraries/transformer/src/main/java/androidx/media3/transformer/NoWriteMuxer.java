/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import android.media.MediaCodec;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;

/** A {@link Muxer} implementation that does nothing. */
/* package */ final class NoWriteMuxer implements Muxer {
  public static final class Factory implements Muxer.Factory {

    private final ImmutableList<String> audioMimeTypes;
    private final ImmutableList<String> videoMimeTypes;

    /**
     * Creates an instance.
     *
     * @param audioMimeTypes The audio {@linkplain MimeTypes mime types} to return in {@link
     *     #getSupportedSampleMimeTypes(int)}.
     * @param videoMimeTypes The video {@linkplain MimeTypes mime types} to return in {@link
     *     #getSupportedSampleMimeTypes(int)}.
     */
    public Factory(ImmutableList<String> audioMimeTypes, ImmutableList<String> videoMimeTypes) {
      this.audioMimeTypes = audioMimeTypes;
      this.videoMimeTypes = videoMimeTypes;
    }

    @Override
    public Muxer create(String path) {
      return new NoWriteMuxer();
    }

    @Override
    public ImmutableList<String> getSupportedSampleMimeTypes(@C.TrackType int trackType) {
      if (trackType == C.TRACK_TYPE_AUDIO) {
        return audioMimeTypes;
      }
      if (trackType == C.TRACK_TYPE_VIDEO) {
        return videoMimeTypes;
      }
      return ImmutableList.of();
    }
  }

  @Override
  public int addTrack(Format format) {
    return 0;
  }

  @Override
  public void writeSampleData(int trackId, ByteBuffer data, MediaCodec.BufferInfo bufferInfo) {}

  @Override
  public void addMetadataEntry(Metadata.Entry metadataEntry) {}

  @Override
  public void close() {}
}
