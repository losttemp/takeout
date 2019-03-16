package com.baidu.iov.dueros.waimai.net.entity.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ubuntu on 18-10-18.
 */

public class PoifoodListBean {
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
            private PoiInfoBean poi_info;
            private List<FoodSpuTagsBean> food_spu_tags;

            public PoiInfoBean getPoi_info() {
                return poi_info;
            }

            public void setPoi_info(PoiInfoBean poi_info) {
                this.poi_info = poi_info;
            }

            public List<FoodSpuTagsBean> getFood_spu_tags() {
                return food_spu_tags;
            }

            public void setFood_spu_tags(List<FoodSpuTagsBean> food_spu_tags) {
                this.food_spu_tags = food_spu_tags;
            }

            public static class PoiInfoBean implements Serializable {
                private long id;
                private long wm_poi_id;
                private String name;
                private int status;
                private String shipping_time;
                private double shipping_fee;
                private int avg_delivery_time;
                private double min_price;
                private String bulletin;
                private String app_delivery_tip;
                private int support_pay;
                private int invoice_support;
                private int invoice_min_price;
                private double wm_poi_score;
                private String poi_back_pic_url;
                private String pic_url;
                private int delivery_type;
                private Object discounts;

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public long getWm_poi_id() {
                    return wm_poi_id;
                }

