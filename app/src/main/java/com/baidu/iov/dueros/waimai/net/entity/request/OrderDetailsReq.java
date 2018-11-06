package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class OrderDetailsReq extends RequestBase {
    private long order_id;
    private String user_phone;

    public OrderDetailsReq(long id, String phone) {
        this.order_id = id;
        this.user_phone = phone;
    }

    public long getId() {
        return order_id;
    }

    public void setId(long id) {
        this.order_id = id;
    }

    public String getPhone() {
        return user_phone;
    }

    public void setPhone(String phone) {
        this.user_phone = phone;
    }

}