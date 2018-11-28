package com.baidu.iov.dueros.waimai.net.entity.response;


import java.io.Serializable;
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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable{

            private String address;
            private int latitude;
            private int longitude;
            private boolean is_hint;
            private long address_id;
            private long mt_address_id;
            private String house;
            private String user_phone;
            private String user_name;
            private int canShipping;
            private String addressRangeTip;
            private int sex;

            public long getAddress_id() {
                return address_id;
            }

            public void setAddress_id(long address_id) {
                this.address_id = address_id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            private String type;

            public int getCanShipping() {
                return canShipping;
            }

            public void setCanShipping(int canShipping) {
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

            public boolean isIs_hint() {
                return is_hint;
            }

            public void setIs_hint(boolean is_hint) {
                this.is_hint = is_hint;
            }

            public long getMt_address_id() {
                return mt_address_id;
            }

            public void setMt_address_id(long mt_address_id) {
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

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }
        }
    }
}
