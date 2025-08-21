/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import static com.google.common.truth.Truth.assertThat;

import android.os.Bundle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link BundleCollectionUtil}. */
@RunWith(AndroidJUnit4.class)
public class BundleCollectionUtilTest {

  @Test
  public void testStringMapToBundle() {
    Map<String, String> originalMap = new HashMap<>();
    originalMap.put("firstKey", "firstValue");
    originalMap.put("secondKey", "repeatedValue");
    originalMap.put("thirdKey", "repeatedValue");
    originalMap.put("", "valueOfEmptyKey");

    Bundle mapAsBundle = BundleCollectionUtil.stringMapToBundle(originalMap);
    Map<String, String> restoredMap = BundleCollectionUtil.bundleToStringHashMap(mapAsBundle);

    assertThat(restoredMap).isEqualTo(originalMap);
  }

  @Test
  public void testGetBundleWithDefault() {
    Bundle fullInnerBundle = new Bundle();
    fullInnerBundle.putString("0", "123");
    fullInnerBundle.putBoolean("1", false);
    fullInnerBundle.putInt("2", 123);
    Bundle defaultBundle = new Bundle();
    defaultBundle.putString("0", "I am default");
    Bundle outerBundle = new Bundle();
    outerBundle.putBundle("0", fullInnerBundle);
    outerBundle.putBundle("1", Bundle.EMPTY);
    outerBundle.putBundle("2", null);

    Bundle restoredInnerBundle =
        BundleCollectionUtil.getBundleWithDefault(outerBundle, "0", defaultBundle);
    Bundle restoredEmptyBundle =
        BundleCollectionUtil.getBundleWithDefault(outerBundle, "1", defaultBundle);
    Bundle restoredNullBundle =
        BundleCollectionUtil.getBundleWithDefault(outerBundle, "2", defaultBundle);

    assertThat(restoredInnerBundle).isEqualTo(fullInnerBundle);
    assertThat(restoredEmptyBundle).isEqualTo(Bundle.EMPTY);
    assertThat(restoredNullBundle).isEqualTo(defaultBundle);
  }

  @Test
  public void testGetIntegerArrayListWithDefault() {
    ArrayList<Integer> normalArray = new ArrayList<>();
    normalArray.add(4);
    normalArray.add(8);
    normalArray.add(16);
    ArrayList<Integer> emptyArray = new ArrayList<>();
    ArrayList<Integer> defaultIntegerArray = new ArrayList<>();
    defaultIntegerArray.add(0);
    Bundle bundle = new Bundle();
    bundle.putIntegerArrayList("0", normalArray);
    bundle.putIntegerArrayList("1", emptyArray);
    bundle.putIntegerArrayList("2", null);

    ArrayList<Integer> restoredIntegerArray =
        BundleCollectionUtil.getIntegerArrayListWithDefault(bundle, "0", defaultIntegerArray);
    ArrayList<Integer> restoredEmptyIntegerArray =
        BundleCollectionUtil.getIntegerArrayListWithDefault(bundle, "1", defaultIntegerArray);
    ArrayList<Integer> restoredNullIntegerArray =
        BundleCollectionUtil.getIntegerArrayListWithDefault(bundle, "2", defaultIntegerArray);

    assertThat(restoredIntegerArray).isEqualTo(normalArray);
    assertThat(restoredEmptyIntegerArray).isEqualTo(emptyArray);
    assertThat(restoredNullIntegerArray).isEqualTo(defaultIntegerArray);
  }
}
