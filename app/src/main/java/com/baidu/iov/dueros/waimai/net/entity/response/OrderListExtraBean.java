package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

/**
 * Created by ubuntu on 18-11-3.
 */

public class OrderListExtraBean {

    private Payload payload;
    private OrderInfos orderInfos;

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setOrderInfos(OrderInfos orderInfos) {
        this.orderInfos = orderInfos;
    }

    public OrderInfos getOrderInfos() {
        return orderInfos;
    }

    public class Payload {

        private String user_phone;
        private Wm_ordering_list wm_ordering_list;
        private Wm_ordering_user wm_ordering_user;
        private int pay_source;
        private String return_url;

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setWm_ordering_list(Wm_ordering_list wm_ordering_list) {
            this.wm_ordering_list = wm_ordering_list;
        }

        public Wm_ordering_list getWm_ordering_list() {
            return wm_ordering_list;
        }

        public void setWm_ordering_user(Wm_ordering_user wm_ordering_user) {
            this.wm_ordering_user = wm_ordering_user;
        }

        public Wm_ordering_user getWm_ordering_user() {
            return wm_ordering_user;
        }

        public void setPay_source(int pay_source) {
            this.pay_source = pay_source;
        }

        public int getPay_source() {
            return pay_source;
        }

        public void setReturn_url(String return_url) {
            this.return_url = return_url;
        }

        public String getReturn_url() {
            return return_url;
        }

        public class Wm_ordering_list {

            private long wm_poi_id;
            private int delivery_time;
            private int pay_type;
            private List<FoodListBean> food_list;

            public void setWm_poi_id(long wm_poi_id) {
                this.wm_poi_id = wm_poi_id;
            }

            public long getWm_poi_id() {
                return wm_poi_id;
            }

            public void setDelivery_time(int delivery_time) {
                this.delivery_time = delivery_time;
            }

            public int getDelivery_time() {
                return delivery_time;
            }

            public void setPay_type(int pay_type) {
                this.pay_type = pay_type;
            }

            public int getPay_type() {
                return pay_type;
            }

            public void setFood_list(List<FoodListBean> food_list) {
                this.food_list = food_list;
            }

            public List<FoodListBean> getFood_list() {
                return food_list;
            }

            public class FoodListBean {

                private int wm_food_sku_id;
                private int count;
                private List<Integer> food_spu_attr_ids;

                public int getWm_food_sku_id() {
                    return wm_food_sku_id;
                }

                public void setWm_food_sku_id(int wm_food_sku_id) {
                    this.wm_food_sku_id = wm_food_sku_id;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public List<Integer> getFood_spu_attr_ids() {
                    return food_spu_attr_ids;
                }

                public void setFood_spu_attr_ids(List<Integer> food_spu_attr_ids) {
                    this.food_spu_attr_ids = food_spu_attr_ids;
                }
            }
        }

        public class Wm_ordering_user {

            private long address_id;
            private String user_phone;
            private String user_name;
            private String user_address;
            private long addr_longitude;
            private long addr_latitude;

            public void setAddress_id(long address_id) {
                this.address_id = address_id;
            }

            public long getAddress_id() {
                return address_id;
            }

            public void setUser_phone(String user_phone) {
                this.user_phone = user_phone;
            }

            public String getUser_phone() {
                return user_phone;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_address(String user_address) {
                this.user_address = user_address;
            }

            public String getUser_address() {
                return user_address;
            }

            public void setAddr_longitude(long addr_longitude) {
                this.addr_longitude = addr_longitude;
            }

            public long getAddr_longitude() {
                return addr_longitude;
            }

            public void setAddr_latitude(long addr_latitude) {
                this.addr_latitude = addr_latitude;
            }

            public long getAddr_latitude() {
                return addr_latitude;
            }

        }

    }

    public class OrderInfos {

        private String goods_name;
        private int goods_total_price;
        private int transport_price;
        private long out_trade_time;
        private List<Food_list> food_list;
        private String wm_pic_url;

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
