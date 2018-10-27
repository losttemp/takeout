package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.presenter.AddressSelectPresenter;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.location.BDLocation;

public class AddressSelectActivity extends BaseActivity<AddressSelectPresenter, AddressSelectPresenter.AddressSelectUi> implements AddressSelectPresenter.AddressSelectUi, LocationManager.LocationCallBack, View.OnClickListener {
    private TextView desTv;
    private TextView desDetails;
    private BDLocation mBDLocation;

    @Override
    AddressSelectPresenter createPresenter() {
        return new AddressSelectPresenter();
    }

    @Override
    AddressSelectPresenter.AddressSelectUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);
        initView();
        initLocation();
        getPresenter().requestData(new ArrayMap<String, String>());
    }

    private void initLocation() {
        LocationManager instance = LocationManager.getInstance(this);
        LocationManager.getInstance(this).setLocationCallBack(this);
        instance.restartLocation();
        mBDLocation = instance.getLastKnownLocation();
        if (mBDLocation != null) {
            String addrStr = mBDLocation.getAddrStr();
            desDetails.setText(addrStr);
        }
    }

    private void initView() {
        desTv = (TextView) findViewById(R.id.address_select_des);
        desDetails = (TextView) findViewById(R.id.address_select_des_details);
        findViewById(R.id.address_select_edit).setOnClickListener(this);
        findViewById(R.id.address_select_add).setOnClickListener(this);
    }


    @Override
    public void onSuccess(CinemaBean data) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddressEditActivity.class);
        switch (v.getId()) {
            case R.id.address_select_edit:
            case R.id.address_select_add:
                if (mBDLocation == null) {
                    Log.d("hhr","start null");
                }
                intent.putExtra("address_select",mBDLocation);
                startActivityForResult(intent,3);
                break;
        }
    }

    @Override
    public void locationCallBack(boolean isSuccess, BDLocation bdLocation) {
        this.mBDLocation = bdLocation;
        if (isSuccess) {
            desDetails.setText(bdLocation.getAddrStr());
        } else {
            desDetails.setText("location erro");
            LocationManager.getInstance(this).requestLocation();
        }
    }
}
