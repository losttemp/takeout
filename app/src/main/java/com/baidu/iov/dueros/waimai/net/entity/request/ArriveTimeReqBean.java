package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class ArriveTimeReqBean extends RequestBase {

    private int longitude;
    private int latitude;
    private long wm_poi_id;

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public long getWm_poi_id() {
        return wm_poi_id;
    }

    public void setWm_poi_id(long wm_poi_id) {
        this.wm_poi_id = wm_poi_id;
    }
}
