package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

import android.util.Log;

public class OrderListModel implements IOrderListModel {

    private static final String TAG = OrderListModel.class.getSimpleName();
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

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Log.d(TAG, "requestOrderList getLogid: "+id);
                }
            }
        });

    }

    @Override
    public void requestOrderCancel(OrderCancelReq orderCancelReq, final RequestCallback callback) {
        if (callback == null) {
            return;
        }
        ApiUtils.getOrderCancel(orderCancelReq, new ApiCallBack<OrderCancelResponse>() {
            @Override
            public void onSuccess(OrderCancelResponse data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Log.d(TAG, "requestOrderCancel getLogid: "+id);
                }
            }
        });
    }
}
