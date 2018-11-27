package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class AddressListReqBean extends RequestBase {

    private Long wmPoiId;

    public Long getWmPoiId() {
        return wmPoiId;
    }

    public void setWmPoiId(Long wmPoiId) {
        this.wmPoiId = wmPoiId;
    }
}
