package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderOwnerReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReq;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;

public interface ISubmitOrderModel extends IModel {

    void requestOrderDetails(OrderDetailsReq orderDetailsReq, final RequestCallback callback);

    void requesLogout(RequestCallback<LogoutBean> requestCallback);

    void requestCheckOrderOwner(OrderOwnerReq orderOwnerReqBean, final RequestCallback callback);
}
