package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

/**
 * Created by ubuntu on 18-11-15.
 */

public class OrderListExtraPayloadBean {


    private WmOrderingListBean wm_ordering_list;
    private WmOrderingUserBean wm_ordering_user;
    private int pay_source;
    private int address_id;

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

    public int getPay_source() {
        return pay_source;
    }

    public void setPay_source(int pay_source) {
        this.pay_source = pay_source;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public static class WmOrderingListBean {

        private String wm_poi_id;
        private int delivery_time;
        private int pay_type;
        private List<FoodListBean> food_list;

        public String getWm_poi_id() {
            return wm_poi_id;
        }

        public void setWm_poi_id(String wm_poi_id) {
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


        private String user_caution;
        private int user_longitude;
        private int user_latitude;

        public String getUser_caution() {
            return user_caution;
        }

        public void setUser_caution(String user_caution) {
            this.user_caution = user_caution;
        }

        public int getUser_longitude() {
            return user_longitude;
        }

        public void setUser_longitude(int user_longitude) {
            this.user_longitude = user_longitude;
        }

        public int getUser_latitude() {
            return user_latitude;
        }

        public void setUser_latitude(int user_latitude) {
            this.user_latitude = user_latitude;
        }
    }
}
