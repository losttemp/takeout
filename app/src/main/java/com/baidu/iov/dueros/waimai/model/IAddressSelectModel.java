package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;

public interface IAddressSelectModel extends IModel {
    void requestAdressList(AddressListReqBean reqBean, final RequestCallback callback);
}
