package com.baidu.iov.dueros.waimai.net.entity.response;

public class AddressEditBean {

    private double spend_time;
    private MeituanBean meituan;
    private IovBean iov;

    public double getSpend_time() {
        return spend_time;
    }

    public void setSpend_time(double spend_time) {
        this.spend_time = spend_time;
    }

    public MeituanBean getMeituan() {
        return meituan;
    }

    public void setMeituan(MeituanBean meituan) {
        this.meituan = meituan;
    }

    public IovBean getIov() {
        return iov;
    }

    public void setIov(IovBean iov) {
        this.iov = iov;
    }

    public static class MeituanBean {

        private int code;
        private String msg;
        private Object errorInfo;
        private AddressListBean.IovBean.DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(Object errorInfo) {
            this.errorInfo = errorInfo;
        }

        public AddressListBean.IovBean.DataBean getData() {
            return data;
        }

        public void setData(AddressListBean.IovBean.DataBean data) {
            this.data = data;
        }

    }

    public static class IovBean {
        /**
         * errno : 0
         * errmsg : success
         * data : 1
         * timestamp : 1543368213
         */

        private int errno;
        private String errmsg;
        private int data;
        private int timestamp;

        public int getErrno() {
            return errno;
        }

        public void setErrno(int errno) {
            this.errno = errno;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }
}

