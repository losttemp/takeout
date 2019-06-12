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

        private int code;

        private IovDataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public IovDataBean getData() {
            return data;
        }

        public void setData(IovDataBean data) {
            this.data = data;
        }

        public static class IovDataBean {

            private int enabled;
            private long systime;

            public int getEnabled() {
                return enabled;
            }

            public void setEnabled(int enabled) {
                this.enabled = enabled;
            }

            public long getSystime() {
                return systime;
            }

            public void setSystime(long systime) {
                this.systime = systime;
            }
        }
    }
}
