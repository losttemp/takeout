package com.baidu.iov.dueros.waimai.model;

import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.ArriveTimeReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReq;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

public class SubmitInfoImpl implements ISubmitInfoModel {

    private static final String TAG = SubmitInfoImpl.class.getSimpleName();

    @Override
    public void requestArriveTimeList(ArriveTimeReqBean arriveTimeReqBean, final RequestCallback<ArriveTimeBean> callback) {

        if (callback == null) {
            return;
        }

        ApiUtils.getArriveTimeList(arriveTimeReqBean, new ApiCallBack<ArriveTimeBean>() {
            @Override
            public void onSuccess(ArriveTimeBean data) {
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
                    Log.d(TAG, "requestArriveTimeList getLogid: "+id);
                }
            }
        });
    }


    @Override
    public void requestOrderPreview(OrderPreviewReqBean orderPreviewReqBean, final RequestCallback<OrderPreviewBean> callback) {
        if (callback == null){
            return;
        }

        ApiUtils.getOrderPreview(orderPreviewReqBean, new ApiCallBack<OrderPreviewBean>(){
            @Override
            public void onSuccess(OrderPreviewBean data) {
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
                    Log.d(TAG, "requestOrderPreview getLogid: "+id);
                }
            }
        });
    }

    @Override
    public void requestOrderSubmitData(OrderSubmitReq orderSubmitReq, final RequestCallback<OrderSubmitBean> callback) {
        if (callback == null) {
            return;
        }


        ApiUtils.getOrderSubmit(orderSubmitReq, new ApiCallBack<OrderSubmitBean>() {
            @Override
            public void onSuccess(OrderSubmitBean data) {
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
                    Log.d(TAG, "requestOrderSubmitData getLogid: "+id);
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
