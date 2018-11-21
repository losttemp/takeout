package com.baidu.iov.dueros.waimai.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressEditPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.view.ClearEditText;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.iov.dueros.waimai.view.TagListView;
import com.baidu.mapapi.search.sug.SuggestionResult;

import java.util.ArrayList;

public class AddressEditActivity extends BaseActivity<AddressEditPresenter, AddressEditPresenter.AddressEditUi> implements AddressEditPresenter.AddressEditUi, View.OnClickListener {
    private ArrayMap<String, String> reqMap;
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
    private int address_id;
    private AddressDeleteReq mAddressDelReq;
    private AddressEditReq mAddrEditReq;
    private AddressAddReq mAddrAddReq;
    AddressListBean.IovBean.DataBean dataBean;
    private SuggestionResult.SuggestionInfo mLocationBean;

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
    }

    private void initData() {

        ArrayList<String> tags = new ArrayList<>();
        tags.add(getResources().getString(R.string.address_home));
        tags.add(getResources().getString(R.string.address_company));
        tags.add(getResources().getString(R.string.address_tag_other));
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
        if (isEditMode) {
            address_title.setText(getResources().getString(R.string.edit_address));
            mAddressDelReq = new AddressDeleteReq();
            dataBean = (AddressListBean.IovBean.DataBean) intent.getSerializableExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS);
            if (dataBean.getSex() == 0){
                ladyButton.setChecked(true);
                sirButton.setChecked(false);
            } else {
                ladyButton.setChecked(false);
                sirButton.setChecked(true);
            }
            mTagListView.setTags(tags, dataBean.getType());
            try {
                address_tv.setText(Encryption.desEncrypt(dataBean.getAddress()));
                et_name.setText(Encryption.desEncrypt(dataBean.getUser_name()));
                et_phone.setText(Encryption.desEncrypt(dataBean.getUser_phone()));
                et_house_num.setText((dataBean.getHouse() == null) ? "" : Encryption.desEncrypt(dataBean.getHouse()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            iv_del_button.setVisibility(View.VISIBLE);
        } else {
            mLocationBean = intent.getParcelableExtra(Constant.ADDRESS_SEARCCH_INTENT_EXTRE_ADDSTR);
            address_title.setText(getResources().getString(R.string.add_address));
            iv_del_button.setVisibility(View.INVISIBLE);
            address_tv.setText(mLocationBean.getKey());
            try {
                et_name.setText(MyApplicationAddressBean.USER_NAMES.get(0));
                et_phone.setText(MyApplicationAddressBean.USER_PHONES.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(this, R.layout.address_simple_list_item, MyApplicationAddressBean.USER_NAMES);
            ArrayAdapter<String> phoneAdapter = new ArrayAdapter<>(this, R.layout.address_simple_list_item, MyApplicationAddressBean.USER_PHONES);
            et_phone.setThreshold(1);
            et_name.setThreshold(1);
            et_name.setAdapter(nameAdapter);
            et_phone.setAdapter(phoneAdapter);
        }
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
            address_tv.setText(mLocationBean.getKey());
        }
    }

    @Override
    public void updateAddressSuccess(AddressEditBean data) {
        Toast.makeText(this, R.string.address_update_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void updateAddressFail(String msg) {
        Toast.makeText(this, R.string.address_update_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addAddressSuccess(AddressAddBean data) {
        if (isEditMode) {
            if (data.getIov().getData() != null) {
                address_id = data.getIov().getData().getAddress_id();
                mAddrEditReq.setAddress_id(address_id);
                mAddrEditReq.setMt_address_id(dataBean.getMt_address_id());
                getPresenter().requestUpdateAddressData(mAddrEditReq);
            } else {
                Toast.makeText(this, R.string.address_update_fail, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (data.getIov().getData() != null) {
                Toast.makeText(this, R.string.address_save_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AddressSelectActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, R.string.address_save_fail, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void addAddressFail(String msg) {
        Toast.makeText(this, R.string.address_save_fail, Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteAddressSuccess(AddressEditBean data) {
        Toast.makeText(this, R.string.address_delete_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void deleteAddressFail(String msg) {
        Toast.makeText(this, R.string.address_delete_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_edit_address:
            case R.id.address_edit_arrow:
                doSearchAddress(true);
                break;
            case R.id.address_edit_save:
                doSave();
                break;
            case R.id.address_del:
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
            Toast.makeText(this, R.string.address_check_name, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_phone.getText())) {
            Toast.makeText(this, R.string.address_check_phone, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address_tv.getText())) {
            Toast.makeText(this, R.string.address_check_address, Toast.LENGTH_SHORT).show();
        } else {
            String house_num = et_house_num.getText().toString() + "";
            String name = Encryption.encrypt(et_name.getText() + "");
            String phone = Encryption.encrypt(et_phone.getText() + "");
            String address = Encryption.encrypt(address_tv.getText() + "");
            Integer latitude;
            Integer longitude;
            if (mLocationBean == null) {
                latitude = dataBean.getLatitude();
                longitude = dataBean.getLongitude();
            } else {
                latitude = (int) mLocationBean.getPt().latitude * LocationManager.SPAN;
                longitude = (int) mLocationBean.getPt().longitude * LocationManager.SPAN;
            }

            if (isEditMode) {
                mAddrEditReq = new AddressEditReq();
                mAddrEditReq.setUser_phone(phone);
                mAddrEditReq.setUser_name(name);
                mAddrEditReq.setAddress(address);
                mAddrEditReq.setType(type);
                mAddrEditReq.setSex(sex);
                mAddrEditReq.setLatitude(latitude);
                mAddrEditReq.setLongitude(longitude);

                if (dataBean.getMt_address_id() == 0) {
                    mAddrEditReq.setAddress_id(dataBean.getAddress_id());
                    getPresenter().requestUpdateAddressData(mAddrEditReq);
                } else {
                    mAddrAddReq.setUser_phone(phone);
                    mAddrAddReq.setUser_name(name);
                    mAddrAddReq.setAddress(address);
                    mAddrAddReq.setType(type);
                    mAddrAddReq.setSex(sex);
                    mAddrAddReq.setLatitude(latitude);
                    mAddrAddReq.setLongitude(longitude);
                    getPresenter().requestAddAddressData(mAddrAddReq);
                }
            } else {
                mAddrAddReq.setUser_phone(phone);
                mAddrAddReq.setUser_name(name);
                mAddrAddReq.setAddress(address);
                mAddrAddReq.setType(type);
                mAddrAddReq.setSex(sex);
                mAddrAddReq.setLatitude(latitude);
                mAddrAddReq.setLongitude(longitude);
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