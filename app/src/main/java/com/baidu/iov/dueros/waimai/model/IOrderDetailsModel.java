package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;

public interface IOrderDetailsModel extends IModel {
    void requestOrderDetails(OrderDetailsReq orderDetailsReq, final RequestCallback callback);

    void requestOrderCancel(OrderCancelReq orderCancelReq, final RequestCallback callback);

}
