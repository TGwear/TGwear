/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.vorbis;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test for {@link VorbisComment}. */
@RunWith(AndroidJUnit4.class)
public final class VorbisCommentTest {
  @Test
  public void populateMediaMetadata_setsMediaMetadataValues() {
    String title = "the title";
    String artist = "artist";
    String albumTitle = "album title";
    String albumArtist = "album Artist";
    int trackNumber = 3;
    int totalTracks = 12;
    int discNumber = 1;
    int totalDiscs = 3;
    String genre = "Metal";
    String description = "a description about the audio.";
    List<Metadata.Entry> entries =
        ImmutableList.of(
            new VorbisComment("TITLE", title),
            new VorbisComment("ArTisT", artist),
            new VorbisComment("ALBUM", albumTitle),
            new VorbisComment("albumartist", albumArtist),
            new VorbisComment("TRACKNUMBER", String.valueOf(trackNumber)),
            new VorbisComment("TOTALTRACKS", String.valueOf(totalTracks)),
            new VorbisComment("DISCNUMBER", String.valueOf(discNumber)),
            new VorbisComment("TOTALDISCS", String.valueOf(totalDiscs)),
            new VorbisComment("GENRE", genre),
            new VorbisComment("DESCRIPTION", description));
    MediaMetadata.Builder builder = MediaMetadata.EMPTY.buildUpon();

    for (Metadata.Entry entry : entries) {
      entry.populateMediaMetadata(builder);
    }
    MediaMetadata mediaMetadata = builder.build();

    assertThat(mediaMetadata.title.toString()).isEqualTo(title);
    assertThat(mediaMetadata.artist.toString()).isEqualTo(artist);
    assertThat(mediaMetadata.albumTitle.toString()).isEqualTo(albumTitle);
    assertThat(mediaMetadata.albumArtist.toString()).isEqualTo(albumArtist);
    assertThat(mediaMetadata.trackNumber).isEqualTo(trackNumber);
    assertThat(mediaMetadata.totalTrackCount).isEqualTo(totalTracks);
    assertThat(mediaMetadata.discNumber).isEqualTo(discNumber);
    assertThat(mediaMetadata.totalDiscCount).isEqualTo(totalDiscs);
    assertThat(mediaMetadata.genre.toString()).isEqualTo(genre);
    assertThat(mediaMetadata.description.toString()).isEqualTo(description);
  }
}
