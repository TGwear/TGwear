/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.icy;

import androidx.annotation.Nullable;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.media3.extractor.metadata.SimpleMetadataDecoder;
import com.google.common.base.Ascii;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Decodes ICY stream information. */
@UnstableApi
public final class IcyDecoder extends SimpleMetadataDecoder {

  private static final Pattern METADATA_ELEMENT = Pattern.compile("(.+?)='(.*?)';", Pattern.DOTALL);
  private static final String STREAM_KEY_NAME = "streamtitle";
  private static final String STREAM_KEY_URL = "streamurl";

  private final CharsetDecoder utf8Decoder;
  private final CharsetDecoder iso88591Decoder;

  public IcyDecoder() {
    utf8Decoder = StandardCharsets.UTF_8.newDecoder();
    iso88591Decoder = StandardCharsets.ISO_8859_1.newDecoder();
  }

  @Override
  protected Metadata decode(MetadataInputBuffer inputBuffer, ByteBuffer buffer) {
    @Nullable String icyString = decodeToString(buffer);
    byte[] icyBytes = new byte[buffer.limit()];
    buffer.get(icyBytes);

    if (icyString == null) {
      return new Metadata(new IcyInfo(icyBytes, /* title= */ null, /* url= */ null));
    }

    @Nullable String name = null;
    @Nullable String url = null;
    int index = 0;
    Matcher matcher = METADATA_ELEMENT.matcher(icyString);
    while (matcher.find(index)) {
      @Nullable String key = matcher.group(1);
      @Nullable String value = matcher.group(2);
      if (key != null) {
        switch (Ascii.toLowerCase(key)) {
          case STREAM_KEY_NAME:
            name = value;
            break;
          case STREAM_KEY_URL:
            url = value;
            break;
          default:
            break;
        }
      }
      index = matcher.end();
    }
    return new Metadata(new IcyInfo(icyBytes, name, url));
  }

  // The ICY spec doesn't specify a character encoding, and there's no way to communicate one
  // either. So try decoding UTF-8 first, then fall back to ISO-8859-1.
  // https://github.com/google/ExoPlayer/issues/6753
  @Nullable
  private String decodeToString(ByteBuffer data) {
    try {
      return utf8Decoder.decode(data).toString();
    } catch (CharacterCodingException e) {
      // Fall through to try ISO-8859-1 decoding.
    } finally {
      utf8Decoder.reset();
      data.rewind();
    }
    try {
      return iso88591Decoder.decode(data).toString();
    } catch (CharacterCodingException e) {
      return null;
    } finally {
      iso88591Decoder.reset();
      data.rewind();
    }
  }
}
