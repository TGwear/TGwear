/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls.offline;

import java.nio.charset.StandardCharsets;

/** Data for HLS downloading tests. */
/* package */ interface HlsDownloadTestData {

  String MULTIVARIANT_PLAYLIST_URI = "test.m3u8";
  int MULTIVARIANT_MEDIA_PLAYLIST_1_INDEX = 0;
  int MULTIVARIANT_MEDIA_PLAYLIST_2_INDEX = 1;
  int MULTIVARIANT_MEDIA_PLAYLIST_3_INDEX = 2;
  int MULTIVARIANT_MEDIA_PLAYLIST_0_INDEX = 3;

  String MEDIA_PLAYLIST_0_DIR = "gear0/";
  String MEDIA_PLAYLIST_0_URI = MEDIA_PLAYLIST_0_DIR + "prog_index.m3u8";
  String MEDIA_PLAYLIST_1_DIR = "gear1/";
  String MEDIA_PLAYLIST_1_URI = MEDIA_PLAYLIST_1_DIR + "prog_index.m3u8";
  String MEDIA_PLAYLIST_2_DIR = "gear2/";
  String MEDIA_PLAYLIST_2_URI = MEDIA_PLAYLIST_2_DIR + "prog_index.m3u8";
  String MEDIA_PLAYLIST_3_DIR = "gear3/";
  String MEDIA_PLAYLIST_3_URI = MEDIA_PLAYLIST_3_DIR + "prog_index.m3u8";

  byte[] MULTIVARIANT_PLAYLIST_DATA =
      ("#EXTM3U\n"
              + "#EXT-X-STREAM-INF:BANDWIDTH=232370,CODECS=\"mp4a.40.2, avc1.4d4015\"\n"
              + MEDIA_PLAYLIST_1_URI
              + "\n"
              + "#EXT-X-STREAM-INF:BANDWIDTH=649879,CODECS=\"mp4a.40.2, avc1.4d401e\"\n"
              + MEDIA_PLAYLIST_2_URI
              + "\n"
              + "#EXT-X-STREAM-INF:BANDWIDTH=991714,CODECS=\"mp4a.40.2, avc1.4d401e\"\n"
              + MEDIA_PLAYLIST_3_URI
              + "\n"
              + "#EXT-X-STREAM-INF:BANDWIDTH=41457,CODECS=\"mp4a.40.2\"\n"
              + MEDIA_PLAYLIST_0_URI)
          .getBytes(StandardCharsets.UTF_8);

  byte[] MEDIA_PLAYLIST_DATA =
      ("#EXTM3U\n"
              + "#EXT-X-TARGETDURATION:10\n"
              + "#EXT-X-VERSION:3\n"
              + "#EXT-X-MEDIA-SEQUENCE:0\n"
              + "#EXT-X-PLAYLIST-TYPE:VOD\n"
              + "#EXTINF:9.97667,\n"
              + "fileSequence0.ts\n"
              + "#EXTINF:9.97667,\n"
              + "fileSequence1.ts\n"
              + "#EXTINF:9.97667,\n"
              + "fileSequence2.ts\n"
              + "#EXT-X-ENDLIST")
          .getBytes(StandardCharsets.UTF_8);

  String ENC_MEDIA_PLAYLIST_URI = "enc_index.m3u8";

  byte[] ENC_MEDIA_PLAYLIST_DATA =
      ("#EXTM3U\n"
              + "#EXT-X-TARGETDURATION:10\n"
              + "#EXT-X-VERSION:3\n"
              + "#EXT-X-MEDIA-SEQUENCE:0\n"
              + "#EXT-X-PLAYLIST-TYPE:VOD\n"
              + "#EXT-X-KEY:METHOD=AES-128,URI=\"enc.key\"\n"
              + "#EXTINF:9.97667,\n"
              + "fileSequence0.ts\n"
              + "#EXTINF:9.97667,\n"
              + "fileSequence1.ts\n"
              + "#EXT-X-KEY:METHOD=AES-128,URI=\"enc2.key\"\n"
              + "#EXTINF:9.97667,\n"
              + "fileSequence2.ts\n"
              + "#EXT-X-ENDLIST")
          .getBytes(StandardCharsets.UTF_8);
}
