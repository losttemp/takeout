package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class addresslistBean {

    public static class DataBeanX {
        /**
         * meituan : {"code":0,"msg":"调用成功","errorInfo":null,"data":[{"addressId":599974979,"name":"崔志刚","gender":1,"phone":"17638916218","address":"瑞成佳苑","houseNumber":"4栋1单元2204","latitude":30502147,"longitude":114436307,"bindType":11,"canShipping":1,"addressRangeTip":""},{"addressId":931424614,"name":"崔志刚","gender":1,"phone":"17638916218","address":"现代·森林小镇","houseNumber":"3栋3单元1001","latitude":30455306,"longitude":114417036,"bindType":11,"canShipping":1,"addressRangeTip":""}]}
         * iov : {}
         */

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
            /**
             * code : 0
             * msg : 调用成功
             * errorInfo : null
             * data : [{"addressId":599974979,"name":"崔志刚","gender":1,"phone":"17638916218","address":"瑞成佳苑","houseNumber":"4栋1单元2204","latitude":30502147,"longitude":114436307,"bindType":11,"canShipping":1,"addressRangeTip":""},{"addressId":931424614,"name":"崔志刚","gender":1,"phone":"17638916218","address":"现代·森林小镇","houseNumber":"3栋3单元1001","latitude":30455306,"longitude":114417036,"bindType":11,"canShipping":1,"addressRangeTip":""}]
             */

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

            public static class DataBean {
                /**
                 * addressId : 599974979
                 * name : 崔志刚
                 * gender : 1
                 * phone : 17638916218
                 * address : 瑞成佳苑
                 * houseNumber : 4栋1单元2204
                 * latitude : 30502147
                 * longitude : 114436307
                 * bindType : 11
                 * canShipping : 1
                 * addressRangeTip :
                 */

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
            }
        }

        public static class IovBean {
        }
    }
}
