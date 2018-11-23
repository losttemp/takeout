package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

/**
 * Created by ubuntu on 18-11-3.
 */

public class OrderListExtraBean {

    private String payload;
    private OrderInfos orderInfos;

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

    public class OrderInfos {

        private String goods_name;
        private int goods_total_price;
        private int transport_price;
        private long out_trade_time;
        private List<Food_list> food_list;
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

        public void setFood_list(List<Food_list> food_list) {
            this.food_list = food_list;
        }

        public List<Food_list> getFood_list() {
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

        public class Food_list {

            private long food_id;
            private long spu_id;
            private String name;
            private double price;
            private double original_price;
            private int count;
            private String spec;
            private int box_num;
            private int box_price;
            private List<String> attrIds;
            private List<String> attrValues;

            public void setFood_id(long food_id) {
                this.food_id = food_id;
            }

            public long getFood_id() {
                return food_id;
            }

            public void setSpu_id(long spu_id) {
                this.spu_id = spu_id;
            }

            public long getSpu_id() {
                return spu_id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getPrice() {
                return price;
            }

            public void setOriginal_price(double original_price) {
                this.original_price = original_price;
            }

            public double getOriginal_price() {
                return original_price;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getCount() {
                return count;
            }

            public void setSpec(String spec) {
                this.spec = spec;
            }

            public String getSpec() {
                return spec;
            }

            public void setBox_num(int box_num) {
                this.box_num = box_num;
            }

            public int getBox_num() {
                return box_num;
            }

            public void setBox_price(int box_price) {
                this.box_price = box_price;
            }

            public int getBox_price() {
                return box_price;
            }

            public void setAttrIds(List<String> attrIds) {
                this.attrIds = attrIds;
            }

            public List<String> getAttrIds() {
                return attrIds;
            }

            public void setAttrValues(List<String> attrValues) {
                this.attrValues = attrValues;
            }

            public List<String> getAttrValues() {
                return attrValues;
            }

        }
    }
}
