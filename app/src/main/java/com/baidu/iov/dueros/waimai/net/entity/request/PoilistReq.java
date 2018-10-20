package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

import java.util.Objects;

/**
 * 获取商家列表所需参数
 *
 * @author ping
 * @date 2018/10/16
 */
public class PoilistReq extends RequestBase {

    /**
     * 用户当前经度
     */
    private int longitude;


    /**
     * 用户当前纬度
     */
    private int latitude;


    /**
     * 关键词
     */
    private String keyword;


    /**
     * 排列方式 0:综合排序 1:月销量从高到低 2:配送速度从快到慢 3:外卖评分从高到低 4:起送价从低到高 5:距离从近到远 6:配送费从低到高 7:人均从低到高 8:人均从高到低
     */
    private int  sortType=0;


    /**
     * 页号 如果不填,默认第一页
     */
    private int pageIndex=1;

    /**
     * 每页数量 不可超过20
     */
    private int  pageSize=20;

    /**
     * 最小均价，不传默认0
     */
    private int minPrice=0;
    /**
     * 最大均价，不传默认999；
     */
    private int maxPrice=999;
    /**
     *均价的code，此值从/common/getFilterConditions 获取筛选栏接口获得；minPrice、maxPrice都不传或者不传priceCode，则不筛选均价
     */
    private String  priceCode;

    /**
     * 逗号分隔int组成的string，除均价外的筛选项
     */
    private String migFilter;

    /**
     * 金刚code
     */
    private int navigateType=0;
    /**
     * 一级品类
     */
    private int categoryType;
    /**
     * 二级品类
     */
    private int secondCategoryType;

    public PoilistReq() {
    }

    public PoilistReq(int longitude, int latitude, String keyword, int sortType, int pageIndex, int pageSize, int minPrice, int maxPrice, String priceCode, String migFilter, int navigateType, int categoryType, int secondCategoryType) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.keyword = keyword;
        this.sortType = sortType;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.priceCode = priceCode;
        this.migFilter = migFilter;
        this.navigateType = navigateType;
        this.categoryType = categoryType;
        this.secondCategoryType = secondCategoryType;
    }

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public String getMigFilter() {
        return migFilter;
    }

    public void setMigFilter(String migFilter) {
        this.migFilter = migFilter;
    }

    public int getNavigateType() {
        return navigateType;
    }

    public void setNavigateType(int navigateType) {
        this.navigateType = navigateType;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public int getSecondCategoryType() {
        return secondCategoryType;
    }

    public void setSecondCategoryType(int secondCategoryType) {
        this.secondCategoryType = secondCategoryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){ return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        PoilistReq that = (PoilistReq) o;
        return longitude == that.longitude &&
                latitude == that.latitude &&
                sortType == that.sortType &&
                pageIndex == that.pageIndex &&
                pageSize == that.pageSize &&
                minPrice == that.minPrice &&
                maxPrice == that.maxPrice &&
                navigateType == that.navigateType &&
                categoryType == that.categoryType &&
                secondCategoryType == that.secondCategoryType &&
                Objects.equals(keyword, that.keyword) &&
                Objects.equals(priceCode, that.priceCode) &&
                Objects.equals(migFilter, that.migFilter);
    }

    @Override
    public int hashCode() {

        return Objects.hash(longitude, latitude, keyword, sortType, pageIndex, pageSize, minPrice, maxPrice, priceCode, migFilter, navigateType, categoryType, secondCategoryType);
    }

    @Override
    public String toString() {
        return "PoilistReq{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", keyword='" + keyword + '\'' +
                ", sortType=" + sortType +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", priceCode='" + priceCode + '\'' +
                ", migFilter='" + migFilter + '\'' +
                ", navigateType=" + navigateType +
                ", categoryType=" + categoryType +
                ", secondCategoryType=" + secondCategoryType +
                '}';
    }
}
