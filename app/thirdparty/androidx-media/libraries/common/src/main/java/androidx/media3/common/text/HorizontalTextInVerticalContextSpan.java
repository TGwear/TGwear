/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.text;

import androidx.media3.common.util.UnstableApi;

/**
 * A styling span for horizontal text in a vertical context.
 *
 * <p>This is used in vertical text to write some characters in a horizontal orientation, known in
 * Japanese as tate-chu-yoko.
 *
 * <p>More information on <a
 * href="https://www.w3.org/TR/jlreq/#handling_of_tatechuyoko">tate-chu-yoko</a> and <a
 * href="https://developer.android.com/guide/topics/text/spans">span styling</a>.
 */
// NOTE: There's no Android layout support for this, so this span currently doesn't extend any
// styling superclasses (e.g. MetricAffectingSpan). The only way to render this styling is to
// extract the spans and do the layout manually.
@UnstableApi
public final class HorizontalTextInVerticalContextSpan implements LanguageFeatureSpan {}
