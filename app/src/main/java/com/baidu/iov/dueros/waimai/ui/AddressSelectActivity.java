package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressSelectAdapter;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressSelectPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.faceos.client.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectActivity extends BaseActivity<AddressSelectPresenter, AddressSelectPresenter.AddressSelectUi> implements AddressSelectPresenter.AddressSelectUi, View.OnClickListener {
    private List<AddressListBean.IovBean.DataBean> mDataList;
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
        initView();
        initData();
/*        Intent i = new Intent();
        i.setData(Uri.parse("bdmapauto://map/navi_end_route"));
        startActivity(i);*/
    }

    private void initData() {
        mDataList = new ArrayList<>();
        getPresenter().requestData(new AddressListReqBean());
        mAdapter = new AddressSelectAdapter(mDataList, this);
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListerner(new AddressSelectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, AddressListBean.IovBean.DataBean dataBean) {
                switch (v.getId()) {
                    case R.id.address_select_details_container:
                        MyApplicationAddressBean.SELECTED_ADDRESS_BEAN = dataBean;
                        SharedPreferences sp = WaiMaiApplication.getInstance().getSharedPreferences
                                ("_cache", AddressSelectActivity.MODE_PRIVATE);
                        String databeanStr = GsonUtil.toJson(dataBean);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Constant.ADDRESS_DATA, databeanStr);
                        editor.commit();
                        Intent homeintent = new Intent(AddressSelectActivity.this, HomeActivity.class);
                        startActivity(homeintent);
                        finish();
                        break;
                    case R.id.address_select_edit:
                        startEditActivity(dataBean);
                        break;
                }
            }
        });
    }

    private void startEditActivity(AddressListBean.IovBean.DataBean dataBean) {
        Intent intent = new Intent(AddressSelectActivity.this, AddressEditActivity.class);
        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS, dataBean);
        AddressSelectActivity.this.startActivity(intent);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.address_select_lv);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);
        findViewById(R.id.address_back).setOnClickListener(this);
        findViewById(R.id.address_select_add).setOnClickListener(this);
    }


    @Override
    public void onSuccess(List<AddressListBean.IovBean.DataBean> data) {
        mDataList.addAll(data);
        mAdapter.setAddressList(mDataList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(String msg) {

    }

    @Override
    public void onRegisterReceiver(AddressSelectPresenter.MReceiver mReceiver, IntentFilter intentFilter) {
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_back:
                finish();
                break;
            case R.id.address_select_add:
                doSearchAddress(false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}