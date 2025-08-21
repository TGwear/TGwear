/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

/**
 * Represents an operation that accepts a single input argument and returns no result. Unlike most
 * other functional interfaces, Consumer is expected to operate via side-effects.
 */
@UnstableApi
public interface Consumer<T> {

  /** Performs this operation on the given argument. */
  void accept(T t);
}
