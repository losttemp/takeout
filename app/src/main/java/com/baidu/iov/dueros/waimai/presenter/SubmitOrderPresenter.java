package com.baidu.iov.dueros.waimai.presenter;


import android.content.Context;
import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;

import com.baidu.iov.dueros.waimai.model.IOrderDetailsModel;
import com.baidu.iov.dueros.waimai.model.ISubmitOrderModel;
import com.baidu.iov.dueros.waimai.model.OrderDetailsModel;
import com.baidu.iov.dueros.waimai.model.SubmitOrderImpl;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitJsonBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderSubmitReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.faceos.client.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class SubmitOrderPresenter extends Presenter<SubmitOrderPresenter.SubmitOrderUi> {

    private ISubmitOrderModel mSubmitOrder;

    private static final String TAG = SubmitOrderPresenter.class.getSimpleName();

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

    public void requestOrderDetails(OrderDetailsReq orderDetailsReq) {
        mSubmitOrder.requestOrderDetails(orderDetailsReq, new RequestCallback<OrderDetailsResponse>() {
            @Override
            public void onSuccess(OrderDetailsResponse data) {
                if (getUi() != null) {
                    getUi().onOrderSubmitSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().onSubmitFailure(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestOrderPreview getLogid: "+id);
            }
        });
    }


    public interface SubmitOrderUi extends Ui {

        void onOrderSubmitSuccess(OrderDetailsResponse data);

        void onSubmitFailure(String msg);
    }
}
