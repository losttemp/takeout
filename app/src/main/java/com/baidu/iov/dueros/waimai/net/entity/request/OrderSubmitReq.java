package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class OrderSubmitReq extends RequestBase {

    private String wm_pic_url;
    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getWm_pic_url() {
        return wm_pic_url;
    }

    public void setWm_pic_url(String wm_pic_url) {
        this.wm_pic_url = wm_pic_url;
    }
}
