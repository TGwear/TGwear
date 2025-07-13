/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description of media constraints for {@code MediaStream} and
 * {@code PeerConnection}.
 */
public class MediaConstraints {
  /** Simple String key/value pair. */
  public static class KeyValuePair {
    private final String key;
    private final String value;

    public KeyValuePair(String key, String value) {
      this.key = key;
      this.value = value;
    }

    @CalledByNative("KeyValuePair")
    public String getKey() {
      return key;
    }

    @CalledByNative("KeyValuePair")
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return key + ": " + value;
    }

    @Override
    public boolean equals(@Nullable Object other) {
      if (this == other) {
        return true;
      }
      if (other == null || getClass() != other.getClass()) {
        return false;
      }
      KeyValuePair that = (KeyValuePair) other;
      return key.equals(that.key) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return key.hashCode() + value.hashCode();
    }
  }

  public final List<KeyValuePair> mandatory;
  public final List<KeyValuePair> optional;

  public MediaConstraints() {
    mandatory = new ArrayList<KeyValuePair>();
    optional = new ArrayList<KeyValuePair>();
  }

  private static String stringifyKeyValuePairList(List<KeyValuePair> list) {
    StringBuilder builder = new StringBuilder("[");
    for (KeyValuePair pair : list) {
      if (builder.length() > 1) {
        builder.append(", ");
      }
      builder.append(pair.toString());
    }
    return builder.append("]").toString();
  }

  @Override
  public String toString() {
    return "mandatory: " + stringifyKeyValuePairList(mandatory) + ", optional: "
        + stringifyKeyValuePairList(optional);
  }

  @CalledByNative
  List<KeyValuePair> getMandatory() {
    return mandatory;
  }

  @CalledByNative
  List<KeyValuePair> getOptional() {
    return optional;
  }
}
