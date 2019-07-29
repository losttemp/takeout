package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.BuildConfig;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressSelectAdapter;
import com.baidu.iov.dueros.waimai.net.Config;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressSelectPresenter;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.utils.VoiceTouchUtils;
import com.baidu.iov.faceos.client.GsonUtil;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectActivity extends BaseActivity<AddressSelectPresenter, AddressSelectPresenter.AddressSelectUi> implements AddressSelectPresenter.AddressSelectUi, View.OnClickListener {

    private final int START_ACTIVITY = 0x789;
    private List<AddressListBean.IovBean.DataBean> mDataList;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoAddress;
    private AddressSelectAdapter mAdapter;
    private final long SIX_HOUR = 6 * 60 * 60 * 1000;
    private AddressSelectPresenter.MReceiver mReceiver;
    private View addBtnView;
    private View networkView;
    private View loadingView;
    private boolean isNeedPlayTTS, isDelay;
    private Button addressAddBtn;
    private int startApp;
    private View listLayout;
    private View add_no_address;

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
        if (savedInstanceState == null) {
            isNeedPlayTTS = getIntent().getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false);
        }
        getPresenter().initDesBeans();
        initView();
        startApp = getIntent().getIntExtra(Constant.START_APP, -1);
        if (startApp == Constant.START_APP_CODE) {
            requestPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> prefix = new ArrayList<>();
        prefix.add("选择");
        AccessibilityClient.getInstance().register(this, true, prefix, null);
        mNoAddress.setVisibility(View.GONE);
        if (NetUtil.getNetWorkState(this)) {
            sendDestination();
            listLayout.setVisibility(View.VISIBLE);
            if (mDataList == null || mDataList.size() < 1)
                addBtnView.setVisibility(View.GONE);
            if (null != networkView)
                networkView.setVisibility(View.GONE);
            initData();
        } else {
            if (null != networkView) {
                listLayout.setVisibility(View.GONE);
                networkView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AccessibilityClient.getInstance().unregister(this);
    }

    private void initData() {
        //加500ms延迟是为了loadingView与编辑界面编辑操作成功后的toast提示错改
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isDelay = false;
                mNoAddress.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                getPresenter().requestData(new AddressListReqBean());
            }
        }, isDelay ? 1000 : 0);
    }

    private void initListener() {
        mAdapter.setOnItemClickListerner(new AddressSelectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, AddressListBean.IovBean.DataBean dataBean) {
                if (loadingView.getVisibility() == View.VISIBLE) return;
                switch (v.getId()) {
                    case R.id.address_select_details_container:
                        String type = dataBean.getType();
                        if (TextUtils.isEmpty(type)) {
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_OTHER, EventType.TOUCH_TYPE);
                        } else if (getString(R.string.address_home).equals(type)) {
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_HOME, EventType.TOUCH_TYPE);
                        } else if (getString(R.string.address_company).equals(type)) {
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_FIRM, EventType.TOUCH_TYPE);
                        } else if (getString(R.string.address_destination).equals(type)) {
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_MUDIDI, EventType.TOUCH_TYPE);
                        } else {
                            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_OTHER, EventType.TOUCH_TYPE);
                        }
                        String databeanStr = GsonUtil.toJson(dataBean);
                        CacheUtils.saveAddressBean(databeanStr);
                        sendBroadcastAPP();
                        if (CacheUtils.getAddrTime() == 0 || (System.currentTimeMillis() - CacheUtils.getAddrTime() > SIX_HOUR)) {
                            CacheUtils.saveAddrTime(System.currentTimeMillis());
                        }
                        try {
                            String address = Encryption.desEncrypt(dataBean.getAddress());
                            CacheUtils.saveAddress(address);
                            HomeActivity.address = address;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity();
                        break;
                    case R.id.address_select_edit:
                        startEditActivity(dataBean);
                        break;
                }
            }
        });

        VoiceTouchUtils.setVoicesTouchSupport(addressAddBtn, mContext.getString(R.string.add_address_text));
        VoiceTouchUtils.setVoiceTouchTTSSupport(addressAddBtn, mContext.getString(R.string.tts_add_new_address));
        VoiceTouchUtils.setVoicesTouchSupport(add_no_address, mContext.getString(R.string.add_address_text));
        VoiceTouchUtils.setVoiceTouchTTSSupport(add_no_address, mContext.getString(R.string.tts_add_new_address));
    }

    private void startEditActivity(AddressListBean.IovBean.DataBean dataBean) {
        Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_START_EDIT, EventType.TOUCH_TYPE);
        Intent intent = new Intent(AddressSelectActivity.this, AddressEditActivity.class);
        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS, dataBean);
        startActivityForResult(intent, START_ACTIVITY);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.address_select_lv);
        mNoAddress = findViewById(R.id.address_none);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);
        findViewById(R.id.address_back).setOnClickListener(this);
        add_no_address = findViewById(R.id.add_no_address);
        add_no_address.setOnClickListener(this);
        addBtnView = findViewById(R.id.address_select_btn_layout);
        listLayout = findViewById(R.id.list_layout);
        networkView = findViewById(R.id.network_view);
        loadingView = findViewById(R.id.loading_view);
        findViewById(R.id.no_internet_btn).setOnClickListener(this);
        addressAddBtn = findViewById(R.id.address_select_add);
        addressAddBtn.setOnClickListener(this);

        mDataList = new ArrayList<>();
        mAdapter = new AddressSelectAdapter(mDataList, this) {
            @Override
            public void addAddress() {
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_START_ADD_NEW, EventType.TOUCH_TYPE);
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_START_POI, EventType.TOUCH_TYPE);
                doSearchAddress(false);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    @Override
    public void onSuccess(List<AddressListBean.IovBean.DataBean> data) {
        if (data.size() == 0) {
            GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getTakeout_address().getHints(), Constant.TTS_ADDRESS_EMPTY_RESULT);
            mNoAddress.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            addBtnView.setVisibility(View.GONE);
            if (isNeedPlayTTS) {
                StandardCmdClient.getInstance().playTTS(AddressSelectActivity.this, getString(R.string.choice_no_address_voice));
                isNeedPlayTTS = false;
            }
        } else {
            GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getTakeout_null().getHints(), Constant.TTS_ADDRESS_ME);
            mNoAddress.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (isNeedPlayTTS) {
                StandardCmdClient.getInstance().playTTS(AddressSelectActivity.this, getString(R.string.choice_address_voice));
                isNeedPlayTTS = false;
            }
            mDataList.clear();
            mDataList.addAll(data);
            addBtnView.setVisibility(View.VISIBLE);
            if (mDataList.size() > 7) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
                mRecyclerView.setLayoutParams(lp);
            } else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mRecyclerView.setLayoutParams(lp);
            }
            mAdapter.setAddressList(mDataList);
            mAdapter.notifyDataSetChanged();
        }
        loadingView.setVisibility(View.GONE);
    }


    @Override
    public void onFailure(String msg) {
        loadingView.setVisibility(View.GONE);
        mNoAddress.setVisibility(View.GONE);
        listLayout.setVisibility(View.GONE);
        networkView.setVisibility(View.VISIBLE);
    }

    @Override
    public void selectListItem(int i) {
        if (i > 0) {
            i = i - 1;
        }
        if (null != mDataList && mDataList.size() > i) {
            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_SELECT_YUYIN, EventType.VOICE_TYPE);
            String databeanStr = GsonUtil.toJson(mDataList.get(i));
            CacheUtils.saveAddressBean(databeanStr);
            sendBroadcastAPP();
            if (CacheUtils.getAddrTime() == 0 || (System.currentTimeMillis() - CacheUtils.getAddrTime() > SIX_HOUR)) {
                CacheUtils.saveAddrTime(System.currentTimeMillis());
            }
            try {
                String address = Encryption.desEncrypt(mDataList.get(i).getAddress());
                CacheUtils.saveAddress(address);
                HomeActivity.address = address;
                StandardCmdClient.getInstance().playTTS(AddressSelectActivity.this, String.format(getString(R.string.harvest_address), address));
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity();
        }else{
            StandardCmdClient.getInstance().playTTS(this, getResources().getString(R.string.tts_out_of_range));
        }
    }

    @Override
    public void nextPage(boolean isNextPage) {
        if (mNoAddress.getVisibility() == View.GONE) {
            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_PAGE_TURNING, EventType.VOICE_TYPE);
            LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            assert manager != null;
            int currentItemPosition = manager.findFirstVisibleItemPosition();
            int last = manager.findLastCompletelyVisibleItemPosition();
            if (isNextPage) {
                if (last == mDataList.size() - 1) {
                    StandardCmdClient.getInstance().playTTS(AddressSelectActivity.this, getString(R.string.last_page));
                } else {
                    StandardCmdClient.getInstance().playTTS(AddressSelectActivity.this, Config.DEFAULT_TTS);
                }
                if (currentItemPosition + getPageNum() * 2 > mDataList.size()) {
                    manager.scrollToPositionWithOffset(mDataList.size() - 1, 0);
                    return;
                }
                manager.scrollToPositionWithOffset(currentItemPosition + getPageNum(), 0);
            } else {
                if (currentItemPosition == 0) {
                    StandardCmdClient.getInstance().playTTS(AddressSelectActivity.this, getString(R.string.first_page));
                } else {
                    StandardCmdClient.getInstance().playTTS(AddressSelectActivity.this, Config.DEFAULT_TTS);
                }
                manager.scrollToPositionWithOffset(currentItemPosition - getPageNum() > 0 ? currentItemPosition - getPageNum() : 0, 0);
            }
        }
    }

    private int getPageNum() {
        return 5;
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
                finishAct();
                break;
            case R.id.add_no_address:
            case R.id.address_select_add:
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_LIST_START_ADD_NEW, EventType.TOUCH_TYPE);
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_START_POI, EventType.TOUCH_TYPE);
                doSearchAddress(false);
                break;
            case R.id.no_internet_btn:
                if (NetUtil.getNetWorkState(this)) {
                    sendDestination();
                    listLayout.setVisibility(View.VISIBLE);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_ACTIVITY && resultCode == RESULT_OK) {
            isDelay = true;
        }
    }

    private void finishAct() {
        if (mDataList.size() == 0) {
            if (startApp != Constant.START_APP_CODE) {
                //当地址列表没有加载出数据，但是有缓存地址时候返回到首页
                finish();
            } else {
                //地址删除完后,直接退出应用
                CacheUtils.saveAddrTime(0);
                CacheUtils.saveAddressBean(null);
                CacheUtils.saveAddress(null);
                AtyContainer.getInstance().finishAllActivity();
            }
        } else if (startApp != Constant.START_APP_CODE && TextUtils.isEmpty(CacheUtils.getAddressBean())) {
            //当删除的地址是缓存的地址的时候,默认给第一个地址
            AddressListBean.IovBean.DataBean dataBean = mDataList.get(0);
            String databeanStr = GsonUtil.toJson(dataBean);
            CacheUtils.saveAddressBean(databeanStr);
            Constant.GOODS_LATITUDE = dataBean.getLatitude();
            Constant.GOODS_LONGITUDE = dataBean.getLongitude();
            sendBroadcastAPP();
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
        } else {
            finish();
        }
    }

    private void sendBroadcastAPP() {
        //统一发送
        Intent intent = new Intent(Constant.PULL_LOCATION);
        intent.setPackage(BuildConfig.APPLICATION_ID);
        sendBroadcast(intent);
    }

    private void sendDestination() {
        sendBroadcast(new Intent("com.baidu.iov.dueros.waimai.requestNaviDes"));
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    private void startActivity() {
        String json = getIntent().getStringExtra(Constant.ORDER_LSIT_EXTRA_STRING);
        if (TextUtils.isEmpty(json)) {
            Intent homeintent = new Intent(AddressSelectActivity.this, HomeActivity.class);
            startActivity(homeintent);
        } else {
            Intent intentFoodList = new Intent(mContext, FoodListActivity.class);
            intentFoodList.putExtra(Constant.STORE_ID, getIntent().getStringExtra(Constant.STORE_ID));
            intentFoodList.putExtra(Constant.ORDER_LSIT_EXTRA_STRING, json);
            intentFoodList.putExtra(Constant.ONE_MORE_ORDER, true);
            intentFoodList.putExtra("flag", false);
            startActivity(intentFoodList);
        }
        finish();
    }
}