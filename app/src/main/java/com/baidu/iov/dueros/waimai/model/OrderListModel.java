package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

import android.util.Log;

public class OrderListModel implements IOrderListModel {

    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestOrderList(OrderListReq orderlistReq, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getOrderList(orderlistReq, new ApiCallBack<OrderListResponse>() {
            @Override
            public void onSuccess(OrderListResponse data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });

    }
}
