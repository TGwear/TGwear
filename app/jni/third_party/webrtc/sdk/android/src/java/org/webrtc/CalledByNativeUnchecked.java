/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  @CalledByNativeUnchecked is used to generate JNI bindings that do not check for exceptions.
 *  It only makes sense to use this annotation on methods that declare a throws... spec.
 *  However, note that the exception received native side maybe an 'unchecked' (RuntimeExpception)
 *  such as NullPointerException, so the native code should differentiate these cases.
 *  Usage of this should be very rare; where possible handle exceptions in the Java side and use a
 *  return value to indicate success / failure.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface CalledByNativeUnchecked {
  /*
   *  If present, tells which inner class the method belongs to.
   */
  public String value() default "";
}
