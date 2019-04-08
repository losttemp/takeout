package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.FoodListAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.presenter.OrderDetailsPresenter;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.BackgroundUtils;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.DeviceUtils;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.iov.dueros.waimai.view.NoClikRecyclerView;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class OrderDetailsActivity extends BaseActivity<OrderDetailsPresenter, OrderDetailsPresenter.OrderDetailsUi> implements OrderDetailsPresenter.OrderDetailsUi, View.OnClickListener {
    /**
     *
     */
    private static final String TAG = OrderDetailsActivity.class.getSimpleName();

    private TextView mArrivalTime, mBusinessName, mPackingFee, mDistributionFee, mDiscount, mRealPay, mContact, mAddress, mExpectedTime, mOrderId, mOrderTime, mPayMethod, mTimerTv, mDeliveryType,mPhone,mName;
    private NoClikRecyclerView mFoodListView;
    private Button mRepeatOrder, mPayOrder, mCancelOrder;
    private FoodListAdaper mFoodListAdaper;
    private LinearLayout mDiscountsLayout;
    private OrderDetailsReq mOrderDetailsReq;
    private OrderCancelReq mOrderCancelReq;
    private long order_id;
    private long expectedTime;
    private String mStoreId ="";
    private int mPayStatus;
    private NumberFormat mNumberFormat;
    private View loadingView;
    private OrderDetailsResponse.MeituanBean.DataBean mOrderDetails = new OrderDetailsResponse.MeituanBean.DataBean();
    private static final int REQUEST_CODE_CALL_PHONE = 600;
    private final int IOV_STATUS_ZERO = 0; //待支付
    private final int IOV_STATUS_WAITING = 1; //待支付
    private final int IOV_STATUS_PAID = 2; //已支付
    private final int IOV_STATUS_NOTIFY_RESTAURANT = 3; //待商家接单
    private final int IOV_STATUS_RESTAURANT_CONFIRM = 4; //商家已接单
    private final int IOV_STATUS_DELIVERING = 5; //派送中
    private final int IOV_STATUS_FINISHED = 6; //已完成
    private final int IOV_STATUS_PAYMENT_FAILED = 7; //支付失败
    private final int IOV_STATUS_CANCELED = 8; //已取消
    private final int IOV_STATUS_REFUNDING = 9; //退款中
    private final int IOV_STATUS_REFUNDED = 10; //已退款
    private final int IOV_STATUS_REFUND_FAILED = 11; //退款失败
    private View networkView;
    private View contentView;
    private boolean isTimeEnd;

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
        mFoodListView.removeAllViews();
        mFoodListView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtil.getNetWorkState(this)) {
            loadingView.setVisibility(View.VISIBLE);
            loadData();
            GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getPay().getDetail(),Constant.TTS_PAY_DETAIL);
        } else {
            if (null != networkView) {
                contentView.setVisibility(View.GONE);
                networkView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setListener() {
        findViewById(R.id.repeat_order).setOnClickListener(this);
        findViewById(R.id.cancel_order).setOnClickListener(this);
        findViewById(R.id.pay_order).setOnClickListener(this);
        findViewById(R.id.phone).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.no_internet_btn).setOnClickListener(this);
    }

    private void initView() {
        mDeliveryType = findViewById(R.id.delivery_type);
        mArrivalTime = findViewById(R.id.arrival_time);
        mBusinessName = findViewById(R.id.business_name);
        mPackingFee = findViewById(R.id.packing_fee);
        mDistributionFee = findViewById(R.id.distribution_fee);
        mDiscount = findViewById(R.id.discount);
        mRealPay = findViewById(R.id.real_pay);
        mPhone = findViewById(R.id.tv_phone);
        mName = findViewById(R.id.tv_name);
        mAddress = findViewById(R.id.address);
        mExpectedTime = findViewById(R.id.expected_time);
        mOrderId = findViewById(R.id.order_id);
        mOrderTime = findViewById(R.id.order_time);
        mPayMethod = findViewById(R.id.pay_method);
        mFoodListView = findViewById(R.id.ll_food);
        mTimerTv = findViewById(R.id.pay_status);
        mRepeatOrder = findViewById(R.id.repeat_order);
        mPayOrder = findViewById(R.id.pay_order);
        mCancelOrder = findViewById(R.id.cancel_order);
        networkView = findViewById(R.id.network_view);
        contentView = findViewById(R.id.order_details_content_layout);
        mDiscountsLayout = findViewById(R.id.discounts_layout);
        loadingView = findViewById(R.id.submit_order_loading);
    }

    private void setTextView() {

        getPayStatus(mOrderDetails.getOut_trade_status());
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
        String name = mOrderDetails.getRecipient_name();

        try {
            address = Encryption.desEncrypt(mOrderDetails.getRecipient_address());
            phone = Encryption.desEncrypt(mOrderDetails.getRecipient_phone());
            name = Encryption.desEncrypt(mOrderDetails.getRecipient_name());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mName.setText(name);
        mPhone.setText(phone);
//        if (mName.getText().length() > 17) {
//            mName.setWidth((int) getResources().getDimension(R.dimen.px400dp));
//        }
        mAddress.setText(address);
//        if (mAddress.getText().length() > 17) {
//            mAddress.setWidth((int) getResources().getDimension(R.dimen.px520dp));
//        }
        mBusinessName.setText(shopName);
        mDistributionFee.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(shippingFee)));
        if (discount != 0) {
            mDiscount.setText(String.format(getString(R.string.discount_money), mNumberFormat.format(discount)));
        } else {
            mDiscountsLayout.setVisibility(View.GONE);
        }
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

        if (mOrderDetails.getDelivery_type() == 1) {
            mDeliveryType.setText(getString(R.string.order_meituan));
        } else {
            mDeliveryType.setText(getString(R.string.order_not_meituan));
        }

        List<OrderDetailsResponse.MeituanBean.DataBean.FoodListBean> mfoodList = mOrderDetails.getFood_list();

        int orientation = RecyclerView.VERTICAL;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, orientation, false);
        mFoodListView.setLayoutManager(mLayoutManager);
        mFoodListAdaper = new FoodListAdaper(this);
        mFoodListView.setAdapter(mFoodListAdaper);
        mFoodListAdaper.setData(mfoodList);
    }

    public void showAllDiscountItem() {
        List<OrderDetailsResponse.MeituanBean.DataBean.DiscountsBean> discountsBeanList = mOrderDetails.getDiscounts();
        mDiscountsLayout.removeAllViews();
        for (OrderDetailsResponse.MeituanBean.DataBean.DiscountsBean discountsBean : discountsBeanList) {
            LayoutInflater inflater = this.getLayoutInflater();
            final RelativeLayout discountItem = (RelativeLayout) inflater.inflate(R.layout.discount_list_item, mDiscountsLayout, false);
            TextView discount_name_tv = discountItem.findViewById(R.id.discount_name);
            discount_name_tv.setText(discountsBean.getName());
            TextView discount_info_tv = discountItem.findViewById(R.id.discount);
            discount_info_tv.setText(String.format(getString(R.string.discount_money), mNumberFormat.format(discountsBean.getReduceFree())));
            mDiscountsLayout.addView(discountItem);
        }

    }

    private void hidePayView() {
        mCancelOrder.setVisibility(View.GONE);
        mPayOrder.setVisibility(View.GONE);
        mRepeatOrder.setVisibility(View.GONE);
        mArrivalTime.setVisibility(View.GONE);
    }

    private void getPayStatus(int status) {
        hidePayView();
        if (status == IOV_STATUS_ZERO || status == IOV_STATUS_WAITING) {
            mPayOrder.setVisibility(View.VISIBLE);
            mCancelOrder.setVisibility(View.VISIBLE);
            mArrivalTime.setVisibility(View.VISIBLE);
            mTimerTv.setText(R.string.waiting_to_pay);
            timerStart();
        } else if (status == IOV_STATUS_PAID) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mCancelOrder.setVisibility(View.VISIBLE);
            mArrivalTime.setVisibility(View.VISIBLE);
            String arrivalTime = formatTime(mOrderDetails.getEstimate_arrival_time(), true);
            mTimerTv.setText(R.string.have_paid);
            mArrivalTime.setText(String.format(getResources().getString(R.string.arrival_time), arrivalTime));
        } else if (status == IOV_STATUS_NOTIFY_RESTAURANT) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mCancelOrder.setVisibility(View.VISIBLE);
            mArrivalTime.setVisibility(View.VISIBLE);
            String arrivalTime = formatTime(mOrderDetails.getEstimate_arrival_time(), true);
            mArrivalTime.setText(String.format(getResources().getString(R.string.arrival_time), arrivalTime));
