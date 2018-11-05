package com.baidu.iov.dueros.waimai.net.entity.request;
import com.baidu.iov.dueros.waimai.net.entity.base.LatLongRequestBase;



/**
 *
 * @author ping
 * @date 2018/10/16
 */
public class PoilistReq extends LatLongRequestBase {
    
    private String keyword;
    
    private Integer  sortType=0;
    
    private Integer page_index;
    
    private Integer  page_size=20;
    
    private Integer minPrice=0;
   
    private Integer maxPrice=999;
    
    private String  priceCode;
    
    private String migFilter;
    
    private Integer navigateType=0;
    
    private Integer categoryType;
   
    private Integer secondCategoryType;

    public PoilistReq() {
    }
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Integer getPage_index() {
        return page_index;
    }

    public void setPage_index(Integer page_index) {
        this.page_index = page_index;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
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
        return "PoilistReq{" +
                ", keyword='" + keyword + '\'' +
                ", sortType=" + sortType +
                ", page_index=" + page_index +
                ", page_size=" + page_size +
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
