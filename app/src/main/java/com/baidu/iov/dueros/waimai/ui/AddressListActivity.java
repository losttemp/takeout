package com.baidu.iov.dueros.waimai.ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.BuildConfig;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressListAdapter;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.net.Config;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressListPresenter;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.faceos.client.GsonUtil;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddressListActivity extends BaseActivity<AddressListPresenter, AddressListPresenter.AddressListUi>
        implements AddressListPresenter.AddressListUi, View.OnClickListener {
    private final static String TAG = AddressListActivity.class.getSimpleName();
    private ImageView mCancelImg;
    private Button mAddBtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AddressListAdapter mAddressListAdapter;
    private List<AddressListBean.IovBean.DataBean> mDataListBean;
    public final static String ADDRESS_DATA = "address_data";
    private AddressListBean.IovBean.DataBean mAddressData;
    private RelativeLayout viewById;
    private View mLoading;
    private LinearLayout mNoNet;
    private Button mNoInternetButton;
    private BroadcastReceiver mReceiver;

    @Override
    AddressListPresenter createPresenter() {
        return new AddressListPresenter();
    }

    @Override
    AddressListPresenter.AddressListUi getUi() {
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        viewById = findViewById(R.id.rv_activity_address_list);
        getPresenter().initDesBeans();
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) viewById.getLayoutParams();
        lp.topMargin = getStateBar();
        viewById.setLayoutParams(lp);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.px962dp));
        getWindow().setGravity(Gravity.TOP);
        Entry.getInstance().onEvent(Constant.ORDERSUBMIT_ADDRESS_DIALOG, EventType.TOUCH_TYPE);

    }

    public void initView() {

        //此处优先从Intent中取值; 如果直接从本地读取,getCanShipping可能获取的值不对
        Intent intent = getIntent();
        mAddressData = (AddressListBean.IovBean.DataBean) intent.getSerializableExtra(Constant.ADDRESS_DATA);

        if (mAddressData == null) {
            SharedPreferences sharedPreferences = getSharedPreferences("_cache", MODE_PRIVATE);
            String addressDataJson = sharedPreferences.getString(Constant.ADDRESS_DATA, null);
            if (addressDataJson != null) {
                mAddressData = GsonUtil.fromJson(addressDataJson, AddressListBean.IovBean.DataBean.class);
            }
        }
        mNoNet = findViewById(R.id.no_net);
        mNoInternetButton = findViewById(R.id.no_internet_btn);
        mLoading = findViewById(R.id.ll_loading);
        mCancelImg = findViewById(R.id.cancel_action);
        mAddBtn = findViewById(R.id.img_add);
        mCancelImg.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mNoInternetButton.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.address_list);
        int orientation = RecyclerView.VERTICAL;
        mLayoutManager = new LinearLayoutManager(this, orientation, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAddressListAdapter = new AddressListAdapter(this);
        mRecyclerView.setAdapter(mAddressListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        setHeader();


        mAddressListAdapter.setOnItemClickListener(new AddressListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.img_select:
                        Entry.getInstance().onEvent(Constant.ORDERSUBMIT_UPDATE_ADDRESS, EventType.TOUCH_TYPE);
                        Intent intent = new Intent(getApplicationContext(), AddressEditActivity.class);
                        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
                        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS, mDataListBean.get(position));
                        intent.putExtra(Constant.ADDRESS_SELECT_ID, mAddressData.getAddress_id());
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        break;

                    default:
                        Entry.getInstance().onEvent(Constant.ORDERSUBMIT_CHANGE_ADDRESS_VOICE, EventType.VOICE_TYPE);
                        Entry.getInstance().onEvent(Constant.ORDERSUBMIT_CHANGE_ADDRESS, EventType.TOUCH_TYPE);
                        AddressListBean.IovBean.DataBean addressData = mDataListBean.get(position);
                        Intent data = new Intent();
                        data.putExtra(ADDRESS_DATA, addressData);
                        SharedPreferences sp = WaiMaiApplication.getInstance().getSharedPreferences
                                ("_cache", AddressSelectActivity.MODE_PRIVATE);
                        String databeanStr = GsonUtil.toJson(addressData);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Constant.ADDRESS_DATA, databeanStr);
                        editor.commit();
                        try {
                            String address = Encryption.desEncrypt(addressData.getAddress());
                            CacheUtils.saveAddress(address);
                            HomeActivity.address = address;
                            sendBroadcastAPP();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        setResult(RESULT_OK, data);
                        if (mDataListBean.get(position).getCanShipping() != 1) {
                            ToastUtils.show(getApplicationContext(), getResources().getString(R.string.order_submit_msg8), Toast.LENGTH_LONG);
                        }
                        finish();
                        break;
                }
            }
        });
        netDataReque();
    }


    private void setHeader() {
        View header = LayoutInflater.from(this).inflate(R.layout.address_title_item, mRecyclerView, false);
        TextView addressTv = header.findViewById(R.id.tv_address_title);
        TextView typeTv = header.findViewById(R.id.tv_address_type_title);
        TextView nameTv = header.findViewById(R.id.tv_name_title);

        try {
            if (mAddressData.getCanShipping() != 1) {
                addressTv.setTextColor(0x99ffffff);
                nameTv.setTextColor(0x99ffffff);
                typeTv.setTextColor(0x99ffffff);
            }
            addressTv.setText(Encryption.desEncrypt(mAddressData.getAddress()));

            if (addressTv.getText().length() > 16) {
                addressTv.setWidth((int) getResources().getDimension(R.dimen.px600dp));
            }
           if (mContext.getString(R.string.address_destination).equals(mAddressData.getType())) {
                typeTv.setBackgroundResource(R.drawable.tag_bg_mudidi);
                typeTv.setText(mAddressData.getType());
                if (MyApplicationAddressBean.USER_PHONES.get(0) != null && MyApplicationAddressBean.USER_NAMES.get(0) != null) {
                    nameTv.setText(MyApplicationAddressBean.USER_NAMES.get(0) + "  " + MyApplicationAddressBean.USER_PHONES.get(0));
                }
            }else{
               String userInfo = Encryption.desEncrypt(mAddressData.getUser_name()) + " " + Encryption.desEncrypt(mAddressData.getUser_phone());
               nameTv.setText(userInfo);
               if (getString(R.string.address_home).equals(mAddressData.getType())) {
                   typeTv.setBackgroundResource(R.drawable.tag_bg_green);
                   typeTv.setText(mAddressData.getType());

               } else if (getString(R.string.address_company).equals(mAddressData.getType())) {
                   typeTv.setBackgroundResource(R.drawable.tag_bg_blue);
                   typeTv.setText(mAddressData.getType());

               } else if (getString(R.string.address_tag_other).equals(mAddressData.getType())) {
                   typeTv.setBackgroundResource(R.drawable.tag_bg);
                   typeTv.setText(mAddressData.getType());

               } else {
                   typeTv.setBackgroundResource(R.drawable.tag_bg);
                   typeTv.setText(getString(R.string.address_tag_other));

               }

           }


        } catch (Exception e) {
            e.printStackTrace();
        }
        mAddressListAdapter.setHeaderView(header);
    }

    private void netDataReque() {
        if (NetUtil.getNetWorkState(this)) {
            mNoNet.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
            if (getIntent() != null) {
                long wmPoiId = getIntent().getLongExtra(Constant.WM_POI_ID, 0);
                getPresenter().requestData(wmPoiId);
                sendUpdataBroadcast();
            }
        } else {
            mNoNet.setVisibility(View.VISIBLE);
            viewById.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ArrayList<String> prefix = new ArrayList<>();
        prefix.add("选择");
        AccessibilityClient.getInstance().register(this, true, prefix, null);
        initView();
    }

    private void sendUpdataBroadcast() {
        Intent intent = new Intent("com.baidu.iov.dueros.waimai.requestNaviDes");
        sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AccessibilityClient.getInstance().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cancel_action:
                finish();
                break;

            case R.id.img_add:
                Entry.getInstance().onEvent(Constant.ORDERSUBMIT_ALERT_ADDRESS, EventType.TOUCH_TYPE);
                doSearchAddress(false);
                break;
            case R.id.no_internet_btn:
                netDataReque();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegisterReceiver(BroadcastReceiver mReceiver, IntentFilter intentFilter) {
        this.registerReceiver(mReceiver, intentFilter);
        this.mReceiver = mReceiver;
    }

    @Override
    public void onGetAddressListSuccess(List<AddressListBean.IovBean.DataBean> data) {
        if (data != null) {
//            mDataListBean = data.getIov().getData();
            this.mDataListBean = data;
            Collections.sort(mDataListBean);
            mAddressListAdapter.setData(mDataListBean);
        } else {
            Lg.getInstance().d(TAG, "not find data !");
        }
        mLoading.setVisibility(View.GONE);
        mNoNet.setVisibility(View.GONE);
        viewById.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetAddressListFailure(String msg) {
        viewById.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mNoNet.setVisibility(View.VISIBLE);
    }

    @Override
    public void selectListItem(int i) {
        if (null != mDataListBean && mDataListBean.size() > i) {
            if (i >= 1) {
                AddressListBean.IovBean.DataBean addressData = mDataListBean.get(i - 1);
                Intent data = new Intent();
                data.putExtra(ADDRESS_DATA, addressData);
                data.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, true);
                SharedPreferences sp = WaiMaiApplication.getInstance().getSharedPreferences
                        ("_cache", AddressSelectActivity.MODE_PRIVATE);
                String databeanStr = GsonUtil.toJson(addressData);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constant.ADDRESS_DATA, databeanStr);
                editor.commit();
                setResult(RESULT_OK, data);
            }
            finish();
        }
    }

    @Override
    public void nextPage(boolean isNextPage) {
        if (mDataListBean.size() > 0) {
            LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            assert manager != null;
            int currentItemPosition = manager.findFirstVisibleItemPosition();
            int last = manager.findLastCompletelyVisibleItemPosition();
            if (isNextPage) {
                if (last == mDataListBean.size()) {
                    StandardCmdClient.getInstance().playTTS(mContext, getString(R.string.last_page));
                } else {
                    StandardCmdClient.getInstance().playTTS(mContext, Config.DEFAULT_TTS);
                }
                if (currentItemPosition + getPageNum() * 2 > mDataListBean.size()) {
                    manager.scrollToPositionWithOffset(mDataListBean.size(), 0);
                    return;
                }
                manager.scrollToPositionWithOffset(currentItemPosition + getPageNum(), 0);
            } else {
                if (currentItemPosition == 0) {
                    StandardCmdClient.getInstance().playTTS(mContext, getString(R.string.first_page));
                } else {
                    StandardCmdClient.getInstance().playTTS(mContext, Config.DEFAULT_TTS);
                }
                manager.scrollToPositionWithOffset(currentItemPosition - getPageNum() > 0 ? currentItemPosition - getPageNum() : 0, 0);
            }
        }
    }

    private int getPageNum() {
        return 4;
    }

    private int getStateBar() {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void sendBroadcastAPP() {
        //统一发送
        Intent intent = new Intent(Constant.PULL_LOCATION);
        intent.setPackage(BuildConfig.APPLICATION_ID);
        sendBroadcast(intent);
    }

}
