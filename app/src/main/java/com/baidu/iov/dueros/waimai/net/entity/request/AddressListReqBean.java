package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class AddressListReqBean extends RequestBase {

    private long wmPoiId;

    public long getWmPoiId() {
        return wmPoiId;
    }

    public void setWmPoiId(long wmPoiId) {
        this.wmPoiId = wmPoiId;
    }
}
