package com.baidu.iov.dueros.waimai.model;


import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

public class AddressListImpl implements IAddressList {

    private static final String TAG = AddressListImpl.class.getSimpleName();

    @Override
    public void requestAddressList(long wm_poi_id, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        AddressListReqBean addressListReqBean = new AddressListReqBean();
        addressListReqBean.setWmPoiId(wm_poi_id);

        ApiUtils.getAddressList(addressListReqBean, new ApiCallBack<AddressListBean>() {
            @Override
            public void onSuccess(AddressListBean data) {
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
                    Log.d(TAG, "getLogid: "+id);
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
