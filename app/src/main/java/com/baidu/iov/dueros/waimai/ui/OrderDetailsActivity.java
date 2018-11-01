package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.adapter.FoodListAdaper;
import com.baidu.iov.dueros.waimai.net.entity.response.TestClass;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;


public class OrderDetailsActivity extends Activity implements View.OnClickListener {
    /**
     *
     */
    private TextView  arrival_time,business_name,packing_fee,distribution_fee,discount,real_pay,contact,address,expected_time,order_id,order_time,pay_method;
    private ListView ll_food;
    private FoodListAdaper mFoodListAdaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        setListener();
        initView();
        setTextView();
        final OkHttpClient okHttpClient=new OkHttpClient();
        mFoodListAdaper = new FoodListAdaper(this);
        TestClass t1 = new TestClass("黄焖鸡米饭","¥20");
        TestClass t2 = new TestClass("农家小炒肉","¥18");
        List<TestClass> mData = new ArrayList<>();
        mData.add(t1);
        mData.add(t2);
        mFoodListAdaper.setData(mData);
        ll_food.setAdapter(mFoodListAdaper);
    }

    private void setListener() {
        findViewById(R.id.repeat_order).setOnClickListener(this);
        findViewById(R.id.cancel_order).setOnClickListener(this);
        findViewById(R.id.phone).setOnClickListener(this);
    }

    private void initView() {
        arrival_time = (TextView)findViewById(R.id.arrival_time);
        business_name = (TextView)findViewById(R.id.business_name);
        packing_fee = (TextView)findViewById(R.id.packing_fee);
        distribution_fee = (TextView)findViewById(R.id.distribution_fee);
        discount = (TextView)findViewById(R.id.discount);
        real_pay = (TextView)findViewById(R.id.real_pay);
        contact = (TextView)findViewById(R.id.contact);
        address = (TextView)findViewById(R.id.address);
        expected_time = (TextView)findViewById(R.id.expected_time);
        order_id = (TextView)findViewById(R.id.order_id);
        order_time = (TextView)findViewById(R.id.order_time);
        pay_method = (TextView)findViewById(R.id.pay_method);
        ll_food = (ListView)findViewById(R.id.ll_food);
    }

    private void setTextView() {
        arrival_time.setText("预计12:00送达");
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
}
