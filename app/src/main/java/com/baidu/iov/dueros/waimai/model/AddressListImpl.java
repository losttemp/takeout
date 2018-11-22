package com.baidu.iov.dueros.waimai.model;


import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

public class AddressListImpl implements IAddressList {

    private static final String TAG = AddressListImpl.class.getSimpleName();

    @Override
    public void requestAddressList(final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        AddressListReqBean addressListReqBean = new AddressListReqBean();

        ApiUtils.getAddressList(addressListReqBean, new ApiCallBack<AddressListBean>() {
            @Override
            public void onSuccess(AddressListBean data) {
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
