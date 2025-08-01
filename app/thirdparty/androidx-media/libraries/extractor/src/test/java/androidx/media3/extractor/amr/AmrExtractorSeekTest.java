/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.amr;

import static com.google.common.truth.Truth.assertThat;

import android.net.Uri;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.extractor.SeekMap;
import androidx.media3.test.utils.FakeExtractorOutput;
import androidx.media3.test.utils.FakeTrackOutput;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link AmrExtractor} seeking behaviour. */
@RunWith(AndroidJUnit4.class)
public final class AmrExtractorSeekTest {

  private static final Random random = new Random(1234L);

  private static final String NARROW_BAND_AMR_FILE = "media/amr/sample_nb.amr";
  private static final int NARROW_BAND_FILE_DURATION_US = 4_360_000;

  private static final String WIDE_BAND_AMR_FILE = "media/amr/sample_wb.amr";
  private static final int WIDE_BAND_FILE_DURATION_US = 3_380_000;

  private FakeTrackOutput expectedTrackOutput;
  private DefaultDataSource dataSource;

  @Before
  public void setUp() {
    dataSource =
        new DefaultDataSource.Factory(ApplicationProvider.getApplicationContext())
            .createDataSource();
  }

  @Test
  public void amrExtractorReads_returnSeekableSeekMap_forNarrowBandAmr() throws IOException {
    String fileName = NARROW_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);

    AmrExtractor extractor = createAmrExtractor();
    SeekMap seekMap =
        TestUtil.extractSeekMap(extractor, new FakeExtractorOutput(), dataSource, fileUri);

