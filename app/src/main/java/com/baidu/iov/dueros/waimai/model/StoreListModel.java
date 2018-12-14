package com.baidu.iov.dueros.waimai.model;

import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.ui.StoreListFragment;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class StoreListModel implements IStoreListModel {
	private static final String TAG = StoreListModel.class.getSimpleName();

	@Override
	public void onReady() {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void requestStoreList(StoreReq storeReq, final RequestCallback callback) {
		Lg.getInstance().d(TAG,"storeReq"+storeReq);
		if (callback == null) {
			return;
		}
		ApiUtils.getStoreList(storeReq, new ApiCallBack<StoreResponse>() {
			@Override
			public void onSuccess(StoreResponse data) {
				callback.onSuccess(data);
			}

			@Override
			public void onFailed(String msg) {
				callback.onFailure(msg);
			}

			@Override
			public void getLogid(String id) {
				if (callback!=null) {
					callback.getLogid(id);
					Log.d(TAG, "getLogid: "+id);
				}
			}
		});

	}

	@Override
	public void requestFilterList(FilterConditionReq filterConditionReq, final RequestCallback
			callback) {

		ApiUtils.getFilterList(filterConditionReq, new ApiCallBack<FilterConditionResponse>() {
			@Override
			public void onSuccess(FilterConditionResponse data) {
				callback.onSuccess(data);
			}

			@Override
			public void onFailed(String msg) {
				callback.onFailure(msg);
			}

			@Override
			public void getLogid(String id) {
				if (callback!=null) {
					callback.getLogid(id);
					Log.d(TAG, "getLogid: "+id);
				}
			}
		});
	}
}
