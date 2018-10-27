package com.baidu.iov.dueros.waimai.model;


import android.util.ArrayMap;
import android.util.Log;

import com.baidu.iov.dueros.waimai.bean.LocationBean;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.LocationManager;

public class AddressSelectModel implements IAddressSelectModel {


    @Override
    public void requestAdressList(ArrayMap<String, String> params, RequestCallback callback) {
        AddressEditReq addressEditReq = new AddressEditReq();
/*        addressEditReq.setAddress("北京市朝阳区");
        addressEditReq.setGender(1);
        addressEditReq.setLatitude(29735952);
        addressEditReq.setLongitude(95369826);
        addressEditReq.setName("张山");
        addressEditReq.setPhone("13896524362");*/
        ApiUtils.getAddressList(addressEditReq, new ApiCallBack<AddressListBean>() {
            @Override
            public void onSuccess(AddressListBean data) {
                Log.d("hhr","AddressListBean"+data.toString());
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
