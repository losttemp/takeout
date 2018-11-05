package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.FoodListAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.presenter.OrderDetailsPresenter;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;


import java.util.List;


public class OrderDetailsActivity extends BaseActivity<OrderDetailsPresenter, OrderDetailsPresenter.OrderDetailsUi>implements OrderDetailsPresenter.OrderDetailsUi, View.OnClickListener {
    /**
     *
     */
    private TextView mArrivalTime, mBusinessName, mPackingFee, mDistributionFee, mDiscount, mRealPay, mContact, mAddress, mExpectedTime, mOrderId, mOrderTime, mPayMethod,mPayStatus;
    private ListView mFood;
    private RelativeLayout mPayMethodInfo;
    private FoodListAdaper mFoodListAdaper;
    private OrderDetailsReq mOrderDetailsReq;
    private long order_id;
    private String user_phone;
    private ArrayMap<String, String> map;
    private OrderDetailsResponse.MeituanBean.DataBean mOrderDetails = new OrderDetailsResponse.MeituanBean.DataBean();
    private OrderCancelResponse.ErrorInfoBean mOrderCancel= new OrderCancelResponse.ErrorInfoBean();

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


    private void setListener() {
        findViewById(R.id.repeat_order).setOnClickListener(this);
        findViewById(R.id.cancel_order).setOnClickListener(this);
        findViewById(R.id.phone).setOnClickListener(this);
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
        mPayMethodInfo = (RelativeLayout) findViewById(R.id.pay_method_info);
        mPayStatus = (TextView)findViewById(R.id.pay_status);
    }

    private void setTextView() {
        mArrivalTime.setText(String.valueOf(mOrderDetails.getEstimate_arrival_time()));
        mBusinessName.setText(mOrderDetails.getPoi_name());
        mPackingFee.setText("¥" + mOrderDetails.getBox_total_price());
        mDistributionFee.setText("¥" + mOrderDetails.getShipping_fee());
        mDiscount.setText("-¥" + (mOrderDetails.getOriginal_price() - mOrderDetails.getTotal()));
        mRealPay.setText("¥" + mOrderDetails.getTotal());
        mContact.setText(mOrderDetails.getRecipient_phone());
        mAddress.setText(mOrderDetails.getRecipient_address());
        mExpectedTime.setText("2018-10-19 （周五）18:30");
        mOrderId.setText(String.valueOf(mOrderDetails.getOrder_id()));
        mOrderTime.setText(String.valueOf(mOrderDetails.getCtime()));
        if (mOrderDetails.getWm_order_pay_type() == 1) {
            mPayMethod.setText("货到付款");
        } else if (mOrderDetails.getWm_order_pay_type() == 2) {
            mPayMethod.setText("在线支付");
        }
        if(mOrderDetails.getPay_status()==1)
        {

        }
        List<OrderDetailsResponse.MeituanBean.DataBean.FoodListBean> mfoodList = mOrderDetails.getFood_list();
        mFoodListAdaper = new FoodListAdaper(this);
        mFoodListAdaper.setData(mfoodList);
        mFood.setAdapter(mFoodListAdaper);


    }

    private void initData() {
        order_id = Long.parseLong("15053513663693918");
        user_phone = "18201010600";
        mOrderDetailsReq = new OrderDetailsReq(order_id, user_phone);
        loadData();
    }

    private void loadData() {
        getPresenter().requestOrderDetails(mOrderDetailsReq);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repeat_order:
                break;
            case R.id.cancel_order:
                ConfirmDialog dialog = new ConfirmDialog.Builder(this)
                        .setTitle("取消订单并退款")
                        .setMessage("退款将原路退还你的支付账户，详情请查看退款进度")
                        .setNegativeButton("取消订单", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getPresenter().requestOrderCancel(mOrderDetailsReq);
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("先等等", new DialogInterface.OnClickListener() {
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
                        .setTitle("温馨提示")
                        .setMessage("该订单需要联系客服取消，确定拨打电话123456789取消吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("10109777", new DialogInterface.OnClickListener() {
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
        mOrderCancel = data.getErrorInfo();
        Log.d("xss","OrderCancel = "+mOrderCancel.getFailCode());
        Toast.makeText(this,"订单已取消",Toast.LENGTH_LONG).show();

    }

    @Override
    public void failure(String msg) {

    }

    @Override
    public void close() {
        finish();
    }
}
