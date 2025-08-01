/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.metadata;

import androidx.annotation.Nullable;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.metadata.MetadataDecoder;
import androidx.media3.extractor.metadata.dvbsi.AppInfoTableDecoder;
import androidx.media3.extractor.metadata.emsg.EventMessageDecoder;
import androidx.media3.extractor.metadata.icy.IcyDecoder;
import androidx.media3.extractor.metadata.id3.Id3Decoder;
import androidx.media3.extractor.metadata.scte35.SpliceInfoDecoder;

/** A factory for {@link MetadataDecoder} instances. */
@UnstableApi
public interface MetadataDecoderFactory {

  /**
   * Returns whether the factory is able to instantiate a {@link MetadataDecoder} for the given
   * {@link Format}.
   *
   * @param format The {@link Format}.
   * @return Whether the factory can instantiate a suitable {@link MetadataDecoder}.
   */
  boolean supportsFormat(Format format);

  /**
   * Creates a {@link MetadataDecoder} for the given {@link Format}.
   *
   * @param format The {@link Format}.
   * @return A new {@link MetadataDecoder}.
   * @throws IllegalArgumentException If the {@link Format} is not supported.
   */
  MetadataDecoder createDecoder(Format format);

  /**
   * Default {@link MetadataDecoder} implementation.
   *
   * <p>The formats supported by this factory are:
   *
   * <ul>
   *   <li>ID3 ({@link Id3Decoder})
   *   <li>EMSG ({@link EventMessageDecoder})
   *   <li>SCTE-35 ({@link SpliceInfoDecoder})
   *   <li>ICY ({@link IcyDecoder})
   * </ul>
   */
  MetadataDecoderFactory DEFAULT =
      new MetadataDecoderFactory() {

        @Override
        public boolean supportsFormat(Format format) {
          @Nullable String mimeType = format.sampleMimeType;
          return MimeTypes.APPLICATION_ID3.equals(mimeType)
              || MimeTypes.APPLICATION_EMSG.equals(mimeType)
              || MimeTypes.APPLICATION_SCTE35.equals(mimeType)
              || MimeTypes.APPLICATION_ICY.equals(mimeType)
              || MimeTypes.APPLICATION_AIT.equals(mimeType);
        }

        @Override
        public MetadataDecoder createDecoder(Format format) {
          @Nullable String mimeType = format.sampleMimeType;
          if (mimeType != null) {
            switch (mimeType) {
              case MimeTypes.APPLICATION_ID3:
                return new Id3Decoder();
              case MimeTypes.APPLICATION_EMSG:
                return new EventMessageDecoder();
              case MimeTypes.APPLICATION_SCTE35:
                return new SpliceInfoDecoder();
              case MimeTypes.APPLICATION_ICY:
                return new IcyDecoder();
              case MimeTypes.APPLICATION_AIT:
                return new AppInfoTableDecoder();
              default:
                break;
            }
          }
          throw new IllegalArgumentException(
              "Attempted to create decoder for unsupported MIME type: " + mimeType);
        }
      };
}
