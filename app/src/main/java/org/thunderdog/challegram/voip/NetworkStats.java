/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package org.thunderdog.challegram.voip;

import androidx.annotation.NonNull;

public class NetworkStats {
    public long bytesSentWifi;
    public long bytesRecvdWifi;
    public long bytesSentMobile;
    public long bytesRecvdMobile;

    public NetworkStats() {
    }

    public NetworkStats(long bytesSentWifi, long bytesRecvdWifi, long bytesSentMobile, long bytesRecvdMobile) {
        this.bytesSentWifi = bytesSentWifi;
        this.bytesRecvdWifi = bytesRecvdWifi;
        this.bytesSentMobile = bytesSentMobile;
        this.bytesRecvdMobile = bytesRecvdMobile;
    }

    @Override
    @NonNull
    public String toString() {
        return "Stats{" +
                "bytesRecvdMobile=" + bytesRecvdMobile +
                ", bytesSentWifi=" + bytesSentWifi +
                ", bytesRecvdWifi=" + bytesRecvdWifi +
                ", bytesSentMobile=" + bytesSentMobile +
                '}';
    }
}
