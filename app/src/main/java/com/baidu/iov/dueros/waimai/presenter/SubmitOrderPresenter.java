package com.baidu.iov.dueros.waimai.presenter;


import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;

import com.baidu.iov.dueros.waimai.model.ISubmitOrderModel;
import com.baidu.iov.dueros.waimai.model.SubmitOrderImpl;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitJsonBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.faceos.client.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class SubmitOrderPresenter extends Presenter<SubmitOrderPresenter.SubmitOrderUi> {


    private ISubmitOrderModel mSubmitOrder;

    public SubmitOrderPresenter() {
        mSubmitOrder = new SubmitOrderImpl();
    }

    @Override
    public void onUiReady(SubmitOrderUi ui) {
        super.onUiReady(ui);
        mSubmitOrder.onReady();
    }

    @Override
    public void onUiUnready(SubmitOrderUi ui) {
        super.onUiUnready(ui);
        mSubmitOrder.onDestroy();
    }

    @Override
    public void onCommandCallback(String cmd, String extra) {

    }

    @Override
    public void registerCmd(Context context) {

    }

    @Override
    public void unregisterCmd(Context context) {

    }

    public void requestOrderSubmitData(AddressListBean.IovBean.DataBean addressData,
                                       PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean,
                                       List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList) {


        OrderSubmitReq orderSubmitReq = new OrderSubmitReq();
        String payload = OnCreateOrderPayLoad(addressData, poiInfoBean, productList);
        orderSubmitReq.setPayload(payload);
        orderSubmitReq.setWm_pic_url(poiInfoBean.getPic_url());
        mSubmitOrder.requestOrderSubmitData(orderSubmitReq, new RequestCallback<OrderSubmitBean>() {

            @Override
            public void onSuccess(OrderSubmitBean data) {
                if (null != getUi()) {
                    getUi().onOrderSubmitSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (null != getUi()) {
                    getUi().onError(msg);
                }
            }
        });

    }

    private String OnCreateOrderPayLoad(AddressListBean.IovBean.DataBean addressData,
                                        PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean,
                                        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList){

        OrderSubmitJsonBean orderSubmitJsonBean = new OrderSubmitJsonBean();
        orderSubmitJsonBean.setUser_phone(addressData.getUser_phone());
        orderSubmitJsonBean.setPay_source(3);

        OrderSubmitJsonBean.WmOrderingListBean wmOrderingListBean = new OrderSubmitJsonBean.WmOrderingListBean();
        wmOrderingListBean.setWm_poi_id(poiInfoBean.getWm_poi_id());
        wmOrderingListBean.setDelivery_time(0);
        wmOrderingListBean.setPay_type(2);

        List<OrderSubmitJsonBean.WmOrderingListBean.FoodListBean> foodListBeans = new ArrayList<>();
        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean : productList){
            OrderSubmitJsonBean.WmOrderingListBean.FoodListBean foodListBean = new OrderSubmitJsonBean.WmOrderingListBean.FoodListBean();
            foodListBean.setWm_food_sku_id(spusBean.getId());
            foodListBean.setCount(spusBean.getNumber());
            foodListBeans.add(foodListBean);
        }

        wmOrderingListBean.setFood_list(foodListBeans);
        orderSubmitJsonBean.setWm_ordering_list(wmOrderingListBean);

        OrderSubmitJsonBean.WmOrderingUserBean wmOrderingUserBean = new OrderSubmitJsonBean.WmOrderingUserBean();
        wmOrderingUserBean.setUser_phone(addressData.getUser_phone());
        wmOrderingUserBean.setUser_name(addressData.getUser_name());
        wmOrderingUserBean.setUser_address(addressData.getAddress());
        wmOrderingUserBean.setAddr_longitude(addressData.getLongitude());
        wmOrderingUserBean.setAddr_latitude(addressData.getLatitude());
        wmOrderingUserBean.setAddress_id(addressData.getMt_address_id());
        orderSubmitJsonBean.setWm_ordering_user(wmOrderingUserBean);

        return GsonUtil.toJson(orderSubmitJsonBean);

    }

    public interface SubmitOrderUi extends Ui {

        void onOrderSubmitSuccess(OrderSubmitBean data);

        void onError(String error);
    }
}
