package com.baidu.iov.dueros.waimai.net.entity.response;

/**
 * Created by ubuntu on 18-11-13.
 */

public class AddressAddBean {


    private MeituanBean meituan;
    private IovBean iov;

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
        /**
         * errno : 0
         * errmsg : success
         * data : {"address_id":18}
         * timestamp : 1542103068
         */

        private int errno;
        private String errmsg;
        private DataBean data;
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

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public static class DataBean {
            /**
             * address_id : 18
             */

            private int address_id;

            public int getAddress_id() {
                return address_id;
            }

            public void setAddress_id(int address_id) {
                this.address_id = address_id;
            }
        }
    }
}

