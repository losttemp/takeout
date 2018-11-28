package com.baidu.iov.dueros.waimai.net.entity.response;

/**
 * Created by ubuntu on 18-11-28.
 */

public class AddressDeleteBean {

    private int errno;
    private String err_msg;
    private DataBean data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

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
        }

        public static class IovBean {

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
}
