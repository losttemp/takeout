package com.baidu.iov.dueros.waimai.net.entity.response;


import java.util.List;

public class OrderListResponse {


    /**
     * meituan : {}
     * iov : {"errno":0,"errmsg":"success","data":[{"order_id":"80714733203506","uid":"10000","out_trade_detail_id":"692","out_trade_no":"15053512313431272","app_id":"2018060003","order_amount":"1","order_name":"外卖QA-iOS自动化测试门店","order_status":"1","order_time":"2018-10-16 17:32:31","expire_time":"2018-10-16 17:32:46","tx_no":"0","pay_channel":"0","pay_type":"0","out_trade_status":"0","extra":"{\"payload\":{\"user_phone\":\"18503040492\",\"wm_ordering_list\":{\"wm_poi_id\":\"1505351\",\"delivery_time\":0,\"pay_type\":2,\"food_list\":[{\"wm_food_sku_id\":1239556963,\"count\":1}]},\"wm_ordering_user\":{\"user_phone\":\"18503040492\",\"user_name\":\"\\u8096\",\"user_address\":\"\\u7f8e\\u56e2\\u5927\\u53a6\",\"addr_longitude\":95369826,\"addr_latitude\":29735952},\"return_url\":\"http:\\/\\/xxx\",\"pay_source\":3},\"orderInfos\":{\"goods_name\":\"\\u5916\\u5356QA-iOS\\u81ea\\u52a8\\u5316\\u6d4b\\u8bd5\\u95e8\\u5e97\",\"goods_total_price\":1,\"transport_price\":0,\"out_trade_time\":1539682292,\"food_list\":[{\"food_id\":1239556963,\"spu_id\":1122027842,\"name\":\"Hdh\",\"price\":0.01,\"original_price\":0.01,\"count\":1,\"spec\":\"\",\"box_num\":1,\"box_price\":0,\"attrIds\":[],\"attrValues\":[]}]}}"}]}
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
    }

    public static class IovBean {
        /**
         * errno : 0
         * errmsg : success
         * data : [{"order_id":"80714733203506","uid":"10000","out_trade_detail_id":"692","out_trade_no":"15053512313431272","app_id":"2018060003","order_amount":"1","order_name":"外卖QA-iOS自动化测试门店","order_status":"1","order_time":"2018-10-16 17:32:31","expire_time":"2018-10-16 17:32:46","tx_no":"0","pay_channel":"0","pay_type":"0","out_trade_status":"0","extra":"{\"payload\":{\"user_phone\":\"18503040492\",\"wm_ordering_list\":{\"wm_poi_id\":\"1505351\",\"delivery_time\":0,\"pay_type\":2,\"food_list\":[{\"wm_food_sku_id\":1239556963,\"count\":1}]},\"wm_ordering_user\":{\"user_phone\":\"18503040492\",\"user_name\":\"\\u8096\",\"user_address\":\"\\u7f8e\\u56e2\\u5927\\u53a6\",\"addr_longitude\":95369826,\"addr_latitude\":29735952},\"return_url\":\"http:\\/\\/xxx\",\"pay_source\":3},\"orderInfos\":{\"goods_name\":\"\\u5916\\u5356QA-iOS\\u81ea\\u52a8\\u5316\\u6d4b\\u8bd5\\u95e8\\u5e97\",\"goods_total_price\":1,\"transport_price\":0,\"out_trade_time\":1539682292,\"food_list\":[{\"food_id\":1239556963,\"spu_id\":1122027842,\"name\":\"Hdh\",\"price\":0.01,\"original_price\":0.01,\"count\":1,\"spec\":\"\",\"box_num\":1,\"box_price\":0,\"attrIds\":[],\"attrValues\":[]}]}}"}]
         */

        private int errno;
        private String errmsg;
        private List<DataBean> data;

        public int getErrno() {
            return errno;
        }

        public void setErrno(int errno) {
            this.errno = errno;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * order_id : 80714733203506
             * uid : 10000
             * out_trade_detail_id : 692
             * out_trade_no : 15053512313431272
             * app_id : 2018060003
             * order_amount : 1
             * order_name : 外卖QA-iOS自动化测试门店
             * order_status : 1
             * order_time : 2018-10-16 17:32:31
             * expire_time : 2018-10-16 17:32:46
             * tx_no : 0
             * pay_channel : 0
             * pay_type : 0
             * out_trade_status : 0
             * extra : {"payload":{"user_phone":"18503040492","wm_ordering_list":{"wm_poi_id":"1505351","delivery_time":0,"pay_type":2,"food_list":[{"wm_food_sku_id":1239556963,"count":1}]},"wm_ordering_user":{"user_phone":"18503040492","user_name":"\u8096","user_address":"\u7f8e\u56e2\u5927\u53a6","addr_longitude":95369826,"addr_latitude":29735952},"return_url":"http:\/\/xxx","pay_source":3},"orderInfos":{"goods_name":"\u5916\u5356QA-iOS\u81ea\u52a8\u5316\u6d4b\u8bd5\u95e8\u5e97","goods_total_price":1,"transport_price":0,"out_trade_time":1539682292,"food_list":[{"food_id":1239556963,"spu_id":1122027842,"name":"Hdh","price":0.01,"original_price":0.01,"count":1,"spec":"","box_num":1,"box_price":0,"attrIds":[],"attrValues":[]}]}}
             */

            private String order_id;
            private String uid;
            private String out_trade_detail_id;
            private String out_trade_no;
            private String app_id;
            private String order_amount;
            private String order_name;
            private String order_status;
            private String order_time;
            private String expire_time;
            private String tx_no;
            private String pay_channel;
            private String pay_type;
            private String out_trade_status;
            private String extra;

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getOut_trade_detail_id() {
                return out_trade_detail_id;
            }

            public void setOut_trade_detail_id(String out_trade_detail_id) {
                this.out_trade_detail_id = out_trade_detail_id;
            }

            public String getOut_trade_no() {
                return out_trade_no;
            }

            public void setOut_trade_no(String out_trade_no) {
                this.out_trade_no = out_trade_no;
            }

            public String getApp_id() {
                return app_id;
            }

            public void setApp_id(String app_id) {
                this.app_id = app_id;
            }

            public String getOrder_amount() {
                return order_amount;
            }

            public void setOrder_amount(String order_amount) {
                this.order_amount = order_amount;
            }

            public String getOrder_name() {
                return order_name;
            }

            public void setOrder_name(String order_name) {
                this.order_name = order_name;
            }

            public String getOrder_status() {
                return order_status;
            }

            public void setOrder_status(String order_status) {
                this.order_status = order_status;
            }

            public String getOrder_time() {
                return order_time;
            }

            public void setOrder_time(String order_time) {
                this.order_time = order_time;
            }

            public String getExpire_time() {
                return expire_time;
            }

            public void setExpire_time(String expire_time) {
                this.expire_time = expire_time;
            }

            public String getTx_no() {
                return tx_no;
            }

            public void setTx_no(String tx_no) {
                this.tx_no = tx_no;
            }

            public String getPay_channel() {
                return pay_channel;
            }

            public void setPay_channel(String pay_channel) {
                this.pay_channel = pay_channel;
            }

            public String getPay_type() {
                return pay_type;
            }

            public void setPay_type(String pay_type) {
                this.pay_type = pay_type;
            }

            public String getOut_trade_status() {
                return out_trade_status;
            }

            public void setOut_trade_status(String out_trade_status) {
                this.out_trade_status = out_trade_status;
            }

            public String getExtra() {
                return extra;
            }

            public void setExtra(String extra) {
                this.extra = extra;
            }
        }
    }
}

