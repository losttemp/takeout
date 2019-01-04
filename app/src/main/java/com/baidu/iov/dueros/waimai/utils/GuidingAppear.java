package com.baidu.iov.dueros.waimai.utils;

import android.app.StatusBarsManager;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.List;

public enum GuidingAppear {
    INSTANCE;
    private Handler mHandler;
    private Runnable mRunnable;
    private int times;
    private final static String APP_NAME = "com.baidu.iov.dueros.waimai";

    public void init(@NonNull final Context context, @NonNull final List<String> strings) {
        disappear();
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                showString(context, strings);
            }
        };
        mHandler.post(mRunnable);
    }

    private void showString(@NonNull Context context, @NonNull List<String> strings) {
        if (strings.size() == 0)
            return;
        if (strings.size() == 1) {
            Lg.getInstance().e("GuidingAppear", strings.get(0));
//            StatusBarsManager.conversationByApp(context, APP_NAME, strings.get(0));
            return;
        }

//        StatusBarsManager.conversationByApp(context, APP_NAME, strings.get(times % strings.size()));
        Lg.getInstance().e("GuidingAppear", strings.get(times % strings.size()));
        times++;
        mHandler.postDelayed(mRunnable, 10000);
    }

    public void disappear() {
        if (mHandler != null && mRunnable != null)
            mHandler.removeCallbacks(mRunnable);
        times = 0;
        if (mHandler != null)
            mHandler = null;
        if (mRunnable != null)
            mRunnable = null;
    }

}
