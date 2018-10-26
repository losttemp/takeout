package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;

/**
 * Created by ubuntu on 18-10-18.
 */

public interface IPoifoodListModel extends IModel {
    void requestPoifoodList(ArrayMap<String, String> params, final RequestCallback callback);
}
