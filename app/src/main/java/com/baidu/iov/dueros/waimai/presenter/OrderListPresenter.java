package com.baidu.iov.dueros.waimai.presenter;

import com.baidu.iov.dueros.waimai.interfacedef.AccountCallback;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.IOrderListModel;
import com.baidu.iov.dueros.waimai.model.OrderListModel;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class OrderListPresenter extends Presenter<OrderListPresenter.OrderListUi> {

    private static final String TAG = OrderListPresenter.class.getSimpleName();

    private IOrderListModel mModel;


    public OrderListPresenter() {
        mModel = new OrderListModel();
    }

    @Override
    public void onUiReady(OrderListUi ui) {
        super.onUiReady(ui);
        mModel.onReady();
    }

    @Override
    public void onUiUnready(OrderListUi ui) {
        super.onUiUnready(ui);
        mModel.onDestroy();
    }

    public void requestOrderList(OrderListReq orderlistReq) {

        mModel.requestOrderList(orderlistReq, new RequestCallback<OrderListResponse>() {
            @Override
            public void onSuccess(OrderListResponse data) {
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
                Lg.getInstance().d(TAG, "requestOrderList getLogid: "+id);
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
                    getUi().orderCancelfail(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestOrderCancel getLogid: "+id);
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

    public void requestOrderDetails(OrderDetailsReq orderDetailsReq) {
        mModel.requestOrderDetails(orderDetailsReq, new RequestCallback<OrderDetailsResponse>() {
            @Override
            public void onSuccess(OrderDetailsResponse data) {
                if (getUi() != null) {
                    getUi().orderDetailsSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().orderDetailsFailure(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestOrderDetails getLogid: "+id);
            }
        });
    }

    public interface OrderListUi extends Ui {
        void updateOrderCancel(OrderCancelResponse data);

        void update(OrderListResponse data);

        void failure(String msg);

        void orderCancelfail(String msg);

        void authFailure(String msg);

        void authSuccess(String msg);

        void close();

        void selectListItem(int i);

        void nextPage(boolean isNextPage);

        void orderDetailsSuccess(OrderDetailsResponse data);

        void orderDetailsFailure(String msg);
    }

}
