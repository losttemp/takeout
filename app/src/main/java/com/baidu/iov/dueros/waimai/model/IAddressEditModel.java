package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;

public interface IAddressEditModel extends IModel {
    void requestAdressList(AddressEditReq addressEditReq, final RequestCallback callback);
}
