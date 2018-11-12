package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressDeleteReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;

public interface IAddressEditModel extends IModel {
    void updateAddressData(AddressEditReq addressEditReq, final RequestCallback callback);
    void addAddressData(AddressEditReq addressEditReq, final RequestCallback callback);
    void deleteAddressData(AddressDeleteReq addressDeleteReq, final RequestCallback callback);
}
