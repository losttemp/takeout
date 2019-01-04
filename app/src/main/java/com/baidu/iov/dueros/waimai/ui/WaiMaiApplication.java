package com.baidu.iov.dueros.waimai.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.GuidingReq;
import com.baidu.iov.dueros.waimai.net.entity.response.GuidingBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.LeakCanaryUtils;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.faceos.client.GsonUtil;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.xiaoduos.syncclient.Entry;

public class WaiMaiApplication extends Application {

	private static WaiMaiApplication mInstance = null;
	private GuidingBean.DataBean.ListBean.WaimaiBean mWaimaiBean;

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

//        ApiUtils.getGuiding(new GuidingReq(), new ApiCallBack<GuidingBean>() {
//            @Override
//            public void onSuccess(GuidingBean data) {
//                if (data.getData().getList().getWaimai() != null) {
//                    mWaimaiBean = data.getData().getList().getWaimai();
//                    Lg.getInstance().d(TAG, mWaimaiBean.getAddress().getEmpty_result().get(0));
//                }
//            }
//
//            @Override
//            public void onFailed(String msg) {
//                Lg.getInstance().e(TAG, "Failed to get the guide. Error MSG: " + msg);
//            }
//
//            @Override
//            public void getLogid(String id) {
//                Lg.getInstance().d(TAG, id);
//            }
//        });
        //TODO zjh 测试数据换回网路获取
        GuidingBean test = GsonUtil.fromJson("\n" +
                        "{\"errno\":0,\"errmsg\":\"success\",\"log_id\":\"2791833319\",\"timestamp\":1546429591,\"data\":{\"list\":{\"waimai\":{\"address\":{\"me\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u9001\\u5230\\u76ee\\u7684\\u5730\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u9001\\u5230\\u516c\\u53f8\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u9001\\u5230\\u5bb6\\u91cc\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u65b0\\u589e\\u5730\\u5740\"],\"search_result\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\"],\"empty_result\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u65b0\\u589e\\u5730\\u5740\"]},\"shop\":{\"list\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7f8e\\u98df\\u5206\\u7c7b\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u9c9c\\u82b1\\u9884\\u5b9a\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u86cb\\u7cd5\\u9884\\u5b9a\"],\"flower\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\"],\"cake\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\"],\"breakfast\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\"]},\"cart\":{\"shop_detail\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u67e5\\u770b\\u8d2d\\u7269\\u8f66\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u786e\\u8ba4\\u4e0b\\u5355\"],\"cart_view\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u6536\\u8d77\\u8d2d\\u7269\\u8f66\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u6e05\\u7a7a\\u8d2d\\u7269\\u8f66\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u786e\\u8ba4\\u4e0b\\u5355\"]},\"pay\":{\"submut\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u53bb\\u652f\\u4ed8\"],\"payment_success\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u6211\\u7684\\u5916\\u5356\\u5230\\u54ea\\u4e86\"],\"detail\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u518d\\u6765\\u4e00\\u5355\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u53d6\\u6d88\\u8ba2\\u5355\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u53bb\\u652f\\u4ed8\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u6211\\u7684\\u5916\\u5356\\u5230\\u54ea\\u4e86\"]},\"search\":{\"search\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\"],\"result\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\"]},\"order\":{\"order\":[\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u7b2c\\u4e00\\u4e2a\",\"\\u4f60\\u597d\\u798f\\u7279\\uff0c\\u6211\\u7684\\u5916\\u5356\\u5230\\u54ea\\u4e86\"]}}},\"tag\":\"cff32f124639b13ca999c68c393e95c5\",\"last_update_time\":1546429591},\"timecost\":43}",
                GuidingBean.class);
        mWaimaiBean = test.getData().getList().getWaimai();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {
                GuidingAppear.INSTANCE.disappear();
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
	}

    public GuidingBean.DataBean.ListBean.WaimaiBean getWaimaiBean() {
	    if (mWaimaiBean == null)
	        return new GuidingBean.DataBean.ListBean.WaimaiBean();
        return mWaimaiBean;
    }

    public static WaiMaiApplication getInstance() {
		return mInstance;
	}
}