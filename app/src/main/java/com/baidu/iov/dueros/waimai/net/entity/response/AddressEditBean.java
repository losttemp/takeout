package com.baidu.iov.dueros.waimai.net.entity.response;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

public class AddressEditBean extends RequestBase {

    /**
     * meituan : {"code":0,"msg":"调用成功","errorInfo":null,"data":null}
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
         * code : 0
         * msg : 调用成功
         * errorInfo : null
         * data : null
         */

        private int code;
        private String msg;
        private Object errorInfo;
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

        public Object getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(Object errorInfo) {
            this.errorInfo = errorInfo;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "MeituanBean{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", errorInfo=" + errorInfo +
                    ", data=" + data +
                    '}';
        }
    }

    public static class IovBean {
    }

    @Override
    public String toString() {
        return "AddressEditBean{" +
                "meituan=" + meituan +
                ", iov=" + iov +
                '}';
    }
}
