package com.baidu.iov.dueros.waimai.net.entity.response;

public class OrderCancelResponse {
    private ErrorInfoBean errorInfo;

    public ErrorInfoBean getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfoBean errorInfo) {
        this.errorInfo = errorInfo;
    }

    public static class ErrorInfoBean {
        /**
         * failCode : 12003
         * name : ,
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