    assertThat(seekMap).isNotNull();
    assertThat(seekMap.getDurationUs()).isEqualTo(NARROW_BAND_FILE_DURATION_US);
    assertThat(seekMap.isSeekable()).isTrue();
  }

  @Test
  public void seeking_handlesSeekingToPositionInFile_extractsCorrectFrame_forNarrowBandAmr()
      throws IOException {
    String fileName = NARROW_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);

    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long targetSeekTimeUs = 980_000;
    int extractedFrameIndex =
        TestUtil.seekToTimeUs(
            extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

    assertThat(extractedFrameIndex).isNotEqualTo(-1);
    assertFirstFrameAfterSeekContainTargetSeekTime(
        trackOutput, targetSeekTimeUs, extractedFrameIndex);
  }

  @Test
  public void seeking_handlesSeekToEoF_extractsLastFrame_forNarrowBandAmr() throws IOException {
    String fileName = NARROW_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);
    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long targetSeekTimeUs = seekMap.getDurationUs();

    int extractedFrameIndex =
        TestUtil.seekToTimeUs(
            extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

    assertThat(extractedFrameIndex).isNotEqualTo(-1);
    assertFirstFrameAfterSeekContainTargetSeekTime(
        trackOutput, targetSeekTimeUs, extractedFrameIndex);
  }

  @Test
  public void seeking_handlesSeekingBackward_extractsCorrectFrames_forNarrowBandAmr()
      throws IOException {
    String fileName = NARROW_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);
    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long firstSeekTimeUs = 980_000;
    TestUtil.seekToTimeUs(extractor, seekMap, firstSeekTimeUs, dataSource, trackOutput, fileUri);

    long targetSeekTimeUs = 0;
    int extractedFrameIndex =
        TestUtil.seekToTimeUs(
            extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

    assertThat(extractedFrameIndex).isNotEqualTo(-1);
    assertFirstFrameAfterSeekContainTargetSeekTime(
        trackOutput, targetSeekTimeUs, extractedFrameIndex);
  }

  @Test
  public void seeking_handlesSeekingForward_extractsCorrectFrames_forNarrowBandAmr()
      throws IOException {
    String fileName = NARROW_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);
    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long firstSeekTimeUs = 980_000;
    TestUtil.seekToTimeUs(extractor, seekMap, firstSeekTimeUs, dataSource, trackOutput, fileUri);

    long targetSeekTimeUs = 1_200_000;
    int extractedFrameIndex =
        TestUtil.seekToTimeUs(
            extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

    assertThat(extractedFrameIndex).isNotEqualTo(-1);
    assertFirstFrameAfterSeekContainTargetSeekTime(
        trackOutput, targetSeekTimeUs, extractedFrameIndex);
  }

  @Test
  public void seeking_handlesRandomSeeks_extractsCorrectFrames_forNarrowBandAmr()
      throws IOException {
    String fileName = NARROW_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);
    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long numSeek = 100;
    for (long i = 0; i < numSeek; i++) {
      long targetSeekTimeUs = random.nextInt(NARROW_BAND_FILE_DURATION_US + 1);
      int extractedFrameIndex =
          TestUtil.seekToTimeUs(
              extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

      assertThat(extractedFrameIndex).isNotEqualTo(-1);
      assertFirstFrameAfterSeekContainTargetSeekTime(
          trackOutput, targetSeekTimeUs, extractedFrameIndex);
    }
  }

  @Test
  public void amrExtractorReads_returnSeekableSeekMap_forWideBandAmr() throws IOException {
    String fileName = WIDE_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);

    AmrExtractor extractor = createAmrExtractor();
    SeekMap seekMap =
        TestUtil.extractSeekMap(extractor, new FakeExtractorOutput(), dataSource, fileUri);

    assertThat(seekMap).isNotNull();
    assertThat(seekMap.getDurationUs()).isEqualTo(WIDE_BAND_FILE_DURATION_US);
    assertThat(seekMap.isSeekable()).isTrue();
  }

  @Test
  public void seeking_handlesSeekingToPositionInFile_extractsCorrectFrame_forWideBandAmr()
      throws IOException {
    String fileName = WIDE_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);

    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long targetSeekTimeUs = 980_000;
    int extractedFrameIndex =
        TestUtil.seekToTimeUs(
            extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

    assertThat(extractedFrameIndex).isNotEqualTo(-1);
    assertFirstFrameAfterSeekContainTargetSeekTime(
        trackOutput, targetSeekTimeUs, extractedFrameIndex);
  }

  @Test
  public void seeking_handlesSeekToEoF_extractsLastFrame_forWideBandAmr() throws IOException {
    String fileName = WIDE_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);
    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long targetSeekTimeUs = seekMap.getDurationUs();

    int extractedFrameIndex =
        TestUtil.seekToTimeUs(
            extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

    assertThat(extractedFrameIndex).isNotEqualTo(-1);
    assertFirstFrameAfterSeekContainTargetSeekTime(
        trackOutput, targetSeekTimeUs, extractedFrameIndex);
  }

  @Test
  public void seeking_handlesSeekingBackward_extractsCorrectFrames_forWideBandAmr()
      throws IOException {
    String fileName = WIDE_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);
    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long firstSeekTimeUs = 980_000;
    TestUtil.seekToTimeUs(extractor, seekMap, firstSeekTimeUs, dataSource, trackOutput, fileUri);

    long targetSeekTimeUs = 0;
    int extractedFrameIndex =
        TestUtil.seekToTimeUs(
            extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

    assertThat(extractedFrameIndex).isNotEqualTo(-1);
    assertFirstFrameAfterSeekContainTargetSeekTime(
        trackOutput, targetSeekTimeUs, extractedFrameIndex);
  }

  @Test
  public void seeking_handlesSeekingForward_extractsCorrectFrames_forWideBandAmr()
      throws IOException, InterruptedException {
    String fileName = WIDE_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);
    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long firstSeekTimeUs = 980_000;
    TestUtil.seekToTimeUs(extractor, seekMap, firstSeekTimeUs, dataSource, trackOutput, fileUri);

    long targetSeekTimeUs = 1_200_000;
    int extractedFrameIndex =
        TestUtil.seekToTimeUs(
            extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

    assertThat(extractedFrameIndex).isNotEqualTo(-1);
    assertFirstFrameAfterSeekContainTargetSeekTime(
        trackOutput, targetSeekTimeUs, extractedFrameIndex);
  }

  @Test
  public void seeking_handlesRandomSeeks_extractsCorrectFrames_forWideBandAmr()
      throws IOException, InterruptedException {
    String fileName = WIDE_BAND_AMR_FILE;
    Uri fileUri = TestUtil.buildAssetUri(fileName);
    expectedTrackOutput =
        TestUtil.extractAllSamplesFromFile(
                createAmrExtractor(), ApplicationProvider.getApplicationContext(), fileName)
            .trackOutputs
            .get(0);
    AmrExtractor extractor = createAmrExtractor();

    FakeExtractorOutput extractorOutput = new FakeExtractorOutput();
    SeekMap seekMap = TestUtil.extractSeekMap(extractor, extractorOutput, dataSource, fileUri);
    FakeTrackOutput trackOutput = extractorOutput.trackOutputs.get(0);

    long numSeek = 100;
    for (long i = 0; i < numSeek; i++) {
      long targetSeekTimeUs = random.nextInt(NARROW_BAND_FILE_DURATION_US + 1);
      int extractedFrameIndex =
          TestUtil.seekToTimeUs(
              extractor, seekMap, targetSeekTimeUs, dataSource, trackOutput, fileUri);

      assertThat(extractedFrameIndex).isNotEqualTo(-1);
      assertFirstFrameAfterSeekContainTargetSeekTime(
          trackOutput, targetSeekTimeUs, extractedFrameIndex);
    }
  }

  // Internal methods

  private AmrExtractor createAmrExtractor() {
    return new AmrExtractor(AmrExtractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING);
  }

  private void assertFirstFrameAfterSeekContainTargetSeekTime(
      FakeTrackOutput trackOutput, long seekTimeUs, int firstFrameIndexAfterSeek) {
    int expectedSampleIndex = findTargetFrameInExpectedOutput(seekTimeUs);
    // Assert that after seeking, the first sample frame written to output contains the sample
    // at seek time.
    trackOutput.assertSample(
        firstFrameIndexAfterSeek,
        expectedTrackOutput.getSampleData(expectedSampleIndex),
        expectedTrackOutput.getSampleTimeUs(expectedSampleIndex),
        expectedTrackOutput.getSampleFlags(expectedSampleIndex),
        expectedTrackOutput.getSampleCryptoData(expectedSampleIndex));
  }

  private int findTargetFrameInExpectedOutput(long seekTimeUs) {
    List<Long> sampleTimes = expectedTrackOutput.getSampleTimesUs();
    for (int i = 0; i < sampleTimes.size() - 1; i++) {
      long currentSampleTime = sampleTimes.get(i);
      long nextSampleTime = sampleTimes.get(i + 1);
      if (currentSampleTime <= seekTimeUs && nextSampleTime > seekTimeUs) {
        return i;
      }
    }
    return sampleTimes.size() - 1;
  }
}
