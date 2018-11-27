package com.baidu.iov.dueros.waimai.net.entity.response;

public class OrderCancelResponse {

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

        private int code;
        private String msg;
        private ErrorInfoBean errorInfo;

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

        public static class ErrorInfoBean {

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

