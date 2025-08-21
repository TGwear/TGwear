/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

/**
 * Annotation for specifying a nullable type.
 *
 * <p>Unlike {@link androidx.annotation.Nullable} used elsewhere in the library, this annotation can
 * be used on {@link ElementType#TYPE_USE} locations like generic type parameters and array element
 * types.
 */
@UnstableApi
@Documented
@Retention(RetentionPolicy.CLASS)
@Nonnull(when = When.MAYBE)
@Target({ElementType.TYPE_USE})
@TypeQualifierNickname
public @interface NullableType {}
