package com.baidu.iov.dueros.waimai.net.entity.response;

/**
 * Created by ubuntu on 18-10-25. TODO
 */

public class PoifoodBean {
    /**
     * iov : {"authorize_url":"https://openapi.waimai.meituan.com/oauth/authorize?app_id=8315367514591523&redirect_uri=http://sandbox.codriverapi.baidu.com/iovservice/waimai/oauthredirect&response_type=code&scope=&state=5bd1b75c324cc"}
     */

    private IovBean iov;

    public IovBean getIov() {
        return iov;
    }

    public void setIov(IovBean iov) {
        this.iov = iov;
    }

    public static class IovBean {
        /**
         * authorize_url : https://openapi.waimai.meituan.com/oauth/authorize?app_id=8315367514591523&redirect_uri=http://sandbox.codriverapi.baidu.com/iovservice/waimai/oauthredirect&response_type=code&scope=&state=5bd1b75c324cc
         */

        private String authorize_url;

        public String getAuthorize_url() {
            return authorize_url;
        }

        public void setAuthorize_url(String authorize_url) {
            this.authorize_url = authorize_url;
        }
    }
}
