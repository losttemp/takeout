package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.LatLongRequestBase;

public class SearchSuggestReq extends LatLongRequestBase {
	private Integer longitude;
	private Integer latitude;
	private String query;

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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return "SearchSuggestReq{" +
				"longitude=" + longitude +
				", latitude=" + latitude +
				", query='" + query + '\'' +
				'}';
	}

}
