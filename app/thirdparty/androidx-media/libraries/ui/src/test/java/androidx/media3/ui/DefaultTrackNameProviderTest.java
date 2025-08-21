/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.ui;

import static com.google.common.truth.Truth.assertThat;

import android.content.res.Resources;
import androidx.media3.common.Format;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for the {@link DefaultMediaDescriptionAdapter}. */
@RunWith(AndroidJUnit4.class)
public class DefaultTrackNameProviderTest {

  @Test
  public void getTrackName_withInvalidLanguage_returnsUnknownWithLanguage() {
    Resources resources = ApplicationProvider.getApplicationContext().getResources();
    DefaultTrackNameProvider provider = new DefaultTrackNameProvider(resources);
    Format format = new Format.Builder().setLanguage("```").build();

    String name = provider.getTrackName(format);

    assertThat(name).isEqualTo(resources.getString(R.string.exo_track_unknown_name, "```"));
  }

  @Test
  public void getTrackName_withLanguageEmptyString_returnsUnknown() {
    Resources resources = ApplicationProvider.getApplicationContext().getResources();
    DefaultTrackNameProvider provider = new DefaultTrackNameProvider(resources);
    Format format = new Format.Builder().setLanguage("").build();

    String name = provider.getTrackName(format);

    assertThat(name).isEqualTo(resources.getString(R.string.exo_track_unknown));
  }

  @Test
  public void getTrackName_withLanguageSpacesNewLine_returnsUnknown() {
    Resources resources = ApplicationProvider.getApplicationContext().getResources();
    DefaultTrackNameProvider provider = new DefaultTrackNameProvider(resources);
    Format format = new Format.Builder().setLanguage("   \n ").build();

    String name = provider.getTrackName(format);

    assertThat(name).isEqualTo(resources.getString(R.string.exo_track_unknown));
  }

  @Test
  public void getTrackName_withLanguageEmptyStringAndLabel_returnsLabel() {
    Resources resources = ApplicationProvider.getApplicationContext().getResources();
    DefaultTrackNameProvider provider = new DefaultTrackNameProvider(resources);
    Format format = new Format.Builder().setLanguage("").setLabel("Main").build();

    String name = provider.getTrackName(format);

    assertThat(name).isEqualTo("Main");
  }
}
