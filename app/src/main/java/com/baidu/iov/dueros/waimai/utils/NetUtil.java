package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetUtil {
    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_WIFI = 1;
    public static final int NETWORK_TYPE_2G   = 2;
    public static final int NETWORK_TYPE_3G   = 3;
    public static final int NETWORK_TYPE_4G   = 4;


    /**
     * get current NetType
     * @param context
     * @return
     */
    public static int getNetType(Context context) {

        int type = NETWORK_TYPE_NONE;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo   = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (null != mobileInfo && mobileInfo.isConnected()) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int netType = tm.getNetworkType();


            switch (netType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    type = NETWORK_TYPE_2G;
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    type = NETWORK_TYPE_3G;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    type = NETWORK_TYPE_4G;
                    break;
                default:
                    type = NETWORK_TYPE_NONE;
                    break;
            }

        }
        if (null != wifiInfo && wifiInfo.isConnected()) {
            type = NETWORK_TYPE_WIFI;
        }
        return type;
    }



    /**
     * isNetworkAvailable
     * @param context
     * @return
     */
    public static boolean getNetWorkState(Context context) {
        Context ct = context.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) ct
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == cm) {
            return false;
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (null != info) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNetWorkAvailable(Context context) {
        boolean isAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null &&  networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }


    /**
     * isWifiNet
     * @param context
     * @return
     */
    public static boolean isWifiNet(Context context) {
        boolean bRet = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo && wifiInfo.isConnectedOrConnecting()) {
            bRet = true;
        }
        return bRet;
    }


    /**
     * isMobileNet 2G/3G/4G
     * @param context
     * @return
     */
    public static boolean isMobileNet(final Context context) {
        boolean ret = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm) {
            NetworkInfo mobileInfo = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != mobileInfo && mobileInfo.isConnectedOrConnecting()) {
                ret = true;
            }
        }
        return ret;
    }


    public static String getWifiMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * getWifiSsid（SSID）
     */
    public static String getWifiSsid(Context context) {
        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi_service.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        if (ssid == null) {
            ssid = "";
        }
        return ssid;
    }



    /**
     * getlocalIP 192.168.1.0
     */
    public static String getlocalIP(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        if (ipAddress == 0)
            return null;
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }

    /**
     * getIPAddress
     * @param context
     * @return
     * @throws SocketException
     */
    public static String getIPAddress(Context context) throws SocketException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } else {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        return null;
    }

    /**
     * setWifiEnabled
     */
    public static void setWifiEnabled(Context context, boolean enable){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }


    /**
     * isWifiEnabled
     */
    public static boolean isWifiEnabled(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }
}
