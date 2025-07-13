/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.thunderdog.challegram.voip;

import static cn.spacexc.neogram.utils.LogUtilsKt.TAG_VOIP;

import org.json.JSONException;
import org.json.JSONObject;

import cn.spacexc.neogram.utils.LogUtils;


/**
 * Created by grishka on 01.03.17.
 */

public class VoIPServerConfig {

    private static JSONObject config;

    public static void setConfig(String json) {
        try {
            config = new JSONObject(json);
            nativeSetConfig(json);
        } catch (JSONException x) {
            LogUtils.INSTANCE.info(TAG_VOIP, "Error parsing VoIP config" + x);
        }
    }

    public static int getInt(String key, int fallback) {
        return config.optInt(key, fallback);
    }

    public static double getDouble(String key, double fallback) {
        return config != null ? config.optDouble(key, fallback) : fallback;
    }

    public static String getString(String key, String fallback) {
        return config != null ? config.optString(key, fallback) : fallback;
    }

    public static boolean getBoolean(String key, boolean fallback) {
        return config != null ? config.optBoolean(key, fallback) : fallback;
    }

    private static native void nativeSetConfig(String json);
}
