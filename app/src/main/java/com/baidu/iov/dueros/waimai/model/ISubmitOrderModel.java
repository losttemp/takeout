package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;

public interface ISubmitOrderModel extends IModel {

    void requestOrderSubmitData(OrderSubmitReq orderSubmitReq, final RequestCallback<OrderSubmitBean> callback);
}
