package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.ISubmitInfoModel;
import com.baidu.iov.dueros.waimai.model.SubmitInfoImpl;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;

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

    public void requestArriveTimeData(ArrayMap<String, String> map) {
        mSubmitInfo.requestArriveTimeList(map, new RequestCallback<ArriveTimeBean>() {

            @Override
            public void onSuccess(ArriveTimeBean data) {
                if (null != getUi()){
                    getUi().onArriveTimeSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (null != getUi()){
                    getUi().onError(msg);
                }
            }
        });

    }

    public void requestOrderSubmitData(OrderSubmitReqBean orderSubmitReqBean) {
        mSubmitInfo.requestOrderSubmitData(orderSubmitReqBean, new RequestCallback<OrderSubmitBean>() {

            @Override
            public void onSuccess(OrderSubmitBean data) {
                if (null != getUi()){
                    getUi().onOrderSubmitSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (null != getUi()){
                    getUi().onError(msg);
                }
            }
        });

    }


    public void requestOrderPreview(OrderPreviewReqBean orderPreviewReqBean){
        mSubmitInfo.requestOrderPreview(orderPreviewReqBean, new RequestCallback<OrderPreviewBean>() {
            @Override
            public void onSuccess(OrderPreviewBean data) {
                if (getUi() != null){
                    getUi().onOrderPreviewSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (getUi() != null){
                    getUi().onError(msg);
                }
            }
        });
    }

    public interface SubmitInfoUi extends Ui {

        void onArriveTimeSuccess(ArriveTimeBean data);
        void onOrderSubmitSuccess(OrderSubmitBean data);
        void onOrderPreviewSuccess(OrderPreviewBean data);
        void onError(String error);
    }
}
