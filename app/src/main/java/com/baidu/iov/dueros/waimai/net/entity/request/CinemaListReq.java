package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

/**
 * @author pengqm
 * @name film
 * @class name：com.baidu.iov.dueros.film.net.entity.request
 * @time 2018/10/12 10:36
 * @change
 * @class describe
 */

public class CinemaListReq extends RequestBase {
    private int  pageNum;
    private int pageSize;
    private int areaId;
    private int aoiId;
    private int brandId;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getAoiId() {
        return aoiId;
    }

    public void setAoiId(int aoiId) {
        this.aoiId = aoiId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return "CinemaListReq{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", areaId=" + areaId +
                ", aoiId=" + aoiId +
                ", brandId=" + brandId +
                '}';
    }
}
