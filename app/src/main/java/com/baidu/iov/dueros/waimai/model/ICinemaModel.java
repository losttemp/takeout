package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;

public interface ICinemaModel extends IModel {
    void requestCinemaList(ArrayMap<String, String> params, final RequestCallback callback);
}
