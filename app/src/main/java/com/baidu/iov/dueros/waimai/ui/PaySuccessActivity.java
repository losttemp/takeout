package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PaySuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mOrderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        initView();
    }

    public void initView(){

        mOrderDetails = findViewById(R.id.order_details);
        mOrderDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.order_details:

                Intent intent = new Intent(this , OrderDetailsActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
