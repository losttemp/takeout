package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

/**
 * Created by ubuntu on 18-10-27.
 */

public class MeituanAuthorizeReq extends RequestBase  {
    private String bduss;

    public String getBduss() {
        return bduss;
    }

    public void setBduss(String bduss) {
        this.bduss = bduss;
    }

    @Override
    public String toString() {
        return "MeituanAuthorizeReq{" +
                "bduss=" + bduss +
                '}';
    }
}
