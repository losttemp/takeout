package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

/**
 * Created by ubuntu on 18-11-12.
 */

public class AddressDeleteReq extends RequestBase {
    private Long address_id;
    private Long mt_address_id;

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
    }

    public Long getMt_address_id() {
        return mt_address_id;
    }

    public void setMt_address_id(Long mt_address_id) {
        this.mt_address_id = mt_address_id;
    }
}
