package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;

public interface IOrderListModel extends IModel {
    void requestOrderList(OrderListReq orderlistReq, final RequestCallback callback);

}
