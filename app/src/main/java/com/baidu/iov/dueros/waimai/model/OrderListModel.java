package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.AccountCallback;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.utils.AccountManager;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;

import android.os.Handler;
import android.os.Looper;
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
                    Lg.getInstance().d(TAG, "requestOrderList getLogid: "+id);
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
                    Lg.getInstance().d(TAG, "requestOrderCancel getLogid: "+id);
                }
            }
        });
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void requestAuthInfo(final AccountCallback callback) {
        AccountManager.getInstance().getAuthInfo(new AccountManager.AccountCallBack() {
            @Override
            public void onAccountSuccess(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(msg);
                    }
                });

            }

            @Override
            public void onAccountFailed(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(msg);
                    }
                });

            }
        });
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

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                }
            }
        });
    }
}
