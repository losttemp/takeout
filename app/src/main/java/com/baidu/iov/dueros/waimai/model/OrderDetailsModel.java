package com.baidu.iov.dueros.waimai.model;

import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class OrderDetailsModel implements IOrderDetailsModel {

    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestOrderDetails(OrderDetailsReq orderDetailsReq, final RequestCallback callback) {
        if (callback == null) {
            return;
        }
        ApiUtils.getOrderDetails(orderDetailsReq, new ApiCallBack<OrderDetailsResponse>() {
            @Override
            public void onSuccess(OrderDetailsResponse data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
            }
        });

    }

    @Override
    public void requestOrderCancel(OrderDetailsReq orderDetailsReq, final RequestCallback callback) {
        if (callback == null) {
            return;
        }
        ApiUtils.getOrderCancel(orderDetailsReq, new ApiCallBack<OrderCancelResponse>() {
            @Override
            public void onSuccess(OrderCancelResponse data) {

            }

            @Override
            public void onFailed(String msg) {
            }
        });
    }
}