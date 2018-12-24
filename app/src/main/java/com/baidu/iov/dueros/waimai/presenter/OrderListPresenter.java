package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.Log;

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
import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_SELECT;

public class OrderListPresenter extends Presenter<OrderListPresenter.OrderListUi> {

    private static final String TAG = OrderListPresenter.class.getSimpleName();

    private IOrderListModel mModel;

    @Override
    public void onCommandCallback(String cmd, String extra) {
        if (getUi() == null) {
            return;
        }

        switch (cmd) {
            case CMD_SELECT:
                getUi().selectListItem(Integer.parseInt(extra));
                break;
            case CMD_NO:
                getUi().close();
                break;
            default:
                break;
        }
    }

    @Override
    public void registerCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mVoiceManager) {
            ArrayList<String> cmdList = new ArrayList<String>();
            cmdList.add(CMD_NO);
            cmdList.add(CMD_SELECT);
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

    public interface OrderListUi extends Ui {
        void updateOrderCancel(OrderCancelResponse data);

        void update(OrderListResponse data);

        void failure(String msg);

        void orderCancelfail(String msg);

        void close();

        void selectListItem(int i);
    }

}
