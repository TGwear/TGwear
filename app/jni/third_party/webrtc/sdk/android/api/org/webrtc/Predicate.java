/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 */
public interface Predicate<T> {
  /**
   * Evaluates this predicate on the given argument.
   *
   * @param arg the input argument
   * @return true if the input argument matches the predicate, otherwise false
   */
  boolean test(T arg);

  /**
   * Returns a composed predicate that represents a short-circuiting logical OR of this predicate
   * and another. When evaluating the composed predicate, if this predicate is true, then the other
   * predicate is not evaluated.
   *
   * @param other a predicate that will be logically-ORed with this predicate
   * @return a composed predicate that represents the short-circuiting logical OR of this predicate
   *     and the other predicate
   */
  default Predicate<T> or(Predicate<? super T> other) {
    return new Predicate<T>() {
      @Override
      public boolean test(T arg) {
        return Predicate.this.test(arg) || other.test(arg);
      }
    };
  }

  /**
   * Returns a composed predicate that represents a short-circuiting logical AND of this predicate
   * and another.
   *
   * @param other a predicate that will be logically-ANDed with this predicate
   * @return a composed predicate that represents the short-circuiting logical AND of this predicate
   *     and the other predicate
   */
  default Predicate<T> and(Predicate<? super T> other) {
    return new Predicate<T>() {
      @Override
      public boolean test(T arg) {
        return Predicate.this.test(arg) && other.test(arg);
      }
    };
  }

  /**
   * Returns a predicate that represents the logical negation of this predicate.
   *
   * @return a predicate that represents the logical negation of this predicate
   */
  default Predicate<T> negate() {
    return new Predicate<T>() {
      @Override
      public boolean test(T arg) {
        return !Predicate.this.test(arg);
      }
    };
  }
}