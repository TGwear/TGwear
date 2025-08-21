/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp.reader;

import static androidx.media3.common.util.Util.getBytesFromHexString;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.exoplayer.rtsp.RtpPayloadFormat;
import androidx.media3.test.utils.FakeExtractorOutput;
import androidx.media3.test.utils.FakeTrackOutput;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Bytes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link RtpAmrReader}. */
@RunWith(AndroidJUnit4.class)
public final class RtpAmrReaderTest {

  private FakeExtractorOutput extractorOutput;

  @Before
  public void setUp() {
    extractorOutput = new FakeExtractorOutput();
  }

  @Test
  public void consume_validAmrNbPackets() {
    RtpAmrReader amrReader = createAmrReader(MimeTypes.AUDIO_AMR);

    amrReader.createTracks(extractorOutput, /* trackId= */ 0);
    amrReader.onReceivingFirstPacket(/* timestamp= */ 2599168056L, /* sequenceNumber= */ 40289);
    amrReader.consume(
        new ParsableByteArray(
            Bytes.concat(
                getBytesFromHexString("00"), // AMR-NB Codec Mode = 0.
                getBytesFromHexString("40"), // AMR-NB ToC, F=0, FT=8.
                getBytesFromHexString("0102030405"))),
        /* timestamp= */ 2599168056L,
        /* sequenceNumber= */ 40289,
        /* rtpMarker= */ false);
    amrReader.consume(
        new ParsableByteArray(
            Bytes.concat(
                getBytesFromHexString("60"), // AMR-NB Codec Mode = 6.
                getBytesFromHexString("C0"), // AMR-NB ToC, F=1, FT=8.
                getBytesFromHexString("060708090A"))),
        /* timestamp= */ 2599169592L,
        /* sequenceNumber= */ 40290,
        /* rtpMarker= */ false);

    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);
    assertThat(trackOutput.getSampleCount()).isEqualTo(2);
    assertThat(trackOutput.getSampleData(0)).isEqualTo(getBytesFromHexString("400102030405"));
    assertThat(trackOutput.getSampleTimeUs(0)).isEqualTo(0);
    assertThat(trackOutput.getSampleData(1)).isEqualTo(getBytesFromHexString("C0060708090A"));
    assertThat(trackOutput.getSampleTimeUs(1)).isEqualTo(192000);
  }

  @Test
  public void consume_amrNbPacketWithInvalidFrameType_throwsIllegalArgumentException() {
    RtpAmrReader amrReader = createAmrReader(MimeTypes.AUDIO_AMR);

    amrReader.createTracks(extractorOutput, /* trackId= */ 0);
    amrReader.onReceivingFirstPacket(/* timestamp= */ 2599168056L, /* sequenceNumber= */ 40289);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            amrReader.consume(
                new ParsableByteArray(
                    Bytes.concat(
                        getBytesFromHexString("00"), // AMR-NB Codec Mode = 0.
                        getBytesFromHexString("68"), // AMR-NB ToC, F=0, FT=13.
                        getBytesFromHexString("0102030405"))),
                /* timestamp= */ 2599168056L,
                /* sequenceNumber= */ 40289,
                /* rtpMarker= */ false));
  }

  @Test
  public void consume_amrNbPacketWithInvalidFrameSize_throwsIllegalArgumentException() {
    // The payload frame type is 8, expecting five bytes of data, getting four.
    RtpAmrReader amrReader = createAmrReader(MimeTypes.AUDIO_AMR);

    amrReader.createTracks(extractorOutput, /* trackId= */ 0);
    amrReader.onReceivingFirstPacket(/* timestamp= */ 2599168056L, /* sequenceNumber= */ 40289);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            amrReader.consume(
                new ParsableByteArray(
                    Bytes.concat(
                        getBytesFromHexString("00"), // AMR-NB Codec Mode = 0.
                        getBytesFromHexString("40"), // AMR-NB ToC, F=0, FT=8.
                        getBytesFromHexString("01020304"))),
                /* timestamp= */ 2599168056L,
                /* sequenceNumber= */ 40289,
                /* rtpMarker= */ false));
  }

  @Test
  public void consume_validAmrWbPackets() {
    RtpAmrReader amrReader = createAmrReader(MimeTypes.AUDIO_AMR_WB);

    amrReader.createTracks(extractorOutput, /* trackId= */ 0);
    amrReader.onReceivingFirstPacket(/* timestamp= */ 2599168056L, /* sequenceNumber= */ 40289);
    amrReader.consume(
        new ParsableByteArray(
            Bytes.concat(
                getBytesFromHexString("60"), // AMR-WB Codec Mode = 6.
                getBytesFromHexString("87"), // AMR-WB ToC, F=0, FT=0.
                getBytesFromHexString("0102030405060708090A0B0C0D0E0F1011"))),
        /* timestamp= */ 2599168056L,
        /* sequenceNumber= */ 40289,
        /* rtpMarker= */ false);
    amrReader.consume(
        new ParsableByteArray(
            Bytes.concat(
                getBytesFromHexString("00"), // AMR-WB Codec Mode = 0.
                getBytesFromHexString("7E"))), // AMR-WB ToC, F=0, FT=15.
        /* timestamp= */ 2599169592L,
        /* sequenceNumber= */ 40290,
        /* rtpMarker= */ false);

    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);
    assertThat(trackOutput.getSampleCount()).isEqualTo(2);
    assertThat(trackOutput.getSampleData(0))
        .isEqualTo(getBytesFromHexString("870102030405060708090A0B0C0D0E0F1011"));
    assertThat(trackOutput.getSampleTimeUs(0)).isEqualTo(0);
    assertThat(trackOutput.getSampleData(1)).isEqualTo(getBytesFromHexString("7E"));
    assertThat(trackOutput.getSampleTimeUs(1)).isEqualTo(96000);
  }

  @Test
  public void consume_amrWbPacketWithInvalidFrameType_throwsIllegalArgumentException() {
    RtpAmrReader amrReader = createAmrReader(MimeTypes.AUDIO_AMR_WB);

    amrReader.createTracks(extractorOutput, /* trackId= */ 0);
    amrReader.onReceivingFirstPacket(/* timestamp= */ 2599168056L, /* sequenceNumber= */ 40289);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            amrReader.consume(
                new ParsableByteArray(
                    Bytes.concat(
                        getBytesFromHexString("00"), // AMR-WB Codec Mode = 0.
                        getBytesFromHexString("D5"), // AMR-WB ToC, F=1, FT=10.
                        getBytesFromHexString("01020304"))),
                /* timestamp= */ 2599168056L,
                /* sequenceNumber= */ 40289,
                /* rtpMarker= */ false));
  }

  @Test
  public void consume_amrWbPacketWithInvalidFrameSize_throwsIllegalArgumentException() {
    // The payload frame type is 4, expecting 40 bytes of data, getting two.
    RtpAmrReader amrReader = createAmrReader(MimeTypes.AUDIO_AMR_WB);

    amrReader.createTracks(extractorOutput, /* trackId= */ 0);
    amrReader.onReceivingFirstPacket(/* timestamp= */ 2599168056L, /* sequenceNumber= */ 40289);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            amrReader.consume(
                new ParsableByteArray(
                    Bytes.concat(
                        getBytesFromHexString("00"), // AMR-WB Codec Mode = 0.
                        getBytesFromHexString("A5"), // AMR-WB ToC, F=1, FT=4.
                        getBytesFromHexString("0102"))),
                /* timestamp= */ 2599168056L,
                /* sequenceNumber= */ 40289,
                /* rtpMarker= */ false));
  }

  private static RtpAmrReader createAmrReader(String mimeType) {
    switch (mimeType) {
      case MimeTypes.AUDIO_AMR:
        return new RtpAmrReader(createRtpPayloadFormat(mimeType, /* sampleRate= */ 8000));
      case MimeTypes.AUDIO_AMR_WB:
        return new RtpAmrReader(createRtpPayloadFormat(mimeType, /* sampleRate= */ 16000));
      default:
        throw new IllegalArgumentException("MimeType " + mimeType + " not supported.");
    }
  }

  private static RtpPayloadFormat createRtpPayloadFormat(String mimeType, int sampleRate) {
    return new RtpPayloadFormat(
        new Format.Builder()
            .setChannelCount(1)
            .setSampleMimeType(mimeType)
            .setSampleRate(sampleRate)
            .build(),
        /* rtpPayloadType= */ 97,
        /* clockRate= */ sampleRate,
        /* fmtpParameters= */ ImmutableMap.of(),
        MimeTypes.AUDIO_AMR.equals(mimeType)
            ? RtpPayloadFormat.RTP_MEDIA_AMR
            : RtpPayloadFormat.RTP_MEDIA_AMR_WB);
  }
}
