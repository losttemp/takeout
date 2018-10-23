package com.baidu.iov.dueros.waimai.ui;

import android.app.Application;

import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.LeakCanaryUtils;
import com.baidu.iov.dueros.waimai.utils.LocationUtils;
import com.baidu.mapapi.SDKInitializer;

public class WaiMaiApplication extends Application {
    public static LocationUtils mLocationUtils = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Constant.LEAKCANARY_DEBUG) {
            new LeakCanaryUtils().install(this);
        }
        SDKInitializer.initialize(this);
        mLocationUtils = LocationUtils.getInstance(getApplicationContext());
        mLocationUtils.getLcation(null, null, 0, true);
        mLocationUtils.startLocation();
    }
}