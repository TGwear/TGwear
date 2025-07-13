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
        DataSavingOption.NEVER,
        DataSavingOption.MOBILE,
        DataSavingOption.ALWAYS,
        DataSavingOption.ROAMING
})
public @interface DataSavingOption {
    // enum from VoIPController.h:93
    int
            NEVER = 0,
            MOBILE = 1,
            ALWAYS = 2,
            ROAMING = 3 /*this field is not present in VoIPController.h and is converted to MOBILE or NEVER*/;
}
