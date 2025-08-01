/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.offline;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.upstream.ParsingLoadable.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A manifest parser that includes only the streams identified by the given stream keys.
 *
 * @param <T> The {@link FilterableManifest} type.
 */
@UnstableApi
public final class FilteringManifestParser<T extends FilterableManifest<T>> implements Parser<T> {

  private final Parser<? extends T> parser;
  @Nullable private final List<StreamKey> streamKeys;

  /**
   * @param parser A parser for the manifest that will be filtered.
   * @param streamKeys The stream keys. If null or empty then filtering will not occur.
   */
  public FilteringManifestParser(Parser<? extends T> parser, @Nullable List<StreamKey> streamKeys) {
    this.parser = parser;
    this.streamKeys = streamKeys;
  }

  @Override
  public T parse(Uri uri, InputStream inputStream) throws IOException {
    T manifest = parser.parse(uri, inputStream);
    return streamKeys == null || streamKeys.isEmpty() ? manifest : manifest.copy(streamKeys);
  }
}
