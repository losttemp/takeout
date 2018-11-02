package com.baidu.iov.dueros.waimai.net.entity.base;

public class LatLongRequestBase extends RequestBase {

    private Integer latitude;
    private Integer longitude;

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
        return "LatLongRequestBase{" +
                ", av=" + av +
                ", ak='" + ak + '\'' +
                ", cn='" + cn + '\'' +
                ", uuid='" + uuid + '\'' +
                ", sign='" + sign + '\'' +
                ", c='" + c + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

}
