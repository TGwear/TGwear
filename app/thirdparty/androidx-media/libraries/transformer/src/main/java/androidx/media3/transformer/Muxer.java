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
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.muxer.MuxerException;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;

/** The muxer for producing media container files. */
@UnstableApi
public interface Muxer {
  /** Factory for muxers. */
  interface Factory {
    /**
     * Returns a new {@link Muxer}.
     *
     * @param path The path to the output file.
     * @throws MuxerException If an error occurs opening the output file for writing.
     */
    Muxer create(String path) throws MuxerException;

    /**
     * Returns the supported sample {@linkplain MimeTypes MIME types} for the given {@link
     * C.TrackType}.
     */
    ImmutableList<String> getSupportedSampleMimeTypes(@C.TrackType int trackType);
  }

  /**
   * Adds a track of the given media format.
   *
   * @param format The {@link Format} of the track.
   * @return A track id for this track, which should be passed to {@link #writeSampleData}.
   * @throws MuxerException If the muxer encounters a problem while adding the track.
   */
  int addTrack(Format format) throws MuxerException;

  /**
   * Writes encoded sample data.
   *
   * @param trackId The track id, previously returned by {@link #addTrack(Format)}.
   * @param byteBuffer A buffer containing the sample data to write to the container.
   * @param bufferInfo The {@link BufferInfo} of the sample.
   * @throws MuxerException If the muxer fails to write the sample.
   */
  void writeSampleData(int trackId, ByteBuffer byteBuffer, BufferInfo bufferInfo)
      throws MuxerException;

  /** Adds {@linkplain Metadata.Entry metadata} about the output file. */
  void addMetadataEntry(Metadata.Entry metadataEntry);

  /**
   * Closes the file.
   *
   * <p>The muxer cannot be used anymore once this method returns.
   *
   * @throws MuxerException If the muxer fails to finish writing the output.
   */
  void close() throws MuxerException;
}
