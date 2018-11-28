package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.IPoifoodListModel;
import com.baidu.iov.dueros.waimai.model.PoifoodListModel;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewJsonBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoidetailinfoBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.faceos.client.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 18-10-18.
 */

public class PoifoodListPresenter extends Presenter<PoifoodListPresenter.PoifoodListUi> {
    private static final String TAG = PoifoodListPresenter.class.getSimpleName();
    private IPoifoodListModel mPoifoodListModel;

    @Override
    public void onCommandCallback(String cmd, String extra) {
    }

    @Override
    public void registerCmd(Context context) {

    }

    @Override
    public void unregisterCmd(Context context) {

    }

    public PoifoodListPresenter() {
        mPoifoodListModel = new PoifoodListModel();
    }

    @Override
    public void onUiReady(PoifoodListUi ui) {
        super.onUiReady(ui);
        mPoifoodListModel.onReady();
    }

    @Override
    public void onUiUnready(PoifoodListUi ui) {
        super.onUiUnready(ui);
        mPoifoodListModel.onDestroy();
    }

    public void requestData(ArrayMap<String, String> map) {
        mPoifoodListModel.requestPoifoodList(map, new RequestCallback<PoifoodListBean>() {

            @Override
            public void onSuccess(PoifoodListBean data) {
                if (getUi() != null) {

                    getUi().onPoifoodListSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {

                    getUi().onPoifoodListError(msg);
                }
            }
        });
        mPoifoodListModel.requestPoidetailinfo(map, new RequestCallback<PoidetailinfoBean>() {

            @Override
            public void onSuccess(PoidetailinfoBean data) {
                if (getUi() != null) {
                    getUi().onPoidetailinfoSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().onPoidetailinfoError(msg);
                }

            }
        });
        mPoifoodListModel.requestPoidetailinfo(map, new RequestCallback<PoidetailinfoBean>() {

            @Override
            public void onSuccess(PoidetailinfoBean data) {
                if (getUi() != null) {
                    getUi().onPoidetailinfoSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().onPoidetailinfoError(msg);
                }

            }
        });
    }

    public void requestOrderPreview(List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeanList,
                                    PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean, int delivery_time) {
        String payload = onCreatePayLoadJson(spusBeanList, poiInfoBean, delivery_time);
        OrderPreviewReqBean orderPreviewReqBean = new OrderPreviewReqBean();
        orderPreviewReqBean.setPayload(Encryption.encrypt(payload));
        mPoifoodListModel.requestOrderPreview(orderPreviewReqBean, new RequestCallback<OrderPreviewBean>() {
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
                                       PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean, int delivery_time) {


        OrderPreviewJsonBean orderPreviewJsonBean = new OrderPreviewJsonBean();
        OrderPreviewJsonBean.WmOrderingListBean wmOrderingListBean = new OrderPreviewJsonBean.WmOrderingListBean();
        wmOrderingListBean.setWm_poi_id(poiInfoBean.getWm_poi_id());
        wmOrderingListBean.setDelivery_time(delivery_time);
        wmOrderingListBean.setPay_type(2);

        List<OrderPreviewJsonBean.WmOrderingListBean.FoodListBean> foodListBeans = new ArrayList<>();
        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean : spusBeanList) {
            OrderPreviewJsonBean.WmOrderingListBean.FoodListBean foodListBean = new OrderPreviewJsonBean.WmOrderingListBean.FoodListBean();

            List<Long> food_spu_attr_ids = new ArrayList<>();
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean attrsBean : spusBean.getAttrs()) {
                long id = attrsBean.getChoiceAttrs().get(0).getId();
                food_spu_attr_ids.add(id);
            }
            foodListBean.setFood_spu_attr_ids(food_spu_attr_ids);
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


    public interface PoifoodListUi extends Ui {
        void onPoifoodListSuccess(PoifoodListBean data);

        void onPoidetailinfoSuccess(PoidetailinfoBean data);

        void onOrderPreviewSuccess(OrderPreviewBean data);

        void onOrderPreviewFailure(String msg);

        void onPoifoodListError(String error);

        void onPoidetailinfoError(String error);
    }
}
