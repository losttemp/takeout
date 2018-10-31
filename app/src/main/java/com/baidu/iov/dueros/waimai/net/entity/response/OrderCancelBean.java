package com.baidu.iov.dueros.waimai.net.entity.response;

public class OrderCancelBean {


    /**
     * errno : 0
     * err_msg : success
     * data : {"meituan":{"code":1,"errorInfo":{"description":"","failCode":"11005","name":"无权访问该接口"},"msg":"无权访问该接口"},"iov":{}}
     * logid : 0845682534
     * timestamp : 1539778445
     */

    private int errno;
    private String err_msg;
    private DataBean data;
    private String logid;
    private int timestamp;

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

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataBean {
        /**
         * meituan : {"code":1,"errorInfo":{"description":"","failCode":"11005","name":"无权访问该接口"},"msg":"无权访问该接口"}
         * iov : {}
         */

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
            /**
             * code : 1
             * errorInfo : {"description":"","failCode":"11005","name":"无权访问该接口"}
             * msg : 无权访问该接口
             */

            private int code;
            private ErrorInfoBean errorInfo;
            private String msg;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public ErrorInfoBean getErrorInfo() {
                return errorInfo;
            }

            public void setErrorInfo(ErrorInfoBean errorInfo) {
                this.errorInfo = errorInfo;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public static class ErrorInfoBean {
            }
        }

        public static class IovBean {
        }
    }
}
