package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.ArriveTimeReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;

/**
 * Created by ubuntu on 18-10-18.
 */

public interface IPoifoodListModel extends IModel {
    void requestPoifoodList(ArrayMap<String, String> params, final RequestCallback callback);

    void requestPoidetailinfo(ArrayMap<String, String> params, final RequestCallback callback);

    void requestOrderPreview(OrderPreviewReqBean orderPreviewReqBean, final RequestCallback<OrderPreviewBean> callback);

    void requestArriveTimeList(ArriveTimeReqBean arriveTimeReqBean, final RequestCallback<ArriveTimeBean> callback);
}
