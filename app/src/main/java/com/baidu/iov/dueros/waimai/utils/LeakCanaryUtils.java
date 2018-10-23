package com.baidu.iov.dueros.waimai.utils;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class LeakCanaryUtils {

    private static RefWatcher sRefWatcher;

    private static Application sInstance;

    public static RefWatcher getRefWatcher() {
        return sRefWatcher;
    }

    public void install(final Application application) {
        if (null == application || LeakCanary.isInAnalyzerProcess(application)) {
            return;
        }
        sInstance = application;
        sRefWatcher = LeakCanary.install(application);
    }
}
