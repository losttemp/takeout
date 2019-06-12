package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

/**
 * Created by ubuntu on 18-11-3.
 */

public class OrderListExtraBean {

    private String payload;
    private OrderInfos orderInfos;
    private String order_id;

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setOrderInfos(OrderInfos orderInfos) {
        this.orderInfos = orderInfos;
    }

    public OrderInfos getOrderInfos() {
        return orderInfos;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public class OrderInfos {

        private String goods_name;
        private int goods_total_price;
        private int transport_price;
        private long out_trade_time;
        private List<FoodDetailBean> food_list;
        private String wm_pic_url;
        private String pay_url;

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_total_price(int goods_total_price) {
            this.goods_total_price = goods_total_price;
        }

        public int getGoods_total_price() {
            return goods_total_price;
        }

        public void setTransport_price(int transport_price) {
            this.transport_price = transport_price;
        }

        public int getTransport_price() {
            return transport_price;
        }

        public void setOut_trade_time(long out_trade_time) {
            this.out_trade_time = out_trade_time;
        }

        public long getOut_trade_time() {
            return out_trade_time;
        }

        public void setFood_list(List<FoodDetailBean> food_list) {
            this.food_list = food_list;
        }

        public List<FoodDetailBean> getFood_list() {
            return food_list;
        }

        public void setWm_pic_url(String wm_pic_url) {
            this.wm_pic_url = wm_pic_url;
        }

        public String getWm_pic_url() {
            return wm_pic_url;
        }

        public void setPay_url(String pay_url) {
            this.pay_url = pay_url;
        }

        public String getPay_url() {
            return pay_url;
        }

    }
}
