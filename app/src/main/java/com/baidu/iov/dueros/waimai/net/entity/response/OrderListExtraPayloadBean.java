package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

/**
 * Created by ubuntu on 18-11-15.
 */

public class OrderListExtraPayloadBean {

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
        private int estimate_arrival_time;
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

        public int getEstimate_arrival_time() {
            return estimate_arrival_time;
        }

        public void setEstimate_arrival_time(int estimate_arrival_time) {
            this.estimate_arrival_time = estimate_arrival_time;
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
