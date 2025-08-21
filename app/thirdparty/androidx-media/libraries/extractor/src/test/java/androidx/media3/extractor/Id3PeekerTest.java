/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.extractor;

import static androidx.media3.test.utils.TestUtil.getByteArray;
import static com.google.common.truth.Truth.assertThat;

import androidx.annotation.Nullable;
import androidx.media3.common.Metadata;
import androidx.media3.extractor.metadata.id3.ApicFrame;
import androidx.media3.extractor.metadata.id3.CommentFrame;
import androidx.media3.test.utils.FakeExtractorInput;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link Id3Peeker}. */
@RunWith(AndroidJUnit4.class)
public final class Id3PeekerTest {

  @Test
  public void peekId3Data_returnNull_ifId3TagNotPresentAtBeginningOfInput() throws IOException {
    Id3Peeker id3Peeker = new Id3Peeker();
    FakeExtractorInput input =
        new FakeExtractorInput.Builder()
            .setData(new byte[] {1, 'I', 'D', '3', 2, 3, 4, 5, 6, 7, 8, 9, 10})
            .build();

    @Nullable Metadata metadata = id3Peeker.peekId3Data(input, /* id3FramePredicate= */ null);
    assertThat(metadata).isNull();
  }

  @Test
  public void peekId3Data_returnId3Tag_ifId3TagPresent() throws IOException {
    Id3Peeker id3Peeker = new Id3Peeker();
    FakeExtractorInput input =
        new FakeExtractorInput.Builder()
            .setData(
                getByteArray(ApplicationProvider.getApplicationContext(), "media/id3/apic.id3"))
            .build();

    @Nullable Metadata metadata = id3Peeker.peekId3Data(input, /* id3FramePredicate= */ null);
    assertThat(metadata).isNotNull();
    assertThat(metadata.length()).isEqualTo(1);

    ApicFrame apicFrame = (ApicFrame) metadata.get(0);
    assertThat(apicFrame.mimeType).isEqualTo("image/jpeg");
    assertThat(apicFrame.pictureType).isEqualTo(16);
    assertThat(apicFrame.description).isEqualTo("Hello World");
    assertThat(apicFrame.pictureData).hasLength(10);
    assertThat(apicFrame.pictureData).isEqualTo(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
  }

  @Test
  public void peekId3Data_returnId3TagAccordingToGivenPredicate_ifId3TagPresent()
      throws IOException {
    Id3Peeker id3Peeker = new Id3Peeker();
    FakeExtractorInput input =
        new FakeExtractorInput.Builder()
            .setData(
                getByteArray(
                    ApplicationProvider.getApplicationContext(), "media/id3/comm_apic.id3"))
            .build();

    @Nullable
    Metadata metadata =
        id3Peeker.peekId3Data(
            input,
            (majorVersion, id0, id1, id2, id3) ->
                id0 == 'C' && id1 == 'O' && id2 == 'M' && id3 == 'M');
    assertThat(metadata).isNotNull();
    assertThat(metadata.length()).isEqualTo(1);

    CommentFrame commentFrame = (CommentFrame) metadata.get(0);
    assertThat(commentFrame.language).isEqualTo("eng");
    assertThat(commentFrame.description).isEqualTo("description");
    assertThat(commentFrame.text).isEqualTo("text");
  }
}
