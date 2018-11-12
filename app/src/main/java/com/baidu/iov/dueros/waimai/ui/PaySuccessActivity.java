package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.bumptech.glide.Glide;

import static com.baidu.iov.dueros.waimai.ui.PaymentActivity.PRODUCT_COUNT;
import static com.baidu.iov.dueros.waimai.ui.PaymentActivity.PRODUCT_NAME;
import static com.baidu.iov.dueros.waimai.ui.PaymentActivity.STORE_NAME;
import static com.baidu.iov.dueros.waimai.ui.PaymentActivity.USER_ADDRESS;
import static com.baidu.iov.dueros.waimai.ui.PaymentActivity.USER_NAME;
import static com.baidu.iov.dueros.waimai.ui.PaymentActivity.USER_PHONE;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.ORDER_ID;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.PIC_URL;

public class PaySuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mOrderDetailsTv;
    private TextView mCountDownTv;
    private ImageView mStorePhotoImg;
    private TextView mStoreNameTv;
    private TextView mProductInfoTv;
    private TextView mUserInfoTv;
    private TextView mDeliveryAddressTv;


    public static final int MSG_UPDATE_TIME = 1;
    public static final int INTERNAL_TIME = 1000;
    private int mCountDownTime = 5;
    private Long mOrderId;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_UPDATE_TIME:
                    if (mCountDownTime > 0) {
                        mCountDownTv.setText(String.format(getString(R.string.complete), --mCountDownTime));
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, INTERNAL_TIME);
                    } else {

                        startOtherActivity();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        initView();
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, INTERNAL_TIME);
    }

    public void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            mOrderId = intent.getLongExtra(ORDER_ID, 0);
            String picUrl = intent.getStringExtra(PIC_URL);
            String storeName = intent.getStringExtra(STORE_NAME);
            String recipientPhone = intent.getStringExtra(USER_PHONE);
            String recipientAddress = intent.getStringExtra(USER_ADDRESS);
            String recipient_name = intent.getStringExtra(USER_NAME);
            String foodNameOne = intent.getStringExtra(PRODUCT_NAME);
            int count = intent.getIntExtra(PRODUCT_COUNT, 0);

            mStorePhotoImg = findViewById(R.id.store_photo_img);
            mStoreNameTv = findViewById(R.id.store_name_tv);
            mProductInfoTv = findViewById(R.id.product_info_tv);
            mUserInfoTv = findViewById(R.id.user_info_tv);
            mDeliveryAddressTv = findViewById(R.id.recipient_address);
            Glide.with(this).load(picUrl).into(mStorePhotoImg);
            mStoreNameTv.setText(storeName);
            mProductInfoTv.setText(String.format(getString(R.string.product_info), foodNameOne, count));
            mUserInfoTv.setText(recipient_name + " " + recipientPhone);
            mDeliveryAddressTv.setText(recipientAddress);


        }

        mOrderDetailsTv = findViewById(R.id.order_details_tv);
        mOrderDetailsTv.setOnClickListener(this);
        mCountDownTv.findViewById(R.id.complete_tv);
        mCountDownTv.setOnClickListener(this);
    }


    private void startOtherActivity() {
        Intent data = new Intent(this, HomeActivity.class);
        data.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(data);
        finish();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.order_details_tv:

                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(ORDER_ID, mOrderId);
                startActivity(intent);
                break;

            case R.id.complete_tv:

                startOtherActivity();

            default:
                break;
        }
    }
}
