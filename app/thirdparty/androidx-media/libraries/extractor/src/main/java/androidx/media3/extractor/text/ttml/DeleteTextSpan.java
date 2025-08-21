/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text.ttml;

import android.text.Spanned;

/**
 * A span used to mark a section of text for later deletion.
 *
 * <p>This is deliberately package-private because it's not generally supported by Android and
 * results in surprising behaviour when simply calling {@link Spanned#toString} (i.e. the text isn't
 * deleted).
 *
 * <p>This span is explicitly handled in {@code TtmlNode#cleanUpText}.
 */
/* package */ final class DeleteTextSpan {}
