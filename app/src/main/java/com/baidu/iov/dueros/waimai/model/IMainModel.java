package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;

public interface IMainModel extends IModel {
    void requestCityList(ArrayMap<String, String> params, final RequestCallback callback);
    void requestCinemaList(ArrayMap<String, String> params, final RequestCallback callback);
}
