/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import kotlin.annotations.jvm.MigrationStatus;
import kotlin.annotations.jvm.UnderMigration;

/**
 * Annotation to declare all type usages in the annotated instance as {@link Nonnull}, unless
 * explicitly marked with a nullable annotation.
 */
// MigrationStatus.STRICT is marked as deprecated because it's considered experimental
@SuppressWarnings("deprecation")
@Nonnull
@TypeQualifierDefault(ElementType.TYPE_USE)
@UnderMigration(status = MigrationStatus.STRICT)
@Retention(RetentionPolicy.CLASS)
@UnstableApi
public @interface NonNullApi {}
