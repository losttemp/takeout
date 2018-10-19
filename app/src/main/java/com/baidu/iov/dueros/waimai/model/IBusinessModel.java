package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
/**
 *  
 *
 * @author ping
 * @date 2018/10/17
 */
public interface IBusinessModel extends IModel {
    
     void requestBusinessBean(ArrayMap<String, String> params, RequestCallback callback);
}
