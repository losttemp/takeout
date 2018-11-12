package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

public class SubmitOrderImpl implements ISubmitOrderModel {

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
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }
}
