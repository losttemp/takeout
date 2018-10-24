package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class OrderListReq extends RequestBase {
    private Integer longitude;
    private Integer latitude;

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "StoreReq{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
