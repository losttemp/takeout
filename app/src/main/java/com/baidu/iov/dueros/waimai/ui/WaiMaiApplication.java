package com.baidu.iov.dueros.waimai.ui;

import android.app.Application;

import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.LeakCanaryUtils;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.mapapi.SDKInitializer;

public class WaiMaiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Constant.LEAKCANARY_DEBUG) {
            new LeakCanaryUtils().install(this);
        }
        SDKInitializer.initialize(this);
        LocationManager locationManager = LocationManager.getInstance(getApplicationContext());
        locationManager.initLocationClient(null, null, 0, true);
        locationManager.startLocation();
    }
}