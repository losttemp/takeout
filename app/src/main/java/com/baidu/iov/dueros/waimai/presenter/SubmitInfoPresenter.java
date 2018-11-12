package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.ISubmitInfoModel;
import com.baidu.iov.dueros.waimai.model.ISubmitOrderModel;
import com.baidu.iov.dueros.waimai.model.SubmitInfoImpl;
import com.baidu.iov.dueros.waimai.net.entity.request.ArriveTimeReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewJsonBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitJsonBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.faceos.client.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class SubmitInfoPresenter extends Presenter<SubmitInfoPresenter.SubmitInfoUi> {

    private ISubmitInfoModel mSubmitInfo;

    public SubmitInfoPresenter() {
        mSubmitInfo = new SubmitInfoImpl();
    }

    @Override
    public void onUiReady(SubmitInfoUi ui) {
        super.onUiReady(ui);
        mSubmitInfo.onReady();
    }

    @Override
    public void onUiUnready(SubmitInfoUi ui) {
        super.onUiUnready(ui);
        mSubmitInfo.onDestroy();
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

    public void requestArriveTimeData(Long wm_poi_id) {

        ArriveTimeReqBean arriveTimeReqBean = new ArriveTimeReqBean();
        arriveTimeReqBean.setLatitude(Constant.LATITUDE);
        arriveTimeReqBean.setLongitude(Constant.LONGITUDE);
        arriveTimeReqBean.setWm_poi_id(wm_poi_id);
        mSubmitInfo.requestArriveTimeList(arriveTimeReqBean, new RequestCallback<ArriveTimeBean>() {

            @Override
            public void onSuccess(ArriveTimeBean data) {
                if (null != getUi()) {
                    getUi().onArriveTimeSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (null != getUi()) {
                    getUi().onOrderPreviewFailure(msg);
                }
            }
        });

    }


    public void requestOrderSubmitData(AddressListBean.IovBean.DataBean addressData,
                                       PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean,
                                       List<OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean> wmOrderingPreviewDetailVoListBean) {


        OrderSubmitReq orderSubmitReq = new OrderSubmitReq();
        String payload = OnCreateOrderPayLoad(addressData, poiInfoBean, wmOrderingPreviewDetailVoListBean);
        orderSubmitReq.setPayload(Encryption.encrypt(payload));
        orderSubmitReq.setWm_pic_url(Encryption.encrypt(poiInfoBean.getPic_url()));
        mSubmitInfo.requestOrderSubmitData(orderSubmitReq, new RequestCallback<OrderSubmitBean>() {

            @Override
            public void onSuccess(OrderSubmitBean data) {
                if (null != getUi()) {
                    getUi().onOrderSubmitSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (null != getUi()) {
                    getUi().onOrderPreviewFailure(msg);
                }
            }
        });

    }

    private String OnCreateOrderPayLoad(AddressListBean.IovBean.DataBean addressData,
                                        PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean,
                                        List<OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean> wmOrderingPreviewDetailVoListBean) {
        OrderSubmitJsonBean orderSubmitJsonBean = new OrderSubmitJsonBean();
        try {

            orderSubmitJsonBean.setUser_phone(Encryption.desEncrypt(addressData.getUser_phone())/*"17638916218"*/);
            orderSubmitJsonBean.setPay_source(3);
            orderSubmitJsonBean.setReturn_url("www.meituan.com");

            OrderSubmitJsonBean.WmOrderingListBean wmOrderingListBean = new OrderSubmitJsonBean.WmOrderingListBean();
            wmOrderingListBean.setWm_poi_id(poiInfoBean.getWm_poi_id()/*1505351*/);
            wmOrderingListBean.setDelivery_time(0);
            wmOrderingListBean.setPay_type(2);

            List<OrderSubmitJsonBean.WmOrderingListBean.FoodListBean> foodListBeans = new ArrayList<>();
            for (OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean previewDetailVoListBean : wmOrderingPreviewDetailVoListBean) {

                OrderSubmitJsonBean.WmOrderingListBean.FoodListBean foodListBean = new OrderSubmitJsonBean.WmOrderingListBean.FoodListBean();
                foodListBean.setCount(previewDetailVoListBean.getCount()/*1*/);
                foodListBean.setWm_food_sku_id(previewDetailVoListBean.getWm_food_sku_id()/*1239556963*/);
                foodListBeans.add(foodListBean);
            }

            wmOrderingListBean.setFood_list(foodListBeans);
            orderSubmitJsonBean.setWm_ordering_list(wmOrderingListBean);

            OrderSubmitJsonBean.WmOrderingUserBean wmOrderingUserBean = new OrderSubmitJsonBean.WmOrderingUserBean();
            wmOrderingUserBean.setUser_phone(Encryption.desEncrypt(addressData.getUser_phone())/*"17638916218"*/);
            wmOrderingUserBean.setUser_name(Encryption.desEncrypt(addressData.getUser_name())/*"肖"*/);
            wmOrderingUserBean.setUser_address(Encryption.desEncrypt(addressData.getAddress())/*"美团大厦"*/);
            wmOrderingUserBean.setAddr_longitude(addressData.getLongitude()/*95369826*/);
            wmOrderingUserBean.setAddr_latitude(addressData.getLatitude()/*29735952*/);
            wmOrderingUserBean.setAddress_id(addressData.getMt_address_id());
            orderSubmitJsonBean.setWm_ordering_user(wmOrderingUserBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return GsonUtil.toJson(orderSubmitJsonBean);

    }


    public void requestOrderPreview(List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeanList,
                                    PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean,
                                    AddressListBean.IovBean.DataBean addressData) {
        String payload = onCreatePayLoadJson(spusBeanList, poiInfoBean, addressData);
        OrderPreviewReqBean orderPreviewReqBean = new OrderPreviewReqBean();
        orderPreviewReqBean.setPayload(Encryption.encrypt(payload));
        mSubmitInfo.requestOrderPreview(orderPreviewReqBean, new RequestCallback<OrderPreviewBean>() {
            @Override
            public void onSuccess(OrderPreviewBean data) {
                if (getUi() != null) {
                    getUi().onOrderPreviewSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (getUi() != null) {
                    getUi().onOrderPreviewFailure(msg);
                }
            }
        });
    }

    private String onCreatePayLoadJson(List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeanList,
                                       PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean,
                                       AddressListBean.IovBean.DataBean addressData) {


        OrderPreviewJsonBean orderPreviewJsonBean = new OrderPreviewJsonBean();

        orderPreviewJsonBean.setUser_phone(/*addressData.getUser_phone()*/"17638916218");
        OrderPreviewJsonBean.WmOrderingListBean wmOrderingListBean = new OrderPreviewJsonBean.WmOrderingListBean();
        wmOrderingListBean.setWm_poi_id(poiInfoBean.getWm_poi_id());
        wmOrderingListBean.setDelivery_time(0);
        wmOrderingListBean.setPay_type(2);

        List<OrderPreviewJsonBean.WmOrderingListBean.FoodListBean> foodListBeans = new ArrayList<>();
        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean : spusBeanList) {
            OrderPreviewJsonBean.WmOrderingListBean.FoodListBean foodListBean = new OrderPreviewJsonBean.WmOrderingListBean.FoodListBean();
            foodListBean.setCount(spusBean.getNumber());
            foodListBean.setWm_food_sku_id(spusBean.getSkus().get(0).getId());
            foodListBeans.add(foodListBean);
        }
        wmOrderingListBean.setFood_list(foodListBeans);
        orderPreviewJsonBean.setWm_ordering_list(wmOrderingListBean);

        OrderPreviewJsonBean.WmOrderingUserBean wmOrderingUserBean = new OrderPreviewJsonBean.WmOrderingUserBean();
        orderPreviewJsonBean.setWm_ordering_user(wmOrderingUserBean);
        return GsonUtil.toJson(orderPreviewJsonBean);
    }

    public interface SubmitInfoUi extends Ui {

        void onArriveTimeSuccess(ArriveTimeBean data);

        void onOrderPreviewSuccess(OrderPreviewBean data);

        void onOrderSubmitSuccess(OrderSubmitBean data);

        void onOrderPreviewFailure(String error);
    }
}
