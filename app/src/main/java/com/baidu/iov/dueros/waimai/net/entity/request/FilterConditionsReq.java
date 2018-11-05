package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.LatLongRequestBase;
import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class FilterConditionsReq extends LatLongRequestBase {
    
    private Integer navigateType;

    private Integer categoryType;

    private Integer secondCategoryType;
    
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
        return "FilterConditionsReq{" +
                "navigateType=" + navigateType +
                ", categoryType=" + categoryType +
                ", secondCategoryType=" + secondCategoryType +
                '}';
    }
}
