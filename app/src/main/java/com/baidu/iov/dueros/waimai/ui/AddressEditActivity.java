package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.presenter.AddressEditPresenter;
import com.baidu.iov.dueros.waimai.view.TagListView;
import com.baidu.location.BDLocation;

import java.util.ArrayList;

public class AddressEditActivity extends BaseActivity<AddressEditPresenter, AddressEditPresenter.AddressEditUi> implements AddressEditPresenter.AddressEditUi, View.OnClickListener {
    private ArrayMap<String, String> reqMap;
    private TextView address_tv;
    private TagListView mTagListView;
    private EditText et_phone;
    private EditText et_name;
    private ImageView iv_del_button;
    private RadioGroup radioGroup;
    private double lat;
    private double lot;
    private BDLocation mBDLocation;

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
        ArrayList<String> tags = new ArrayList<>();
        tags.add(getResources().getString(R.string.address_home));
        tags.add(getResources().getString(R.string.address_company));
        tags.add(getResources().getString(R.string.address_tag_other));
        mTagListView.setTags(tags);
        radioGroup = (RadioGroup) findViewById(R.id.address_edit_gender);
        findViewById(R.id.address_edit_house_num);
        findViewById(R.id.address_edit_save).setOnClickListener(this);
        address_tv.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mBDLocation = intent.getParcelableExtra("address_select_bd_location");
            iv_del_button.setVisibility(View.VISIBLE);
            String address = intent.getStringExtra("address_select_address");
            lat = intent.getDoubleExtra("address_select_lat", 0);
            lot = intent.getDoubleExtra("address_select_lo", 0);
            String phone = intent.getStringExtra("address_select_phone");
            String name = intent.getStringExtra("address_select_name");
            address_tv.setText(address);
            et_name.setText(name);
            et_phone.setText(phone);
        } else {
            iv_del_button.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3) {
            address_tv.setText(data.getStringExtra("addStr"));
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
                Intent intent = new Intent(this, AddressSearchActivity.class);
                intent.putExtra("address_edit_bd_location",mBDLocation);
                startActivityForResult(intent, 3);
                break;
            case R.id.address_edit_save:
                String s = mTagListView.getmTagValue();
                reqMap = new ArrayMap<>();
                getPresenter().requestData(reqMap);
                startActivity(new Intent(this, AddressSelectActivity.class));
                break;
        }
    }
}
