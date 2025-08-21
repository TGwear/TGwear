/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.SniffFailure;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.InlineMe;
import java.util.List;

/** Thrown if the input format was not recognized. */
@UnstableApi
public class UnrecognizedInputFormatException extends ParserException {

  /** The {@link Uri} from which the unrecognized data was read. */
  public final Uri uri;

  /**
   * Sniff failures from {@link Extractor#getSniffFailureDetails()} from any extractors that were
   * checked while trying to recognize the input data.
   *
   * <p>May be empty if no extractors provided additional sniffing failure details.
   */
  public final ImmutableList<SniffFailure> sniffFailures;

  /**
   * @deprecated Use {@link #UnrecognizedInputFormatException(String, Uri, List)} instead.
   */
  @InlineMe(
      replacement = "this(message, uri, ImmutableList.of())",
      imports = "com.google.common.collect.ImmutableList")
  @Deprecated
  public UnrecognizedInputFormatException(String message, Uri uri) {
    this(message, uri, ImmutableList.of());
  }

  /**
   * Constructs a new instance.
   *
   * @param message The detail message for the exception.
   * @param uri The {@link Uri} from which the unrecognized data was read.
   * @param sniffFailures Sniff failures from any extractors that were used to sniff the data while
   *     trying to recognize it.
   */
  public UnrecognizedInputFormatException(
      String message, Uri uri, List<? extends SniffFailure> sniffFailures) {
    super(message, /* cause= */ null, /* contentIsMalformed= */ false, C.DATA_TYPE_MEDIA);
    this.uri = uri;
    this.sniffFailures = ImmutableList.copyOf(sniffFailures);
  }
}
