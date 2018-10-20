package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

public class HomeModel implements IHomeModel {

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

		storeReq.setLongitude(95369826);
		storeReq.setLatitude(29735952);

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
}
