package com.baidu.iov.dueros.waimai.net.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * @author ping
 * @date 2018/10/16
 */
public class BusinessBean {

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

    @Override
    public String toString() {
        return "BusinessBean{" +
                "meituan=" + meituan +
                ", iov=" + iov +
                '}';
    }

    public static class MeituanBean {

        @Expose
        @SerializedName("code")
        private int code;

        @Expose
        @SerializedName("msg")
        private String msg;

        @Expose
        @SerializedName("errorInfo")
        private ErrorInfo errorInfo;

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

        public ErrorInfo getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(ErrorInfo errorInfo) {
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
            return "MeituanBean{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", errorInfo=" + errorInfo +
                    ", mBusiness=" + mBusiness +
                    '}';
        }

        public static class ErrorInfo{
            
            private String failCode;
            private String name;
            private String description;

            public String getFailCode() {
                return failCode;
            }

            public void setFailCode(String failCode) {
                this.failCode = failCode;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            @Override
            public String toString() {
                return "ErrorInfo{" +
                        "failCode='" + failCode + '\'' +
                        ", name='" + name + '\'' +
                        ", description='" + description + '\'' +
                        '}';
            }
        }



        public static class Business {

            @Expose
            @SerializedName("poi_total_num")
            private int poiTotalNum;

            @Expose
            @SerializedName("have_next_page")
            private int haveNextPage;

            @Expose
            @SerializedName("current_page_index")
            private int currentPageIndex;

            @Expose
            @SerializedName("page_size")
            private int pageSize;

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


            public static class OpenPoiBaseInfo {


                @Expose
                @SerializedName("wm_poi_id")
                private long wmPoiId;

                @Expose
                @SerializedName("status")
                private int status;


                @Expose
                @SerializedName("status_desc")
                private String statusDesc;


                @Expose
                @SerializedName("name")
                private String name;


                @Expose
                @SerializedName("pic_url")
                private String picUrl;


                @Expose
                @SerializedName("shipping_fee")
                private double shippingFee;


                @Expose
                @SerializedName("min_price")
                private double minPrice;


                @Expose
                @SerializedName("wm_poi_score")
                private double wmPoiScore;


                @Expose
                @SerializedName("avg_delivery_time")
                private int avgDeliveryTime;

                @Expose
                @SerializedName("distance")
                private String distance;

                @Expose
                @SerializedName("longitude")
                private int longitude;

                @Expose
                @SerializedName("latitude")
                private int latitude;

                @Expose
                @SerializedName("address")
                private String address;

                @Expose
                @SerializedName("month_sale_num")
                private int monthSaleNum;

                @Expose
                @SerializedName("delivery_type")
                private int deliveryType;

                @Expose
                @SerializedName("invoice_support")
                private int invoiceSupport;


                @Expose
                @SerializedName("invoice_min_price")
                private int invoiceMinPrice;

                @Expose
                @SerializedName("average_price_tip")
                private String averagePriceTip;


                @Expose
                @SerializedName("poi_type_icon")
                private String poiTypeIcon;

                @Expose
                @SerializedName("discounts")
                private List<Discount> discounts;


                @Expose
                @SerializedName("product_list")
                private List<Product> productList;

                @Expose
                @SerializedName("categoryInfoList")
                private List<CategoryInfo> categoryInfoList;

                private boolean openDiscount=false;

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


                public List<CategoryInfo> getCategoryInfoList() {
                    return categoryInfoList;
                }

                public void setCategoryInfoList(List<CategoryInfo> categoryInfoList) {
                    this.categoryInfoList = categoryInfoList;
                }

                public boolean isOpenDiscount() {
                    return openDiscount;
                }

                public void setOpenDiscount(boolean openDiscount) {
                    this.openDiscount = openDiscount;
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
                            ", poiTypeIcon='" + poiTypeIcon + '\'' +
                            ", discounts=" + discounts +
                            ", productList=" + productList +
                            ", categoryInfoList=" + categoryInfoList +
                            ", openDiscount=" + openDiscount +
                            '}';
                }

                public static class Discount {


                    @Expose
                    @SerializedName("info")
                    private String info;


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


                public static class Product {

                    @Expose
                    @SerializedName("id")
                    private long id;


                    @Expose
                    @SerializedName("name")
                    private String name;


                    @Expose
                    @SerializedName("price")
                    private double price;


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
                
                public static  class CategoryInfo{
                    @Expose
                    @SerializedName("name")
                    private String name;
                    @Expose
                    @SerializedName("level")
                    private int level;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getLevel() {
                        return level;
                    }

                    public void setLevel(int level) {
                        this.level = level;
                    }

                    @Override
                    public String toString() {
                        return "CategoryInfo{" +
                                "name='" + name + '\'' +
                                ", level=" + level +
                                '}';
                    }
                }

            }
        }

    }

    public static class IovBean {
    }
}
