package com.baidu.iov.dueros.waimai.model;


import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;

import java.util.List;

public class AddressEditModel implements IAddressEditModel {

    @Override
    public void requestAdressList(AddressEditReq addressEditReq, RequestCallback callback) {
        if (callback == null) {
            return;
        }

        addressEditReq.setUser_name("Stanford Zhang");
        addressEditReq.setSex(1);
        addressEditReq.setUser_name("17638916218");
        addressEditReq.setAddress("WuHan JiangXia");
        addressEditReq.setHouse("18");
        addressEditReq.setLatitude(Constant.LATITUDE);
        addressEditReq.setLongitude(Constant.LONGITUDE);
        addressEditReq.setType("家");

        Log.d("hhr", addressEditReq.toString());
        ApiUtils.addAddress(addressEditReq, new ApiCallBack<AddressEditBean>() {
            @Override
            public void onSuccess(AddressEditBean data) {
                Log.d("hhr", "AddressEditBean" + data.toString());
            }

            @Override
            public void onFailed(String msg) {
                Log.d("hhr", "Address erro:--" + msg);
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
