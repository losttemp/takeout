package com.baidu.iov.dueros.waimai.net.entity.response;

/**
 * Created by ubuntu on 18-10-27.
 */

public class MeituanAuthorizeResponse {

    private Meituan meituan;
    private Iov iov;

    public void setMeituan(Meituan meituan) {
        this.meituan = meituan;
    }

    public Meituan getMeituan() {
        return meituan;
    }


    public void setIov(Iov iov) {
        this.iov = iov;
    }

    public Iov getIov() {
        return iov;
    }

    public class Meituan {
    }

    public class Iov {

        private String authorize_url;

        private boolean authorized;

        public void setAuthorizeUrl(String authorize_url) {
            this.authorize_url = authorize_url;
        }

        public String getAuthorizeUrl() {
            return authorize_url;
        }

        public void setAuthorizedState(boolean authorize) {
            this.authorized = authorize;
        }

        public boolean getAuthorizedState() {
            return authorized;
        }

    }
}


