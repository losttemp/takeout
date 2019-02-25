package com.baidu.iov.dueros.waimai.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.GuidingReq;
import com.baidu.iov.dueros.waimai.net.entity.response.GuidingBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.LeakCanaryUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.faceos.client.GsonUtil;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.xiaoduos.syncclient.Entry;

public class WaiMaiApplication extends Application {

	private static WaiMaiApplication mInstance = null;
	private GuidingBean.ListBean.WaimaiBean mWaimaiBean;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		if (Constant.LEAKCANARY_DEBUG) {
			new LeakCanaryUtils().install(this);
		}
		SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.GCJ02);
		LocationManager locationManager = LocationManager.getInstance(getApplicationContext());
		locationManager.initLocationClient(null, null, 0, true);
		locationManager.startLocation();
		final Entry entry = Entry.getInstance();
		entry.init(getApplicationContext(), new Entry.Callback() {
			@Override
			public void onSuccess() {
                String uuid = entry.getUUID();
                if (!TextUtils.isEmpty(uuid)){
                    Constant.UUID = entry.getUUID();
                }
			}

			@Override
			public void onFailure() {
			}
		});

        GuidingBean test = GsonUtil.fromJson("{\n" +
                        "        \"list\": {\n" +
                        "            \"waimai\": {\n" +
                        "                \"address\": {\n" +
                        "                    \"me\": [\n" +
                        "                        \"你好福特，送到目的地\", \n" +
                        "                        \"你好福特，送到公司\", \n" +
                        "                        \"你好福特，送到家里\", \n" +
                        "                        \"你好福特，第一个\", \n" +
                        "                        \"你好福特，新增地址\"\n" +
                        "                    ], \n" +
                        "                    \"search_result\": [\n" +
                        "                        \"你好福特，第一个\"\n" +
                        "                    ], \n" +
                        "                    \"empty_result\": [\n" +
                        "                        \"你好福特，新增地址\"\n" +
                        "                    ]\n" +
                        "                }, \n" +
                        "                \"shop\": {\n" +
                        "                    \"list\": [\n" +
                        "                        \"你好福特，第一个\", \n" +
                        "                        \"你好福特，美食分类\", \n" +
                        "                        \"你好福特，鲜花预定\", \n" +
                        "                        \"你好福特，蛋糕预定\"\n" +
                        "                    ], \n" +
                        "                    \"flower\": [\n" +
                        "                        \"你好福特，第一个\"\n" +
                        "                    ], \n" +
                        "                    \"cake\": [\n" +
                        "                        \"你好福特，第一个\"\n" +
                        "                    ], \n" +
                        "                    \"breakfast\": [\n" +
                        "                        \"你好福特，第一个\"\n" +
                        "                    ]\n" +
                        "                }, \n" +
                        "                \"cart\": {\n" +
                        "                    \"shop_detail\": [\n" +
                        "                        \"你好福特，第一个\", \n" +
                        "                        \"你好福特，查看购物车\", \n" +
                        "                        \"你好福特，确认下单\"\n" +
                        "                    ], \n" +
                        "                    \"cart_view\": [\n" +
                        "                        \"你好福特，收起购物车\", \n" +
                        "                        \"你好福特，清空购物车\", \n" +
                        "                        \"你好福特，确认下单\"\n" +
                        "                    ]\n" +
                        "                }, \n" +
                        "                \"pay\": {\n" +
                        "                    \"submut\": [\n" +
                        "                        \"你好福特，去支付\"\n" +
                        "                    ], \n" +
                        "                    \"payment_success\": [\n" +
                        "                        \"你好福特，我的外卖到哪了\"\n" +
                        "                    ], \n" +
                        "                    \"detail\": [\n" +
                        "                        \"你好福特，再来一单\", \n" +
                        "                        \"你好福特，取消订单\", \n" +
                        "                        \"你好福特，去支付\", \n" +
                        "                        \"你好福特，我的外卖到哪了\"\n" +
                        "                    ]\n" +
                        "                }, \n" +
                        "                \"search\": {\n" +
                        "                    \"search\": [\n" +
                        "                        \"你好福特，第一个\"\n" +
                        "                    ], \n" +
                        "                    \"result\": [\n" +
                        "                        \"你好福特，第一个\"\n" +
                        "                    ]\n" +
                        "                }, \n" +
                        "                \"order\": {\n" +
                        "                    \"order\": [\n" +
                        "                        \"你好福特，第一个\", \n" +
                        "                        \"你好福特，我的外卖到哪了\"\n" +
                        "                    ]\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }, \n" +
                        "        \"tag\": \"cff32f124639b13ca999c68c393e95c5\", \n" +
                        "        \"last_update_time\": 1546583693\n" +
                        "    }",
                GuidingBean.class);
        mWaimaiBean = test.getList().getWaimai();

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

    public GuidingBean.ListBean.WaimaiBean getWaimaiBean() {
	    if (mWaimaiBean == null)
	        return new GuidingBean.ListBean.WaimaiBean();
        return mWaimaiBean;
    }

    public void setmWaimaiBean(GuidingBean.ListBean.WaimaiBean mWaimaiBean) {
        this.mWaimaiBean = mWaimaiBean;
    }

    public static WaiMaiApplication getInstance() {
		return mInstance;
	}
}