package com.baidu.iov.dueros.waimai.net.entity.request;


import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class GuidingReq extends RequestBase {
    public String app_name;
    public long timestamp;

    public GuidingReq() {
        this.app_name = "takeout";
        long timeStampSec = System.currentTimeMillis() / 1000;
        String timestamp = String.format("%010d", timeStampSec);
        this.timestamp = Long.parseLong(timestamp);
    }
}
