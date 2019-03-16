package com.baidu.iov.dueros.waimai.model;


import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.ArriveTimeReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReq;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;

public interface ISubmitInfoModel extends IModel {

    void requestArriveTimeList(ArriveTimeReqBean arriveTimeReqBean, final RequestCallback<ArriveTimeBean> callback);

    void requestOrderPreview(OrderPreviewReqBean orderPreviewReqBean, final RequestCallback<OrderPreviewBean> callback);

    void requestOrderSubmitData(OrderSubmitReq orderSubmitReq, final RequestCallback<OrderSubmitBean> callback);

    /**
     * 此处增加了从网上获取地址列表的逻辑, 主要获取当前默认地址是否在配送范围内
     */
    void requestAddressList(long wm_poi_id, final RequestCallback callback);
}
