package com.baidu.iov.dueros.waimai.model;


import android.util.ArrayMap;
import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

public class AddressEditModel implements IAddressEditModel {

    @Override
    public void requestAdressList(ArrayMap<String, String> params, RequestCallback callback) {
        if (callback == null) {
            return;
        }
        AddressEditReq addressEditReq = new AddressEditReq();
        addressEditReq.setAddress("WuHan JiangXia");
        addressEditReq.setGender(1);
        addressEditReq.setLatitude(29735952);
        addressEditReq.setLongitude(95369826);
        addressEditReq.setName("Stanford Zhang");
        addressEditReq.setPhone("13896524362");
        ApiUtils.addAddress(addressEditReq, new ApiCallBack<AddressEditBean>() {
            @Override
            public void onSuccess(AddressEditBean data) {
                Log.d("hhr","AddressEditBean"+data.toString());
            }

            @Override
            public void onFailed(String msg) {
                Log.d("hhr","Ade erro"+msg);
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
