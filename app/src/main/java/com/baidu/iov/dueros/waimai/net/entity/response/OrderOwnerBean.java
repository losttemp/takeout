package com.baidu.iov.dueros.waimai.net.entity.response;

public class OrderOwnerBean {

    private IovBean iov;

    public IovBean getIov() {
        return iov;
    }

    public void setIov(IovBean iov) {
        this.iov = iov;
    }

    public static class IovBean {

        private int enabled;
    }
}
