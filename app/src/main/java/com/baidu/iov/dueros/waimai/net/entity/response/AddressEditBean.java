package com.baidu.iov.dueros.waimai.net.entity.response;

import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

import java.util.List;

public class AddressEditBean {


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
        return "AddressEditBean{" +
                "meituan=" + meituan +
                ", iov=" + iov +
                '}';
    }

    public static class MeituanBean {

        private int code;
        private String msg;
        private Object errorInfo;
        private List<DataBean> data;

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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "MeituanBean{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", errorInfo=" + errorInfo +
                    ", data=" + data +
                    '}';
        }

        public static class DataBean {

            private int addressId;
            private String name;
            private int gender;
            private String phone;
            private String address;
            private String houseNumber;
            private int latitude;
            private int longitude;
            private int bindType;
            private int canShipping;
            private String addressRangeTip;

            public int getAddressId() {
                return addressId;
            }

            public void setAddressId(int addressId) {
                this.addressId = addressId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getHouseNumber() {
                return houseNumber;
            }

            public void setHouseNumber(String houseNumber) {
                this.houseNumber = houseNumber;
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

            public int getBindType() {
                return bindType;
            }

            public void setBindType(int bindType) {
                this.bindType = bindType;
            }

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

            @Override
            public String toString() {
                return "DataBean{" +
                        "addressId=" + addressId +
                        ", name='" + name + '\'' +
                        ", gender=" + gender +
                        ", phone='" + phone + '\'' +
                        ", address='" + address + '\'' +
                        ", houseNumber='" + houseNumber + '\'' +
                        ", latitude=" + latitude +
                        ", longitude=" + longitude +
                        ", bindType=" + bindType +
                        ", canShipping=" + canShipping +
                        ", addressRangeTip='" + addressRangeTip + '\'' +
                        '}';
            }
        }
    }

    public static class IovBean {
    }
}
