package com.baidu.iov.dueros.waimai.net.entity.response;

public class OrderpreviewBean {

    /**
     * errno : 0
     * err_msg : success
     * data : {"meituan":{"code":1,"msg":"addressId错误","errorInfo":{"failCode":"11014","name":"addressId错误","description":""},"data":null},"iov":{}}
     * logid : 0506348496
     * timestamp : 1539778107
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
         * meituan : {"code":1,"msg":"addressId错误","errorInfo":{"failCode":"11014","name":"addressId错误","description":""},"data":null}
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
             * msg : addressId错误
             * errorInfo : {"failCode":"11014","name":"addressId错误","description":""}
             * data : null
             */

            private int code;
            private String msg;
            private ErrorInfoBean errorInfo;
            private Object data;

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

            public ErrorInfoBean getErrorInfo() {
                return errorInfo;
            }

            public void setErrorInfo(ErrorInfoBean errorInfo) {
                this.errorInfo = errorInfo;
            }

            public Object getData() {
                return data;
            }

            public void setData(Object data) {
                this.data = data;
            }

            public static class ErrorInfoBean {
                /**
                 * failCode : 11014
                 * name : addressId错误
                 * description :
                 */

                private String failCode;
                private String name;
                private String description;

                public String getFailCode() {
                    return failCode;
                }

                public void setFailCode(String failCode) {
                    this.failCode = failCode;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }
            }
        }

        public static class IovBean {
        }
    }
}
