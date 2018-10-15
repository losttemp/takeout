package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

/**
 * @author pengqm
 * @name film
 * @class name：com.baidu.iov.dueros.film.net.entity.request
 * @time 2018/10/12 13:50
 * @change
 * @class describe
 */

public class CinemaInfoReq extends RequestBase {
    private int cinemaId;

    public int getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    @Override
    public String toString() {
        return "CinemaInfoReq{" +
                "cinemaId=" + cinemaId +
                '}';
    }
}
