package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.baidu.iov.dueros.waimai.ui.WaiMaiApplication;

/**
 * Created by v_chenxuran on 2018/11/11
 * Describe: 资源获取工具类，方便操作资源
 */
public final class ResUtils {

    private ResUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }



    public static Context getContext() {
        return WaiMaiApplication.getInstance().getApplicationContext();
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中定义的字符信息
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到String.xml中定义的字符信息,带占位符
     */
    public static String getString(int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    /**
     * 得到String.xml中定义的字符数组信息
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到color.xml中定义的颜色信息
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到Drawable资源
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public static String[] getStringArray(final @ArrayRes int arrayRes) {
        return getResources().getStringArray(arrayRes);
    }

    public static int[] getIntArray(final @ArrayRes int arrayRes) {
        return getResources().getIntArray(arrayRes);
    }
}
