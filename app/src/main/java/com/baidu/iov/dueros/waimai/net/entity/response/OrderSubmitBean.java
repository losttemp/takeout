package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class OrderSubmitBean {

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
        private DataBean data;

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

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {

            private int code;
            private long order_id;
            private int min_price;
            private String payUrl;
            private String wx_pay_params;
            private List<WmOrderingUnavaliableFoodVoListBean> wm_ordering_unavaliable_food_vo_list;
            private List<MinCountFoodlistBean> min_count_foodlist;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public long getOrder_id() {
                return order_id;
            }

            public void setOrder_id(long order_id) {
                this.order_id = order_id;
            }

            public int getMin_price() {
                return min_price;
            }

            public void setMin_price(int min_price) {
                this.min_price = min_price;
            }

            public String getPayUrl() {
                return payUrl;
            }

            public void setPayUrl(String payUrl) {
                this.payUrl = payUrl;
            }

            public String getWx_pay_params() {
                return wx_pay_params;
            }

            public void setWx_pay_params(String wx_pay_params) {
                this.wx_pay_params = wx_pay_params;
            }

            public List<WmOrderingUnavaliableFoodVoListBean> getWm_ordering_unavaliable_food_vo_list() {
                return wm_ordering_unavaliable_food_vo_list;
            }

            public void setWm_ordering_unavaliable_food_vo_list(List<WmOrderingUnavaliableFoodVoListBean> wm_ordering_unavaliable_food_vo_list) {
                this.wm_ordering_unavaliable_food_vo_list = wm_ordering_unavaliable_food_vo_list;
            }

            public List<MinCountFoodlistBean> getMin_count_foodlist() {
                return min_count_foodlist;
            }

            public void setMin_count_foodlist(List<MinCountFoodlistBean> min_count_foodlist) {
                this.min_count_foodlist = min_count_foodlist;
            }

            public static class WmOrderingUnavaliableFoodVoListBean {

                private int wm_food_sku_id;
                private String wm_food_name;
                private int stock;
                private int wm_food_spu_id;

                public int getWm_food_sku_id() {
                    return wm_food_sku_id;
                }

                public void setWm_food_sku_id(int wm_food_sku_id) {
                    this.wm_food_sku_id = wm_food_sku_id;
                }

                public String getWm_food_name() {
                    return wm_food_name;
                }

                public void setWm_food_name(String wm_food_name) {
                    this.wm_food_name = wm_food_name;
                }

                public int getStock() {
                    return stock;
                }

                public void setStock(int stock) {
                    this.stock = stock;
                }

                public int getWm_food_spu_id() {
                    return wm_food_spu_id;
                }

                public void setWm_food_spu_id(int wm_food_spu_id) {
                    this.wm_food_spu_id = wm_food_spu_id;
                }
            }

            public static class MinCountFoodlistBean {

                private int wm_food_sku_id;
                private String wm_food_name;
                private int wm_food_spu_id;
                private int cur_count;
                private int min_count;

                public int getWm_food_sku_id() {
                    return wm_food_sku_id;
                }

                public void setWm_food_sku_id(int wm_food_sku_id) {
                    this.wm_food_sku_id = wm_food_sku_id;
                }

                public String getWm_food_name() {
                    return wm_food_name;
                }

                public void setWm_food_name(String wm_food_name) {
                    this.wm_food_name = wm_food_name;
                }

                public int getWm_food_spu_id() {
                    return wm_food_spu_id;
                }

                public void setWm_food_spu_id(int wm_food_spu_id) {
                    this.wm_food_spu_id = wm_food_spu_id;
                }

                public int getCur_count() {
                    return cur_count;
                }

                public void setCur_count(int cur_count) {
                    this.cur_count = cur_count;
                }

                public int getMin_count() {
                    return min_count;
                }

                public void setMin_count(int min_count) {
                    this.min_count = min_count;
                }
            }
        }
    }
}

