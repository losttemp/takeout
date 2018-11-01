package com.baidu.iov.dueros.waimai.model;


import android.util.ArrayMap;
import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

public class AddressSelectModel implements IAddressSelectModel {

    @Override
    public void requestAdressList(AddressListReqBean reqBean, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getAddressList(reqBean, new ApiCallBack<AddressListBean>() {
            @Override
            public void onSuccess(AddressListBean data) {
                Log.d("hhr", "AddressListBean" + data.toString());
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
                Log.d("hhr", "Ade erro" + msg);
            }
        });
    }

    @Override
    public void onReady() {
    }

    @Override
    public void onDestroy() {
    }

}
