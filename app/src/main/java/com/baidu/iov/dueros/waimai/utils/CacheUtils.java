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
        String bduss = shared().getString(Key.SAVE_BDUSS, "");
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
}
