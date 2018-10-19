package com.baidu.iov.dueros.waimai.net.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取商家列表所需参数
 *
 * @author ping
 * @date 2018/10/16
 */
public class BusinessBean {
    @Expose
    @SerializedName("code")
    private int code;
    
    @Expose
    @SerializedName("msg")
    private String msg;
    
    @Expose
    @SerializedName("errorInfo")
    private String errorInfo;
    
    @Expose
    @SerializedName("data") 
    private Business mBusiness;

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

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Business getmBusiness() {
        return mBusiness;
    }

    public void setmBusiness(Business mBusiness) {
        this.mBusiness = mBusiness;
    }

    @Override
    public String toString() {
        return "BusinessBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                ", mBusiness=" + mBusiness +
                '}';
    }

    /**
     * 商家信息
     */
    public static class Business {
        /**
         * 符合搜索的商家总数
         */
        @Expose
        @SerializedName("poi_total_num")
        private int poiTotalNum;
        /**
         * 是否有下一页
         */
        @Expose
        @SerializedName("have_next_page")
        private int haveNextPage;
        /**
         * 当前页号
         */
        @Expose
        @SerializedName("current_page_index")
        private int currentPageIndex;
        /**
         * 每页数量
         */
        @Expose
        @SerializedName("page_size")
        private int pageSize;
        /**
         * 商家列表
         */
        @Expose
        @SerializedName("openPoiBaseInfoList")
        private List<OpenPoiBaseInfo> openPoiBaseInfoList;

        public int getPoiTotalNum() {
            return poiTotalNum;
        }

        public void setPoiTotalNum(int poiTotalNum) {
            this.poiTotalNum = poiTotalNum;
        }

        public int getHaveNextPage() {
            return haveNextPage;
        }

        public void setHaveNextPage(int haveNextPage) {
            this.haveNextPage = haveNextPage;
        }

        public int getCurrentPageIndex() {
            return currentPageIndex;
        }

