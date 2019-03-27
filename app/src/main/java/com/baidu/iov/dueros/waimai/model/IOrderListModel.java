package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.AccountCallback;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;

public interface IOrderListModel extends IModel {
    void requestOrderList(OrderListReq orderlistReq, final RequestCallback callback);
    void requestOrderCancel(OrderCancelReq orderCancelReq, final RequestCallback callback);
    void requestOrderDetails(OrderDetailsReq orderDetailsReq, final RequestCallback callback);
    void requestAuthInfo(final AccountCallback callback);
}
