package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

/**
 * Created by ubuntu on 18-10-18.
 */

public class PoifoodListReq extends RequestBase {

    private Integer longitude;
    private Integer latitude;
    private long wm_poi_id;

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

    public long getWm_poi_id() {
        return wm_poi_id;
    }

    public void setWm_poi_id(long wm_poi_id) {
        this.wm_poi_id = wm_poi_id;
    }
}
