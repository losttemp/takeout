package com.baidu.iov.dueros.waimai.net.entity.response;


import java.util.List;

public class ArriveTimeBean {


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

            private String date;
            private int status;
            private String info;
            private List<TimelistBean> timelist;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public List<TimelistBean> getTimelist() {
                return timelist;
            }

            public void setTimelist(List<TimelistBean> timelist) {
                this.timelist = timelist;
            }

            public static class TimelistBean {

                private String date_type_tip;
                private String view_time;
                private int unixtime;
                private String view_shipping_fee;

                public String getDate_type_tip() {
                    return date_type_tip;
                }

                public void setDate_type_tip(String date_type_tip) {
                    this.date_type_tip = date_type_tip;
                }

                public String getView_time() {
                    return view_time;
                }

                public void setView_time(String view_time) {
                    this.view_time = view_time;
                }

                public int getUnixtime() {
                    return unixtime;
                }

                public void setUnixtime(int unixtime) {
                    this.unixtime = unixtime;
                }

                public String getView_shipping_fee() {
                    return view_shipping_fee;
                }

                public void setView_shipping_fee(String view_shipping_fee) {
                    this.view_shipping_fee = view_shipping_fee;
                }
            }
        }
    }


}
