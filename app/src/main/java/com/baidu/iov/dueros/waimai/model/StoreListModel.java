package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;

public class StoreListModel implements IStoreListModel {

	@Override
	public void onReady() {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void requestStoreList(StoreReq storeReq, final RequestCallback callback) {
		if (callback == null) {
			return;
		}

		storeReq.setLongitude(Constant.LONGITUDE);
		storeReq.setLatitude(Constant.LATITUDE);

		ApiUtils.getStoreList(storeReq, new ApiCallBack<StoreResponse>() {
			@Override
			public void onSuccess(StoreResponse data) {
				callback.onSuccess(data);
			}

			@Override
			public void onFailed(String msg) {
				callback.onFailure(msg);
			}
		});

	}

	@Override
	public void requestFilterList(FilterConditionReq filterConditionReq, final RequestCallback
			callback) {
		filterConditionReq.setLongitude(Constant.LONGITUDE);
		filterConditionReq.setLatitude(Constant.LATITUDE);

		ApiUtils.getFilterList(filterConditionReq, new ApiCallBack<FilterConditionResponse>() {
			@Override
			public void onSuccess(FilterConditionResponse data) {
				callback.onSuccess(data);
			}

			@Override
			public void onFailed(String msg) {
				callback.onFailure(msg);
			}
		});
	}
}
