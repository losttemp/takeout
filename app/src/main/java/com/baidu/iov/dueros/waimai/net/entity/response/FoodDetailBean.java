package com.baidu.iov.dueros.waimai.net.entity.response;

import java.io.Serializable;
import java.util.List;

public class FoodDetailBean implements Serializable {

    private long food_id;
    private long spu_id;
    private String name;
    private double price;
    private double original_price;
    private int count;
    private String spec;
    private double box_num;
    private double box_price;
    private List<String> attrIds;
    private List<String> attrValues;

    public void setFood_id(long food_id) {
        this.food_id = food_id;
    }

    public long getFood_id() {
        return food_id;
    }

    public void setSpu_id(long spu_id) {
        this.spu_id = spu_id;
    }

    public long getSpu_id() {
        return spu_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setOriginal_price(double original_price) {
        this.original_price = original_price;
    }

    public double getOriginal_price() {
        return original_price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSpec() {
        return spec;
    }

    public void setBox_num(double box_num) {
        this.box_num = box_num;
    }

    public double getBox_num() {
        return box_num;
    }

    public double getBox_price() {
        return box_price;
    }

    public void setBox_price(double box_price) {
        this.box_price = box_price;
    }

    public void setAttrIds(List<String> attrIds) {
        this.attrIds = attrIds;
    }

    public List<String> getAttrIds() {
        return attrIds;
    }

    public void setAttrValues(List<String> attrValues) {
        this.attrValues = attrValues;
    }

    public List<String> getAttrValues() {
        return attrValues;
    }

}
