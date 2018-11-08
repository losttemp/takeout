package com.baidu.iov.dueros.waimai.model;


import com.baidu.iov.dueros.waimai.interfacedef.AccountCallback;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.MeituanAuthorizeReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.MeituanAuthorizeResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.AccountManager;
import com.baidu.iov.dueros.waimai.utils.Lg;
/**
 * Created by ubuntu on 18-10-27.
 */

public class MeituanAuthModel implements IMeituanAuthModel {
    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestAccountInfo(final AccountCallback callback) {
        AccountManager.getInstance().getAccountInfo(new AccountManager.AccountCallBack() {
            @Override
            public void onAccountSuccess(String msg) {
                callback.onSuccess(msg);
            }

            @Override
            public void onAccountFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void requestAuthInfo(final AccountCallback callback) {
        AccountManager.getInstance().getAuthInfo(new AccountManager.AccountCallBack() {
            @Override
            public void onAccountSuccess(String msg) {
                callback.onSuccess(msg);
            }

            @Override
            public void onAccountFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void requestMeituanAuth(MeituanAuthorizeReq meituanAuthReq, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getMeituanAuth(meituanAuthReq, new ApiCallBack<MeituanAuthorizeResponse>() {
            @Override
            public void onSuccess(MeituanAuthorizeResponse data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });

    }

    @Override
    public void requestAdressList(AddressListReqBean reqBean, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getAddressList(reqBean, new ApiCallBack<AddressListBean>() {
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
}
