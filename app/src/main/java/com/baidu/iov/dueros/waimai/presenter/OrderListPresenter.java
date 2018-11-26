package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.OrderListModel;
import com.baidu.iov.dueros.waimai.model.IOrderListModel;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.ArrayList;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_NO;

public class OrderListPresenter extends Presenter<OrderListPresenter.OrderListUi> {

    private static final String TAG = OrderListPresenter.class.getSimpleName();

    private IOrderListModel mModel;

    @Override
    public void onCommandCallback(String cmd, String extra) {
        if (CMD_NO.equals(cmd) && null != getUi()) {
            getUi().close();
        }
    }

    @Override
    public void registerCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mVoiceManager) {
            ArrayList<String> cmdList = new ArrayList<String>();
            cmdList.add(CMD_NO);
            //mVoiceController.registerCmd(context, cmdList, mVoiceCallback);
            mVoiceManager.registerCmd(context, cmdList, mVoiceCallback);
        }
    }

    @Override
    public void unregisterCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mVoiceManager) {
            mVoiceManager.unregisterCmd(context, mVoiceCallback);
        }
    }

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
        });
    }

    public interface OrderListUi extends Ui {
        void updateOrderCancel(OrderCancelResponse data);

        void update(OrderListResponse data);

        void failure(String msg);

        void orderCancelfail(String msg);

        void close();
    }

}
