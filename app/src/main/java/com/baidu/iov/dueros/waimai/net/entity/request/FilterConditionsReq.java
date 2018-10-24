package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class FilterConditionsReq extends RequestBase {

    private Integer longitude;

    private Integer latitude;

    private Integer navigateType;

    private Integer categoryType;

    private Integer secondCategoryType;

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

    public Integer getNavigateType() {
        return navigateType;
    }

    public void setNavigateType(Integer navigateType) {
        this.navigateType = navigateType;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getSecondCategoryType() {
        return secondCategoryType;
    }

    public void setSecondCategoryType(Integer secondCategoryType) {
        this.secondCategoryType = secondCategoryType;
    }

    @Override
    public String toString() {
        return "FilterconditionsReq{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", navigateType=" + navigateType +
                ", categoryType=" + categoryType +
                ", secondCategoryType=" + secondCategoryType +
                '}';
    }
}
