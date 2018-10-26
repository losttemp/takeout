package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.PoilistReq;

/**
 *  
 *
 * @author ping
 * @date 2018/10/17
 */
public interface IBusinessModel extends IModel {
    
     void requestBusinessBean(PoilistReq poilistReq, RequestCallback callback);
     void requestFilterConditions(FilterConditionsReq filterConditionsReq, final RequestCallback callback);
}
