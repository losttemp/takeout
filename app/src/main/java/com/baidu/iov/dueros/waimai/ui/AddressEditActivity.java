package com.baidu.iov.dueros.waimai.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressHintListAdapter;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressAddReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressDeleteReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressAddBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressDeleteBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressEditPresenter;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.utils.ResUtils;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.StringUtils;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.iov.dueros.waimai.view.TagListView;
import com.baidu.iov.faceos.client.GsonUtil;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.util.ArrayList;

public class AddressEditActivity extends BaseActivity<AddressEditPresenter, AddressEditPresenter.AddressEditUi> implements AddressEditPresenter.AddressEditUi, View.OnClickListener {
    private static final String TAG = AddressEditActivity.class.getSimpleName();

    private TextView address_title;
    private TextView address_tv;
    private ImageView address_arrow;
    private TagListView mTagListView;
    private AutoCompleteTextView et_phone;
    private AutoCompleteTextView et_name;
    private ImageView iv_del_button;
    private RadioGroup radioGroup;
    private RadioButton sirButton;
    private RadioButton ladyButton;
    private EditText et_house_num;
    private boolean isEditMode;
    private long address_id;
    private AddressDeleteReq mAddressDelReq;
    private AddressEditReq mAddrEditReq;
    private AddressAddReq mAddrAddReq;
    AddressListBean.IovBean.DataBean dataBean;
    private PoiInfo mLocationBean;
    private ImageView nameCloseView;
    private ImageView phoneCloseView;
    private ImageView houseCloseView;
    private final int nameMaxLength = 10;
    private final int houseMaxLength = 30;
    //因为网络请求保存收货地址是异步的, 在网络请求完成之前不允许重复点击的
    private boolean isDealWidthSaveRequest = false;

    @Override
    AddressEditPresenter createPresenter() {
        return new AddressEditPresenter();
    }

