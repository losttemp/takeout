package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class GuidingBean {


    /**
     * list : {"waimai":{"address":{"me":["你好福特，送到目的地","你好福特，送到公司","你好福特，送到家里","你好福特，第一个","你好福特，新增地址"],"search_result":["你好福特，第一个"],"empty_result":["你好福特，新增地址"]},"shop":{"list":["你好福特，第一个","你好福特，美食分类","你好福特，鲜花预定","你好福特，蛋糕预定"],"flower":["你好福特，第一个"],"cake":["你好福特，第一个"],"breakfast":["你好福特，第一个"]},"cart":{"shop_detail":["你好福特，第一个","你好福特，查看购物车","你好福特，确认下单"],"cart_view":["你好福特，收起购物车","你好福特，清空购物车","你好福特，确认下单"]},"pay":{"submut":["你好福特，去支付"],"payment_success":["你好福特，我的外卖到哪了"],"detail":["你好福特，再来一单","你好福特，取消订单","你好福特，去支付","你好福特，我的外卖到哪了"]},"search":{"search":["你好福特，第一个"],"result":["你好福特，第一个"]},"order":{"order":["你好福特，第一个","你好福特，我的外卖到哪了"]}}}
     * tag : cff32f124639b13ca999c68c393e95c5
     * last_update_time : 1546583693
     */

    private ListBean list;
    private String tag;
    private int last_update_time;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(int last_update_time) {
        this.last_update_time = last_update_time;
    }

    public static class ListBean {
        /**
         * waimai : {"address":{"me":["你好福特，送到目的地","你好福特，送到公司","你好福特，送到家里","你好福特，第一个","你好福特，新增地址"],"search_result":["你好福特，第一个"],"empty_result":["你好福特，新增地址"]},"shop":{"list":["你好福特，第一个","你好福特，美食分类","你好福特，鲜花预定","你好福特，蛋糕预定"],"flower":["你好福特，第一个"],"cake":["你好福特，第一个"],"breakfast":["你好福特，第一个"]},"cart":{"shop_detail":["你好福特，第一个","你好福特，查看购物车","你好福特，确认下单"],"cart_view":["你好福特，收起购物车","你好福特，清空购物车","你好福特，确认下单"]},"pay":{"submut":["你好福特，去支付"],"payment_success":["你好福特，我的外卖到哪了"],"detail":["你好福特，再来一单","你好福特，取消订单","你好福特，去支付","你好福特，我的外卖到哪了"]},"search":{"search":["你好福特，第一个"],"result":["你好福特，第一个"]},"order":{"order":["你好福特，第一个","你好福特，我的外卖到哪了"]}}
         */

        private WaimaiBean waimai;

        public WaimaiBean getWaimai() {
            return waimai;
        }

        public void setWaimai(WaimaiBean waimai) {
            this.waimai = waimai;
        }

        public static class WaimaiBean {
            /**
             * address : {"me":["你好福特，送到目的地","你好福特，送到公司","你好福特，送到家里","你好福特，第一个","你好福特，新增地址"],"search_result":["你好福特，第一个"],"empty_result":["你好福特，新增地址"]}
             * shop : {"list":["你好福特，第一个","你好福特，美食分类","你好福特，鲜花预定","你好福特，蛋糕预定"],"flower":["你好福特，第一个"],"cake":["你好福特，第一个"],"breakfast":["你好福特，第一个"]}
             * cart : {"shop_detail":["你好福特，第一个","你好福特，查看购物车","你好福特，确认下单"],"cart_view":["你好福特，收起购物车","你好福特，清空购物车","你好福特，确认下单"]}
             * pay : {"submut":["你好福特，去支付"],"payment_success":["你好福特，我的外卖到哪了"],"detail":["你好福特，再来一单","你好福特，取消订单","你好福特，去支付","你好福特，我的外卖到哪了"]}
             * search : {"search":["你好福特，第一个"],"result":["你好福特，第一个"]}
             * order : {"order":["你好福特，第一个","你好福特，我的外卖到哪了"]}
             */

            private AddressBean address;
            private ShopBean shop;
            private CartBean cart;
            private PayBean pay;
            private SearchBean search;
            private OrderBean order;

            public AddressBean getAddress() {
                return address;
            }

            public void setAddress(AddressBean address) {
                this.address = address;
            }

            public ShopBean getShop() {
                return shop;
            }

            public void setShop(ShopBean shop) {
                this.shop = shop;
            }

            public CartBean getCart() {
                return cart;
            }

            public void setCart(CartBean cart) {
                this.cart = cart;
            }

            public PayBean getPay() {
                return pay;
            }

            public void setPay(PayBean pay) {
                this.pay = pay;
            }

            public SearchBean getSearch() {
                return search;
            }

            public void setSearch(SearchBean search) {
                this.search = search;
            }

            public OrderBean getOrder() {
                return order;
            }

            public void setOrder(OrderBean order) {
                this.order = order;
            }

            public static class AddressBean {
                private List<String> me;
                private List<String> search_result;
                private List<String> empty_result;

                public List<String> getMe() {
                    return me;
                }

                public void setMe(List<String> me) {
                    this.me = me;
                }

                public List<String> getSearch_result() {
                    return search_result;
                }

                public void setSearch_result(List<String> search_result) {
                    this.search_result = search_result;
                }

                public List<String> getEmpty_result() {
                    return empty_result;
                }

                public void setEmpty_result(List<String> empty_result) {
                    this.empty_result = empty_result;
                }
            }

            public static class ShopBean {
                private List<String> list;
                private List<String> flower;
                private List<String> cake;
                private List<String> breakfast;

                public List<String> getList() {
                    return list;
                }

                public void setList(List<String> list) {
                    this.list = list;
                }

                public List<String> getFlower() {
                    return flower;
                }

                public void setFlower(List<String> flower) {
                    this.flower = flower;
                }

                public List<String> getCake() {
                    return cake;
                }

                public void setCake(List<String> cake) {
                    this.cake = cake;
                }

                public List<String> getBreakfast() {
                    return breakfast;
                }

                public void setBreakfast(List<String> breakfast) {
                    this.breakfast = breakfast;
                }
            }

            public static class CartBean {
                private List<String> shop_detail;
                private List<String> cart_view;

                public List<String> getShop_detail() {
                    return shop_detail;
                }

                public void setShop_detail(List<String> shop_detail) {
                    this.shop_detail = shop_detail;
                }

                public List<String> getCart_view() {
                    return cart_view;
                }

                public void setCart_view(List<String> cart_view) {
                    this.cart_view = cart_view;
                }
            }

            public static class PayBean {
                private List<String> submut;
                private List<String> payment_success;
                private List<String> detail;

                public List<String> getSubmut() {
                    return submut;
                }

                public void setSubmut(List<String> submut) {
                    this.submut = submut;
                }

                public List<String> getPayment_success() {
                    return payment_success;
                }

                public void setPayment_success(List<String> payment_success) {
                    this.payment_success = payment_success;
                }

                public List<String> getDetail() {
                    return detail;
                }

                public void setDetail(List<String> detail) {
                    this.detail = detail;
                }
            }

            public static class SearchBean {
                private List<String> search;
                private List<String> result;

                public List<String> getSearch() {
                    return search;
                }

                public void setSearch(List<String> search) {
                    this.search = search;
                }

                public List<String> getResult() {
                    return result;
                }

                public void setResult(List<String> result) {
                    this.result = result;
                }
            }

            public static class OrderBean {
                private List<String> order;

                public List<String> getOrder() {
                    return order;
                }

                public void setOrder(List<String> order) {
                    this.order = order;
                }
            }
        }
    }
}
