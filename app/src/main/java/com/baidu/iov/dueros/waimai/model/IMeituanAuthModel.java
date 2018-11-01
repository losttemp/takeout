package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.AccountCallback;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.MeituanAuthorizeReq;

/**
 * Created by ubuntu on 18-10-27.
 */

public interface IMeituanAuthModel extends IModel {
    void requestAccountInfo(final AccountCallback callback);
    void requestAuthInfo(final AccountCallback callback);
    void requestMeituanAuth(MeituanAuthorizeReq meituanAuthtReq, final RequestCallback callback);

}