    @Override
    AddressEditPresenter.AddressEditUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add_or_edit);
        initView();
        initData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        AccessibilityClient.getInstance().unregister(this);
    }

    private void initView() {
        address_title = (TextView) findViewById(R.id.address_title);
        address_tv = (TextView) findViewById(R.id.address_edit_address);
        address_arrow = (ImageView) findViewById(R.id.address_edit_arrow);
        et_name = (AutoCompleteTextView) findViewById(R.id.address_edit_name);
        et_phone = (AutoCompleteTextView) findViewById(R.id.address_edit_phone);
        iv_del_button = (ImageView) findViewById(R.id.address_del);
        mTagListView = (TagListView) findViewById(R.id.address_edit_tags);
        radioGroup = (RadioGroup) findViewById(R.id.address_edit_gender);
        sirButton = (RadioButton) findViewById(R.id.address_edit_sir);
        ladyButton = (RadioButton) findViewById(R.id.address_edit_lady);
        et_house_num = (EditText) findViewById(R.id.address_edit_house_num);

        nameCloseView = findViewById(R.id.et_name_close);
        phoneCloseView = findViewById(R.id.et_phone_close);
        houseCloseView = findViewById(R.id.et_house_close);
        nameCloseView.setOnClickListener(this);
        phoneCloseView.setOnClickListener(this);
        houseCloseView.setOnClickListener(this);
        findViewById(R.id.address_edit_save).setOnClickListener(this);
        findViewById(R.id.address_back).setOnClickListener(this);
        iv_del_button.setOnClickListener(this);
        address_tv.setOnClickListener(this);
        address_arrow.setOnClickListener(this);

        StringUtils.setEditTextInputSpeChat(et_name);
        StringUtils.setEditTextInputSpeChat(et_house_num);
    }

    private void initData() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add(getResources().getString(R.string.address_company));
        tags.add(getResources().getString(R.string.address_home));
        tags.add(getResources().getString(R.string.address_tag_other));
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
        if (isEditMode) {
            address_title.setText(getResources().getString(R.string.edit_address));
            mAddressDelReq = new AddressDeleteReq();
            dataBean = (AddressListBean.IovBean.DataBean) intent.getSerializableExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS);
            if (null != dataBean.getSex() && dataBean.getSex() == 0) {
                ladyButton.setChecked(true);
                sirButton.setChecked(false);
            } else {
                ladyButton.setChecked(false);
                sirButton.setChecked(true);
            }
            String type = null;
            if (!TextUtils.isEmpty(dataBean.getType())) {
                type = dataBean.getType();
            } else {
                type = getResources().getString(R.string.address_tag_other);
            }
            mTagListView.setTags(tags, type);
            if (null != dataBean.getAddress_id()) {
                iv_del_button.setVisibility(View.VISIBLE);
            }
            try {
                address_tv.setText(Encryption.desEncrypt(dataBean.getAddress()));
                String name = Encryption.desEncrypt(dataBean.getUser_name());
                if (!TextUtils.isEmpty(name)) {
                    if (name.length() > 10) {
                        name = name.substring(0, 10);
                    }
                    et_name.setText(name);
                }
                et_phone.setText(Encryption.desEncrypt(dataBean.getUser_phone()));
                et_house_num.setText((dataBean.getHouse() == null) ? "" : Encryption.desEncrypt(dataBean.getHouse()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mLocationBean = intent.getParcelableExtra(Constant.ADDRESS_SEARCCH_INTENT_EXTRE_ADDSTR);
            address_title.setText(getResources().getString(R.string.add_address));
            iv_del_button.setVisibility(View.INVISIBLE);
            address_tv.setText(mLocationBean.getName());
            mTagListView.setTags(tags, "");
            if (null != MyApplicationAddressBean.USER_NAMES && MyApplicationAddressBean.USER_NAMES.size() > 0) {
                String name = MyApplicationAddressBean.USER_NAMES.get(0);
                if (name.length() > 10) {
                    name = name.substring(0, 10);
                }
                et_name.setText(name);
            } else {
                nameCloseView.setVisibility(View.GONE);
            }
            if (null != MyApplicationAddressBean.USER_PHONES && MyApplicationAddressBean.USER_PHONES.size() > 0) {
                et_phone.setText(MyApplicationAddressBean.USER_PHONES.get(0));
            } else {
                phoneCloseView.setVisibility(View.GONE);
            }

        }
        if (et_house_num.getText().toString().length() <= 0) {
            houseCloseView.setVisibility(View.GONE);
        }
        AddressHintListAdapter nameAdapter = new AddressHintListAdapter(this, MyApplicationAddressBean.USER_NAMES);
        AddressHintListAdapter phoneAdapter = new AddressHintListAdapter(this, MyApplicationAddressBean.USER_PHONES);
        et_phone.setThreshold(1);
        et_name.setThreshold(1);
        et_name.setAdapter(nameAdapter);
        et_phone.setAdapter(phoneAdapter);
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus && MyApplicationAddressBean.USER_NAMES.size() > 0) {
                    view.showDropDown();
                }
            }
        });
        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus && MyApplicationAddressBean.USER_PHONES.size() > 0) {
                    view.showDropDown();
                }
            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    nameCloseView.setVisibility(View.VISIBLE);
                } else {
                    nameCloseView.setVisibility(View.GONE);
                }
                if (s.length() > nameMaxLength) {
                    ToastUtils.show(AddressEditActivity.this, getResources().getString(R.string.edit_text_view_max_length_hint), Toast.LENGTH_SHORT);
                    et_name.setText(s.toString().substring(0, nameMaxLength));
                    et_name.setSelection(et_name.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    phoneCloseView.setVisibility(View.VISIBLE);
                } else {
                    phoneCloseView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    phoneCloseView.setVisibility(View.VISIBLE);
                } else {
                    phoneCloseView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_house_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    houseCloseView.setVisibility(View.VISIBLE);
                } else {
                    houseCloseView.setVisibility(View.GONE);
                }
                if (s.length() > houseMaxLength) {
                    ToastUtils.show(AddressEditActivity.this, getResources().getString(R.string.edit_text_view_max_length_hint), Toast.LENGTH_SHORT);
                    et_house_num.setText(s.toString().substring(0, houseMaxLength));
                    et_house_num.setSelection(et_house_num.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.address_del).setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_EDITACT_DELETE, EventType.TOUCH_TYPE);
                        initTTS = true;
                        deleteAddressData();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        findViewById(R.id.address_edit_save).setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        initTTS = true;
                        doSave();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private boolean initTTS = false;

    @Override
    protected void onResume() {
        super.onResume();
        AccessibilityClient.getInstance().register(this, true, null, null);
    }

    private void deleteAddressData() {
        mAddressDelReq.setAddress_id(dataBean.getAddress_id());
        if (dataBean.getMt_address_id() != null) {
            mAddressDelReq.setMt_address_id(dataBean.getMt_address_id());
        }
        getPresenter().requestDeleteAddressData(mAddressDelReq);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE) {
            mLocationBean = data.getParcelableExtra(Constant.ADDRESS_SEARCCH_INTENT_EXTRE_ADDSTR);
            address_tv.setText(TextUtils.isEmpty(mLocationBean.getName()) ? "" : mLocationBean.getName());
        }
    }

    @Override
    public void updateAddressSuccess(AddressEditBean data, AddressEditReq addressEditreq) {
        isDealWidthSaveRequest = false;
        if (data.getMeituan().getCode() == 0) {
            sendTTS(R.string.tts_save_address_success);
            showToast(R.string.address_update_success);
            if (dataBean.getAddress_id() != null && getIntent().getLongExtra(Constant.ADDRESS_SELECT_ID, 0) == dataBean.getAddress_id()) {
                AddressListBean.IovBean.DataBean bean = new AddressListBean.IovBean.DataBean();
                bean.setAddress(addressEditreq.getAddress());
                bean.setAddress_id(addressEditreq.getAddress_id());
                bean.setHouse(addressEditreq.getHouse());
                bean.setMt_address_id(addressEditreq.getMt_address_id());
                bean.setLatitude(addressEditreq.getLatitude());
                bean.setSex(addressEditreq.getSex());
                bean.setType(addressEditreq.getType());
                bean.setUser_name(addressEditreq.getUser_name());
                bean.setUser_phone(addressEditreq.getUser_phone());
                bean.setAddressRangeTip(dataBean.getAddressRangeTip());
                bean.setCanShipping(dataBean.getCanShipping());
                bean.setIs_hint(dataBean.isIs_hint());
                CacheUtils.saveAddressBean(GsonUtil.toJson(bean));
            }
            setResult(RESULT_OK);
            finish();
        } else {
            ToastUtils.show(this, getString(R.string.address_update_fail), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void updateAddressFail(String msg) {
        isDealWidthSaveRequest = false;
        ToastUtils.show(this, getResources().getString(R.string.address_update_fail), Toast.LENGTH_SHORT);
    }

    @Override
    public void addAddressSuccess(AddressAddBean data) {
        isDealWidthSaveRequest = false;
        if (isEditMode && !getString(R.string.address_destination).equals(dataBean.getType())) {
            if (data.getMeituan().getCode() == 0) {
                if (data.getIov().getErrno() == 0) {
                    address_id = data.getIov().getData().getAddress_id();
                    mAddrEditReq.setAddress_id(address_id);
                    mAddrEditReq.setMt_address_id(dataBean.getMt_address_id() == null ?
                            0 : dataBean.getMt_address_id());
                    getPresenter().requestUpdateAddressData(mAddrEditReq);
                } else {
                    ToastUtils.show(this, getString(R.string.address_update_fail), Toast.LENGTH_SHORT);
                }
            } else {
                ToastUtils.show(this, getString(R.string.address_update_fail), Toast.LENGTH_SHORT);
            }
        } else {
            if (data.getMeituan().getCode() == 0) {
                sendTTS(R.string.tts_save_address_success);
                showToast(R.string.address_save_success);
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtils.show(this, getString(R.string.address_save_fail), Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void addAddressFail(String msg) {
        isDealWidthSaveRequest = false;
        ToastUtils.show(this, getString(R.string.address_save_fail), Toast.LENGTH_LONG);
    }

    @Override
    public void deleteAddressSuccess(AddressDeleteBean data) {
        if (data.getIov().getErrno() == 0) {
            showToast(R.string.address_delete_success);
            sendTTS(R.string.tts_delete_address);
            String json = CacheUtils.getAddressBean();
            AddressListBean.IovBean.DataBean bean = GsonUtil.fromJson(json, AddressListBean.IovBean.DataBean.class);
            if (bean != null) {
                getPresenter().isDeteleCache(dataBean.getAddress(), dataBean.getUser_phone(), bean.getAddress(), bean.getUser_phone());
            }
            setResult(RESULT_OK);
            finish();
        } else {
            ToastUtils.show(this, getString(R.string.address_delete_fail), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void deleteAddressFail(String msg) {
        ToastUtils.show(this, getString(R.string.address_delete_fail), Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_edit_address:
            case R.id.address_edit_arrow:
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_EDITACT_START_POI, EventType.TOUCH_TYPE);
                doSearchAddress(true);
                break;
            case R.id.address_edit_save:
                if (isDealWidthSaveRequest) {
                    Lg.getInstance().d(TAG, "isDealWidthSaveRequest");
                } else {
                    doSave();
                }
                break;
            case R.id.address_del:
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_EDITACT_DELETE, EventType.TOUCH_TYPE);
                doClear();
                break;
            case R.id.address_back:
                finish();
                break;
            case R.id.et_name_close:
                et_name.setText("");
                break;
            case R.id.et_phone_close:
                et_phone.setText("");
                break;
            case R.id.et_house_close:
                et_house_num.setText("");
                break;
        }
    }

    private void sendTTS(int stringId) {
        if (initTTS) {
            initTTS = false;
            StandardCmdClient.getInstance().playTTS(mContext, ResUtils.getString(stringId));
        }
    }

    private void doSave() {
        mAddrAddReq = new AddressAddReq();
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        int sex;
        if (checkedRadioButtonId == R.id.address_edit_lady) {
            sex = 0;
        } else {
            sex = 1;
        }
        String type = mTagListView.getmTagValue();

        if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
            ToastUtils.show(this, getResources().getString(R.string.address_check_name), Toast.LENGTH_SHORT);
            sendTTS(R.string.tts_save_address_error);
        } else if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
            ToastUtils.show(this, getResources().getString(R.string.address_check_phone), Toast.LENGTH_SHORT);
            sendTTS(R.string.tts_save_address_error);
        } else if (TextUtils.isEmpty(address_tv.getText().toString().trim())) {
            ToastUtils.show(this, getResources().getString(R.string.address_check_address), Toast.LENGTH_SHORT);
            sendTTS(R.string.tts_save_address_error);
        } else if (TextUtils.isEmpty(type)) {
            ToastUtils.show(this, getResources().getString(R.string.address_check_tagvalue), Toast.LENGTH_SHORT);
            sendTTS(R.string.tts_save_address_error);
        } else {
            String house_num = Encryption.encrypt(et_house_num.getText().toString().trim() + "");
            String name = Encryption.encrypt(et_name.getText().toString().trim() + "");
            String phone = Encryption.encrypt(et_phone.getText().toString().trim() + "");
            String address = Encryption.encrypt(address_tv.getText().toString().trim() + "");
            Integer latitude;
            Integer longitude;
            if (mLocationBean == null) {
                latitude = dataBean.getLatitude();
                longitude = dataBean.getLongitude();
            } else {
                latitude = (int) (mLocationBean.getLocation().latitude * LocationManager.SPAN);
                longitude = (int) (mLocationBean.getLocation().longitude * LocationManager.SPAN);
            }

            if (isEditMode) {
                try {
                    if (TextUtils.isEmpty(dataBean.getUser_phone()) && !StringUtils.isChinaPhoneLegal(et_phone.getText().toString())) {
                        ToastUtils.show(this, getResources().getString(R.string.address_phone_error_hint_text), Toast.LENGTH_SHORT);
                        return;
                    } else if (!TextUtils.isEmpty(dataBean.getUser_phone())) {
                        String oldPhone = Encryption.desEncrypt(dataBean.getUser_phone());
                        if (!oldPhone.equals(et_phone.getText().toString()) && !StringUtils.isChinaPhoneLegal(et_phone.getText().toString())) {
                            ToastUtils.show(this, getResources().getString(R.string.address_phone_error_hint_text), Toast.LENGTH_SHORT);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_EDITACT_EDIT_DATA, EventType.TOUCH_TYPE);
                mAddrEditReq = new AddressEditReq();
                mAddrEditReq.setUser_phone(phone);
                mAddrEditReq.setUser_name(name);
                mAddrEditReq.setAddress(address);
                mAddrEditReq.setType(type);
                mAddrEditReq.setSex(sex);
                mAddrEditReq.setHouse(house_num);
                mAddrEditReq.setLatitude(latitude);
                mAddrEditReq.setLongitude(longitude);

                if (null != dataBean.getAddress_id()) {
                    mAddrEditReq.setAddress_id(dataBean.getAddress_id());
                    if (null != dataBean.getMt_address_id()) {
                        mAddrEditReq.setMt_address_id(dataBean.getMt_address_id());
                    }
                    Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_EDITACT_SAVE, EventType.TOUCH_TYPE);
                    isDealWidthSaveRequest = true; // 因为requestUpdateAddressData 是网络请求, 异步处理的, 在完成请求前是不应该相应点击事件的
                    getPresenter().requestUpdateAddressData(mAddrEditReq);
                } else {
                    if (null != dataBean.getMt_address_id()) {
                        mAddrAddReq.setMt_address_id(dataBean.getMt_address_id());
                    }
                    mAddrAddReq.setUser_phone(phone);
                    mAddrAddReq.setUser_name(name);
                    mAddrAddReq.setAddress(address);
                    mAddrAddReq.setType(type);
                    mAddrAddReq.setSex(sex);
                    mAddrAddReq.setHouse(house_num);
                    mAddrAddReq.setLatitude(latitude);
                    mAddrAddReq.setLongitude(longitude);
                    Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_SAVE, EventType.TOUCH_TYPE);
                    isDealWidthSaveRequest = true; // 因为requestUpdateAddressData 是网络请求, 异步处理的, 在完成请求前是不应该相应点击事件的
                    getPresenter().requestAddAddressData(mAddrAddReq);
                }
            } else {
                if (!StringUtils.isChinaPhoneLegal(et_phone.getText().toString())) {
                    ToastUtils.show(this, getResources().getString(R.string.address_phone_error_hint_text), Toast.LENGTH_SHORT);
                    return;
                }
                mAddrAddReq.setUser_phone(phone);
                mAddrAddReq.setUser_name(name);
                mAddrAddReq.setAddress(address);
                mAddrAddReq.setType(type);
                mAddrAddReq.setSex(sex);
                mAddrAddReq.setHouse(house_num);
                mAddrAddReq.setLatitude(latitude);
                mAddrAddReq.setLongitude(longitude);
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_SAVE, EventType.TOUCH_TYPE);
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_EDIT_DATA, EventType.TOUCH_TYPE);
                isDealWidthSaveRequest = true; // 因为requestUpdateAddressData 是网络请求, 异步处理的, 在完成请求前是不应该相应点击事件的
                getPresenter().requestAddAddressData(mAddrAddReq);
            }
        }
    }

    private void doClear() {
        ConfirmDialog dialog = new ConfirmDialog.Builder(this)
                .setTitle(R.string.delete_address_title)
                .setMessage(R.string.delete_address_message)
                .setNegativeButton(R.string.delete_address_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAddressData();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.delete_address_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCloseButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    //统一toast
    private void showToast(int stringId) {
        ToastUtils.customTime(this, getString(stringId), 500);
    }
}