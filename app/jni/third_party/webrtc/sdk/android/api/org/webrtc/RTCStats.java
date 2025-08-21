/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.util.Map;

/**
 * Java version of webrtc::RTCStats. Represents an RTCStats object, as
 * described in https://w3c.github.io/webrtc-stats/. The `id`, `timestampUs`
 * and `type` accessors have the same meaning for this class as for the
 * RTCStats dictionary. Each RTCStatsReport produced by getStats contains
 * multiple RTCStats objects; one for each underlying object (codec, stream,
 * transport, etc.) that was inspected to produce the stats.
 */
public class RTCStats {
  private final long timestampUs;
  private final String type;
  private final String id;
  private final Map<String, Object> members;

  public RTCStats(long timestampUs, String type, String id, Map<String, Object> members) {
    this.timestampUs = timestampUs;
    this.type = type;
    this.id = id;
    this.members = members;
  }

  // Timestamp in microseconds.
  public double getTimestampUs() {
    return timestampUs;
  }

  // Equivalent to RTCStatsType in the stats spec. Indicates the type of the
  // object that was inspected to produce the stats.
  public String getType() {
    return type;
  }

  // Unique ID representing this stats object. May be referred to by members of
  // other stats objects.
  public String getId() {
    return id;
  }

  /**
   * Returns map of member names to values. Returns as an ordered map so that
   * the stats object can be serialized with a consistent ordering.
   *
   * Values will be one of the following objects:
   * - Boolean
   * - Integer (for 32-bit signed integers)
   * - Long (for 32-bit unsigned and 64-bit signed integers)
   * - BigInteger (for 64-bit unsigned integers)
   * - Double
   * - String
   * - The array form of any of the above (e.g., Integer[])
   * - Map of String keys to BigInteger / Double values
   */
  public Map<String, Object> getMembers() {
    return members;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{ timestampUs: ")
        .append(timestampUs)
        .append(", type: ")
        .append(type)
        .append(", id: ")
        .append(id);
    boolean first = true;
    for (Map.Entry<String, Object> entry : members.entrySet()) {
      builder.append(", ").append(entry.getKey()).append(": ");
      appendValue(builder, entry.getValue());
    }
    builder.append(" }");
    return builder.toString();
  }

  private static void appendValue(StringBuilder builder, Object value) {
    if (value instanceof Object[]) {
      Object[] arrayValue = (Object[]) value;
      builder.append('[');
      for (int i = 0; i < arrayValue.length; ++i) {
        if (i != 0) {
          builder.append(", ");
        }
        appendValue(builder, arrayValue[i]);
      }
      builder.append(']');
    } else if (value instanceof String) {
      // Enclose strings in quotes to make it clear they're strings.
      builder.append('"').append(value).append('"');
    } else {
      builder.append(value);
    }
  }

  // TODO(bugs.webrtc.org/8557) Use ctor directly with full Map type.
  @SuppressWarnings("unchecked")
  @CalledByNative
  static RTCStats create(long timestampUs, String type, String id, Map members) {
    return new RTCStats(timestampUs, type, id, members);
  }
}
