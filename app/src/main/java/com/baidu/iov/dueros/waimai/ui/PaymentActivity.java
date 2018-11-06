package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.ORDER_ID;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.PAY_URL;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.SHOP_NAME;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.TOTAL_COST;

public class PaymentActivity extends AppCompatActivity {

    private TextView mTimerTv;
    private TextView mAmountTv;
    private TextView mOrderIdTv;
    private TextView mShopNameTv;
    private ImageView mPayUrlImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        timerStart();
    }


    private void initView() {

        mAmountTv = findViewById(R.id.amount);
        mOrderIdTv = findViewById(R.id.order_id);
        mShopNameTv = findViewById(R.id.shop_name);
        mPayUrlImg = findViewById(R.id.qr_code);
        mTimerTv = findViewById(R.id.tv_pay_time);

        NumberFormat nf = new DecimalFormat("##.##");
        Intent intent = getIntent();
        if (intent != null) {

            double amount = intent.getDoubleExtra(TOTAL_COST, 0);
            long orderId = intent.getLongExtra(ORDER_ID, 0);
            String shopName = intent.getStringExtra(SHOP_NAME);
            String payUrl = intent.getStringExtra(PAY_URL);

            if (amount != 0){
                mAmountTv.setText(String.format(getResources().getString(R.string.cost_text), nf.format(amount)));
            }else {
                mAmountTv.setText("0.00");
            }
            mOrderIdTv.setText(String.valueOf(orderId));
            mShopNameTv.setText(shopName);
            Glide.with(this).load(payUrl).into(mPayUrlImg);

        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerCancel();
    }

    private CountDownTimer mTimer = new CountDownTimer(15 * 60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mTimerTv.setText(String.format(getResources().getString(R.string.count_down_timer), formatTime(millisUntilFinished)));
        }

        @Override
        public void onFinish() {

            mTimerTv.setText(String.format(getResources().getString(R.string.count_down_timer), "00:00"));
        }
    };

    public String formatTime(long millisecond) {
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

    public void timerCancel() {
        mTimer.cancel();
    }

    public void timerStart() {
        mTimer.start();
    }

}
