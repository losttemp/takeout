package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.iov.dueros.waimai.view.NoClikRecyclerView;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class OrderDetailsActivity extends BaseActivity<OrderDetailsPresenter, OrderDetailsPresenter.OrderDetailsUi> implements OrderDetailsPresenter.OrderDetailsUi, View.OnClickListener {
    /**
     *
     */
    private TextView mArrivalTime, mBusinessName, mPackingFee, mDistributionFee, mDiscount, mRealPay, mContact, mAddress, mExpectedTime, mOrderId, mOrderTime, mPayMethod, mPayStatus;
    private NoClikRecyclerView mFoodListView;
    private Button mRepeatOrder, mPayOrder, mCancelOrder;
    private FoodListAdaper mFoodListAdaper;
    private OrderDetailsReq mOrderDetailsReq;
    private OrderCancelReq mOrderCancelReq;
    private long order_id;
    private long expectedTime;
    private NumberFormat mNumberFormat;
    private OrderDetailsResponse.MeituanBean.DataBean mOrderDetails = new OrderDetailsResponse.MeituanBean.DataBean();
    private static final int REQUEST_CODE_CALL_PHONE = 600;

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
        mArrivalTime = findViewById(R.id.arrival_time);
        mBusinessName = findViewById(R.id.business_name);
        mPackingFee = findViewById(R.id.packing_fee);
        mDistributionFee = findViewById(R.id.distribution_fee);
        mDiscount = findViewById(R.id.discount);
        mRealPay = findViewById(R.id.real_pay);
        mContact = findViewById(R.id.contact);
        mAddress = findViewById(R.id.address);
        mExpectedTime = findViewById(R.id.expected_time);
        mOrderId = findViewById(R.id.order_id);
        mOrderTime = findViewById(R.id.order_time);
        mPayMethod = findViewById(R.id.pay_method);
        mFoodListView = findViewById(R.id.ll_food);
        mPayStatus = findViewById(R.id.pay_status);
        mRepeatOrder = findViewById(R.id.repeat_order);
        mPayOrder = findViewById(R.id.pay_order);
        mCancelOrder = findViewById(R.id.cancel_order);
    }

    private void setTextView() {

        getPayStatus(mOrderDetails.getPay_status());

        mNumberFormat = new DecimalFormat("##.##");

        long orderTime = mOrderDetails.getOrder_time();
        long orderId = mOrderDetails.getOrder_id();
        double boxTotalPrice = mOrderDetails.getBox_total_price();
        double totalCost = mOrderDetails.getTotal();
        double shippingFee = mOrderDetails.getShipping_fee();
        double discount = mOrderDetails.getOriginal_price() - mOrderDetails.getTotal();
        String shopName = mOrderDetails.getPoi_name();
        String address = mOrderDetails.getRecipient_address();
        String phone = mOrderDetails.getRecipient_phone();

        mContact.setText(phone);
        mAddress.setText(address);
        mBusinessName.setText(shopName);
        mDistributionFee.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(shippingFee)));
        mDiscount.setText(String.format(getString(R.string.discount_money), mNumberFormat.format(discount)));
        mRealPay.setText(String.format(getString(R.string.actual_payment), mNumberFormat.format(totalCost)));
        mPackingFee.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(boxTotalPrice)));
        mOrderId.setText(String.valueOf(orderId));
        mOrderTime.setText(String.valueOf(formatTime(orderTime, false)));

        if (mOrderDetails.getWm_order_pay_type() == 1) {
            mPayMethod.setText(R.string.order_pay_type1);
        } else if (mOrderDetails.getWm_order_pay_type() == 2) {
            mPayMethod.setText(R.string.order_pay_type2);
        }

        if (expectedTime == 0) {

            mExpectedTime.setText(getString(R.string.expected_delivery_immediately));
        } else {

            mExpectedTime.setText(formatTime(expectedTime, false));
        }

        List<OrderDetailsResponse.MeituanBean.DataBean.FoodListBean> mfoodList = mOrderDetails.getFood_list();

        int orientation = RecyclerView.VERTICAL;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, orientation, false);
        mFoodListView.setLayoutManager(mLayoutManager);
        mFoodListAdaper = new FoodListAdaper(this);
        mFoodListView.setAdapter(mFoodListAdaper);
        mFoodListAdaper.setData(mfoodList);
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
            String arrivalTime = formatTime(mOrderDetails.getEstimate_arrival_time(), true);
            mPayStatus.setText(R.string.have_paid);
            mArrivalTime.setText(String.format(getResources().getString(R.string.arrival_time), arrivalTime));
        }
    }


    public String formatTime(long time, boolean isArrivalTime) {
        time = time * 1000L;
        Date date = new Date(time);
        SimpleDateFormat format;
        if (isArrivalTime) {
            format = new SimpleDateFormat("HH:mm");
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }

        return format.format(date);
    }

    private void initData() {
        order_id = getIntent().getLongExtra(Constant.ORDER_ID, -1);
        expectedTime = getIntent().getLongExtra(Constant.EXPECTED_TIME, 0);
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
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (ActivityCompat.checkSelfPermission(OrderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(OrderDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
                                    } else {
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        Uri data = Uri.parse("tel:" + "10109777");
                                        intent.setData(data);
                                        startActivity(intent);
                                    }
                                }
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
        if (data.getMeituan().getCode() == 0) {
            Toast toast = new Toast(this);
            toast.makeText(this, R.string.order_cancel_toast, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            String msg = data.getMeituan().getErrorInfo().getName();
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void failure(String msg) {

    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission was granted, yay! Do the contacts-related task you need to do.
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "10109777");
                intent.setData(data);
                startActivity(intent);
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
                finish();
            }
        }
    }
}
