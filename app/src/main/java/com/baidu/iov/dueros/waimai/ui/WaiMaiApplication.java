package com.baidu.iov.dueros.waimai.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.baidu.iov.dueros.waimai.net.entity.response.GuideBean;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GsonUtil;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.LocationManager;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.xiaoduos.syncclient.Entry;

import java.util.ArrayList;
import java.util.HashMap;

public class WaiMaiApplication extends Application {

	private static WaiMaiApplication mInstance = null;
	public static boolean START = false;
//	private GuidingBean.ListBean.WaimaiBean mWaimaiBean;
    private GuideBean.DataBean.GeneralListBean.TakeoutBean mWaimaiBean;

    private HashMap<String, ArrayList<Integer>> tipsMap=new HashMap<>();

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;

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

        GuideBean test1 = GsonUtil.fromJson("{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"success\",\n" +
                "    \"log_id\": 3686857159,\n" +
                "    \"timestamp\": 1557800032,\n" +
                "    \"data\": {\n" +
                "        \"list\": [],\n" +
                "        \"generalList\": {\"takeout\": {\n" +
                "            \"takeout_add\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"第一个\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"加入购物车页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"查看购物车\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"加入购物车页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"确认下单\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"加入购物车页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]},\n" +
                "            \"takeout_address\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"送到目的地\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"送货地址页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"送到公司\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"送货地址页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"送到家里\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"送货地址页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"新增地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"送货地址页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"添加地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"送货地址页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]},\n" +
                "            \"takeout_cake\": {\"hints\": [{\n" +
                "                \"hint\": \"第一个\",\n" +
                "                \"is_online_support\": \"1\",\n" +
                "                \"is_offline_support\": \"0\",\n" +
                "                \"bot_desc\": \"蛋糕商家列表页\",\n" +
                "                \"extend_json\": null,\n" +
                "                \"extend_json_decoded\": null\n" +
                "            }]},\n" +
                "            \"takeout_detail\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"第一个\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺详情页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"查看购物车\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺详情页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"确认下单\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺详情页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]},\n" +
                "            \"takeout_edit\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"删除地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"编辑收货地址页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"删掉收货地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"编辑收货地址页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]},\n" +
                "            \"takeout_flower\": {\"hints\": [{\n" +
                "                \"hint\": \"第一个\",\n" +
                "                \"is_online_support\": \"1\",\n" +
                "                \"is_offline_support\": \"0\",\n" +
                "                \"bot_desc\": \"鲜花商家列表页\",\n" +
                "                \"extend_json\": null,\n" +
                "                \"extend_json_decoded\": null\n" +
                "            }]},\n" +
                "            \"takeout_food\": {\"hints\": [{\n" +
                "                \"hint\": \"第一个\",\n" +
                "                \"is_online_support\": \"1\",\n" +
                "                \"is_offline_support\": \"0\",\n" +
                "                \"bot_desc\": \"包子粥店\",\n" +
                "                \"extend_json\": null,\n" +
                "                \"extend_json_decoded\": null\n" +
                "            }]},\n" +
                "            \"takeout_list\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"第一个\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"美食分类\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"鲜花预定\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"蛋糕预定\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"外卖订单\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"选择订单\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"查看订单\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"打开订单\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"修改地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"更换地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"更改地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"店铺列表页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]},\n" +
                "            \"takeout_null\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"新增地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"我的收货地址为空\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"添加地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"我的收货地址为空\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]},\n" +
                "            \"takeout_orderlist\": {\"hints\": [{\n" +
                "                \"hint\": \"我的外卖到哪了\",\n" +
                "                \"is_online_support\": \"1\",\n" +
                "                \"is_offline_support\": \"0\",\n" +
                "                \"bot_desc\": \"\",\n" +
                "                \"extend_json\": null,\n" +
                "                \"extend_json_decoded\": null\n" +
                "            }]},\n" +
                "            \"takeout_out\": {\"hints\": [{\n" +
                "                \"hint\": \"第一个\",\n" +
                "                \"is_online_support\": \"1\",\n" +
                "                \"is_offline_support\": \"0\",\n" +
                "                \"bot_desc\": \"搜索结果页\",\n" +
                "                \"extend_json\": null,\n" +
                "                \"extend_json_decoded\": null\n" +
                "            }]},\n" +
                "            \"takeout_pay\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"订单详情\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"修改配送地址弹框\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"查看详情\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"修改配送地址弹框\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]},\n" +
                "            \"takeout_search\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"清空历史记录\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"删除历史记录\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]},\n" +
                "            \"takeout_shopping\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"收起购物车\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"查看购物车页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"清空购物车\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"查看购物车页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"确认下单\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"查看购物车页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]},\n" +
                "            \"takeout_submit\": {\"hints\": [\n" +
                "                {\n" +
                "                    \"hint\": \"去支付\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"提交订单页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"取消支付\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"提交订单页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"取消订单\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"提交订单页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"修改时间\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"提交订单页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"换个时间\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"提交订单页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"修改地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"提交订单页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"hint\": \"换个地址\",\n" +
                "                    \"is_online_support\": \"1\",\n" +
                "                    \"is_offline_support\": \"0\",\n" +
                "                    \"bot_desc\": \"提交订单页\",\n" +
                "                    \"extend_json\": null,\n" +
                "                    \"extend_json_decoded\": null\n" +
                "                }\n" +
                "            ]}\n" +
                "        }},\n" +
                "        \"tag\": \"ee4026aba5ef0b637fa8abe9e0c21290\",\n" +
                "        \"last_update_time\": 1557800032\n" +
                "    },\n" +
                "    \"timecost\": 9\n" +
                "}",GuideBean.class);

        mWaimaiBean = test1.getData().getGeneralList().getTakeout();

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

//    public GuidingBean.ListBean.WaimaiBean getWaimaiBean() {
//	    if (mWaimaiBean == null)
//	        return new GuidingBean.ListBean.WaimaiBean();
//        return mWaimaiBean;
//    }
//
//    public void setmWaimaiBean(GuidingBean.ListBean.WaimaiBean mWaimaiBean) {
//        this.mWaimaiBean = mWaimaiBean;
//    }

    public GuideBean.DataBean.GeneralListBean.TakeoutBean getWaimaiBean(){
        if (mWaimaiBean == null)
            return new GuideBean.DataBean.GeneralListBean.TakeoutBean();
        return mWaimaiBean;
    }

    public void setmWaimaiBean(GuideBean.DataBean.GeneralListBean.TakeoutBean mWaimaiBean){
        this.mWaimaiBean = mWaimaiBean;
    }

    public static WaiMaiApplication getInstance() {
		return mInstance;
	}

    public HashMap<String, ArrayList<Integer>> getTipsMap() {
        return tipsMap;
    }

    public void setTipsMap(HashMap<String, ArrayList<Integer>> tipsMap) {
        this.tipsMap = tipsMap;
    }
}