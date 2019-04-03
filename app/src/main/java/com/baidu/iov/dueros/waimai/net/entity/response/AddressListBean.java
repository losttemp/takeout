package com.baidu.iov.dueros.waimai.net.entity.response;


import android.text.TextUtils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class AddressListBean {

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
    }

    public static class IovBean {
        private List<DataBean> data;
        private String user_phone;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public static class DataBean implements Serializable, Comparable<DataBean> {

            private String address;
            private Integer latitude;
            private Integer longitude;
            private boolean is_hint;
            private Long address_id = 0l;
            private Long mt_address_id = 0l;
            private String house;
            private String user_phone;
            private String user_name;
            private Integer canShipping =0;
            private String addressRangeTip;
            private Integer sex;
            private int item_type;

            public Long getAddress_id() {
                return address_id;
            }

            public void setAddress_id(Long address_id) {
                this.address_id = address_id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            private String type;

            public Integer getCanShipping() {
                return canShipping;
            }

            public void setCanShipping(Integer canShipping) {
                this.canShipping = canShipping;
            }

            public String getAddressRangeTip() {
                return addressRangeTip;
            }

            public void setAddressRangeTip(String addressRangeTip) {
                this.addressRangeTip = addressRangeTip;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public Integer getLatitude() {
                return latitude;
            }

            public void setLatitude(Integer latitude) {
                this.latitude = latitude;
            }

            public Integer getLongitude() {
                return longitude;
            }

            public void setLongitude(Integer longitude) {
                this.longitude = longitude;
            }

            public boolean isIs_hint() {
                return is_hint;
            }

            public void setIs_hint(boolean is_hint) {
                this.is_hint = is_hint;
            }

            public Long getMt_address_id() {
                return mt_address_id;
            }

            public void setMt_address_id(Long mt_address_id) {
                this.mt_address_id = mt_address_id;
            }

            public String getHouse() {
                return house;
            }

            public void setHouse(String house) {
                this.house = house;
            }

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

            public Integer getSex() {
                return sex;
            }

            public void setSex(Integer sex) {
                this.sex = sex;
            }

            public int getItem_type() {
                return item_type;
            }

            public void setItem_type(int item_type) {
                this.item_type = item_type;
            }

            @Override
            public int compareTo(DataBean o) {
                if ((int)o.getCanShipping() != this.getCanShipping()) {
                    return  o.getCanShipping() - this.getCanShipping();
                } else {

                    if ((o.getType() == null || o.getType().equals("")) && (this.getType() == null || this.getType().equals(""))){
                        return 0;
                    }

                    if ((o.getType() == null || o.getType().equals("")) && this.getType().equals("家里")){
                        return -1;
                    }

                    if ((o.getType() == null || o.getType().equals("")) && this.getType().equals("公司")){
                        return -1;
                    }

                    if (o.getType().equals("公司") && (this.getType() == null || this.getType().equals(""))){
                        return 1;
                    }

                    if (o.getType().equals("家里") && (this.getType() == null || this.getType().equals(""))){
                        return 1;
                    }
                    if (o.getType().equals("公司") && this.getType().equals("家里")){
                        return 1;
                    }

                    if (o.getType().equals("家里") && this.getType().equals("公司")){
                        return -1;
                    }



                }

                return 0;
            }
        }
    }
}
