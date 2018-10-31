package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class PoiarrivetimeBean {
    private int errno;
    private String err_msg;
    private DataBeanX data;
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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
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

    public static class DataBeanX {

        private MeituanBean meituan;

        public MeituanBean getMeituan() {
            return meituan;
        }

        public void setMeituan(MeituanBean meituan) {
            this.meituan = meituan;
        }


        public static class MeituanBean {
            private int code;
            private String msg;
            private Object errorInfo;
            private List<DataBean> data;

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

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                public static class TimelistBean {
                }
            }
        }
    }
}
