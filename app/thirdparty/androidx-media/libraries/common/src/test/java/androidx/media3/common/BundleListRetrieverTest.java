/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static com.google.common.truth.Truth.assertThat;

import android.os.Bundle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.truth.os.BundleSubject;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link BundleListRetriever}. */
@RunWith(AndroidJUnit4.class)
public class BundleListRetrieverTest {

  @Test
  public void getList_inProcess_returnsOriginalImmutableList() {
    int count = 100_000;
    ImmutableList.Builder<Bundle> listBuilder = ImmutableList.builder();
    for (int i = 0; i < count; i++) {
      Bundle bundle = new Bundle();
      bundle.putInt("i", i);
      listBuilder.add(bundle);
    }
    ImmutableList<Bundle> listBefore = listBuilder.build();

    ImmutableList<Bundle> listAfter =
        BundleListRetriever.getList(new BundleListRetriever(listBefore));

    assertThat(listAfter).isSameInstanceAs(listBefore);
  }

  @Test
  public void getList_fromRemoteBinder_preservedLargeList() {
    int count = 100_000;
    ImmutableList.Builder<Bundle> listBuilder = ImmutableList.builder();
    for (int i = 0; i < count; i++) {
      Bundle bundle = new Bundle();
      bundle.putInt("i", i);
      listBuilder.add(bundle);
    }
    ImmutableList<Bundle> listBefore = listBuilder.build();

    ImmutableList<Bundle> listAfter =
        BundleListRetriever.getListFromRemoteBinder(new BundleListRetriever(listBefore));

    for (int i = 0; i < count; i++) {
      Bundle bundle = listAfter.get(i);
      BundleSubject.assertThat(bundle).integer("i").isEqualTo(i);
    }
  }
}
