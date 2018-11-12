package com.baidu.iov.dueros.waimai.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressSelectAdapter;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEndBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressSelectPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectActivity extends BaseActivity<AddressSelectPresenter, AddressSelectPresenter.AddressSelectUi> implements AddressSelectPresenter.AddressSelectUi, View.OnClickListener {
    private List<AddressListBean.IovBean.DataBean> mDataList;
    private AddressListBean.IovBean.DataBean mHeaderDataBean;
    private RecyclerView mRecyclerView;
    private AddressSelectAdapter mAdapter;
    private AddressEndBean navi;

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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.baidu.naviauto.open.api");
        MReceiver mReceiver = new MReceiver();
        registerReceiver(mReceiver, intentFilter);
        Intent i = new Intent();
        i.setData(Uri.parse("bdmapauto://map/navi_end_route"));
        startActivity(i);
        mDataList = new ArrayList<>();
        getPresenter().requestData(new AddressListReqBean());
        initView();
        initData();
    }

    class MReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == "com.baidu.naviauto.open.api"){
                String s = intent.getStringExtra("dest_json");
                Lg.getInstance().d("xss","getAction   "+s);
                parseJSON(s);
                Lg.getInstance().d("xss","AddOnSuccess");
                AddressListBean.IovBean.DataBean dataBean = new AddressListBean.IovBean.DataBean();
                Lg.getInstance().d("xss","AddOnSuccess =  "+navi.getAddress());
                dataBean.setUser_name("xss");
                dataBean.setUser_phone("1888888888");
                dataBean.setAddress(navi.getAddress()+navi.getName());
                mDataList.add(0,dataBean);
            }
        }
    }

    private void parseJSON(String jsonData) {
        Gson gson = new Gson();
        navi = gson.fromJson(jsonData,
                new TypeToken<AddressEndBean>() {}.getType());
    }

    private void initData() {
        mHeaderDataBean = new AddressListBean.IovBean.DataBean();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.address_select_lv);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);
        findViewById(R.id.address_back).setOnClickListener(this);
        findViewById(R.id.address_select_add).setOnClickListener(this);
        mAdapter = new AddressSelectAdapter(mDataList, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListerner(new AddressSelectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, AddressListBean.IovBean.DataBean dataBean) {
                switch (v.getId()) {
                    case R.id.address_select_des:
//                    case R.id.address_select_details_container:
//                        finish();
                        break;
                    case R.id.address_select_edit:
                        Intent intent = new Intent(AddressSelectActivity.this, AddressEditActivity.class);
                        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT,true);
                        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS,dataBean);
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
        switch (v.getId()) {
            case R.id.address_back:
                finish();
                break;
            case R.id.address_select_add:
                Intent intent = new Intent(this, AddressEditActivity.class);
                intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT,false);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}