                public void setWm_poi_id(long wm_poi_id) {
                    this.wm_poi_id = wm_poi_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getShipping_time() {
                    return shipping_time;
                }

                public void setShipping_time(String shipping_time) {
                    this.shipping_time = shipping_time;
                }

                public double getShipping_fee() {
                    return shipping_fee;
                }

                public void setShipping_fee(double shipping_fee) {
                    this.shipping_fee = shipping_fee;
                }

                public int getAvg_delivery_time() {
                    return avg_delivery_time;
                }

                public void setAvg_delivery_time(int avg_delivery_time) {
                    this.avg_delivery_time = avg_delivery_time;
                }

                public double getMin_price() {
                    return min_price;
                }

                public void setMin_price(double min_price) {
                    this.min_price = min_price;
                }

                public String getBulletin() {
                    return bulletin;
                }

                public void setBulletin(String bulletin) {
                    this.bulletin = bulletin;
                }

                public String getApp_delivery_tip() {
                    return app_delivery_tip;
                }

                public void setApp_delivery_tip(String app_delivery_tip) {
                    this.app_delivery_tip = app_delivery_tip;
                }

                public int getSupport_pay() {
                    return support_pay;
                }

                public void setSupport_pay(int support_pay) {
                    this.support_pay = support_pay;
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

                public double getWm_poi_score() {
                    return wm_poi_score;
                }

                public void setWm_poi_score(double wm_poi_score) {
                    this.wm_poi_score = wm_poi_score;
                }

                public String getPoi_back_pic_url() {
                    return poi_back_pic_url;
                }

                public void setPoi_back_pic_url(String poi_back_pic_url) {
                    this.poi_back_pic_url = poi_back_pic_url;
                }

                public String getPic_url() {
                    return pic_url;
                }

                public void setPic_url(String pic_url) {
                    this.pic_url = pic_url;
                }

                public int getDelivery_type() {
                    return delivery_type;
                }

                public void setDelivery_type(int delivery_type) {
                    this.delivery_type = delivery_type;
                }

                public Object getDiscounts() {
                    return discounts;
                }

                public void setDiscounts(Object discounts) {
                    this.discounts = discounts;
                }
            }

            public static class FoodSpuTagsBean {
                private String tag;
                private String name;
                private String icon;
                private int type;
                private int selected;
                private String description;
                private int sequence;
                private List<SpusBean> spus;

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public int getSelected() {
                    return selected;
                }

                public void setSelected(int selected) {
                    this.selected = selected;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getSequence() {
                    return sequence;
                }

                public void setSequence(int sequence) {
                    this.sequence = sequence;
                }

                public List<SpusBean> getSpus() {
                    return spus;
                }

                public void setSpus(List<SpusBean> spus) {
                    this.spus = spus;
                }

                public static class SpusBean implements Serializable {
                    private int section;
                    private int id;
                    private String name;
                    private double min_price;
                    private String unit;
                    private String tag;
                    private String description;
                    private String picture;
                    private int month_saled;
                    private int status;
                    private int realStatus;
                    private int virtualStatus;
                    private String sku_label;
                    private int sequence;
                    private String shippingTimeX;
                    private int mport_sell_status;
                    private List<AttrsBean> attrs;
                    private List<SkusBean> skus;
                    private List<SkusBean> choiceSkus;
                    private int number;
                    private Integer restrict;
                    private int specificationsNumber;

                    public int getSpecificationsNumber() {
                        return specificationsNumber;
                    }

                    public void setSpecificationsNumber(int specificationsNumber) {
                        this.specificationsNumber = specificationsNumber;
                    }

                    public Integer getRestrict() {
                        return restrict;
                    }

                    public void setRestrict(Integer restrict) {
                        this.restrict = restrict;
                    }

                    public Object deepClone() throws Exception {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);

                        oos.writeObject(this);

                        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                        ObjectInputStream ois = new ObjectInputStream(bis);

                        return ois.readObject();
                    }

                    @Override
                    public boolean equals(Object obj) {
                        SpusBean spusBean = (SpusBean) obj;
                        if (choiceSkus == null) {
                            return id == spusBean.getId() && attrs.equals(spusBean.getAttrs());
                        } else {
                            if (spusBean.getChoiceSkus() != null) {
                                return id == spusBean.getId() && attrs.equals(spusBean.getAttrs()) &&
                                        id == spusBean.getId() && choiceSkus.get(0).spec.equals(spusBean.getChoiceSkus().get(0).spec);
                            }
                        }
                        return super.equals(obj);
                    }

                    public int getSection() {
                        return section;
                    }

                    public void setSection(int section) {
                        this.section = section;
                    }

                    public List<SkusBean> getChoiceSkus() {
                        return choiceSkus;
                    }

                    public void setChoiceSkus(List<SkusBean> choiceSkus) {
                        this.choiceSkus = choiceSkus;
                    }

                    public int getNumber() {
                        return number;
                    }

                    public void setNumber(int number) {
                        this.number = number;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public double getMin_price() {
                        return min_price;
                    }

                    public void setMin_price(double min_price) {
                        this.min_price = min_price;
                    }

                    public String getUnit() {
                        return unit;
                    }

                    public void setUnit(String unit) {
                        this.unit = unit;
                    }

                    public String getTag() {
                        return tag;
                    }

                    public void setTag(String tag) {
                        this.tag = tag;
                    }

                    public String getDescription() {
                        return description;
                    }

                    public void setDescription(String description) {
                        this.description = description;
                    }

                    public String getPicture() {
                        return picture;
                    }

                    public void setPicture(String picture) {
                        this.picture = picture;
                    }

                    public int getMonth_saled() {
                        return month_saled;
                    }

                    public void setMonth_saled(int month_saled) {
                        this.month_saled = month_saled;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public int getRealStatus() {
                        return realStatus;
                    }

                    public void setRealStatus(int realStatus) {
                        this.realStatus = realStatus;
                    }

                    public int getVirtualStatus() {
                        return virtualStatus;
                    }

                    public void setVirtualStatus(int virtualStatus) {
                        this.virtualStatus = virtualStatus;
                    }

                    public String getSku_label() {
                        return sku_label;
                    }

                    public void setSku_label(String sku_label) {
                        this.sku_label = sku_label;
                    }

                    public int getSequence() {
                        return sequence;
                    }

                    public void setSequence(int sequence) {
                        this.sequence = sequence;
                    }

                    public String getShippingTimeX() {
                        return shippingTimeX;
                    }

                    public void setShippingTimeX(String shippingTimeX) {
                        this.shippingTimeX = shippingTimeX;
                    }

                    public int getMport_sell_status() {
                        return mport_sell_status;
                    }

                    public void setMport_sell_status(int mport_sell_status) {
                        this.mport_sell_status = mport_sell_status;
                    }

                    public List<AttrsBean> getAttrs() {
                        return attrs;
                    }

                    public void setAttrs(List<AttrsBean> attrs) {
                        this.attrs = attrs;
                    }

                    public List<SkusBean> getSkus() {
                        return skus;
                    }

                    public void setSkus(List<SkusBean> skus) {
                        this.skus = skus;
                    }

                    public static class AttrsBean implements Serializable {
                        private String name;
                        private List<ValuesBean> values;
                        private List<ValuesBean> choiceAttrs;

                        @Override
                        public boolean equals(Object obj) {
                            AttrsBean attrsBean = (AttrsBean) obj;
                            if (choiceAttrs != null) {
                                return choiceAttrs.equals(attrsBean.getChoiceAttrs());
                            }
                            return super.equals(obj);
                        }

                        public List<ValuesBean> getChoiceAttrs() {
                            return choiceAttrs;
                        }

                        public void setChoiceAttrs(List<ValuesBean> choiceAttrs) {
                            this.choiceAttrs = choiceAttrs;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public List<ValuesBean> getValues() {
                            return values;
                        }

                        public void setValues(List<ValuesBean> values) {
                            this.values = values;
                        }

                        public static class ValuesBean implements Serializable {
                            private long id;
                            private String value;

                            @Override
                            public boolean equals(Object obj) {
                                ValuesBean valuesBean = (ValuesBean) obj;
                                return id == valuesBean.getId() && value.equals(valuesBean.getValue());
                            }

                            public long getId() {
                                return id;
                            }

                            public void setId(long id) {
                                this.id = id;
                            }

                            public String getValue() {
                                return value;
                            }

                            public void setValue(String value) {
                                this.value = value;
                            }

                        }
                    }

                    public static class SkusBean implements Serializable {
                        private int id;
                        private String spec;
                        private String description;
                        private String picture;
                        private double price;
                        private double origin_price;
                        private double box_num;
                        private double box_price;
                        private int min_order_count;
                        private int status;
                        private int stock;
                        private int realStock;
                        private int virtualStock;
                        private int restrict;

                        @Override
                        public boolean equals(Object obj) {
                            SkusBean skusBean = (SkusBean) obj;
                            return spec.equals(skusBean.getSpec());
                        }

                        public int getId() {
                            return id;
                        }

                        public void setId(int id) {
                            this.id = id;
                        }

                        public String getSpec() {
                            return spec;
                        }

                        public void setSpec(String spec) {
                            this.spec = spec;
                        }

                        public String getDescription() {
                            return description;
                        }

                        public void setDescription(String description) {
                            this.description = description;
                        }

                        public String getPicture() {
                            return picture;
                        }

                        public void setPicture(String picture) {
                            this.picture = picture;
                        }

                        public double getPrice() {
                            return price;
                        }

                        public void setPrice(double price) {
                            this.price = price;
                        }

                        public double getOrigin_price() {
                            return origin_price;
                        }

                        public void setOrigin_price(double origin_price) {
                            this.origin_price = origin_price;
                        }

                        public double getBox_num() {
                            return box_num;
                        }

                        public void setBox_num(double box_num) {
                            this.box_num = box_num;
                        }

                        public double getBox_price() {
                            return box_price;
                        }

                        public void setBox_price(double box_price) {
                            this.box_price = box_price;
                        }

                        public int getMin_order_count() {
                            return min_order_count;
                        }

                        public void setMin_order_count(int min_order_count) {
                            this.min_order_count = min_order_count;
                        }

                        public int getStatus() {
                            return status;
                        }

                        public void setStatus(int status) {
                            this.status = status;
                        }

                        public int getStock() {
                            return stock;
                        }

                        public void setStock(int stock) {
                            this.stock = stock;
                        }

                        public int getRealStock() {
                            return realStock;
                        }

                        public void setRealStock(int realStock) {
                            this.realStock = realStock;
                        }

                        public int getVirtualStock() {
                            return virtualStock;
                        }

                        public void setVirtualStock(int virtualStock) {
                            this.virtualStock = virtualStock;
                        }

                        public int getRestrict() {
                            return restrict;
                        }

                        public void setRestrict(int restrict) {
                            this.restrict = restrict;
                        }
                    }
                }
            }
        }
    }

    public static class IovBean {
    }
}
