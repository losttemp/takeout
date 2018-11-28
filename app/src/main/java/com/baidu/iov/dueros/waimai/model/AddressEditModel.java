package com.baidu.iov.dueros.waimai.model;


import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressAddReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressDeleteReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressAddBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressDeleteBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;

import java.util.List;

public class AddressEditModel implements IAddressEditModel {

    @Override
    public void updateAddressData(AddressEditReq addressEditReq, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.updateAddress(addressEditReq, new ApiCallBack<AddressEditBean>() {
            @Override
            public void onSuccess(AddressEditBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }


    @Override
    public void addAddressData(AddressAddReq addressAddReq, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.addAddress(addressAddReq, new ApiCallBack<AddressAddBean>() {
            @Override
            public void onSuccess(AddressAddBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void deleteAddressData(AddressDeleteReq addressDeleteReq, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.deleteAddress(addressDeleteReq, new ApiCallBack<AddressDeleteBean>() {
            @Override
            public void onSuccess(AddressDeleteBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
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
