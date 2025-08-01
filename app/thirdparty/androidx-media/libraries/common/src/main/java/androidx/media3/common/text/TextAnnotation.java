/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.text;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;
import androidx.media3.common.util.UnstableApi;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** Properties of a text annotation (i.e. ruby, text emphasis marks). */
@UnstableApi
public final class TextAnnotation {
  /** The text annotation position is unknown. */
  public static final int POSITION_UNKNOWN = -1;

  /**
   * For horizontal text, the text annotation should be positioned above the base text.
   *
   * <p>For vertical text it should be positioned to the right, same as CSS's <a
   * href="https://developer.mozilla.org/en-US/docs/Web/CSS/ruby-position">ruby-position</a>.
   */
  public static final int POSITION_BEFORE = 1;

  /**
   * For horizontal text, the text annotation should be positioned below the base text.
   *
   * <p>For vertical text it should be positioned to the left, same as CSS's <a
   * href="https://developer.mozilla.org/en-US/docs/Web/CSS/ruby-position">ruby-position</a>.
   */
  public static final int POSITION_AFTER = 2;

  /**
   * The possible positions of the annotation text relative to the base text.
   *
   * <p>One of:
   *
   * <ul>
   *   <li>{@link #POSITION_UNKNOWN}
   *   <li>{@link #POSITION_BEFORE}
   *   <li>{@link #POSITION_AFTER}
   * </ul>
   */
  @Documented
  @Retention(SOURCE)
  @Target(TYPE_USE)
  @IntDef({POSITION_UNKNOWN, POSITION_BEFORE, POSITION_AFTER})
  public @interface Position {}

  private TextAnnotation() {}
}
