package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.LatLongRequestBase;

public class FilterConditionReq extends LatLongRequestBase {
	
	private Integer navigate_type;
	private Integer first_category_type;
	private Integer second_category_type;
	
	public Integer getNavigate_type() {
		return navigate_type;
	}

	public void setNavigate_type(Integer navigate_type) {
		this.navigate_type = navigate_type;
	}

	public Integer getFirst_category_type() {
		return first_category_type;
	}

	public void setFirst_category_type(Integer first_category_type) {
		this.first_category_type = first_category_type;
	}

	public Integer getSecond_category_type() {
		return second_category_type;
	}

	public void setSecond_category_type(Integer second_category_type) {
		this.second_category_type = second_category_type;
	}

	@Override
	public String toString() {
		return "FilterConditionReq{" +
				"navigate_type=" + navigate_type +
				", first_category_type=" + first_category_type +
				", second_category_type=" + second_category_type +
				'}';
	}
}
