/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.thunderdog.challegram.voip;

import static cn.spacexc.neogram.utils.LogUtilsKt.TAG_VOIP;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import cn.spacexc.neogram.Application;
import cn.spacexc.neogram.utils.LogUtils;

/**
 * Created by grishka on 16.01.2018.
 */

@SuppressWarnings("unused")
public class JNIUtilities {
    @TargetApi(23)
    public static String getCurrentNetworkInterfaceName() {
        ConnectivityManager cm = (ConnectivityManager) Application.Companion.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network net = cm.getActiveNetwork();
        if (net == null)
            return null;
        LinkProperties props = cm.getLinkProperties(net);
        if (props == null)
            return null;
        return props.getInterfaceName();
    }

    public static String[] getLocalNetworkAddressesAndInterfaceName() {
        ConnectivityManager cm = (ConnectivityManager) Application.Companion.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network net = cm.getActiveNetwork();
            if (net == null)
                return null;
            LinkProperties linkProps = cm.getLinkProperties(net);
            if (linkProps == null)
                return null;
            String ipv4 = null, ipv6 = null;
            for (LinkAddress addr : linkProps.getLinkAddresses()) {
                InetAddress a = addr.getAddress();
                if (a instanceof Inet4Address) {
                    if (!a.isLinkLocalAddress()) {
                        ipv4 = a.getHostAddress();
                    }
                } else if (a instanceof Inet6Address) {
                    if (!a.isLinkLocalAddress() && (a.getAddress()[0] & 0xF0) != 0xF0) {
                        ipv6 = a.getHostAddress();
                    }
                }
            }
            return new String[]{linkProps.getInterfaceName(), ipv4, ipv6};
        } else {
            try {
                Enumeration<NetworkInterface> itfs = NetworkInterface.getNetworkInterfaces();
                if (itfs == null)
                    return null;
                while (itfs.hasMoreElements()) {
                    NetworkInterface itf = itfs.nextElement();
                    if (itf.isLoopback() || !itf.isUp())
                        continue;
                    Enumeration<InetAddress> addrs = itf.getInetAddresses();
                    String ipv4 = null, ipv6 = null;
                    while (addrs.hasMoreElements()) {
                        InetAddress a = addrs.nextElement();
                        if (a instanceof Inet4Address) {
                            if (!a.isLinkLocalAddress()) {
                                ipv4 = a.getHostAddress();
                            }
                        } else if (a instanceof Inet6Address) {
                            if (!a.isLinkLocalAddress() && (a.getAddress()[0] & 0xF0) != 0xF0) {
                                ipv6 = a.getHostAddress();
                            }
                        }
                    }
                    return new String[]{itf.getName(), ipv4, ipv6};
                }
                return null;
            } catch (Exception x) {
                x.printStackTrace();
                LogUtils.INSTANCE.info(TAG_VOIP, x.toString());
                return null;
            }
        }
    }

    // [name, country, mcc, mnc]
    public static String[] getCarrierInfo() {
        TelephonyManager tm = (TelephonyManager) Application.Companion.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= 24) {
            tm = tm.createForSubscriptionId(SubscriptionManager.getDefaultDataSubscriptionId());
        }
        if (!TextUtils.isEmpty(tm.getNetworkOperatorName())) {
            String mnc = "", mcc = "";
            String carrierID = tm.getNetworkOperator();
            if (carrierID != null && carrierID.length() > 3) {
                mcc = carrierID.substring(0, 3);
                mnc = carrierID.substring(3);
            }
            return new String[]{tm.getNetworkOperatorName(), tm.getNetworkCountryIso().toUpperCase(), mcc, mnc};
        }
        return null;
    }

    public static int[] getWifiInfo() {
        try {
            WifiManager wmgr = (WifiManager) Application.Companion.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wmgr.getConnectionInfo();
            return new int[]{info.getRssi(), info.getLinkSpeed()};
        } catch (Exception ignore) {
        }
        return null;
    }
}