package com.baidu.iov.dueros.waimai.bean;

/**
 * Created by ubuntu on 18-11-19.
 */

public class PoifoodSpusTagsBean {
    private String foodSpuTagsBeanName;
    private Integer tag;
    private Integer number;

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer index) {
        this.tag = index;
    }

    public String getFoodSpuTagsBeanName() {
        return foodSpuTagsBeanName;
    }

    public void setFoodSpuTagsBeanName(String foodSpuTagsBeanName) {
        this.foodSpuTagsBeanName = foodSpuTagsBeanName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
