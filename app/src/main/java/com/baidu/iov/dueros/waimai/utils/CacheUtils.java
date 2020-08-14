package com.baidu.iov.dueros.waimai.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.baidu.iov.dueros.waimai.ui.WaiMaiApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SuppressWarnings("WeakerAccess")
public class CacheUtils {

    public static class Key {
        public static final String SAVE_BDUSS = "save_bduss";
        public static final String SAVE_AUTH = "save_auth";
    }

    public static SharedPreferences shared() {
        return WaiMaiApplication.getInstance().getSharedPreferences("_cache", Activity.MODE_PRIVATE);
    }

    public static String getBduss() {
        String bduss = shared().getString(Key.SAVE_BDUSS, "BDRk9MbWU2bWF6eHpobDl4SXh-dGluTEJ" +
                "XTjRsRmlwd0dKVzNXR3I1WFR4RjFmRUFBQUFBJCQAA" +
                "AAAAAAAAAEAAABS2N2MaHBkdjMwMDUAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAANM3Nl~TNzZfSD");
        if (!TextUtils.isEmpty(bduss)) {
            return bduss;
        }
        return "";
    }

    public static void saveBduss(String bduss) {
        shared().edit().putString(Key.SAVE_BDUSS, bduss).apply();
    }

    public static Boolean getAuth() {
        Boolean auth = shared().getBoolean(Key.SAVE_AUTH, false);
        return auth;
    }

    public static void saveAuth(Boolean auth) {
        shared().edit().putBoolean(Key.SAVE_AUTH, auth).apply();
    }

    public static void saveAddressBean(String addressbean) {
        shared().edit().putString(Constant.ADDRESS_DATA, addressbean).apply();
    }

    public static String getAddressBean() {
        String address = shared().getString(Constant.ADDRESS_DATA, "");
        return address;
    }

    public static void saveAddress(String address) {
        shared().edit().putString(Constant.ADDRESS_SELECTED, address).apply();
    }

    public static String getAddress() {
        String address = shared().getString(Constant.ADDRESS_SELECTED, "");
        return address;
    }

    public static void saveAddrTime(long address) {
        shared().edit().putLong(Constant.ADDRESS_SELECTED_TIME, address).apply();
    }

    public static long getAddrTime() {
        long time = shared().getLong(Constant.ADDRESS_SELECTED_TIME, 0);
        return time;
    }
}