//            mTimerTv.setText(R.string.have_paid);
            mTimerTv.setText(R.string.notify_restaurant);
        } else if (status == IOV_STATUS_RESTAURANT_CONFIRM) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mCancelOrder.setVisibility(View.VISIBLE);
            mArrivalTime.setVisibility(View.VISIBLE);
            String arrivalTime = formatTime(mOrderDetails.getEstimate_arrival_time(), true);
//            mTimerTv.setText(R.string.have_paid);
            mArrivalTime.setText(String.format(getResources().getString(R.string.arrival_time), arrivalTime));
            mTimerTv.setText(R.string.restaurant_confirm);
        } else if (status == IOV_STATUS_DELIVERING) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mCancelOrder.setVisibility(View.VISIBLE);
            mArrivalTime.setVisibility(View.VISIBLE);
            String arrivalTime = formatTime(mOrderDetails.getEstimate_arrival_time(), true);
            mArrivalTime.setText(String.format(getResources().getString(R.string.arrival_time), arrivalTime));
            mTimerTv.setText(R.string.delivering);
        } else if (status == IOV_STATUS_FINISHED) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mArrivalTime.setVisibility(View.VISIBLE);
            String arrivalTime = formatTime(mOrderDetails.getEstimate_arrival_time(), true);
            mArrivalTime.setText(String.format(getResources().getString(R.string.arrival_time), arrivalTime));
            mTimerTv.setText(R.string.pay_done);
        } else if (status == IOV_STATUS_PAYMENT_FAILED) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mTimerTv.setText(R.string.pay_fail);
        } else if (status == IOV_STATUS_CANCELED) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mTimerTv.setText(R.string.pay_cancel);
        } else if (status == IOV_STATUS_REFUNDING) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mTimerTv.setText(R.string.pay_refunding);
        } else if (status == IOV_STATUS_REFUNDED) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mTimerTv.setText(R.string.pay_refunded);
        } else if (status == IOV_STATUS_REFUND_FAILED) {
            mRepeatOrder.setVisibility(View.VISIBLE);
            mTimerTv.setText(R.string.refund_fail);
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
        mStoreId = getIntent().getStringExtra(Constant.STORE_ID);
        order_id = getIntent().getLongExtra(Constant.ORDER_ID, -1);
        if (null != getIntent().getStringExtra("extra") && !getIntent().getStringExtra("extra").equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("extra"));
                order_id = Long.valueOf(jsonObject.getString("order_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        expectedTime = Long.parseLong(String.valueOf(getIntent().getIntExtra(Constant.EXPECTED_TIME, 0)));
        mOrderDetailsReq = new OrderDetailsReq();
        mOrderDetailsReq.setId(order_id);
    }

    public void timerCancel() {
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void timerStart() {
        mTimer.start();
    }

    private CountDownTimer mTimer;

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
                Entry.getInstance().onEvent(Constant.GOBACK_TO_PREACTIVITY, EventType.TOUCH_TYPE);
                if (getIntent().getBooleanExtra("flag",false)){
                    finish();
                }else {
                    AtyContainer.getInstance().finishAllActivity();
                }
//                finish();
                break;
            case R.id.repeat_order:
                Intent intentFoodList = new Intent(OrderDetailsActivity.this, FoodListActivity.class);
                intentFoodList.putExtra(Constant.STORE_ID, String.valueOf(mOrderDetails.getWm_poi_id()));
                intentFoodList.putExtra(Constant.ORDER_LSIT_BEAN, mOrderDetails);
                intentFoodList.putExtra(Constant.ONE_MORE_ORDER, true);
                intentFoodList.putExtra("flag",true);
                startActivity(intentFoodList);
                finish();
                break;
            case R.id.pay_order:
                if (CacheUtils.getAuth()) {
                    orderSubmit();
                } else {
                    getPresenter().requestAuthInfo();
                }
                break;
            case R.id.cancel_order:
                mOrderCancelReq = new OrderCancelReq(mOrderDetailsReq.getId());
                if (mRepeatOrder.getVisibility() == View.VISIBLE){
                    ConfirmDialog dialog = new ConfirmDialog.Builder(this)
                            .setTitle(R.string.order_cancel_title)
                            .setMessage(R.string.order_cancel_message)
                            .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
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
                }else {
                    ConfirmDialog dialog = new ConfirmDialog.Builder(this)
                            .setTitle(R.string.order_cancel_title)
                            .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
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
                }
                break;
            case R.id.phone:
                Entry.getInstance().onEvent(Constant.CALL_FOR_CANCLE_ORDER, EventType.TOUCH_TYPE);
                String mMessage = getResources().getString(R.string.contact_meituan_message);
                SpannableString spanColor = new SpannableString(mMessage);
                spanColor.setSpan(new ForegroundColorSpan(Color.parseColor("#10CBE5")), 11, 19, 0);

                ConfirmDialog dialog1 = new ConfirmDialog.Builder(this)
                        .setTitle(R.string.contact_meituan_title)
                        .setSpannableMessage(spanColor)
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (ActivityCompat.checkSelfPermission(OrderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(OrderDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
                                    } else {
                                        startActionCall();
                                    }
                                }
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.order_cancel_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog1.show();
                break;
            case R.id.no_internet_btn:
                if (NetUtil.getNetWorkState(this)) {
                    loadData();
                } else {
                    ToastUtils.show(this, getResources().getString(R.string.is_network_connected), Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    private void orderSubmit() {
        Intent intentPayment = new Intent(OrderDetailsActivity.this, PaymentActivity.class);
        intentPayment.putExtra(Constant.STORE_ID, mOrderDetails.getWm_poi_id()+"");
        intentPayment.putExtra("total_cost", mOrderDetails.getTotal());
        intentPayment.putExtra("order_id", mOrderDetails.getOrder_id());
        intentPayment.putExtra("shop_name", mOrderDetails.getPoi_name());
        intentPayment.putExtra("pay_url", getIntent().getStringExtra("pay_url"));
        intentPayment.putExtra("pic_url", getIntent().getStringExtra("pic_url"));
        intentPayment.putExtra("flag",true);
        intentPayment.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentPayment);
        finish();
    }

    @Override
    public void update(OrderDetailsResponse data) {
        if(data!=null){
            mOrderDetails = data.getMeituan().getData();
            long orderTime = (long) mOrderDetails.getOrder_time() * 1000L;
            mTimer = new CountDownTimer(15 * 60 * 1000L - (System.currentTimeMillis() - orderTime), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mArrivalTime.setText(String.format(getResources().getString(R.string.count_down_timer), formatCountDownTime(millisUntilFinished)));
                }

                @Override
                public void onFinish() {
                    isTimeEnd  = true;
//                mOrderCancelReq = new OrderCancelReq(mOrderDetailsReq.getId());
//                getPresenter().requestOrderCancel(mOrderCancelReq);
                    mArrivalTime.setText(String.format(getResources().getString(R.string.count_down_timer), "00:00"));
                    mPayStatus = mOrderDetails.getPay_status();
                    if (mPayStatus != Constant.PAY_STATUS_SUCCESS) {

                        ConfirmDialog dialog = new ConfirmDialog.Builder(OrderDetailsActivity.this)
                                .setTitle(R.string.pay_title)
                                .setMessage(R.string.pay_time_out)
                                .setNegativeButton(R.string.anew_submit, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent();
                                        intent.setClass(OrderDetailsActivity.this, FoodListActivity.class);
                                        intent.putExtra(Constant.TO_SHOW_SHOP_CART, true);
                                        intent.putExtra(Constant.STORE_ID, mStoreId);
                                        intent.putExtra("flag", true);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .setPositiveButton(R.string.back_store, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setClass(OrderDetailsActivity.this, FoodListActivity.class);
                                        intent.putExtra(Constant.STORE_ID, mStoreId);
                                        intent.putExtra("flag", true);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .setCloseButton(new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        dialog.setCanceledOnTouchOutside(false);
                        if (isForeground(OrderDetailsActivity.this)
                                &&!OrderDetailsActivity.this.isFinishing()) {
                            dialog.show();
                        }
                    }
                }

            };

            setTextView();
            if (null != mOrderDetails && mOrderDetails.getEstimate_arrival_time() != 0) {
                long time = mOrderDetails.getEstimate_arrival_time();
                time = time * 1000L;
                Date date = new Date(time);
                SimpleDateFormat format1, format2;
                format1 = new SimpleDateFormat("yyyy-MM-dd");
                format2 = new SimpleDateFormat("HH:mm");
                mExpectedTime.setText(format1.format(date) + " (" + getWeek(time) + ") " + format2.format(date));
            }
        }else {
            Lg.getInstance().d(TAG, "no find data !");
        }

        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        networkView.setVisibility(View.GONE);

    }

    /**
     * 判断某个activity是否在前台显示
     */
    public static boolean isForeground(AppCompatActivity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台,返回true，为显示,否则不是
     */
    public static boolean isForeground(AppCompatActivity context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    public static String getWeek(long time) {

        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(time));

        int year = cd.get(Calendar.YEAR); //获取年份
        int month = cd.get(Calendar.MONTH); //获取月份
        int day = cd.get(Calendar.DAY_OF_MONTH); //获取日期
        int week = cd.get(Calendar.DAY_OF_WEEK); //获取星期

        String weekString;
        switch (week) {
            case Calendar.SUNDAY:
                weekString = "周日";
                break;
            case Calendar.MONDAY:
                weekString = "周一";
                break;
            case Calendar.TUESDAY:
                weekString = "周二";
                break;
            case Calendar.WEDNESDAY:
                weekString = "周三";
                break;
            case Calendar.THURSDAY:
                weekString = "周四";
                break;
            case Calendar.FRIDAY:
                weekString = "周五";
                break;
            default:
                weekString = "周六";
                break;

        }

        return weekString;
    }

    @Override
    public void updateOrderCancel(OrderCancelResponse data) {
        if (isTimeEnd){
            timerCancel();
            loadData();
            isTimeEnd = false;
            return;
        }


        if (data.getMeituan().getCode() == 0) {
            ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_cancel_toast), Toast.LENGTH_SHORT);
            mArrivalTime.setText("");
            timerCancel();
            loadData();
        } else {
            String mMessage = getResources().getString(R.string.remind_message);
            SpannableString spanColor = new SpannableString(mMessage);
            spanColor.setSpan(new ForegroundColorSpan(Color.parseColor("#10CBE5")), 5, 12, 0);
            ConfirmDialog dialog1 = new ConfirmDialog.Builder(this)
                    .setTitle(R.string.order_cancel)
                    .setSpannableMessage(spanColor)
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
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(OrderDetailsActivity.this,
                                            Manifest.permission.CALL_PHONE)) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    } else {
                                        ActivityCompat.requestPermissions(OrderDetailsActivity.this,
                                                new String[]{Manifest.permission.CALL_PHONE},
                                                REQUEST_CODE_CALL_PHONE);
                                    }
                                } else {
                                    startActionCall();
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
    public void authFailure(String msg) {
        ToastUtils.show(this,"授权失败，请开启服务授权",Toast.LENGTH_SHORT);
    }

    @Override
    public void authSuccess(String msg) {
        boolean isBackground = BackgroundUtils.isBackground(getBaseContext());
        if (!isBackground){
            orderSubmit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission was granted, yay! Do the contacts-related task you need to do.
                startActionCall();
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

    private void startActionCall(){
        if (!DeviceUtils.checkBluetooth(OrderDetailsActivity.this)) return;
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10109777");
        intent.setData(data);
        startActivity(intent);
    }

}
