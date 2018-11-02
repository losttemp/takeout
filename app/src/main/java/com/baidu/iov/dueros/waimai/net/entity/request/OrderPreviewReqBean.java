package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

import java.util.List;

public class OrderPreviewReqBean extends RequestBase {

    private String user_phone;
    private WmOrderingListBean wm_ordering_list;
    private WmOrderingUserBean wm_ordering_user;
    private int address_id;

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public WmOrderingListBean getWm_ordering_list() {
        return wm_ordering_list;
    }

    public void setWm_ordering_list(WmOrderingListBean wm_ordering_list) {
        this.wm_ordering_list = wm_ordering_list;
    }

    public WmOrderingUserBean getWm_ordering_user() {
        return wm_ordering_user;
    }

    public void setWm_ordering_user(WmOrderingUserBean wm_ordering_user) {
        this.wm_ordering_user = wm_ordering_user;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public static class WmOrderingListBean {

        private int wm_poi_id;
        private int delivery_time;
        private int pay_type;
        private List<FoodListBean> food_list;

        public int getWm_poi_id() {
            return wm_poi_id;
        }

        public void setWm_poi_id(int wm_poi_id) {
            this.wm_poi_id = wm_poi_id;
        }

        public int getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(int delivery_time) {
            this.delivery_time = delivery_time;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public List<FoodListBean> getFood_list() {
            return food_list;
        }

        public void setFood_list(List<FoodListBean> food_list) {
            this.food_list = food_list;
        }

        public static class FoodListBean {

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

    public static class WmOrderingUserBean {

        private String user_phone;
        private String user_name;
        private String user_address;
        private String user_caution;
        private String house_number;
        private int addr_latitude;
        private int addr_longitu_longitude;
        private int user_latitude;

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_address() {
            return user_address;
        }

        public void setUser_address(String user_address) {
            this.user_address = user_address;
        }

        public String getUser_caution() {
            return user_caution;
        }

        public void setUser_caution(String user_caution) {
            this.user_caution = user_caution;
        }

        public String getHouse_number() {
            return house_number;
        }

        public void setHouse_number(String house_number) {
            this.house_number = house_number;
        }

        public int getAddr_latitude() {
            return addr_latitude;
        }

        public void setAddr_latitude(int addr_latitude) {
            this.addr_latitude = addr_latitude;
        }

        public int getAddr_longitu_longitude() {
            return addr_longitu_longitude;
        }

        public void setAddr_longitu_longitude(int addr_longitu_longitude) {
            this.addr_longitu_longitude = addr_longitu_longitude;
        }

        public int getUser_latitude() {
            return user_latitude;
        }

        public void setUser_latitude(int user_latitude) {
            this.user_latitude = user_latitude;
        }
    }
}
