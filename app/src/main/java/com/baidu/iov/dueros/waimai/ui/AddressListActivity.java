package com.baidu.iov.dueros.waimai.ui;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressListAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressListPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.List;

public class AddressListActivity extends BaseActivity<AddressListPresenter, AddressListPresenter.AddressListUi>
        implements AddressListPresenter.AddressListUi, View.OnClickListener {
    private final static String TAG = AddressListActivity.class.getSimpleName();
    private ArrayMap<String, String> map;
    private TextView mCancel;
    private CheckBox mAdd;
    private ListView mAddressListView;
    private AddressListAdapter mAddressListAdapter;
    private List<AddressListBean.IovBean.DataBean> mDataListBean;


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
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 700);
        getWindow().setGravity(Gravity.TOP);

        initView();
    }

    private void loadData(int areaId, int aoiId, int brandId, int featureId, int extraId) {
        Lg.getInstance().d(TAG, "loadData areaId:" + areaId + "aoiId:" + aoiId + " brandId:" + brandId + " featureId:" + featureId + " extraId:" + extraId);
        if (areaId != -1) {
            map.put(Constant.AREA_ID, areaId + "");
        }
        if (aoiId != -1) {
            map.put(Constant.AOI_ID, aoiId + "");
        }
        if (brandId != -1) {
            map.put(Constant.BRAND_ID, brandId + "");
        }
        if (featureId != -1) {
            map.put(Constant.FEATURE_ID, featureId + "");
        }
        if (extraId != -1) {
            map.put(Constant.EXTRA_ID, extraId + "");
        }
        getPresenter().requestData(map);
    }


    public void initView() {

        mAddressListAdapter = new AddressListAdapter(this);
        mAddressListView = findViewById(R.id.list_address);
        mAddressListView.setAdapter(mAddressListAdapter);
        mCancel = findViewById(R.id.cancel_action);
        //mAdd = findViewById(R.id.img_add);
        //mAdd.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        map = new ArrayMap<>();
        loadData(-1, -1, -1, -1, Constant.SPECIAL_HALL_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cancel_action:
                finish();
                break;

         /*   case R.id.img_add:
                break;*/

            default:
                break;
        }
    }

    @Override
    public void onSuccess(AddressListBean data) {

        if (data != null) {
            mDataListBean = data.getIov().getData();
            mAddressListAdapter.setData(mDataListBean);
        } else {
            Lg.getInstance().d(TAG, "not find data !");
        }
    }

    @Override
    public void onError(String error) {

    }
}
