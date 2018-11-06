package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.SearchSuggestReq;
import com.baidu.iov.dueros.waimai.net.entity.response.SearchSuggestResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;

public class SearchModel implements ISearchModel {

	@Override
	public void onReady() {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void requestSuggestList(String query, final RequestCallback
			callback) {
		if (callback == null) {
			return;
		}

		SearchSuggestReq searchSuggestReq = new SearchSuggestReq();
		searchSuggestReq.setLongitude(Constant.LONGITUDE);
		searchSuggestReq.setLatitude(Constant.LATITUDE);
		searchSuggestReq.setQuery(query);

		ApiUtils.getSearchSuggest(searchSuggestReq, new ApiCallBack<SearchSuggestResponse>() {
			@Override
			public void onSuccess(SearchSuggestResponse data) {
				callback.onSuccess(data);
			}

			@Override
			public void onFailed(String msg) {
				callback.onFailure(msg);
			}
		});
	}

}
