package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;

public interface ISearchModel extends IModel {
	void requestSuggestList(String query, final RequestCallback callback);

}
