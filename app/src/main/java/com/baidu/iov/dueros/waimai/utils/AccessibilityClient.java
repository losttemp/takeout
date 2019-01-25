package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hegui on 2018/10/5.
 */

public class AccessibilityClient {
    private static final String TAG = "AccessibilityClient";
    private static final String PACKAGE_NAME = "com.baidu.che.codriver";
    private static final String ACTION_REGISTER = "com.baidu.duerosauto.accessibility.register";
    private static final String ACTION_UNREGISTER = "com.baidu.duerosauto.accessibility.unregister";
    private static final String KEY_ENABLE = "enable";
    private static final String KEY_PREFIX = "prefix";
    private static final String KEY_SUFFIX = "suffix";
    private static final String KEY_PKG_NAME = "pkg_name";

    private static class SingletonHolder {
        private static AccessibilityClient accessbilityClient = new AccessibilityClient();
    }

    public static AccessibilityClient getInstance() {
        return SingletonHolder.accessbilityClient;
    }

    /*
     * 注册接口
     * @context ：上下文
     * @enable ：模拟点击的使能开关
     * @prefix ：要注册的前缀集合
     * @suffix ：要注册的后缀集合
     */
    public void register(Context context, boolean enable,
                         ArrayList<String> prefix, ArrayList<String> suffix) {
        if (context == null) {
            Log.d(TAG, "register() context == null");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_REGISTER);
        intent.putExtra(KEY_PKG_NAME, context.getPackageName());
        intent.putExtra(KEY_ENABLE, enable);
        if (null != prefix && prefix.size() > 0) {
            intent.putStringArrayListExtra(KEY_PREFIX, prefix);
        }
        if (null != suffix && suffix.size() > 0) {
            intent.putStringArrayListExtra(KEY_SUFFIX, suffix);
        }
        intent.setPackage(PACKAGE_NAME);
        context.sendBroadcast(intent);
        Log.d(TAG, "register() succeed");
    }

    /*
     * 取消注册指令
     * @context ：上下文
     * @callback : 该参数必须要与注册时的参数一致
     */
    public void unregister(Context context) {
        if (context == null) {
            Log.d(TAG, "unregister() context == null");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_UNREGISTER);
        intent.putExtra(KEY_PKG_NAME, context.getPackageName());
        intent.setPackage(PACKAGE_NAME);
        context.sendBroadcast(intent);
        Log.d(TAG, "unregister() succeed");
    }

}
