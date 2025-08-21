/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.thunderdog.challegram.voip;

import java.nio.ByteBuffer;

/**
 * Created by grishka on 01.04.17.
 */

public class Resampler {
    public static native int convert44to48(ByteBuffer from, ByteBuffer to);

    public static native int convert48to44(ByteBuffer from, ByteBuffer to);
}
