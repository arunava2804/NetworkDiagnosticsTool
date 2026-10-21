package com.arunava.networktool.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.arunava.networktool.model.NetworkStatusModel;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class NetworkInfoProvider {

    public static NetworkStatusModel getNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return new NetworkStatusModel("Unknown", "N/A", "N/A", false);

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) {
            return new NetworkStatusModel("Disconnected", "N/A", "N/A", false);
        }

        NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
        if (caps == null) {
            return new NetworkStatusModel("Disconnected", "N/A", "N/A", false);
        }

        String type = "Cellular / Mobile Data";
        String linkSpeed = caps.getLinkDownstreamBandwidthKbps() > 0 
                ? (caps.getLinkDownstreamBandwidthKbps() / 1000) + " Mbps" 
                : "Dynamic";

        if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            type = "Wi-Fi";
            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wm != null) {
                WifiInfo info = wm.getConnectionInfo();
                if (info != null && info.getLinkSpeed() > 0) {
                    linkSpeed = info.getLinkSpeed() + " " + WifiInfo.LINK_SPEED_UNITS;
                }
            }
        } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            type = "Ethernet";
        }

        String ip = getLocalIpAddress();
        return new NetworkStatusModel(type, ip, linkSpeed, true);
    }

    private static String getLocalIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {}
        return "127.0.0.1";
    }
}
