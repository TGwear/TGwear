/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.smoothstreaming.offline;

import static androidx.media3.common.util.Assertions.checkNotNull;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.exoplayer.offline.SegmentDownloader;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifest;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifest.StreamElement;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser;
import androidx.media3.exoplayer.upstream.ParsingLoadable.Parser;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * A downloader for SmoothStreaming streams.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * SimpleCache cache = new SimpleCache(downloadFolder, new NoOpCacheEvictor(), databaseProvider);
 * CacheDataSource.Factory cacheDataSourceFactory =
 *     new CacheDataSource.Factory()
 *         .setCache(cache)
 *         .setUpstreamDataSourceFactory(new DefaultHttpDataSource.Factory());
 * // Create a downloader for the first track of the first stream element.
 * SsDownloader ssDownloader =
 *     new SsDownloader(
 *         new MediaItem.Builder()
 *             .setUri(manifestUri)
 *             .setStreamKeys(Collections.singletonList(new StreamKey(0, 0)))
 *             .build(),
 *         cacheDataSourceFactory);
 * // Perform the download.
 * ssDownloader.download(progressListener);
 * // Use the downloaded data for playback.
 * SsMediaSource mediaSource =
 *     new SsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem);
 * }</pre>
 */
@UnstableApi
public final class SsDownloader extends SegmentDownloader<SsManifest> {

  /**
   * Creates an instance.
   *
   * @param mediaItem The {@link MediaItem} to be downloaded.
   * @param cacheDataSourceFactory A {@link CacheDataSource.Factory} for the cache into which the
   *     download will be written.
   */
  public SsDownloader(MediaItem mediaItem, CacheDataSource.Factory cacheDataSourceFactory) {
    this(mediaItem, cacheDataSourceFactory, Runnable::run);
  }

  /**
   * Creates an instance.
   *
   * @param mediaItem The {@link MediaItem} to be downloaded.
   * @param cacheDataSourceFactory A {@link CacheDataSource.Factory} for the cache into which the
   *     download will be written.
   * @param executor An {@link Executor} used to make requests for the media being downloaded.
   *     Providing an {@link Executor} that uses multiple threads will speed up the download by
   *     allowing parts of it to be executed in parallel.
   */
  public SsDownloader(
      MediaItem mediaItem, CacheDataSource.Factory cacheDataSourceFactory, Executor executor) {
    this(
        mediaItem
            .buildUpon()
            .setUri(
                Util.fixSmoothStreamingIsmManifestUri(
                    checkNotNull(mediaItem.localConfiguration).uri))
            .build(),
        new SsManifestParser(),
        cacheDataSourceFactory,
        executor,
        DEFAULT_MAX_MERGED_SEGMENT_START_TIME_DIFF_MS);
  }

  /**
   * @deprecated Use {@link SsDownloader#SsDownloader(MediaItem, Parser, CacheDataSource.Factory,
   *     Executor, long)} instead.
   */
  @Deprecated
  public SsDownloader(
      MediaItem mediaItem,
      Parser<SsManifest> manifestParser,
      CacheDataSource.Factory cacheDataSourceFactory,
      Executor executor) {
    this(
        mediaItem,
        manifestParser,
        cacheDataSourceFactory,
        executor,
        DEFAULT_MAX_MERGED_SEGMENT_START_TIME_DIFF_MS);
  }

  /**
   * Creates a new instance.
   *
   * @param mediaItem The {@link MediaItem} to be downloaded.
   * @param manifestParser A parser for SmoothStreaming manifests.
   * @param cacheDataSourceFactory A {@link CacheDataSource.Factory} for the cache into which the
   *     download will be written.
   * @param executor An {@link Executor} used to make requests for the media being downloaded.
   *     Providing an {@link Executor} that uses multiple threads will speed up the download by
   *     allowing parts of it to be executed in parallel.
   * @param maxMergedSegmentStartTimeDiffMs The maximum difference of the start time of two
   *     segments, up to which the segments (of the same URI) should be merged into a single
   *     download segment, in milliseconds.
   */
  public SsDownloader(
      MediaItem mediaItem,
      Parser<SsManifest> manifestParser,
      CacheDataSource.Factory cacheDataSourceFactory,
      Executor executor,
      long maxMergedSegmentStartTimeDiffMs) {
    super(
        mediaItem,
        manifestParser,
        cacheDataSourceFactory,
        executor,
        maxMergedSegmentStartTimeDiffMs);
  }

  @Override
  protected List<Segment> getSegments(
      DataSource dataSource, SsManifest manifest, boolean removing) {
    ArrayList<Segment> segments = new ArrayList<>();
    for (StreamElement streamElement : manifest.streamElements) {
      for (int i = 0; i < streamElement.formats.length; i++) {
        for (int j = 0; j < streamElement.chunkCount; j++) {
          segments.add(
              new Segment(
                  streamElement.getStartTimeUs(j),
                  new DataSpec(streamElement.buildRequestUri(i, j))));
        }
      }
    }
    return segments;
  }
}
