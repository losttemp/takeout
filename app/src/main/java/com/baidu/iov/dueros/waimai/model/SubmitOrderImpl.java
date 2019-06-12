package com.baidu.iov.dueros.waimai.model;

import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderOwnerReq;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderOwnerBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class SubmitOrderImpl implements ISubmitOrderModel {

    private static final String TAG = SubmitOrderImpl.class.getSimpleName();

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

    @Override
    public void requesLogout(final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.logout(new RequestBase(), new ApiCallBack<LogoutBean>() {
            @Override
            public void onSuccess(LogoutBean data) {
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
                }
            }
        });
    }

    @Override
    public void requestCheckOrderOwner(OrderOwnerReq OrderOwnerReqBean, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getCheckOrderOwner(OrderOwnerReqBean, new ApiCallBack<OrderOwnerBean>() {
            @Override
            public void onSuccess(OrderOwnerBean data) {
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
