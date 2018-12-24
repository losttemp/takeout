package com.baidu.iov.dueros.waimai.model;


import android.nfc.Tag;
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
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.List;

public class AddressEditModel implements IAddressEditModel {

    private static final String TAG = AddressEditModel.class.getSimpleName();

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

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Lg.getInstance().d(TAG, "updateAddressData getLogid: "+id);
                }
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

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Lg.getInstance().d(TAG, "addAddressData getLogid: "+id);
                }
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

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Lg.getInstance().d(TAG, "deleteAddressData getLogid: "+id);
                }
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
