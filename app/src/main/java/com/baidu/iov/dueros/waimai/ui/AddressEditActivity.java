package com.baidu.iov.dueros.waimai.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressDeleteReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressEditPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.iov.dueros.waimai.view.TagListView;

import java.util.ArrayList;

public class AddressEditActivity extends BaseActivity<AddressEditPresenter, AddressEditPresenter.AddressEditUi> implements AddressEditPresenter.AddressEditUi, View.OnClickListener {
    private ArrayMap<String, String> reqMap;
    private TextView address_title;
    private TextView address_tv;
    private TagListView mTagListView;
    private EditText et_phone;
    private EditText et_name;
    private ImageView iv_del_button;
    private RadioGroup radioGroup;
    private EditText et_house_num;
    private boolean isEditModle;
    private AddressDeleteReq mAddressDelReq;
    private AddressEditReq mAddrEditReq;
    AddressListBean.IovBean.DataBean dataBean;

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
        et_name = (EditText) findViewById(R.id.address_edit_name);
        et_phone = (EditText) findViewById(R.id.address_edit_phone);
        iv_del_button = (ImageView) findViewById(R.id.address_del);
        mTagListView = (TagListView) findViewById(R.id.address_edit_tags);
        radioGroup = (RadioGroup) findViewById(R.id.address_edit_gender);
        et_house_num = (EditText) findViewById(R.id.address_edit_house_num);
        findViewById(R.id.address_edit_save).setOnClickListener(this);
        findViewById(R.id.address_back).setOnClickListener(this);
        iv_del_button.setOnClickListener(this);
        address_tv.setOnClickListener(this);
    }

    private void initData() {

        ArrayList<String> tags = new ArrayList<>();
        tags.add(getResources().getString(R.string.address_home));
        tags.add(getResources().getString(R.string.address_company));
        tags.add(getResources().getString(R.string.address_tag_other));
        mTagListView.setTags(tags);
        Intent intent = getIntent();
        isEditModle = intent.getBooleanExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
        mAddrEditReq = new AddressEditReq();
        if (isEditModle) {
            address_title.setText(getResources().getString(R.string.edit_address));
            mAddressDelReq = new AddressDeleteReq();
            dataBean = (AddressListBean.IovBean.DataBean) intent.getSerializableExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS);
            try {
                address_tv.setText(Encryption.desEncrypt(dataBean.getAddress()));
                et_name.setText(Encryption.desEncrypt(dataBean.getUser_name()));
                et_phone.setText(Encryption.desEncrypt(dataBean.getUser_phone()));
                et_house_num.setText(Encryption.desEncrypt(dataBean.getHouse()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            iv_del_button.setVisibility(View.VISIBLE);
        } else {
            address_title.setText(getResources().getString(R.string.add_address));
            iv_del_button.setVisibility(View.GONE);
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
            String address = data.getStringExtra(Constant.ADDRESS_SEARCCH_INTENT_EXTRE_ADDSTR);
            address_tv.setText(address);
        }
    }


    @Override
    public void updateAddressSuccess(AddressEditBean data) {

    }

    @Override
    public void updateAddressFail(String msg) {

    }

    @Override
    public void addAddressSuccess(AddressEditBean data) {

    }

    @Override
    public void addAddressFail(String msg) {

    }

    @Override
    public void deleteAddressSuccess(AddressEditBean data) {
        finish();
    }

    @Override
    public void deleteAddressFail(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_edit_address:
                doSearchAddress();
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


    private void doSearchAddress() {
        Intent intent = new Intent(this, AddressSuggestionActivity.class);
        if (mBDLocation != null) {
            intent.putExtra(Constant.ADDRESS_EDIT_INTENT_EXTRE_CITY, mBDLocation.getCity());
            startActivityForResult(intent, Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE);
        } else {
//                    TODO:
        }
    }

    private void doSave() {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        int sex;
        if (checkedRadioButtonId == R.id.address_edit_lady) {
            sex = 1;
        } else {
            sex = 0;
        }

        if (TextUtils.isEmpty(et_name.getText())) {
            Toast.makeText(this, "please check name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_phone.getText())) {
            Toast.makeText(this, "please check phone", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address_tv.getText())) {
            Toast.makeText(this, "please check address", Toast.LENGTH_SHORT).show();
        } else {
            String s = mTagListView.getmTagValue();
            String house_num = et_house_num.getText().toString() + "";
            String name = et_name.getText() + "";
            String phone = et_phone.getText() + "";
            long addressId = dataBean.getMt_address_id();
            String address = address_tv.getText()+"";
            mAddrEditReq.setUser_phone(Encryption.encrypt(phone));
            mAddrEditReq.setUser_name(Encryption.encrypt(name));
            mAddrEditReq.setAddress(Encryption.encrypt(address));
            mAddrEditReq.setHouse(Encryption.encrypt(house_num));
            mAddrEditReq.setType(s);
            mAddrEditReq.setSex(sex);
            if (isEditModle) {
                mAddrEditReq.setAddress_id(addressId);
                mAddrEditReq.setLatitude(dataBean.getLatitude());
                mAddrEditReq.setLongitude(dataBean.getLongitude());
                getPresenter().requestUpdateAddressData(mAddrEditReq);
            } else {
                getPresenter().requestAddAddressData(mAddrEditReq);
            }
            finish();
        }
    }

    private void doClear() {
        ConfirmDialog dialog = new ConfirmDialog.Builder(this)
                .setTitle(R.string.delete_address_title)
                .setMessage(R.string.delete_address_message)
                .setNegativeButton(R.string.delete_address_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAddressDelReq.setAddress_id(dataBean.getMt_address_id());
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