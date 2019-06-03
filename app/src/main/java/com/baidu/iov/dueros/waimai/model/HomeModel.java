package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class HomeModel implements IHomeModel {

	private static final String TAG = HomeModel.class.getSimpleName();

	@Override
	public void onReady() {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void requesLogout(final RequestCallback callback) {
		if (callback == null) {
			return;
		}

		ApiUtils.logout(new RequestBase(), new ApiCallBack<LogoutBean>() {
			@Override
			public void onSuccess(LogoutBean data) {
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
				}
			}
		});
	}
}
