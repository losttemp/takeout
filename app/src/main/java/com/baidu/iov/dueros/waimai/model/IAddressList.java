package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;

public interface IAddressList extends IModel {

    void requestAddressList(long wm_poi_id, final RequestCallback callback);
}
