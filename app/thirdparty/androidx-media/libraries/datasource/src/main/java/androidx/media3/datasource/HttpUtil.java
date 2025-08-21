/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static java.lang.Math.max;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import com.google.common.net.HttpHeaders;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Utility methods for HTTP. */
@UnstableApi
public final class HttpUtil {

  private static final String TAG = "HttpUtil";
  private static final Pattern CONTENT_RANGE_WITH_START_AND_END =
      Pattern.compile("bytes (\\d+)-(\\d+)/(?:\\d+|\\*)");
  private static final Pattern CONTENT_RANGE_WITH_SIZE =
      Pattern.compile("bytes (?:(?:\\d+-\\d+)|\\*)/(\\d+)");

  /** Class only contains static methods. */
  private HttpUtil() {}

  /**
   * Builds a {@link HttpHeaders#RANGE Range header} for the given position and length.
   *
   * @param position The request position.
   * @param length The request length, or {@link C#LENGTH_UNSET} if the request is unbounded.
   * @return The corresponding range header, or {@code null} if a header is unnecessary because the
   *     whole resource is being requested.
   */
  @Nullable
  public static String buildRangeRequestHeader(long position, long length) {
    if (position == 0 && length == C.LENGTH_UNSET) {
      return null;
    }
    StringBuilder rangeValue = new StringBuilder();
    rangeValue.append("bytes=");
    rangeValue.append(position);
    rangeValue.append("-");
    if (length != C.LENGTH_UNSET) {
      rangeValue.append(position + length - 1);
    }
    return rangeValue.toString();
  }

  /**
   * Attempts to parse the document size from a {@link HttpHeaders#CONTENT_RANGE Content-Range
   * header}.
   *
   * @param contentRangeHeader The {@link HttpHeaders#CONTENT_RANGE Content-Range header}, or {@code
   *     null} if not set.
   * @return The document size, or {@link C#LENGTH_UNSET} if it could not be determined.
   */
  public static long getDocumentSize(@Nullable String contentRangeHeader) {
    if (TextUtils.isEmpty(contentRangeHeader)) {
      return C.LENGTH_UNSET;
    }
    Matcher matcher = CONTENT_RANGE_WITH_SIZE.matcher(contentRangeHeader);
    return matcher.matches() ? Long.parseLong(checkNotNull(matcher.group(1))) : C.LENGTH_UNSET;
  }

  /**
   * Attempts to parse the length of a response body from the corresponding response headers.
   *
   * @param contentLengthHeader The {@link HttpHeaders#CONTENT_LENGTH Content-Length header}, or
   *     {@code null} if not set.
   * @param contentRangeHeader The {@link HttpHeaders#CONTENT_RANGE Content-Range header}, or {@code
   *     null} if not set.
   * @return The length of the response body, or {@link C#LENGTH_UNSET} if it could not be
   *     determined.
   */
  public static long getContentLength(
      @Nullable String contentLengthHeader, @Nullable String contentRangeHeader) {
    long contentLength = C.LENGTH_UNSET;
    if (!TextUtils.isEmpty(contentLengthHeader)) {
      try {
        contentLength = Long.parseLong(contentLengthHeader);
      } catch (NumberFormatException e) {
        Log.e(TAG, "Unexpected Content-Length [" + contentLengthHeader + "]");
      }
    }
    if (!TextUtils.isEmpty(contentRangeHeader)) {
      Matcher matcher = CONTENT_RANGE_WITH_START_AND_END.matcher(contentRangeHeader);
      if (matcher.matches()) {
        try {
          long contentLengthFromRange =
              Long.parseLong(checkNotNull(matcher.group(2)))
                  - Long.parseLong(checkNotNull(matcher.group(1)))
                  + 1;
          if (contentLength < 0) {
            // Some proxy servers strip the Content-Length header. Fall back to the length
            // calculated here in this case.
            contentLength = contentLengthFromRange;
          } else if (contentLength != contentLengthFromRange) {
            // If there is a discrepancy between the Content-Length and Content-Range headers,
            // assume the one with the larger value is correct. We have seen cases where carrier
            // change one of them to reduce the size of a request, but it is unlikely anybody would
            // increase it.
            Log.w(
                TAG,
                "Inconsistent headers [" + contentLengthHeader + "] [" + contentRangeHeader + "]");
            contentLength = max(contentLength, contentLengthFromRange);
          }
        } catch (NumberFormatException e) {
          Log.e(TAG, "Unexpected Content-Range [" + contentRangeHeader + "]");
        }
      }
    }
    return contentLength;
  }
}
