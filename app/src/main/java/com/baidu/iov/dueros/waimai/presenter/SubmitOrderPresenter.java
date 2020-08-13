package com.baidu.iov.dueros.waimai.presenter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.ISubmitOrderModel;
import com.baidu.iov.dueros.waimai.model.SubmitOrderImpl;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderOwnerReq;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderOwnerBean;
import com.baidu.iov.dueros.waimai.ui.AddressSelectActivity;
import com.baidu.iov.dueros.waimai.ui.HomeActivity;
import com.baidu.iov.dueros.waimai.ui.TakeawayLoginActivity;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;

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


    public void requestCheckOrderOwner(long order_id) {
        OrderOwnerReq req = new OrderOwnerReq();
        req.setOrder_id(order_id);
        mSubmitOrder.requestCheckOrderOwner(req, new RequestCallback<OrderOwnerBean>() {
            @Override
            public void onSuccess(OrderOwnerBean data) {
                if (null != getUi()) {
                    getUi().onCheckOrderOwnerSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != getUi()) {
                    getUi().onCheckOrderOwnerError(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestCheckOrderOwner getLogId:" + id);
            }
        });
    }

    public void requesLogout() {
        mSubmitOrder.requesLogout(new RequestCallback<LogoutBean>() {
            @Override
            public void onSuccess(LogoutBean data) {
                if (getUi() != null) {
                    getUi().onLogoutSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().onLogoutError(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requesLogout getLogId:" + id);
            }
        });
    }


    public interface SubmitOrderUi extends Ui {

        void onOrderSubmitSuccess(OrderDetailsResponse data);

        void onSubmitFailure(String msg);

        void onCheckOrderOwnerSuccess(OrderOwnerBean orderOwnerBean);

        void onCheckOrderOwnerError(String error);

        void onLogoutSuccess(LogoutBean data);

        void onLogoutError(String msg);
    }

    /**
     * 提示用户再来一单的账号不同
     *
     * @param mContext
     */
    public void startCheckOrderOwnerDialog(final Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.permission_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        Button btn_sure = layout.findViewById(R.id.to_setting);
        Button btn_cancel = layout.findViewById(R.id.i_know);
        TextView textView = layout.findViewById(R.id.persion_hint);
        TextView textViewTwo = layout.findViewById(R.id.persion_hint_two);
        textView.setText("下单订单账号与当前登录的美团账号不");
        textViewTwo.setText("一致，是否需切换登录账号");
        btn_sure.setText(mContext.getString(R.string.ok));
        btn_cancel.setText(mContext.getString(R.string.close));
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setWindowAnimations(R.style.Dialog);
        dialog.show();
        if (dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setLayout((int) mContext.getResources().getDimension(R.dimen.px912dp), (int) mContext.getResources().getDimension(R.dimen.px516dp));
            window.setBackgroundDrawableResource(R.drawable.permission_dialog_bg);
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER_HORIZONTAL);
            window.setGravity(Gravity.TOP);
            lp.y = (int) mContext.getResources().getDimension(R.dimen.px480dp);
            window.setAttributes(lp);
        }
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requesLogout();
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AtyContainer.getInstance().finishAllActivity();
                Intent intent;
                if (CacheUtils.getAddrTime() == 0) {
                    if (TextUtils.isEmpty(CacheUtils.getAddress())) {
                        intent = new Intent(mContext, TakeawayLoginActivity.class);
                    } else {
                        intent = new Intent(mContext, AddressSelectActivity.class);
                    }
                } else {
                    intent = new Intent(mContext, HomeActivity.class);
                }
                mContext.startActivity(intent);
            }
        });
    }
}
