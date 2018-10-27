package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.presenter.AddressEditPresenter;
import com.baidu.location.BDLocation;

public class AddressEditActivity extends BaseActivity<AddressEditPresenter, AddressEditPresenter.AddressEditUi> implements AddressEditPresenter.AddressEditUi, View.OnClickListener {
    private ArrayMap<String, String> reqMap;
    private TextView address_tv;
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
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("hhr","onactivityresult");
        if (resultCode == 3) {
                Log.d("hhr",mBDLocation.getAddrStr());
/*                intent.putExtra("address_search_lat", pt.longitude);
                intent.putExtra("addStr",addr);
                intent.putExtra("address_search_lat",pt.longitude);*/
                address_tv.setText(data.getStringExtra("addStr"));
        }
    }

    private void initView() {
        address_tv = (TextView) findViewById(R.id.address_edit_address);
        findViewById(R.id.address_edit_name);
        findViewById(R.id.address_edit_phone);
        findViewById(R.id.address_edit_gender);
        findViewById(R.id.address_edit_house_num);
        findViewById(R.id.address_edit_save).setOnClickListener(this);
        address_tv.setOnClickListener(this);
        mBDLocation = getIntent().getParcelableExtra("address_select");
        if (mBDLocation == null) {
            address_tv.setText("click to select address");
        } else {
            Log.d("hhr",mBDLocation.getAddrStr());
            address_tv.setText(mBDLocation.getAddrStr());
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
                intent.putExtra("address_select",mBDLocation);
                startActivityForResult(intent,3);
                break;
            case R.id.address_edit_save:
                reqMap = new ArrayMap<>();
                reqMap.put("name", "stanford");
                reqMap.put("gender", 1 + "");
                reqMap.put("phone", "13851236498");
                reqMap.put("address", "beijing beijing");
                reqMap.put("longitude", "95369826 ");
                reqMap.put("latitude", "29735952");
                getPresenter().requestData(reqMap);
                startActivity(new Intent(this,AddressSelectActivity.class));
                break;
        }
    }
}
