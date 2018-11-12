package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class OrderPreviewBean {

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
            private String msg;
            private WmOrderingPreviewOrderVoBean wm_ordering_preview_order_vo;
            private String discountWarnTip;
            private String token;
            private List<WmOrderingPreviewDetailVoListBean> wm_ordering_preview_detail_vo_list;
            private List<WmOrderingUnavaliableFoodVoListBean> wm_ordering_unavaliable_food_vo_list;
            private List<MinCountFoodlistBean> min_count_foodlist;
            private List<DiscountsBean> discounts;

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

            public WmOrderingPreviewOrderVoBean getWm_ordering_preview_order_vo() {
                return wm_ordering_preview_order_vo;
            }

            public void setWm_ordering_preview_order_vo(WmOrderingPreviewOrderVoBean wm_ordering_preview_order_vo) {
                this.wm_ordering_preview_order_vo = wm_ordering_preview_order_vo;
            }

            public String getDiscountWarnTip() {
                return discountWarnTip;
            }

            public void setDiscountWarnTip(String discountWarnTip) {
                this.discountWarnTip = discountWarnTip;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public List<WmOrderingPreviewDetailVoListBean> getWm_ordering_preview_detail_vo_list() {
                return wm_ordering_preview_detail_vo_list;
            }

            public void setWm_ordering_preview_detail_vo_list(List<WmOrderingPreviewDetailVoListBean> wm_ordering_preview_detail_vo_list) {
                this.wm_ordering_preview_detail_vo_list = wm_ordering_preview_detail_vo_list;
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

            public List<DiscountsBean> getDiscounts() {
                return discounts;
            }

            public void setDiscounts(List<DiscountsBean> discounts) {
                this.discounts = discounts;
            }

            public static class WmOrderingPreviewOrderVoBean {

                private String recipient_address;
                private String recipient_name;
                private String recipient_phone;
                private double shipping_fee;
                private int estimate_arrival_time;
                private String caution;
                private String invoice_title;
                private String invoice_taxpayer_id;
                private int wm_order_pay_type;
                private long wm_poi_id;
                private String poi_name;
                private double wm_poi_min_fee;
                private double total;
                private double original_price;
                private double box_total_price;
                private String user_phone;
                private int is_pre_order;
                private int first_order;
                private int delivery_type;

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

                public String getRecipient_phone() {
                    return recipient_phone;
                }

                public void setRecipient_phone(String recipient_phone) {
                    this.recipient_phone = recipient_phone;
                }

                public double getShipping_fee() {
                    return shipping_fee;
                }

                public void setShipping_fee(double shipping_fee) {
                    this.shipping_fee = shipping_fee;
                }

                public int getEstimate_arrival_time() {
                    return estimate_arrival_time;
                }

                public void setEstimate_arrival_time(int estimate_arrival_time) {
                    this.estimate_arrival_time = estimate_arrival_time;
                }

                public String getCaution() {
                    return caution;
                }

                public void setCaution(String caution) {
                    this.caution = caution;
                }

                public String getInvoice_title() {
                    return invoice_title;
                }

                public void setInvoice_title(String invoice_title) {
                    this.invoice_title = invoice_title;
                }

                public String getInvoice_taxpayer_id() {
                    return invoice_taxpayer_id;
                }

                public void setInvoice_taxpayer_id(String invoice_taxpayer_id) {
                    this.invoice_taxpayer_id = invoice_taxpayer_id;
                }

                public int getWm_order_pay_type() {
                    return wm_order_pay_type;
                }

                public void setWm_order_pay_type(int wm_order_pay_type) {
                    this.wm_order_pay_type = wm_order_pay_type;
                }

                public long getWm_poi_id() {
                    return wm_poi_id;
                }

                public void setWm_poi_id(long wm_poi_id) {
                    this.wm_poi_id = wm_poi_id;
                }

                public String getPoi_name() {
                    return poi_name;
                }

                public void setPoi_name(String poi_name) {
                    this.poi_name = poi_name;
                }

                public double getWm_poi_min_fee() {
                    return wm_poi_min_fee;
                }

                public void setWm_poi_min_fee(double wm_poi_min_fee) {
                    this.wm_poi_min_fee = wm_poi_min_fee;
                }

                public double getTotal() {
                    return total;
                }

                public void setTotal(int total) {
                    this.total = total;
                }

                public double getOriginal_price() {
                    return original_price;
                }

                public void setOriginal_price(int original_price) {
                    this.original_price = original_price;
                }

                public double getBox_total_price() {
                    return box_total_price;
                }

                public void setBox_total_price(int box_total_price) {
                    this.box_total_price = box_total_price;
                }

                public String getUser_phone() {
                    return user_phone;
                }

                public void setUser_phone(String user_phone) {
                    this.user_phone = user_phone;
                }

                public int getIs_pre_order() {
                    return is_pre_order;
                }

                public void setIs_pre_order(int is_pre_order) {
                    this.is_pre_order = is_pre_order;
                }

                public int getFirst_order() {
                    return first_order;
                }

                public void setFirst_order(int first_order) {
                    this.first_order = first_order;
                }

                public int getDelivery_type() {
                    return delivery_type;
                }

                public void setDelivery_type(int delivery_type) {
                    this.delivery_type = delivery_type;
                }
            }

            public static class WmOrderingPreviewDetailVoListBean {

                private Long wm_food_sku_id;
                private double food_price;
                private String unit;
                private int count;
                private int box_num;
                private double box_price;
                private String food_name;
                private double origin_food_price;
                private String picture;
                private String spec;
                private Long wm_food_spu_id;
                private List<WmOrderingPreviewFoodSpuAttrListBean> wm_ordering_preview_food_spu_attr_list;

                public Long getWm_food_sku_id() {
                    return wm_food_sku_id;
                }

                public void setWm_food_sku_id(Long wm_food_sku_id) {
                    this.wm_food_sku_id = wm_food_sku_id;
                }

                public double getFood_price() {
                    return food_price;
                }

                public void setFood_price(int food_price) {
                    this.food_price = food_price;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public int getBox_num() {
                    return box_num;
                }

                public void setBox_num(int box_num) {
                    this.box_num = box_num;
                }

                public double getBox_price() {
                    return box_price;
                }

                public void setBox_price(int box_price) {
                    this.box_price = box_price;
                }

                public String getFood_name() {
                    return food_name;
                }

                public void setFood_name(String food_name) {
                    this.food_name = food_name;
                }

                public double getOrigin_food_price() {
                    return origin_food_price;
                }

                public void setOrigin_food_price(int origin_food_price) {
                    this.origin_food_price = origin_food_price;
                }

                public void setFood_price(double food_price) {
                    this.food_price = food_price;
                }

                public void setBox_price(double box_price) {
                    this.box_price = box_price;
                }

                public void setOrigin_food_price(double origin_food_price) {
                    this.origin_food_price = origin_food_price;
                }

                public String getPicture() {
                    return picture;
                }

                public void setPicture(String picture) {
                    this.picture = picture;
                }

                public String getSpec() {
                    return spec;
                }

                public void setSpec(String spec) {
                    this.spec = spec;
                }


                public Long getWm_food_spu_id() {
                    return wm_food_spu_id;
                }

                public void setWm_food_spu_id(Long wm_food_spu_id) {
                    this.wm_food_spu_id = wm_food_spu_id;
                }

                public List<WmOrderingPreviewFoodSpuAttrListBean> getWm_ordering_preview_food_spu_attr_list() {
                    return wm_ordering_preview_food_spu_attr_list;
                }

                public void setWm_ordering_preview_food_spu_attr_list(List<WmOrderingPreviewFoodSpuAttrListBean> wm_ordering_preview_food_spu_attr_list) {
                    this.wm_ordering_preview_food_spu_attr_list = wm_ordering_preview_food_spu_attr_list;
                }

                public static class WmOrderingPreviewFoodSpuAttrListBean {

                    private int id;
                    private int wm_food_spu_id;
                    private int no;
                    private String name;
                    private String value;
                    private int valid;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getWm_food_spu_id() {
                        return wm_food_spu_id;
                    }

                    public void setWm_food_spu_id(int wm_food_spu_id) {
                        this.wm_food_spu_id = wm_food_spu_id;
                    }

                    public int getNo() {
                        return no;
                    }

                    public void setNo(int no) {
                        this.no = no;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                    public int getValid() {
                        return valid;
                    }

                    public void setValid(int valid) {
                        this.valid = valid;
                    }
                }
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

            public static class DiscountsBean {

                private String name;
                private String info;
                private int reduceFree;
                private String icon_url;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public int getReduceFree() {
                    return reduceFree;
                }

                public void setReduceFree(int reduceFree) {
                    this.reduceFree = reduceFree;
                }

                public String getIcon_url() {
                    return icon_url;
                }

                public void setIcon_url(String icon_url) {
                    this.icon_url = icon_url;
                }
            }
        }
    }
}

