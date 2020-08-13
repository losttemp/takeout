package com.baidu.iov.dueros.waimai.utils;

import com.google.gson.Gson;

public class GsonUtil {
    private static Gson sGson = new Gson();

    public GsonUtil() {
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return json == null ? null : sGson.fromJson(json, type);
    }

    public static String toJson(Object obj) {
        return obj == null ? null : sGson.toJson(obj);
    }
}
