/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link DefaultSubtitleParserFactory}. */
@RunWith(AndroidJUnit4.class)
public class DefaultSubtitleParserFactoryTest {

  /**
   * This test loops through all the public fields of {@link MimeTypes} and assumes all the static,
   * string fields with a single "/" in them are MIME types - then it uses these to 'fuzz' the
   * {@link DefaultSubtitleParserFactory} to check that, for each MIME type, it either consistently
   * supports or doesn't support it.
   */
  @Test
  public void formatSupportIsConsistent() throws Exception {
    DefaultSubtitleParserFactory factory = new DefaultSubtitleParserFactory();
    for (Field field : MimeTypes.class.getFields()) {
      if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(String.class)) {
        String fieldValue = (String) field.get(null);
        // Filter to only MIME types (values with exactly one '/')
        if (CharMatcher.is('/').countIn(fieldValue) == 1) {
          Format.Builder formatBuilder = new Format.Builder().setSampleMimeType(fieldValue);
          if (fieldValue.equals(MimeTypes.APPLICATION_DVBSUBS)) {
            formatBuilder.setInitializationData(ImmutableList.of(new byte[] {1, 2, 3, 4}));
          }
          if (fieldValue.equals(MimeTypes.APPLICATION_VOBSUB)) {
            formatBuilder.setInitializationData(ImmutableList.of(new byte[] {1, 2, 3, 4}));
          }
          Format format = formatBuilder.build();
          if (factory.supportsFormat(format)) {
            try {
              assertThat(factory.getCueReplacementBehavior(format))
                  .isEqualTo(factory.create(format).getCueReplacementBehavior());
            } catch (IllegalArgumentException e) {
              throw new AssertionError(
                  "Unexpected error for supported MIME type (" + fieldValue + ")", e);
            }
          } else {
            assertThrows(
                "MIME=" + fieldValue,
                IllegalArgumentException.class,
                () -> factory.getCueReplacementBehavior(format));
            assertThrows(
                "MIME=" + fieldValue, IllegalArgumentException.class, () -> factory.create(format));
          }
        }
      }
    }
  }
}
