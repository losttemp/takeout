package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CommonUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.VoiceTouchUtils;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;
import com.bumptech.glide.Glide;

public class PaySuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mOrderDetailsTv;
    private Button mCountDownTv;
    private ImageView mStorePhotoImg;
    private TextView mStoreNameTv;
    private TextView mProductInfoTv;
    private TextView mUserInfoTv,mUserInfoPhone;
    private TextView mDeliveryAddressTv;
    private ImageView mFinishImg;


    public static final int MSG_UPDATE_TIME = 1;
    public static final int INTERNAL_TIME = 1000;
    private int mCountDownTime = 5;
    private Long mOrderId;
    private int mExpectedTime;
    private boolean isGo2OrderDetail;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_UPDATE_TIME:
                    if (mCountDownTime > 0) {
                        mCountDownTv.setText(String.format(getString(R.string.complete), --mCountDownTime));
                        if (!isGo2OrderDetail)
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
        setStatusBar(false, ContextCompat.getColor(this, R.color.base_color));
        initView();
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, INTERNAL_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getPay().getPayment_success(),Constant.TTS_PAY_PAYMENT_SUCCESS);
    }

    public void setStatusBar(boolean translucent, @ColorInt int color) {
        CommonUtils.setTranslucentStatusBar(this, translucent);
        if (color != 0) {
            CommonUtils.setStatusBarColor(this, color);
        }
    }

    public void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            mOrderId = intent.getLongExtra(Constant.ORDER_ID, 0);
            String picUrl = intent.getStringExtra(Constant.PIC_URL);
            String storeName = intent.getStringExtra(Constant.STORE_NAME);
            String recipientPhone = intent.getStringExtra(Constant.USER_PHONE);
            String recipientAddress = intent.getStringExtra(Constant.USER_ADDRESS);
            String recipient_name = intent.getStringExtra(Constant.USER_NAME);
            String foodNameOne = intent.getStringExtra(Constant.PRODUCT_NAME);
            mExpectedTime = intent.getIntExtra(Constant.EXPECTED_TIME, 0);
            int count = intent.getIntExtra(Constant.PRODUCT_COUNT, 0);

            mStorePhotoImg = findViewById(R.id.store_photo_img);
            mStoreNameTv = findViewById(R.id.store_name_tv);
            mProductInfoTv = findViewById(R.id.product_info_tv);
            mUserInfoTv = findViewById(R.id.user_info_tv);
            mUserInfoPhone = findViewById(R.id.user_info_phone);
            mDeliveryAddressTv = findViewById(R.id.recipient_address);
            Glide.with(this).load(picUrl).into(mStorePhotoImg);
            mStoreNameTv.setText(storeName);
            mProductInfoTv.setText(String.format(getString(R.string.product_info), foodNameOne, count));

            try {
                recipientAddress = Encryption.desEncrypt(recipientAddress);
                recipientPhone = Encryption.desEncrypt(recipientPhone);
                recipient_name = Encryption.desEncrypt(recipient_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mUserInfoTv.setText(recipient_name);
            mUserInfoPhone.setText(recipientPhone);
            mDeliveryAddressTv.setText(recipientAddress);

        }

        mOrderDetailsTv = findViewById(R.id.order_details_tv);
        mOrderDetailsTv.setOnClickListener(this);
        mCountDownTv = findViewById(R.id.complete_tv);
        mCountDownTv.setText(String.format(getString(R.string.complete), mCountDownTime));
        mCountDownTv.setOnClickListener(this);
        mFinishImg = findViewById(R.id.finish_img);
        mFinishImg.setOnClickListener(this);

        VoiceTouchUtils.setVoicesTouchSupport(mOrderDetailsTv, R.array.check_order_detail);
        VoiceTouchUtils.setVoiceTouchTTSSupport(mOrderDetailsTv, getString(R.string.tts_show_order_detail));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
                isGo2OrderDetail = true;
                Entry.getInstance().onEvent(Constant.PAYSUCCESS_TO_ORDERDETAIL, EventType.TOUCH_TYPE);
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra(Constant.ORDER_ID, mOrderId);
                intent.putExtra(Constant.EXPECTED_TIME, mExpectedTime);
                intent.putExtra("flag",true);
                startActivity(intent);
                finish();
                break;

            case R.id.complete_tv:

                startOtherActivity();
                break;
            case R.id.finish_img:
                if (getIntent().getBooleanExtra("flag", false)) {
                    finish();
                } else {
                    AtyContainer.getInstance().finishAllActivity();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
