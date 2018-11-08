package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class SubmitOrderImpl implements ISubmitOrderModel {


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
                callback.onSuccess(data);
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
