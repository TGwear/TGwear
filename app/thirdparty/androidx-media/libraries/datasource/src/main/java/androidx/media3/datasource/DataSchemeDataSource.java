/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static androidx.media3.common.util.Util.castNonNull;
import static java.lang.Math.min;

import android.net.Uri;
import android.util.Base64;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.ParserException;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/** A {@link DataSource} for reading data URLs, as defined by RFC 2397. */
@UnstableApi
public final class DataSchemeDataSource extends BaseDataSource {

  public static final String SCHEME_DATA = "data";

  @Nullable private DataSpec dataSpec;
  @Nullable private byte[] data;
  private int readPosition;
  private int bytesRemaining;

  public DataSchemeDataSource() {
    super(/* isNetwork= */ false);
  }

  @Override
  public long open(DataSpec dataSpec) throws IOException {
    transferInitializing(dataSpec);
    this.dataSpec = dataSpec;
    Uri uri = dataSpec.uri.normalizeScheme();
    String scheme = uri.getScheme();
    Assertions.checkArgument(SCHEME_DATA.equals(scheme), "Unsupported scheme: " + scheme);
    String[] uriParts = Util.split(uri.getSchemeSpecificPart(), ",");
    if (uriParts.length != 2) {
      throw ParserException.createForMalformedDataOfUnknownType(
          "Unexpected URI format: " + uri, /* cause= */ null);
    }
    String dataString = uriParts[1];
    if (uriParts[0].contains(";base64")) {
      try {
        data = Base64.decode(dataString, /* flags= */ Base64.DEFAULT);
      } catch (IllegalArgumentException e) {
        throw ParserException.createForMalformedDataOfUnknownType(
            "Error while parsing Base64 encoded string: " + dataString, e);
      }
    } else {
      // TODO: Add support for other charsets.
      data = Util.getUtf8Bytes(URLDecoder.decode(dataString, StandardCharsets.US_ASCII.name()));
    }
    if (dataSpec.position > data.length) {
      data = null;
      throw new DataSourceException(PlaybackException.ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE);
    }
    readPosition = (int) dataSpec.position;
    bytesRemaining = data.length - readPosition;
    if (dataSpec.length != C.LENGTH_UNSET) {
      bytesRemaining = (int) min(bytesRemaining, dataSpec.length);
    }
    transferStarted(dataSpec);
    return dataSpec.length != C.LENGTH_UNSET ? dataSpec.length : bytesRemaining;
  }

  @Override
  public int read(byte[] buffer, int offset, int length) {
    if (length == 0) {
      return 0;
    }
    if (bytesRemaining == 0) {
      return C.RESULT_END_OF_INPUT;
    }
    length = min(length, bytesRemaining);
    System.arraycopy(castNonNull(data), readPosition, buffer, offset, length);
    readPosition += length;
    bytesRemaining -= length;
    bytesTransferred(length);
    return length;
  }

  @Override
  @Nullable
  public Uri getUri() {
    return dataSpec != null ? dataSpec.uri : null;
  }

  @Override
  public void close() {
    if (data != null) {
      data = null;
      transferEnded();
    }
    dataSpec = null;
  }
}
