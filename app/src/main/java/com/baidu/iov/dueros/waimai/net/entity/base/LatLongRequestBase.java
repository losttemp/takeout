package com.baidu.iov.dueros.waimai.net.entity.base;

import com.baidu.iov.dueros.waimai.utils.Constant;

public class LatLongRequestBase extends RequestBase {

	private Integer latitude;
	private Integer longitude;

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

	public LatLongRequestBase() {
		this.longitude = Constant.LONGITUDE;
		this.latitude = Constant.LATITUDE;
	}

	@Override
	public String toString() {
		return "LatLongRequestBase{" +
				", av=" + av +
				", ak='" + ak + '\'' +
				", cn='" + cn + '\'' +
				", uuid='" + uuid + '\'' +
				", sign='" + sign + '\'' +
				", c='" + c + '\'' +
				", latitude='" + latitude + '\'' +
				", longitude='" + longitude + '\'' +
				'}';
	}

}
