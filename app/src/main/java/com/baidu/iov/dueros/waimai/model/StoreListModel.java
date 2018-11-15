package com.baidu.iov.dueros.waimai.model;

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
		if (callback == null) {
			return;
		}

		storeReq.setLatitude(Constant.LATITUDE);
		storeReq.setLongitude(Constant.LONGITUDE);

		Lg.getInstance().e(TAG,"Constant.LATITUDE:"+Constant.LATITUDE+"  Constant.LONGITUDE"+Constant.LONGITUDE);

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
