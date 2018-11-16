package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.FoodListAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.presenter.OrderDetailsPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;


import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


public class OrderDetailsActivity extends BaseActivity<OrderDetailsPresenter, OrderDetailsPresenter.OrderDetailsUi> implements OrderDetailsPresenter.OrderDetailsUi, View.OnClickListener {
    /**
     *
     */
    private TextView mArrivalTime, mBusinessName, mPackingFee, mDistributionFee, mDiscount, mRealPay, mContact, mAddress, mExpectedTime, mOrderId, mOrderTime, mPayMethod, mPayStatus;
    private ListView mFood;
    private Button mRepeatOrder, mPayOrder, mCancelOrder;
    private FoodListAdaper mFoodListAdaper;
    private OrderDetailsReq mOrderDetailsReq;
    private OrderCancelReq mOrderCancelReq;
    private long order_id;
    private String user_phone;
    private ArrayMap<String, String> map;
    private OrderDetailsResponse.MeituanBean.DataBean mOrderDetails = new OrderDetailsResponse.MeituanBean.DataBean();

    @Override
    OrderDetailsPresenter createPresenter() {
        return new OrderDetailsPresenter();
    }

    @Override
    OrderDetailsPresenter.OrderDetailsUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerCancel();
    }

    private void setListener() {
        findViewById(R.id.repeat_order).setOnClickListener(this);
        findViewById(R.id.cancel_order).setOnClickListener(this);
        findViewById(R.id.pay_order).setOnClickListener(this);
        findViewById(R.id.phone).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void initView() {
        mArrivalTime = (TextView) findViewById(R.id.arrival_time);
        mBusinessName = (TextView) findViewById(R.id.business_name);
        mPackingFee = (TextView) findViewById(R.id.packing_fee);
        mDistributionFee = (TextView) findViewById(R.id.distribution_fee);
        mDiscount = (TextView) findViewById(R.id.discount);
        mRealPay = (TextView) findViewById(R.id.real_pay);
        mContact = (TextView) findViewById(R.id.contact);
        mAddress = (TextView) findViewById(R.id.address);
        mExpectedTime = (TextView) findViewById(R.id.expected_time);
        mOrderId = (TextView) findViewById(R.id.order_id);
        mOrderTime = (TextView) findViewById(R.id.order_time);
        mPayMethod = (TextView) findViewById(R.id.pay_method);
        mFood = (ListView) findViewById(R.id.ll_food);
        mPayStatus = (TextView) findViewById(R.id.pay_status);
        mRepeatOrder = (Button) findViewById(R.id.repeat_order);
        mPayOrder = (Button) findViewById(R.id.pay_order);
        mCancelOrder = (Button) findViewById(R.id.cancel_order);
    }

    private void setTextView() {
        getPayStatus(mOrderDetails.getPay_status());
        mBusinessName.setText(mOrderDetails.getPoi_name());
        mPackingFee.setText("¥" + mOrderDetails.getBox_total_price());
        mDistributionFee.setText("¥" + mOrderDetails.getShipping_fee());
        mDiscount.setText("-¥" + (mOrderDetails.getOriginal_price() - mOrderDetails.getTotal()));
        mRealPay.setText("实付：¥" + mOrderDetails.getTotal());
        mContact.setText(mOrderDetails.getRecipient_phone());
        mAddress.setText(mOrderDetails.getRecipient_address());
        mExpectedTime.setText("2018-10-19 （周五）18:30");
        mOrderId.setText(String.valueOf(mOrderDetails.getOrder_id()));
        mOrderTime.setText(String.valueOf(mOrderDetails.getCtime()));
        if (mOrderDetails.getWm_order_pay_type() == 1) {
            mPayMethod.setText(R.string.order_pay_type1);
        } else if (mOrderDetails.getWm_order_pay_type() == 2) {
            mPayMethod.setText(R.string.order_pay_type2);
        }
        List<OrderDetailsResponse.MeituanBean.DataBean.FoodListBean> mfoodList = mOrderDetails.getFood_list();
        mFoodListAdaper = new FoodListAdaper(this);
        mFoodListAdaper.setData(mfoodList);
        mFood.setAdapter(mFoodListAdaper);
    }

    private void hidePayView() {
        mCancelOrder.setVisibility(View.GONE);
        mPayOrder.setVisibility(View.GONE);
        mRepeatOrder.setVisibility(View.GONE);
        mArrivalTime.setVisibility(View.GONE);
    }

    private void getPayStatus(int status) {
        hidePayView();
        if (status == 1) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mPayStatus.setText(R.string.pay_done);
        } else if (status == 2) {
            mPayOrder.setVisibility(View.VISIBLE);
            mCancelOrder.setVisibility(View.VISIBLE);
            timerStart();
        } else if (status == 3) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mCancelOrder.setVisibility(View.VISIBLE);
            mArrivalTime.setVisibility(View.VISIBLE);
            String arrivalTime = formatTime(mOrderDetails.getEstimate_arrival_time());
            mPayStatus.setText(R.string.have_paid);
            mArrivalTime.setText(String.format(getResources().getString(R.string.arrival_time), arrivalTime));
        }
    }

    public String formatTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        return format.format(date);
    }

    private void initData() {
        String phone = Encryption.encrypt(user_phone);
        order_id = getIntent().getLongExtra(Constant.ORDER_ID,-1);
        mOrderDetailsReq = new OrderDetailsReq();
        mOrderDetailsReq.setId(order_id);
        loadData();
    }

    public void timerCancel() {
        mTimer.cancel();
    }

    public void timerStart() {
        mTimer.start();
    }

    private CountDownTimer mTimer = new CountDownTimer(15 * 60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mPayStatus.setText(String.format(getResources().getString(R.string.count_down_timer), formatCountDownTime(millisUntilFinished)));
        }

        @Override
        public void onFinish() {

            mPayStatus.setText(String.format(getResources().getString(R.string.count_down_timer), "00:00"));
        }
    };

    public String formatCountDownTime(long millisecond) {
        int minute;
        int second;

        minute = (int) ((millisecond / 1000) / 60);
        second = (int) ((millisecond / 1000) % 60);

        if (minute < 10) {
            if (second < 10) {
                return "0" + minute + ":" + "0" + second;
            } else {
                return "0" + minute + ":" + second;
            }
        } else {
            if (second < 10) {
                return minute + ":" + "0" + second;
            } else {
                return minute + ":" + second;
            }
        }
    }

    private void loadData() {
        getPresenter().requestOrderDetails(mOrderDetailsReq);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.repeat_order:
                Intent intentFoodList = new Intent(OrderDetailsActivity.this, FoodListActivity.class);
                intentFoodList.putExtra(Constant.STORE_ID, mOrderDetails.getWm_poi_id());
                startActivity(intentFoodList);
                finish();
                break;
            case R.id.pay_order:
                Intent intentPayment = new Intent(OrderDetailsActivity.this, PaymentActivity.class);
                startActivity(intentPayment);
                finish();
                break;
            case R.id.cancel_order:
                mOrderCancelReq = new OrderCancelReq(mOrderDetailsReq.getId(), mOrderDetailsReq.getPhone());
                ConfirmDialog dialog = new ConfirmDialog.Builder(this)
                        .setTitle(R.string.order_cancel_title)
                        .setMessage(R.string.order_cancel_message)
                        .setNegativeButton(R.string.order_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getPresenter().requestOrderCancel(mOrderCancelReq);
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.order_cancel_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCloseButton(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                break;
            case R.id.phone:
                ConfirmDialog dialog1 = new ConfirmDialog.Builder(this)
                        .setTitle(R.string.remind_title)
                        .setMessage(R.string.remind_message)
                        .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.remind_phone, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                Uri data = Uri.parse("tel:" + "10109777");
                                intent.setData(data);
                                if (ActivityCompat.checkSelfPermission(OrderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .setCloseButton(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog1.show();
                break;
        }
    }

    @Override
    public void update(OrderDetailsResponse data) {
        mOrderDetails = data.getMeituan().getData();
        setTextView();
    }

    @Override
    public void updateOrderCancel(OrderCancelResponse data) {
        Toast toast = new Toast(this);
        toast.makeText(this,R.string.order_cancel_toast,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void failure(String msg) {

    }

    @Override
    public void close() {
        finish();
    }
}
