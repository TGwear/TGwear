/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.util.Locale;

/**
 * Description of an RFC 4566 Session.
 * SDPs are passed as serialized Strings in Java-land and are materialized
 * to SessionDescriptionInterface as appropriate in the JNI layer.
 */
public class SessionDescription {
  /** Java-land enum version of SessionDescriptionInterface's type() string. */
  public static enum Type {
    OFFER,
    PRANSWER,
    ANSWER,
    ROLLBACK;

    public String canonicalForm() {
      return name().toLowerCase(Locale.US);
    }

    @CalledByNative("Type")
    public static Type fromCanonicalForm(String canonical) {
      return Type.valueOf(Type.class, canonical.toUpperCase(Locale.US));
    }
  }

  public final Type type;
  public final String description;

  @CalledByNative
  public SessionDescription(Type type, String description) {
    this.type = type;
    this.description = description;
  }

  @CalledByNative
  String getDescription() {
    return description;
  }

  @CalledByNative
  String getTypeInCanonicalForm() {
    return type.canonicalForm();
  }
}
