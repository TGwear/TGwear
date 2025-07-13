/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package org.thunderdog.challegram.voip;

import androidx.annotation.Keep;

import org.drinkless.tdlib.TdApi;

@Keep
public class Socks5Proxy {
    public final String host;
    public final int port;

    public final String username;
    public final String password;

    public Socks5Proxy(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public Socks5Proxy(TdApi.InternalLinkTypeProxy proxy) {
        if (proxy.type.getConstructor() != TdApi.ProxyTypeSocks5.CONSTRUCTOR)
            throw new IllegalArgumentException(proxy.type.toString());
        TdApi.ProxyTypeSocks5 socks5 = (TdApi.ProxyTypeSocks5) proxy.type;
        this.host = proxy.server;
        this.port = proxy.port;
        this.username = socks5.username;
        this.password = socks5.password;
    }
}
