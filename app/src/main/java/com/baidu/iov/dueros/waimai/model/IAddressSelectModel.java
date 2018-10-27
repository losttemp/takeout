package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;

public interface IAddressSelectModel extends IModel {
    void requestAdressList(ArrayMap<String, String> params, final RequestCallback callback);
}
