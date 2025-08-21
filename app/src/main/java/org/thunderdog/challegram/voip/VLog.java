/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.thunderdog.challegram.voip;

import static com.gohj99.tgwear.utils.LogKt.TAG_VOIP;

import android.util.Log;

class VLog {
    public static void v(String msg) {
        Log.v(TAG_VOIP, msg);
    }

    public static void d(String msg) {
        Log.d(TAG_VOIP, msg);
    }

    public static void i(String msg) {
        Log.i(TAG_VOIP, msg);
    }

    public static void w(String msg) {
        Log.w(TAG_VOIP, msg);
    }

    public static void e(String msg) {
        Log.e(TAG_VOIP, msg);
    }

    public static void e(Throwable x) {
        x.printStackTrace();
        Log.e(TAG_VOIP, x.toString());
        // e(null, x);
    }

    public static void e(String msg, Throwable x) {
        x.printStackTrace();
        Log.e(TAG_VOIP, msg);
		/*StringWriter sw=new StringWriter();
		if(!TextUtils.isEmpty(msg)){
			sw.append(msg);
			sw.append(": ");
		}
		PrintWriter pw=new PrintWriter(sw);
		x.printStackTrace(pw);
		String[] lines=sw.toString().split("\n");
		for(String line:lines)
			e(line);*/
    }
}
