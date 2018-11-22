package com.baidu.iov.dueros.waimai.model;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;

public interface IAddressList extends IModel {

    void requestAddressList(final RequestCallback callback);
}
