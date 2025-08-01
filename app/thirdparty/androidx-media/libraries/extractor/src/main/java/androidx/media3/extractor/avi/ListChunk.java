/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.avi;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.ParsableByteArray;
import com.google.common.collect.ImmutableList;

/** Represents an AVI LIST. */
/* package */ final class ListChunk implements AviChunk {

  public static ListChunk parseFrom(int listType, ParsableByteArray body) {
    ImmutableList.Builder<AviChunk> builder = new ImmutableList.Builder<>();
    int listBodyEndPosition = body.limit();
    @C.TrackType int currentTrackType = C.TRACK_TYPE_NONE;
    while (body.bytesLeft() > 8) {
      int type = body.readLittleEndianInt();
      int size = body.readLittleEndianInt();
      int innerBoxBodyEndPosition = body.getPosition() + size;
      body.setLimit(innerBoxBodyEndPosition);
      @Nullable AviChunk aviChunk;
      if (type == AviExtractor.FOURCC_LIST) {
        int innerListType = body.readLittleEndianInt();
        aviChunk = parseFrom(innerListType, body);
      } else {
        aviChunk = createBox(type, currentTrackType, body);
      }
      if (aviChunk != null) {
        if (aviChunk.getType() == AviExtractor.FOURCC_strh) {
          currentTrackType = ((AviStreamHeaderChunk) aviChunk).getTrackType();
        }
        builder.add(aviChunk);
      }
      body.setPosition(innerBoxBodyEndPosition);
      body.setLimit(listBodyEndPosition);
    }
    return new ListChunk(listType, builder.build());
  }

  public final ImmutableList<AviChunk> children;
  private final int type;

  private ListChunk(int type, ImmutableList<AviChunk> children) {
    this.type = type;
    this.children = children;
  }

  @Override
  public int getType() {
    return type;
  }

  @Nullable
  @SuppressWarnings("unchecked")
  public <T extends AviChunk> T getChild(Class<T> c) {
    for (AviChunk aviChunk : children) {
      if (aviChunk.getClass() == c) {
        return (T) aviChunk;
      }
    }
    return null;
  }

  @Nullable
  private static AviChunk createBox(
      int chunkType, @C.TrackType int trackType, ParsableByteArray body) {
    switch (chunkType) {
      case AviExtractor.FOURCC_avih:
        return AviMainHeaderChunk.parseFrom(body);
      case AviExtractor.FOURCC_strh:
        return AviStreamHeaderChunk.parseFrom(body);
      case AviExtractor.FOURCC_strf:
        return StreamFormatChunk.parseFrom(trackType, body);
      case AviExtractor.FOURCC_strn:
        return StreamNameChunk.parseFrom(body);
      default:
        return null;
    }
  }
}
