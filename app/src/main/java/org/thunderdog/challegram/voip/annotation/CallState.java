/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package org.thunderdog.challegram.voip.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        CallState.WAIT_INIT,
        CallState.WAIT_INIT_ACK,
        CallState.ESTABLISHED,
        CallState.FAILED,
        CallState.RECONNECTING
})
public @interface CallState {
    // enum from VoIPController.h:62
    int
            WAIT_INIT = 1,
            WAIT_INIT_ACK = 2,
            ESTABLISHED = 3,
            FAILED = 4,
            RECONNECTING = 5;
}
