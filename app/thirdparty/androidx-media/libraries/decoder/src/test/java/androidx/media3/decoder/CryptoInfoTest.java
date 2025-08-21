/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.decoder;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link CryptoInfo} */
@RunWith(AndroidJUnit4.class)
public class CryptoInfoTest {

  private CryptoInfo cryptoInfo;

  @Before
  public void setUp() {
    cryptoInfo = new CryptoInfo();
  }

  @Test
  public void increaseClearDataFirstSubSampleBy_numBytesOfClearDataIsNullAndZeroInput_isNoOp() {
    cryptoInfo.increaseClearDataFirstSubSampleBy(0);

    assertThat(cryptoInfo.numBytesOfClearData).isNull();
    assertThat(cryptoInfo.getFrameworkCryptoInfo().numBytesOfClearData).isNull();
  }

  @Test
  public void increaseClearDataFirstSubSampleBy_withNumBytesOfClearDataSetAndZeroInput_isNoOp() {
    int[] data = new int[] {1, 1, 1, 1};
    cryptoInfo.numBytesOfClearData = data;
    cryptoInfo.getFrameworkCryptoInfo().numBytesOfClearData = data;

    cryptoInfo.increaseClearDataFirstSubSampleBy(5);

    assertThat(cryptoInfo.numBytesOfClearData[0]).isEqualTo(6);
    assertThat(cryptoInfo.getFrameworkCryptoInfo().numBytesOfClearData[0]).isEqualTo(6);
  }

  @Test
  public void increaseClearDataFirstSubSampleBy_withSharedClearDataPointer_setsValue() {
    int[] data = new int[] {1, 1, 1, 1};
    cryptoInfo.numBytesOfClearData = data;
    cryptoInfo.getFrameworkCryptoInfo().numBytesOfClearData = data;

    cryptoInfo.increaseClearDataFirstSubSampleBy(5);

    assertThat(cryptoInfo.numBytesOfClearData[0]).isEqualTo(6);
    assertThat(cryptoInfo.getFrameworkCryptoInfo().numBytesOfClearData[0]).isEqualTo(6);
  }
}
