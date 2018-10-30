package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

/**
 * Created by ubuntu on 18-10-27.
 */

public class MeituanAuthorizeReq extends RequestBase  {
    private String mBduss;

    public String getBduss() {
        return mBduss;
    }

    public void setBduss(String bduss) {
        this.mBduss = bduss;
    }

    @Override
    public String toString() {
        return "MeituanAuthorizeReq{" +
                "bduss=" + mBduss +
                '}';
    }
}
