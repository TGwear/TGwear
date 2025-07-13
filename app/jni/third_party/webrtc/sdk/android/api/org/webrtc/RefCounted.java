/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Interface for ref counted objects in WebRTC. These objects have significant resources that need
 * to be freed when they are no longer in use. Each objects starts with ref count of one when
 * created. If a reference is passed as a parameter to a method, the caller has ownesrship of the
 * object by default - calling release is not necessary unless retain is called.
 */
public interface RefCounted {
  /** Increases ref count by one. */
  @CalledByNative void retain();

  /**
   * Decreases ref count by one. When the ref count reaches zero, resources related to the object
   * will be freed.
   */
  @CalledByNative void release();
}
