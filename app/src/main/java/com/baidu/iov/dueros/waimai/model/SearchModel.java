package com.baidu.iov.dueros.waimai.model;

import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.SearchSuggestReq;
import com.baidu.iov.dueros.waimai.net.entity.response.SearchSuggestResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class SearchModel implements ISearchModel {

	private static final String TAG = SearchModel.class.getSimpleName();
	@Override
	public void onReady() {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void requestSuggestList(SearchSuggestReq searchSuggestReq, final RequestCallback
			callback) {
		if (callback == null) {
			return;
		}
		
		ApiUtils.getSearchSuggest(searchSuggestReq, new ApiCallBack<SearchSuggestResponse>() {
			@Override
			public void onSuccess(SearchSuggestResponse data) {
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
					Lg.getInstance().d(TAG, "getLogid: "+id);
				}
			}
		});
	}

}
