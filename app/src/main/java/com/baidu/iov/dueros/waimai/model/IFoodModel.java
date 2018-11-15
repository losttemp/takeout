package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;

/**
 *
 *
 * @author ping
 * @date 2018/10/22
 */
public interface IFoodModel extends IModel {
	void requestFilterConditions(FilterConditionReq filterConditionReq, final RequestCallback callback);

}
