package com.baidu.iov.dueros.waimai.ui;

import android.app.Application;

import com.baidu.iov.dueros.waimai.utils.LocationUtils;
import com.baidu.mapapi.SDKInitializer;

public class WaiMaiApplication extends Application {
    public static LocationUtils mLocationUtils = null;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        mLocationUtils = LocationUtils.getInstance(getApplicationContext());
        mLocationUtils.getLcation(null, null, 0, true);
        mLocationUtils.startLocation();
    }
}