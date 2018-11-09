package com.baidu.iov.dueros.waimai.ui;

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
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressEditPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.view.TagListView;

import java.util.ArrayList;

public class AddressEditActivity extends BaseActivity<AddressEditPresenter, AddressEditPresenter.AddressEditUi> implements AddressEditPresenter.AddressEditUi, View.OnClickListener {
    private ArrayMap<String, String> reqMap;
    private TextView address_tv;
    private TagListView mTagListView;
    private EditText et_phone;
    private EditText et_name;
    private ImageView iv_del_button;
    private RadioGroup radioGroup;
    private EditText et_house_num;
    private boolean isEditModle;

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
        address_tv = (TextView) findViewById(R.id.address_edit_address);
        et_name = (EditText) findViewById(R.id.address_edit_name);
        et_phone = (EditText) findViewById(R.id.address_edit_phone);
        iv_del_button = (ImageView) findViewById(R.id.address_del);
        mTagListView = (TagListView) findViewById(R.id.address_edit_tags);
        radioGroup = (RadioGroup) findViewById(R.id.address_edit_gender);
        et_house_num = (EditText) findViewById(R.id.address_edit_house_num);
        findViewById(R.id.address_edit_save).setOnClickListener(this);
        findViewById(R.id.address_back).setOnClickListener(this);
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
        if (isEditModle) {
            AddressListBean.IovBean.DataBean dataBean = (AddressListBean.IovBean.DataBean) intent.getSerializableExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS);
            address_tv.setText(dataBean.getAddress());
            et_name.setText(dataBean.getUser_name());
            et_phone.setText(dataBean.getUser_phone());
            iv_del_button.setVisibility(View.VISIBLE);
        } else {
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
    public void onSuccess(AddressEditBean data) {

    }

    @Override
    public void onError(String error) {

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
            reqMap = new ArrayMap<>();
            getPresenter().requestData(reqMap);
            finish();
        }
    }

    private void doClear() {

    }
}