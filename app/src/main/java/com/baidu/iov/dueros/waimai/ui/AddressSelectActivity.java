package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressSelectAdapter;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressSelectPresenter;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.location.BDLocation;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectActivity extends BaseActivity<AddressSelectPresenter, AddressSelectPresenter.AddressSelectUi> implements AddressSelectPresenter.AddressSelectUi, LocationManager.LocationCallBack, View.OnClickListener {
    private BDLocation mBDLocation;
    private List<AddressListBean.IovBean.DataBean> mDataList;
    private AddressListBean.IovBean.DataBean mHeaderDataBean;
    private RecyclerView mRecyclerView;
    private AddressSelectAdapter mAdapter;

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
        mDataList = new ArrayList<>();
        getPresenter().requestData(new AddressListReqBean());
        initView();
        initData();
        initLocation();
    }

    private void initData() {
        mHeaderDataBean = new AddressListBean.IovBean.DataBean();
    }

    private void initLocation() {
        LocationManager instance = LocationManager.getInstance(this);
        LocationManager.getInstance(this).setLocationCallBack(this);
        instance.restartLocation();
        mBDLocation = instance.getLastKnownLocation();
        if (mBDLocation != null) {
            String addrStr = mBDLocation.getAddrStr();
            if (TextUtils.isEmpty(addrStr)) {
                mHeaderDataBean.setAddress(addrStr);
            }
        }
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.address_select_lv);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);
        findViewById(R.id.address_back).setOnClickListener(this);
        findViewById(R.id.address_select_add).setOnClickListener(this);
        mAdapter = new AddressSelectAdapter(mDataList, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmItemClickListerner(new AddressSelectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, AddressListBean.IovBean.DataBean dataBean) {
                switch (v.getId()) {
                    case R.id.address_select_des_details:
                        Intent homeintent = new Intent(AddressSelectActivity.this, HomeActivity.class);
                        AddressSelectActivity.this.startActivity(homeintent);
                        finish();
                        break;
                    case R.id.address_select_edit:
                        Intent intent = new Intent(AddressSelectActivity.this, AddressEditActivity.class);
                        intent.putExtra("address_select_address",dataBean.getAddress());
                        intent.putExtra("address_select_lat", dataBean.getLatitude());
                       intent.putExtra("address_select_lo", dataBean.getLongitude());
                        intent.putExtra("address_select_phone", dataBean.getUser_phone());
                        intent.putExtra("address_select_name", dataBean.getUser_name());
                        intent.putExtra("address_select_bd_location",mBDLocation);
                        AddressSelectActivity.this.startActivity(intent);
                        break;
                }
            }
        });
    }


    @Override
    public void onSuccess(List<AddressListBean.IovBean.DataBean> data) {
        mDataList.addAll(data);
        mAdapter.setAddressList(mDataList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddressEditActivity.class);
        switch (v.getId()) {
            case R.id.address_back:
                finish();
                break;
            case R.id.address_select_add:
                if (mBDLocation == null) {
                }
                intent.putExtra("address_select", mBDLocation);
                startActivityForResult(intent, 3);
                break;
        }
    }

    @Override
    public void locationCallBack(boolean isSuccess, BDLocation bdLocation) {
        this.mBDLocation = bdLocation;
        if (isSuccess) {
            mHeaderDataBean.setUser_name("ZhangSan");
            mHeaderDataBean.setUser_phone("1888888888");
            mHeaderDataBean.setAddress(bdLocation.getAddrStr());
            mDataList.add(mHeaderDataBean);
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, bdLocation.getLocType() + " eoor", Toast.LENGTH_SHORT).show();
            LocationManager.getInstance(this).requestLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationManager.getInstance(this).stopLocation();
    }
}
