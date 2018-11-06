package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class OrderDetailsResponse {

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

            private long order_id;
            private int order_time;
            private int wm_order_pay_type;
            private int pay_status;
            private double total;
            private double original_price;
            private int shipping_fee;
            private int box_total_price;
            private int night_shipping_fee;
            private int status;
            private String remark;
            private int is_pre_order;
            private int has_been_invoiced;
            private String invoice_title;
            private Object invoice_taxpayer_id;
            private int ctime;
            private int utime;
            private int longitude;
            private int latitude;
            private int address_longitude;
            private int address_latitude;
            private int city_id;
            private String user_phone;
            private int user_id;
            private int estimate_arrival_time;
            private String poi_name;
            private long wm_poi_id;
            private String recipient_phone;
            private String recipient_address;
            private String recipient_name;
            private Object courier_name;
            private Object courier_phone;
            private Object courier_ava;
            private int courier_longitude;
            private int courier_latitude;
            private Object logistics_code;
            private int logistics_status;
            private Object channel;
            private int delivery_type;
            private List<FoodListBean> food_list;
            private List<?> discounts;

            public long getOrder_id() {
                return order_id;
            }

            public void setOrder_id(long order_id) {
                this.order_id = order_id;
            }

            public int getOrder_time() {
                return order_time;
            }

            public void setOrder_time(int order_time) {
                this.order_time = order_time;
            }

            public int getWm_order_pay_type() {
                return wm_order_pay_type;
            }

            public void setWm_order_pay_type(int wm_order_pay_type) {
                this.wm_order_pay_type = wm_order_pay_type;
            }

            public int getPay_status() {
                return pay_status;
            }

            public void setPay_status(int pay_status) {
                this.pay_status = pay_status;
            }

            public double getTotal() {
                return total;
            }

            public void setTotal(double total) {
                this.total = total;
            }

            public double getOriginal_price() {
                return original_price;
            }

            public void setOriginal_price(double original_price) {
                this.original_price = original_price;
            }

            public int getShipping_fee() {
                return shipping_fee;
            }

            public void setShipping_fee(int shipping_fee) {
                this.shipping_fee = shipping_fee;
            }

            public int getBox_total_price() {
                return box_total_price;
            }

            public void setBox_total_price(int box_total_price) {
                this.box_total_price = box_total_price;
            }

            public int getNight_shipping_fee() {
                return night_shipping_fee;
            }

            public void setNight_shipping_fee(int night_shipping_fee) {
                this.night_shipping_fee = night_shipping_fee;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getIs_pre_order() {
                return is_pre_order;
            }

            public void setIs_pre_order(int is_pre_order) {
                this.is_pre_order = is_pre_order;
            }

            public int getHas_been_invoiced() {
                return has_been_invoiced;
            }

            public void setHas_been_invoiced(int has_been_invoiced) {
                this.has_been_invoiced = has_been_invoiced;
            }

            public String getInvoice_title() {
                return invoice_title;
            }

            public void setInvoice_title(String invoice_title) {
                this.invoice_title = invoice_title;
            }

            public Object getInvoice_taxpayer_id() {
                return invoice_taxpayer_id;
            }

            public void setInvoice_taxpayer_id(Object invoice_taxpayer_id) {
                this.invoice_taxpayer_id = invoice_taxpayer_id;
            }

            public int getCtime() {
                return ctime;
            }

            public void setCtime(int ctime) {
                this.ctime = ctime;
            }

            public int getUtime() {
                return utime;
            }

            public void setUtime(int utime) {
                this.utime = utime;
            }

            public int getLongitude() {
                return longitude;
            }

            public void setLongitude(int longitude) {
                this.longitude = longitude;
            }

            public int getLatitude() {
                return latitude;
            }

            public void setLatitude(int latitude) {
                this.latitude = latitude;
            }

            public int getAddress_longitude() {
                return address_longitude;
            }

            public void setAddress_longitude(int address_longitude) {
                this.address_longitude = address_longitude;
            }

            public int getAddress_latitude() {
                return address_latitude;
            }

            public void setAddress_latitude(int address_latitude) {
                this.address_latitude = address_latitude;
            }

            public int getCity_id() {
                return city_id;
            }

            public void setCity_id(int city_id) {
                this.city_id = city_id;
            }

            public String getUser_phone() {
                return user_phone;
            }

            public void setUser_phone(String user_phone) {
                this.user_phone = user_phone;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getEstimate_arrival_time() {
                return estimate_arrival_time;
            }

            public void setEstimate_arrival_time(int estimate_arrival_time) {
                this.estimate_arrival_time = estimate_arrival_time;
            }

            public String getPoi_name() {
                return poi_name;
            }

            public void setPoi_name(String poi_name) {
                this.poi_name = poi_name;
            }

            public long getWm_poi_id() {
                return wm_poi_id;
            }

            public void setWm_poi_id(long wm_poi_id) {
                this.wm_poi_id = wm_poi_id;
            }

            public String getRecipient_phone() {
                return recipient_phone;
            }

            public void setRecipient_phone(String recipient_phone) {
                this.recipient_phone = recipient_phone;
            }

            public String getRecipient_address() {
                return recipient_address;
            }

            public void setRecipient_address(String recipient_address) {
                this.recipient_address = recipient_address;
            }

            public String getRecipient_name() {
                return recipient_name;
            }

            public void setRecipient_name(String recipient_name) {
                this.recipient_name = recipient_name;
            }

            public Object getCourier_name() {
                return courier_name;
            }

            public void setCourier_name(Object courier_name) {
                this.courier_name = courier_name;
            }

            public Object getCourier_phone() {
                return courier_phone;
            }

            public void setCourier_phone(Object courier_phone) {
                this.courier_phone = courier_phone;
            }

            public Object getCourier_ava() {
                return courier_ava;
            }

            public void setCourier_ava(Object courier_ava) {
                this.courier_ava = courier_ava;
            }

            public int getCourier_longitude() {
                return courier_longitude;
            }

            public void setCourier_longitude(int courier_longitude) {
                this.courier_longitude = courier_longitude;
            }

            public int getCourier_latitude() {
                return courier_latitude;
            }

            public void setCourier_latitude(int courier_latitude) {
                this.courier_latitude = courier_latitude;
            }

            public Object getLogistics_code() {
                return logistics_code;
            }

            public void setLogistics_code(Object logistics_code) {
                this.logistics_code = logistics_code;
            }

            public int getLogistics_status() {
                return logistics_status;
            }

            public void setLogistics_status(int logistics_status) {
                this.logistics_status = logistics_status;
            }

            public Object getChannel() {
                return channel;
            }

            public void setChannel(Object channel) {
                this.channel = channel;
            }

            public int getDelivery_type() {
                return delivery_type;
            }

            public void setDelivery_type(int delivery_type) {
                this.delivery_type = delivery_type;
            }

            public List<FoodListBean> getFood_list() {
                return food_list;
            }

            public void setFood_list(List<FoodListBean> food_list) {
                this.food_list = food_list;
            }

            public List<?> getDiscounts() {
                return discounts;
            }

            public void setDiscounts(List<?> discounts) {
                this.discounts = discounts;
            }

            public static class FoodListBean {
                /**
                 * food_id : 1239556963
                 * spu_id : 1122027842
                 * name : Hdh
                 * price : 0.01
                 * original_price : 0.01
                 * count : 1
                 * spec :
                 * box_num : 1
                 * box_price : 0
                 * attrIds : []
                 * attrValues : []
                 */

                private int food_id;
                private int spu_id;
                private String name;
                private double price;
                private double original_price;
                private int count;
                private String spec;
                private int box_num;
                private int box_price;
                private List<?> attrIds;
                private List<?> attrValues;

                public int getFood_id() {
                    return food_id;
                }

                public void setFood_id(int food_id) {
                    this.food_id = food_id;
                }

                public int getSpu_id() {
                    return spu_id;
                }

                public void setSpu_id(int spu_id) {
                    this.spu_id = spu_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public double getOriginal_price() {
                    return original_price;
                }

                public void setOriginal_price(double original_price) {
                    this.original_price = original_price;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public String getSpec() {
                    return spec;
                }

                public void setSpec(String spec) {
                    this.spec = spec;
                }

                public int getBox_num() {
                    return box_num;
                }

                public void setBox_num(int box_num) {
                    this.box_num = box_num;
                }

                public int getBox_price() {
                    return box_price;
                }

                public void setBox_price(int box_price) {
                    this.box_price = box_price;
                }

                public List<?> getAttrIds() {
                    return attrIds;
                }

                public void setAttrIds(List<?> attrIds) {
                    this.attrIds = attrIds;
                }

                public List<?> getAttrValues() {
                    return attrValues;
                }

                public void setAttrValues(List<?> attrValues) {
                    this.attrValues = attrValues;
                }
            }
        }
    }
}