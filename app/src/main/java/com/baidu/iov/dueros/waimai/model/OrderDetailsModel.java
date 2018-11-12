package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
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
        ApiUtils.getOrderDetails(orderDetailsReq, new ApiCallBack<OrderDetailsResponse>() {
            @Override
            public void onSuccess(OrderDetailsResponse data) {
                if (callback!=null) {
                    callback.onSuccess(data);
                }
            }

            @Override
            public void onFailed(String msg) {
                if (callback!=null) {
                    callback.onFailure(msg);
                }
            }
        });

    }

    @Override
    public void requestOrderCancel(OrderCancelReq orderCancelReq, final RequestCallback callback) {
        Lg.getInstance().d("OkHttp","OrderCancelModel");
        ApiUtils.getOrderCancel(orderCancelReq, new ApiCallBack<OrderCancelResponse>() {
            @Override
            public void onSuccess(OrderCancelResponse data) {
                Lg.getInstance().d("OkHttp","OrderCancelModel  Success");
                if (callback!=null) {
                    Lg.getInstance().d("OkHttp","OrderCancelModel  no null + info" + data.getErrorInfo());
                    callback.onSuccess(data);
                }
            }

            @Override
            public void onFailed(String msg) {
                if (callback!=null) {
                    callback.onFailure(msg);
                }
            }
        });
    }
}