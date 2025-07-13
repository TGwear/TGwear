/*
 * Copyright (c) 2018-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.util.HashMap;
import java.util.Map;

public class JavaTypesTestHelper {
  @CalledByNative
  public static Map createTestStringMap() {
    Map<String, String> testMap = new HashMap<String, String>();
    testMap.put("one", "1");
    testMap.put("two", "2");
    testMap.put("three", "3");
    return testMap;
  }
}
