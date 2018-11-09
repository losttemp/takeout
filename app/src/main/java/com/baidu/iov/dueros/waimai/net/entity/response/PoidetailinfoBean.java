package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

/**
 * Created by ubuntu on 18-10-30.
 */

public class PoidetailinfoBean {

    private MeituanBean meituan;
    private IovBean iov;

    public MeituanBean getMeituan() {
        return meituan;
    }

    public void setMeituan(MeituanBean meituan) {
        this.meituan = meituan;
    }

    public IovBean getIov() {
        return iov;
    }

    public void setIov(IovBean iov) {
        this.iov = iov;
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

            private long wm_poi_id;
            private int status;
            private String status_desc;
            private String name;
            private String pic_url;
            private double shipping_fee;
            private double min_price;
            private double wm_poi_score;
            private int avg_delivery_time;
            private String poi_type_icon;
            private String distance;
            private int latitude;
            private int longitude;
            private String address;
            private int month_sale_num;
            private int delivery_type;
            private int invoice_support;
            private int invoice_min_price;
            private String average_price_tip;
            private String call_center;
            private String shipping_time;
            private List<?> product_list;
            private List<DiscountsBean> discounts;
            private List<?> categoryInfoList;
            private List<PoiUserCommentVOListBean> poiUserCommentVOList;

            public long getWm_poi_id() {
                return wm_poi_id;
            }

            public void setWm_poi_id(long wm_poi_id) {
                this.wm_poi_id = wm_poi_id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getStatus_desc() {
                return status_desc;
            }

            public void setStatus_desc(String status_desc) {
                this.status_desc = status_desc;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPic_url() {
                return pic_url;
            }

            public void setPic_url(String pic_url) {
                this.pic_url = pic_url;
            }

            public double getShipping_fee() {
                return shipping_fee;
            }

            public void setShipping_fee(double shipping_fee) {
                this.shipping_fee = shipping_fee;
            }

            public double getMin_price() {
                return min_price;
            }

            public void setMin_price(double min_price) {
                this.min_price = min_price;
            }

            public double getWm_poi_score() {
                return wm_poi_score;
            }

            public void setWm_poi_score(double wm_poi_score) {
                this.wm_poi_score = wm_poi_score;
            }

            public int getAvg_delivery_time() {
                return avg_delivery_time;
            }

            public void setAvg_delivery_time(int avg_delivery_time) {
                this.avg_delivery_time = avg_delivery_time;
            }

            public String getPoi_type_icon() {
                return poi_type_icon;
            }

            public void setPoi_type_icon(String poi_type_icon) {
                this.poi_type_icon = poi_type_icon;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public int getLatitude() {
                return latitude;
            }

            public void setLatitude(int latitude) {
                this.latitude = latitude;
            }

            public int getLongitude() {
                return longitude;
            }

            public void setLongitude(int longitude) {
                this.longitude = longitude;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getMonth_sale_num() {
                return month_sale_num;
            }

            public void setMonth_sale_num(int month_sale_num) {
                this.month_sale_num = month_sale_num;
            }

            public int getDelivery_type() {
                return delivery_type;
            }

            public void setDelivery_type(int delivery_type) {
                this.delivery_type = delivery_type;
            }

            public int getInvoice_support() {
                return invoice_support;
            }

            public void setInvoice_support(int invoice_support) {
                this.invoice_support = invoice_support;
            }

            public int getInvoice_min_price() {
                return invoice_min_price;
            }

            public void setInvoice_min_price(int invoice_min_price) {
                this.invoice_min_price = invoice_min_price;
            }

            public String getAverage_price_tip() {
                return average_price_tip;
            }

            public void setAverage_price_tip(String average_price_tip) {
                this.average_price_tip = average_price_tip;
            }

            public String getCall_center() {
                return call_center;
            }

            public void setCall_center(String call_center) {
                this.call_center = call_center;
            }

            public String getShipping_time() {
                return shipping_time;
            }

            public void setShipping_time(String shipping_time) {
                this.shipping_time = shipping_time;
            }

            public List<?> getProduct_list() {
                return product_list;
            }

            public void setProduct_list(List<?> product_list) {
                this.product_list = product_list;
            }

            public List<DiscountsBean> getDiscounts() {
                return discounts;
            }

            public void setDiscounts(List<DiscountsBean> discounts) {
                this.discounts = discounts;
            }

            public List<?> getCategoryInfoList() {
                return categoryInfoList;
            }

            public void setCategoryInfoList(List<?> categoryInfoList) {
                this.categoryInfoList = categoryInfoList;
            }

            public List<PoiUserCommentVOListBean> getPoiUserCommentVOList() {
                return poiUserCommentVOList;
            }

            public void setPoiUserCommentVOList(List<PoiUserCommentVOListBean> poiUserCommentVOList) {
                this.poiUserCommentVOList = poiUserCommentVOList;
            }

            public static class DiscountsBean {
                private String info;
                private String icon_url;
                private String name;
                private int reduceFree;

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public String getIcon_url() {
                    return icon_url;
                }

                public void setIcon_url(String icon_url) {
                    this.icon_url = icon_url;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getReduceFree() {
                    return reduceFree;
                }

                public void setReduceFree(int reduceFree) {
                    this.reduceFree = reduceFree;
                }
            }

            public static class PoiUserCommentVOListBean {
                private String user_name;
                private int ship_time;
                private String comment;
                private int comment_score;
                private int comment_time;

                public String getUser_name() {
                    return user_name;
                }

                public void setUser_name(String user_name) {
                    this.user_name = user_name;
                }

                public int getShip_time() {
                    return ship_time;
                }

                public void setShip_time(int ship_time) {
                    this.ship_time = ship_time;
                }

                public String getComment() {
                    return comment;
                }

                public void setComment(String comment) {
                    this.comment = comment;
                }

                public int getComment_score() {
                    return comment_score;
                }

                public void setComment_score(int comment_score) {
                    this.comment_score = comment_score;
                }

                public int getComment_time() {
                    return comment_time;
                }

                public void setComment_time(int comment_time) {
                    this.comment_time = comment_time;
                }
            }
        }
    }

    public static class IovBean {
    }
}
