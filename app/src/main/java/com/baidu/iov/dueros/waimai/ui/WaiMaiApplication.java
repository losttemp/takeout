package com.baidu.iov.dueros.waimai.ui;

import android.app.Application;

import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.LeakCanaryUtils;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.xiaoduos.syncclient.Entry;

public class WaiMaiApplication extends Application {

	private static WaiMaiApplication mInstance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		if (Constant.LEAKCANARY_DEBUG) {
			new LeakCanaryUtils().install(this);
		}
		SDKInitializer.initialize(this);
		LocationManager locationManager = LocationManager.getInstance(getApplicationContext());
		locationManager.initLocationClient(null, null, 0, true);
		locationManager.startLocation();
		final Entry entry = Entry.getInstance();
		entry.init(getApplicationContext(), new Entry.Callback() {
			@Override
			public void onSuccess() {
				Constant.UUID = entry.getUUID();
			}

			@Override
			public void onFailure() {

			}
		});
	}

	public static WaiMaiApplication getInstance() {
		return mInstance;
	}
}