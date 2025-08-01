/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package org.thunderdog.challegram.voip;

import androidx.annotation.NonNull;

import com.gohj99.tgwear.Application;

import java.io.File;

public class VoIPPersistentConfig {
    static @NonNull File getVoipConfigFile() {
        File dir = new File(Application.Companion.getApplication().getFilesDir(), "tgvoip");
        return new File(dir, "voip_persistent_state.json");
    }

    public static long getVoipConfigFileSize() {
        File file = getVoipConfigFile();
        return file.exists() ? file.length() : 0;
    }
}
