package com.baidu.iov.dueros.waimai.net.entity.response;

public class TestClass {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "testClass{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    private String name;
    private String price;

    public TestClass(String name, String price) {
        this.name = name;
        this.price = price;
    }
}
