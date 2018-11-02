package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;

public interface ISubmitInfoModel extends IModel {

    void requestArriveTimeList(ArrayMap<String, String> params, final RequestCallback<ArriveTimeBean> callback);

    void requestOrderSubmitData(OrderSubmitReqBean orderSubmitReqBean, final RequestCallback<OrderSubmitBean> callback);

    void requestOrderPreview(OrderPreviewReqBean orderPreviewReqBean, final RequestCallback<OrderPreviewBean> callback);

}
