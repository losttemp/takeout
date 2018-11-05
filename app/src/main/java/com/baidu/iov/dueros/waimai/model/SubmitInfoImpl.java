package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.ArriveTimeReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReq;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;

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
                Lg.getInstance().d("zhangbing","-----------Order ID " + data.getMeituan().getData().getOrder_id());
                //OrderSubmitBean cinemaInfoData = parseCinemaInfo(data);
                //callback.onSuccess(cinemaInfoData);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
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
                Lg.getInstance().d("zhangbing","--------------token = " +
                        data.getMeituan().getData().getToken());
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
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
