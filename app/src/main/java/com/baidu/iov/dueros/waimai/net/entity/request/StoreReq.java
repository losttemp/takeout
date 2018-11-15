package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.LatLongRequestBase;

public class StoreReq extends LatLongRequestBase {
	
	private Integer sortType;
	private Integer page_index;
	private Integer page_size;
	private Integer minPrice;
	private Integer maxPrice;
	private Integer navigateType;
	private Integer categoryType;
	private Integer secondCategoryType;
	private String keyword;
	private String priceCode;
	private String migFilter;
	
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	@Override
	public String toString() {
		return "StoreReq{" +
				"sortType=" + sortType +
				", page_index=" + page_index +
				", page_size=" + page_size +
				", minPrice=" + minPrice +
				", maxPrice=" + maxPrice +
				", navigateType=" + navigateType +
				", categoryType=" + categoryType +
				", secondCategoryType=" + secondCategoryType +
				", keyword='" + keyword + '\'' +
				", priceCode='" + priceCode + '\'' +
				", migFilter='" + migFilter + '\'' +
				'}';
	}
}
