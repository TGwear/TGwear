/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.google.media.networktester;

public class NetworkTester extends Thread {
  private native static long CreateTestController();
  private native static void TestControllerConnect(long testController);
  private native static void TestControllerRun(long testController);
  private native static boolean TestControllerIsDone(long testController);
  private native static void DestroyTestController(long testController);
  static {
    System.loadLibrary("network_tester_so");
  }

  @Override
  public void run() {
    final long testController = CreateTestController();
    TestControllerConnect(testController);
    while (!Thread.interrupted() && !TestControllerIsDone(testController)) {
      TestControllerRun(testController);
    }
    DestroyTestController(testController);
  }
}