        public void setCurrentPageIndex(int currentPageIndex) {
            this.currentPageIndex = currentPageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<OpenPoiBaseInfo> getOpenPoiBaseInfoList() {
            return openPoiBaseInfoList;
        }

        public void setOpenPoiBaseInfoList(List<OpenPoiBaseInfo> openPoiBaseInfoList) {
            this.openPoiBaseInfoList = openPoiBaseInfoList;
        }

        @Override
        public String toString() {
            return "Business{" +
                    "poiTotalNum=" + poiTotalNum +
                    ", haveNextPage=" + haveNextPage +
                    ", currentPageIndex=" + currentPageIndex +
                    ", pageSize=" + pageSize +
                    ", openPoiBaseInfoList=" + openPoiBaseInfoList +
                    '}';
        }

        /**
         * 商家信息
         */
        public static class OpenPoiBaseInfo {

            /**
             * 商家ID
             */
            @Expose
            @SerializedName("wm_poi_id")
            private long wmPoiId;

            /**
             * 营业状态 1:可配送 2:忙碌中 3:休息中
             */
            @Expose
            @SerializedName("status")
            private int status;

            /**
             * 营业状态描述
             */
            @Expose
            @SerializedName("status_desc")
            private String statusDesc;

            /**
             * 商家名字
             */
            @Expose
            @SerializedName("name")
            private String name;

            /**
             * 商家图标
             */
            @Expose
            @SerializedName("pic_url")
            private String picUrl;

            /**
             * 配送费
             */
            @Expose
            @SerializedName("shipping_fee")
            private double shippingFee;

            /**
             * 起送价
             */
            @Expose
            @SerializedName("min_price")
            private double minPrice;

            /**
             * 商家评分
             */
            @Expose
            @SerializedName("wm_poi_score")
            private double wmPoiScore;

            /**
             * 平均配送时间
             */
            @Expose
            @SerializedName("avg_delivery_time")
            private int avgDeliveryTime;

            /**
             * 我和商家的距离
             */
            @Expose
            @SerializedName("distance")
            private String distance;

            /**
             * 商家位置经度
             */
            @Expose
            @SerializedName("longitude")
            private int longitude;


            /**
             * 商家位置纬度
             */
            @Expose
            @SerializedName("latitude")
            private int latitude;
            /**
             * 商家地址
             */
            @Expose
            @SerializedName("address")
            private String address;
            /**
             * 月售
             */
            @Expose
            @SerializedName("month_sale_num")
            private int monthSaleNum;
            /**
             * 配送类型，1:美团专送，0:非美团专送
             */
            @Expose
            @SerializedName("delivery_type")
            private int deliveryType;
            /**
             * 是否支持开发票，1:支持 0:不支持
             */
            @Expose
            @SerializedName("invoice_support")
            private int invoiceSupport;

            /**
             * 是否支持开发票，1:支持 0:不支持
             */
            @Expose
            @SerializedName("invoice_min_price")
            private int invoiceMinPrice;
            /**
             * 人均价格展示文案
             */
            @Expose
            @SerializedName("average_price_tip")
            private String averagePriceTip;

            /**
             * 商家图片角标url
             */
            @Expose
            @SerializedName("poi_type_icon")
            private String poiTypeIcon;
            /**
             * 门店信息列表
             */
            @Expose
            @SerializedName("discounts")
            private List<Discount> discounts;

            /**
             * 菜品列表，搜索时有
             */
            @Expose
            @SerializedName("product_list")
            private List<Product> productList;

            public long getWmPoiId() {
                return wmPoiId;
            }

            public void setWmPoiId(long wmPoiId) {
                this.wmPoiId = wmPoiId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getStatusDesc() {
                return statusDesc;
            }

            public void setStatusDesc(String statusDesc) {
                this.statusDesc = statusDesc;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public double getShippingFee() {
                return shippingFee;
            }

            public void setShippingFee(double shippingFee) {
                this.shippingFee = shippingFee;
            }

            public double getMinPrice() {
                return minPrice;
            }

            public void setMinPrice(double minPrice) {
                this.minPrice = minPrice;
            }

            public double getWmPoiScore() {
                return wmPoiScore;
            }

            public void setWmPoiScore(double wmPoiScore) {
                this.wmPoiScore = wmPoiScore;
            }

            public int getAvgDeliveryTime() {
                return avgDeliveryTime;
            }

            public void setAvgDeliveryTime(int avgDeliveryTime) {
                this.avgDeliveryTime = avgDeliveryTime;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
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

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getMonthSaleNum() {
                return monthSaleNum;
            }

            public void setMonthSaleNum(int monthSaleNum) {
                this.monthSaleNum = monthSaleNum;
            }

            public int getDeliveryType() {
                return deliveryType;
            }

            public void setDeliveryType(int deliveryType) {
                this.deliveryType = deliveryType;
            }

            public int getInvoiceSupport() {
                return invoiceSupport;
            }

            public void setInvoiceSupport(int invoiceSupport) {
                this.invoiceSupport = invoiceSupport;
            }

            public int getInvoiceMinPrice() {
                return invoiceMinPrice;
            }

            public void setInvoiceMinPrice(int invoiceMinPrice) {
                this.invoiceMinPrice = invoiceMinPrice;
            }

            public String getAveragePriceTip() {
                return averagePriceTip;
            }

            public void setAveragePriceTip(String averagePriceTip) {
                this.averagePriceTip = averagePriceTip;
            }

            public String getPoiTypeIcon() {
                return poiTypeIcon;
            }

            public void setPoiTypeIcon(String poiTypeIcon) {
                this.poiTypeIcon = poiTypeIcon;
            }

            public List<Discount> getDiscounts() {
                return discounts;
            }

            public void setDiscounts(List<Discount> discounts) {
                this.discounts = discounts;
            }

            public List<Product> getProductList() {
                return productList;
            }

            public void setProductList(List<Product> productList) {
                this.productList = productList;
            }

            @Override
            public String toString() {
                return "OpenPoiBaseInfo{" +
                        "wmPoiId=" + wmPoiId +
                        ", status=" + status +
                        ", statusDesc='" + statusDesc + '\'' +
                        ", name='" + name + '\'' +
                        ", picUrl='" + picUrl + '\'' +
                        ", shippingFee=" + shippingFee +
                        ", minPrice=" + minPrice +
                        ", wmPoiScore=" + wmPoiScore +
                        ", avgDeliveryTime=" + avgDeliveryTime +
                        ", distance='" + distance + '\'' +
                        ", longitude=" + longitude +
                        ", latitude=" + latitude +
                        ", address='" + address + '\'' +
                        ", monthSaleNum=" + monthSaleNum +
                        ", deliveryType=" + deliveryType +
                        ", invoiceSupport=" + invoiceSupport +
                        ", invoiceMinPrice=" + invoiceMinPrice +
                        ", averagePriceTip='" + averagePriceTip + '\'' +
                        ", poiTypeIcon=" + poiTypeIcon +
                        ", discounts=" + discounts +
                        ", productList=" + productList +
                        '}';
            }

            /**
             * 门店信息
             */
            public static class Discount {

                /**
                 * 活动描述文案
                 */
                @Expose
                @SerializedName("info")
                private String info;

                /**
                 * 活动图标url
                 */
                @Expose
                @SerializedName("icon_url")
                private String iconUrl;

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public String getIconUrl() {
                    return iconUrl;
                }

                public void setIconUrl(String iconUrl) {
                    this.iconUrl = iconUrl;
                }

                @Override
                public String toString() {
                    return "Discount{" +
                            "info='" + info + '\'' +
                            ", iconUrl='" + iconUrl + '\'' +
                            '}';
                }
            }

            /**
             * 菜品
             */
            public static class Product {
                /**
                 * 商品ID
                 */
                @Expose
                @SerializedName("id")
                private long id;

                /**
                 * 商品名称
                 */
                @Expose
                @SerializedName("name")
                private String name;

                /**
                 * 价格
                 */
                @Expose
                @SerializedName("price")
                private double price;

                /**
                 * 菜品图片
                 */
                @Expose
                @SerializedName("picture")
                private String picture;

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
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

                public String getPicture() {
                    return picture;
                }

                public void setPicture(String picture) {
                    this.picture = picture;
                }

                @Override
                public String toString() {
                    return "Product{" +
                            "id=" + id +
                            ", name='" + name + '\'' +
                            ", price=" + price +
                            ", picture='" + picture + '\'' +
                            '}';
                }
            }

        }
    }
}
