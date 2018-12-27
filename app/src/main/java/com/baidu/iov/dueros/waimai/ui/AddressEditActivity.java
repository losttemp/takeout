package com.baidu.iov.dueros.waimai.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressAddReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressDeleteReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressAddBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressDeleteBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressEditPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.utils.StringUtils;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.ClearEditText;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.iov.dueros.waimai.view.TagListView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.util.ArrayList;

public class AddressEditActivity extends BaseActivity<AddressEditPresenter, AddressEditPresenter.AddressEditUi> implements AddressEditPresenter.AddressEditUi, View.OnClickListener {
    private TextView address_title;
    private TextView address_tv;
    private ImageView address_arrow;
    private TagListView mTagListView;
    private ClearEditText et_phone;
    private ClearEditText et_name;
    private ImageView iv_del_button;
    private RadioGroup radioGroup;
    private RadioButton sirButton;
    private RadioButton ladyButton;
    private ClearEditText et_house_num;
    private boolean isEditMode;
    private long address_id;
    private AddressDeleteReq mAddressDelReq;
    private AddressEditReq mAddrEditReq;
    private AddressAddReq mAddrAddReq;
    AddressListBean.IovBean.DataBean dataBean;
    private PoiInfo mLocationBean;

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

    private void initView() {
        address_title = (TextView) findViewById(R.id.address_title);
        address_tv = (TextView) findViewById(R.id.address_edit_address);
        address_arrow = (ImageView) findViewById(R.id.address_edit_arrow);
        et_name = (ClearEditText) findViewById(R.id.address_edit_name);
        et_phone = (ClearEditText) findViewById(R.id.address_edit_phone);
        iv_del_button = (ImageView) findViewById(R.id.address_del);
        mTagListView = (TagListView) findViewById(R.id.address_edit_tags);
        radioGroup = (RadioGroup) findViewById(R.id.address_edit_gender);
        sirButton = (RadioButton) findViewById(R.id.address_edit_sir);
        ladyButton = (RadioButton) findViewById(R.id.address_edit_lady);
        et_house_num = (ClearEditText) findViewById(R.id.address_edit_house_num);
        findViewById(R.id.address_edit_save).setOnClickListener(this);
        findViewById(R.id.address_back).setOnClickListener(this);
        iv_del_button.setOnClickListener(this);
        address_tv.setOnClickListener(this);
        address_arrow.setOnClickListener(this);
        et_name.setMaxLength(10);
        et_house_num.setMaxLength(30);
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
                et_name.setText(Encryption.desEncrypt(dataBean.getUser_name()));
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
            et_house_num.setText(mLocationBean.getAddress());
            mTagListView.setTags(tags, "");
            if (null != MyApplicationAddressBean.USER_NAMES && MyApplicationAddressBean.USER_NAMES.size() > 0) {
                et_name.setText(MyApplicationAddressBean.USER_NAMES.get(0));
            }
            if (null != MyApplicationAddressBean.USER_PHONES && MyApplicationAddressBean.USER_PHONES.size() > 0) {
                et_phone.setText(MyApplicationAddressBean.USER_PHONES.get(0));
            }else{
                //fix bug
                et_phone.setText("");
            }
        }
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(this, R.layout.address_simple_list_item, MyApplicationAddressBean.USER_NAMES);
        ArrayAdapter<String> phoneAdapter = new ArrayAdapter<>(this, R.layout.address_simple_list_item, MyApplicationAddressBean.USER_PHONES);
        et_phone.setThreshold(1);
        et_name.setThreshold(1);
        et_name.setAdapter(nameAdapter);
        et_phone.setAdapter(phoneAdapter);
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE) {
            mLocationBean = data.getParcelableExtra(Constant.ADDRESS_SEARCCH_INTENT_EXTRE_ADDSTR);
            address_tv.setText(mLocationBean.getName());
        }
    }

    @Override
    public void updateAddressSuccess(AddressEditBean data) {
        if (data.getMeituan().getCode() == 0) {
            ToastUtils.show(this, getResources().getString(R.string.address_update_success), Toast.LENGTH_SHORT);
            finish();
        } else {
            String msg = data.getMeituan().getMsg();
            ToastUtils.show(this, msg, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void updateAddressFail(String msg) {
        ToastUtils.show(this, getResources().getString(R.string.address_update_fail), Toast.LENGTH_SHORT);
    }

    @Override
    public void addAddressSuccess(AddressAddBean data) {
        if (isEditMode) {
            if (data.getMeituan().getCode() == 0) {
                if (data.getIov().getErrno() == 0) {
                    address_id = data.getIov().getData().getAddress_id();
                    mAddrEditReq.setAddress_id(address_id);
                    mAddrEditReq.setMt_address_id(dataBean.getMt_address_id() == null ?
                            0 : dataBean.getMt_address_id());
                    getPresenter().requestUpdateAddressData(mAddrEditReq);
                } else {
                    String msg = data.getIov().getErrmsg();
                    ToastUtils.show(this, msg, Toast.LENGTH_SHORT);
                }
            } else {
                ToastUtils.show(this, getResources().getString(R.string.address_update_fail), Toast.LENGTH_SHORT);
            }
        } else {
            if (data.getMeituan().getCode() == 0) {
                ToastUtils.show(this, getResources().getString(R.string.address_save_success), Toast.LENGTH_SHORT);
                if (!MyApplicationAddressBean.USER_PHONES.contains(et_phone.getText().toString().trim())) {
                    MyApplicationAddressBean.USER_PHONES.add(et_phone.getText().toString().trim());
                }
                if (!MyApplicationAddressBean.USER_NAMES.contains(et_name.getText().toString().trim())) {
                    MyApplicationAddressBean.USER_NAMES.add(et_name.getText().toString().trim());
                }
                finish();
            } else {
                ToastUtils.show(this,getResources().getString(R.string.address_save_fail), Toast.LENGTH_LONG);
            }
        }

    }

    @Override
    public void addAddressFail(String msg) {
        ToastUtils.show(this, getResources().getString(R.string.address_save_fail), Toast.LENGTH_LONG);
    }

    @Override
    public void deleteAddressSuccess(AddressDeleteBean data) {
        if (data.getIov().getErrno() == 0) {
            ToastUtils.show(this, getResources().getString(R.string.address_delete_success), Toast.LENGTH_SHORT);
            finish();
        } else {
            String msg = data.getIov().getErrmsg();
            ToastUtils.show(this, msg, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void deleteAddressFail(String msg) {
        ToastUtils.show(this, getResources().getString(R.string.address_delete_fail), Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_edit_address:
            case R.id.address_edit_arrow:
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_EDITACT_START_POI,EventType.TOUCH_TYPE);
                doSearchAddress(true);
                break;
            case R.id.address_edit_save:
                doSave();
                break;
            case R.id.address_del:
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_EDITACT_DELETE,EventType.TOUCH_TYPE);
                doClear();
                break;
            case R.id.address_back:
                finish();
                break;
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

        if (TextUtils.isEmpty(et_name.getText())) {
            ToastUtils.show(this, getResources().getString(R.string.address_check_name), Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(et_phone.getText())) {
            ToastUtils.show(this, getResources().getString(R.string.address_check_phone), Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(address_tv.getText())) {
            ToastUtils.show(this, getResources().getString(R.string.address_check_address), Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(type)) {
            ToastUtils.show(this, getResources().getString(R.string.address_check_tagvalue), Toast.LENGTH_SHORT);
        } else {
            String house_num = Encryption.encrypt(et_house_num.getText().toString() + "");
            String name = Encryption.encrypt(et_name.getText() + "");
            String phone = Encryption.encrypt(et_phone.getText() + "");
            String address = Encryption.encrypt(address_tv.getText() + "");
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
                    String oldPhone = Encryption.desEncrypt(dataBean.getUser_phone());
                    if (!oldPhone.equals(et_phone.getText().toString()) && !StringUtils.isChinaPhoneLegal(et_phone.getText().toString())) {
                        ToastUtils.show(this, getResources().getString(R.string.address_phone_error_hint_text), Toast.LENGTH_SHORT);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_EDITACT_EDIT_DATA,EventType.TOUCH_TYPE);
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
                    Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_EDITACT_SAVE,EventType.TOUCH_TYPE);
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
                    Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_SAVE,EventType.TOUCH_TYPE);
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
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_SAVE,EventType.TOUCH_TYPE);
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_NEWACT_EDIT_DATA,EventType.TOUCH_TYPE);
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
                        mAddressDelReq.setAddress_id(dataBean.getAddress_id());
                        getPresenter().requestDeleteAddressData(mAddressDelReq);
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
}