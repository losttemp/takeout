package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.adapter.FoodListAdaper;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.TestClass;
import com.baidu.iov.dueros.waimai.presenter.OrderDetailsPresenter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;


public class OrderDetailsActivity extends BaseActivity<OrderDetailsPresenter, OrderDetailsPresenter.OrderDetailsUi> implements OrderDetailsPresenter.OrderDetailsUi, View.OnClickListener {
    /**
     *
     */
    private TextView  mArrivalTime,mBusinessName,mPackingFee,mDistributionFee,mDiscount,mRealPay,mContact,mAddress,mExpectedTime,mOrderId,mOrderTime,mPayMethod;
    private ListView mFood;
    private RelativeLayout mPayMethodInfo;
    private OrderQueryData mData;
    private FoodListAdaper mFoodListAdaper;
    private long id;
    private String phone;

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
        setListener();
        initView();
        setTextView();
        requesData();
        initData();
        final OkHttpClient okHttpClient=new OkHttpClient();
        mFoodListAdaper = new FoodListAdaper(this);
        TestClass t1 = new TestClass("黄焖鸡米饭","¥20");
        TestClass t2 = new TestClass("农家小炒肉","¥18");
        List<TestClass> mData = new ArrayList<>();
        mData.add(t1);
        mData.add(t2);
        mFoodListAdaper.setData(mData);
        mFood.setAdapter(mFoodListAdaper);
    }

    private void requesData() {

    }

    private void setListener() {
        findViewById(R.id.repeat_order).setOnClickListener(this);
        findViewById(R.id.cancel_order).setOnClickListener(this);
        findViewById(R.id.phone).setOnClickListener(this);
    }

    private void initView() {
        mArrivalTime = (TextView)findViewById(R.id.arrival_time);
        mBusinessName = (TextView)findViewById(R.id.business_name);
        mPackingFee = (TextView)findViewById(R.id.packing_fee);
        mDistributionFee = (TextView)findViewById(R.id.distribution_fee);
        mDiscount = (TextView)findViewById(R.id.discount);
        mRealPay = (TextView)findViewById(R.id.real_pay);
        mContact = (TextView)findViewById(R.id.contact);
        mAddress = (TextView)findViewById(R.id.address);
        mExpectedTime = (TextView)findViewById(R.id.expected_time);
        mOrderId = (TextView)findViewById(R.id.order_id);
        mOrderTime = (TextView)findViewById(R.id.order_time);
        mPayMethod = (TextView)findViewById(R.id.pay_method);
        mFood = (ListView)findViewById(R.id.ll_food);
        mPayMethodInfo = (RelativeLayout) findViewById(R.id.pay_method_info);
    }

    private void setTextView() {
        mArrivalTime.setText("预计12:00送达");
    }

    private void initData() {
//        loadData();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repeat_order:
            case R.id.cancel_order:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("取消订单并退款")
                        .setMessage("退款将原路退还你的支付账户，详情请查看退款进度")
                        .setNegativeButton("先等等", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("取消订单", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                break;
            case R.id.phone:
                AlertDialog dialog1 = new AlertDialog.Builder(this)
                        .setTitle("温馨提示")
                        .setMessage("该订单需要联系客服取消，确定拨打电话123456789取消吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                Uri data = Uri.parse("tel:" + "18818580550");
                                intent.setData(data);
                                if (ActivityCompat.checkSelfPermission(OrderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }).create();
                dialog1.show();
                break;
        }
    }

    @Override
    public void update(OrderDetailsResponse data) {

    }

    @Override
    public void failure(String msg) {

    }

    @Override
    public void close() {
        finish();
    }
}
