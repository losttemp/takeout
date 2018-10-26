package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;

public interface IHomeModel extends IModel {
	void requestStoreList(StoreReq storeReq, final RequestCallback callback);

	void requestFilterList(FilterConditionReq filterConditionReq, final RequestCallback callback);

}
