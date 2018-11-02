package com.baidu.iov.dueros.waimai.ui;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    private TextView mTimerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        timerStart();
    }


    private void initView(){

        mTimerTv = findViewById(R.id.tv_pay_time);



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

    public String formatTime(long millisecond){
        int minute;
        int second;

        minute = (int)((millisecond / 1000) / 60);
        second = (int)((millisecond / 1000) % 60);

        if (minute < 10){
            if (second < 10){
                return "0" + minute + ":" + "0" + second;
            }else {
                return "0" + minute + ":" + second;
            }
        }else {
            if (second < 10){
                return minute + ":" + "0" +second;
            }else {
                return minute + ":" + second;
            }
        }
    }

    public void timerCancel(){
        mTimer.cancel();
    }

    public void timerStart(){
        mTimer.start();
    }

}
