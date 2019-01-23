package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressSelectAdapter;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressSelectPresenter;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.baidu.iov.faceos.client.GsonUtil;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectActivity extends BaseActivity<AddressSelectPresenter, AddressSelectPresenter.AddressSelectUi> implements AddressSelectPresenter.AddressSelectUi, View.OnClickListener {
    private List<AddressListBean.IovBean.DataBean> mDataList;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoAddress;
    private AddressSelectAdapter mAdapter;
    private final long SIX_HOUR = 6 * 60 * 60 * 1000;
    private AddressSelectPresenter.MReceiver mReceiver;
    private View addBtnView;
    private View networkView;
    private View loadingView;
    private boolean isNeedPlayTTS;

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
        isNeedPlayTTS = getIntent().getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false);
        getPresenter().initDesBeans();
        initView();
        if (getIntent().getIntExtra(Constant.START_APP,-1)==Constant.START_APP_CODE){
            requestPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNoAddress.setVisibility(View.GONE);
        addBtnView.setVisibility(View.GONE);
        if (NetUtil.getNetWorkState(this)) {
            sendBroadcast(new Intent("com.baidu.iov.dueros.waimai.requestNaviDes"));
            networkView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
            initData();
            GuidingAppear.INSTANCE.init(this, WaiMaiApplication.getInstance().getWaimaiBean().getAddress().getMe());
        } else {
            if (null != networkView) {
                networkView.setVisibility(View.VISIBLE);
            }
        }

    }

    private void initData() {
        mDataList = new ArrayList<>();
        getPresenter().requestData(new AddressListReqBean());
        mAdapter = new AddressSelectAdapter(mDataList, this) {
            @Override
            public void addAddress() {
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_START_ADD_NEW,EventType.TOUCH_TYPE);
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_START_POI,EventType.TOUCH_TYPE);
                doSearchAddress(false);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListerner(new AddressSelectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, AddressListBean.IovBean.DataBean dataBean) {
                switch (v.getId()) {
                    case R.id.address_select_details_container:
                        String type =dataBean.getType();
                        if (TextUtils.isEmpty(type)){
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_OTHER,EventType.TOUCH_TYPE);
                        }else if (getString(R.string.address_home).equals(type)) {
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_HOME,EventType.TOUCH_TYPE);
                        } else if (getString(R.string.address_company).equals(type)) {
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_FIRM,EventType.TOUCH_TYPE);
                        }else if (getString(R.string.address_destination).equals(type)){
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_MUDIDI,EventType.TOUCH_TYPE);
                        }else{
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_OTHER,EventType.TOUCH_TYPE);
                        }
                        String databeanStr = GsonUtil.toJson(dataBean);
                        CacheUtils.saveAddressBean(databeanStr);
                        sendBroadcast(new Intent(Constant.PULL_LOCATION));
                        if (CacheUtils.getAddrTime() == 0 || (System.currentTimeMillis() - CacheUtils.getAddrTime() > SIX_HOUR)) {
                            CacheUtils.saveAddrTime(System.currentTimeMillis());
                        }
                        Intent homeintent = new Intent(AddressSelectActivity.this, HomeActivity.class);
                        try {
                            String address = Encryption.desEncrypt(dataBean.getAddress());
                            CacheUtils.saveAddress(address);
                            HomeActivity.address = address;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_START_EDIT,EventType.TOUCH_TYPE);
        Intent intent = new Intent(AddressSelectActivity.this, AddressEditActivity.class);
        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS, dataBean);
        AddressSelectActivity.this.startActivity(intent);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.address_select_lv);
        mNoAddress = findViewById(R.id.address_none);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);
        findViewById(R.id.address_back).setOnClickListener(this);
        findViewById(R.id.address_select_add).setOnClickListener(this);
        findViewById(R.id.add_no_address).setOnClickListener(this);
        addBtnView = findViewById(R.id.address_select_btn_layout);
        networkView = findViewById(R.id.network_view);
        loadingView = findViewById(R.id.loading_view);
        findViewById(R.id.no_internet_btn).setOnClickListener(this);
    }

    @Override
    public void onSuccess(List<AddressListBean.IovBean.DataBean> data) {
        loadingView.setVisibility(View.GONE);
        if (data.size() == 0) {
            GuidingAppear.INSTANCE.init(this, WaiMaiApplication.getInstance().getWaimaiBean().getAddress().getEmpty_result());
            mNoAddress.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            if (isNeedPlayTTS) {
                VoiceManager.getInstance().playTTS(AddressSelectActivity.this, getString(R.string.choice_no_address_voice));
                isNeedPlayTTS = false;
            }
        } else {
            mNoAddress.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (isNeedPlayTTS) {
                VoiceManager.getInstance().playTTS(AddressSelectActivity.this, getString(R.string.choice_address_voice));
                isNeedPlayTTS = false;
            }
            mDataList.clear();
            mDataList.addAll(data);
            if (mDataList.size()>7){
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
                mRecyclerView.setLayoutParams(lp);
                addBtnView.setVisibility(View.VISIBLE);
            }else{
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mRecyclerView.setLayoutParams(lp);
                addBtnView.setVisibility(View.GONE);
                AddressListBean.IovBean.DataBean dataBean = new AddressListBean.IovBean.DataBean();
                dataBean.setItem_type(1);
                mDataList.add(dataBean);
            }
            mAdapter.setAddressList(mDataList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(String msg) {
        loadingView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        networkView.setVisibility(View.VISIBLE);
    }

    @Override
    public void selectListItem(int i) {
        if (null != mDataList && mDataList.size() >= i) {
            String databeanStr = GsonUtil.toJson(mDataList.get(i));
            CacheUtils.saveAddressBean(databeanStr);
            sendBroadcast(new Intent(Constant.PULL_LOCATION));
            if (CacheUtils.getAddrTime() == 0 || (System.currentTimeMillis() - CacheUtils.getAddrTime() > SIX_HOUR)) {
                CacheUtils.saveAddrTime(System.currentTimeMillis());
            }
            Intent homeintent = new Intent(AddressSelectActivity.this, HomeActivity.class);
            try {
                String address = Encryption.desEncrypt(mDataList.get(i).getAddress());
                CacheUtils.saveAddress(address);
                HomeActivity.address = address;
                VoiceManager.getInstance().playTTS(AddressSelectActivity.this, String.format(getString(R.string.harvest_address), address));
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(homeintent);
            finish();
        }
    }

    @Override
    public void onRegisterReceiver(AddressSelectPresenter.MReceiver receiver, IntentFilter intentFilter) {
        this.registerReceiver(receiver, intentFilter);
        this.mReceiver = receiver;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_back:
                finish();
                break;
            case R.id.add_no_address:
            case R.id.address_select_add:
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_START_ADD_NEW,EventType.TOUCH_TYPE);
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_START_POI,EventType.TOUCH_TYPE);
                doSearchAddress(false);
                break;
            case R.id.no_internet_btn:
                if (NetUtil.getNetWorkState(this)) {
                    sendBroadcast(new Intent("com.baidu.iov.dueros.waimai.requestNaviDes"));
                    networkView.setVisibility(View.GONE);
                    initData();
                } else {
                    ToastUtils.show(this, getResources().getString(R.string.is_network_connected), Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}