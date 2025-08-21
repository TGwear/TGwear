/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import androidx.annotation.IntDef;
import androidx.media3.common.Player;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Util class for repeat mode handling. */
@UnstableApi
public final class RepeatModeUtil {

  // LINT.IfChange
  /**
   * Set of repeat toggle modes. Can be combined using bit-wise operations. Possible flag values are
   * {@link #REPEAT_TOGGLE_MODE_NONE}, {@link #REPEAT_TOGGLE_MODE_ONE} and {@link
   * #REPEAT_TOGGLE_MODE_ALL}.
   */
  // @Target list includes both 'default' targets and TYPE_USE, to ensure backwards compatibility
  // with Kotlin usages from before TYPE_USE was added.
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @Target({FIELD, METHOD, PARAMETER, LOCAL_VARIABLE, TYPE_USE})
  @IntDef(
      flag = true,
      value = {REPEAT_TOGGLE_MODE_NONE, REPEAT_TOGGLE_MODE_ONE, REPEAT_TOGGLE_MODE_ALL})
  public @interface RepeatToggleModes {}

  /** All repeat mode buttons disabled. */
  public static final int REPEAT_TOGGLE_MODE_NONE = 0;

  /** "Repeat One" button enabled. */
  public static final int REPEAT_TOGGLE_MODE_ONE = 1;

  /** "Repeat All" button enabled. */
  public static final int REPEAT_TOGGLE_MODE_ALL = 1 << 1; // 2

  // LINT.ThenChange(../../../../../../../../ui/src/main/res/values/attrs.xml)

  private RepeatModeUtil() {
    // Prevent instantiation.
  }

  /**
   * Gets the next repeat mode out of {@code enabledModes} starting from {@code currentMode}.
   *
   * @param currentMode The current {@link Player.RepeatMode}.
   * @param enabledModes The bitmask of enabled {@link RepeatToggleModes}.
   * @return The next repeat mode.
   */
  public static @Player.RepeatMode int getNextRepeatMode(
      @Player.RepeatMode int currentMode, @RepeatToggleModes int enabledModes) {
    for (int offset = 1; offset <= 2; offset++) {
      @Player.RepeatMode int proposedMode = (currentMode + offset) % 3;
      if (isRepeatModeEnabled(proposedMode, enabledModes)) {
        return proposedMode;
      }
    }
    return currentMode;
  }

  /**
   * Verifies whether a given {@link Player.RepeatMode} is enabled in the bitmask of {@link
   * RepeatToggleModes}.
   *
   * @param repeatMode The {@link Player.RepeatMode} to check.
   * @param enabledModes The bitmask of enabled {@link RepeatToggleModes}.
   * @return {@code true} if enabled.
   */
  public static boolean isRepeatModeEnabled(
      @Player.RepeatMode int repeatMode, @RepeatToggleModes int enabledModes) {
    switch (repeatMode) {
      case Player.REPEAT_MODE_OFF:
        return true;
      case Player.REPEAT_MODE_ONE:
        return (enabledModes & REPEAT_TOGGLE_MODE_ONE) != 0;
      case Player.REPEAT_MODE_ALL:
        return (enabledModes & REPEAT_TOGGLE_MODE_ALL) != 0;
      default:
        return false;
    }
  }
}
