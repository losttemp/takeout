package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.SearchSuggestReq;

public interface ISearchModel extends IModel {
	void requestSuggestList(SearchSuggestReq searchSuggestReq, final RequestCallback callback);

}
