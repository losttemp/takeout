package com.baidu.iov.dueros.waimai.presenter;

import com.baidu.iov.dueros.waimai.interfacedef.AccountCallback;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.IOrderDetailsModel;
import com.baidu.iov.dueros.waimai.model.OrderDetailsModel;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class OrderDetailsPresenter extends Presenter<OrderDetailsPresenter.OrderDetailsUi> {

    private static final String TAG = OrderDetailsPresenter.class.getSimpleName();

    private IOrderDetailsModel mModel;



    public OrderDetailsPresenter() {
        mModel = new OrderDetailsModel();
    }


    @Override
    public void onUiReady(OrderDetailsUi ui) {
        super.onUiReady(ui);
        mModel.onReady();
    }

    @Override
    public void onUiUnready(OrderDetailsUi ui) {
        super.onUiUnready(ui);
        mModel.onDestroy();
    }

    public void requestOrderDetails(OrderDetailsReq orderDetailsReq) {
        mModel.requestOrderDetails(orderDetailsReq, new RequestCallback<OrderDetailsResponse>() {
            @Override
            public void onSuccess(OrderDetailsResponse data) {
                if (getUi() != null) {
                    getUi().update(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().failure(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestOrderDetails getLogid: "+id);
            }
        });
    }

    public void requestAuthInfo() {
        mModel.requestAuthInfo(new AccountCallback() {
            @Override
            public void onSuccess(String msg) {
                if (null != getUi()) {
                    getUi().authSuccess(msg);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != getUi()) {
                    getUi().authFailure(msg);
                }
            }
        });
    }

    public void requestOrderCancel(OrderCancelReq orderCancelReq) {
        mModel.requestOrderCancel(orderCancelReq, new RequestCallback<OrderCancelResponse>() {
            @Override
            public void onSuccess(OrderCancelResponse data) {
                if (getUi() != null) {
                    getUi().updateOrderCancel(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().failure(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestOrderCancel getLogid: "+id);
            }
        });
    }

    public interface OrderDetailsUi extends Ui {
        void update(OrderDetailsResponse data);

        void updateOrderCancel(OrderCancelResponse data);

        void failure(String msg);

        void close();

        void authFailure(String msg);

        void authSuccess(String msg);
    }

}
