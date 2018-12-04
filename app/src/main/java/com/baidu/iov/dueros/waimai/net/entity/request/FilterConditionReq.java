package com.baidu.iov.dueros.waimai.net.entity.request;
import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;
public class FilterConditionReq extends RequestBase {
	private Integer latitude;
	private Integer longitude;
	
	private Integer navigate_type;
	private Integer first_category_type;
	private Integer second_category_type;

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

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
				"latitude=" + latitude +
				", longitude=" + longitude +
				", navigate_type=" + navigate_type +
				", first_category_type=" + first_category_type +
				", second_category_type=" + second_category_type +
				'}';
	}
}
