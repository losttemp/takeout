package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressListAdapter;
import com.baidu.iov.dueros.waimai.adapter.StoreAdaper;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressListPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.List;

public class AddressListActivity extends BaseActivity<AddressListPresenter, AddressListPresenter.AddressListUi>
        implements AddressListPresenter.AddressListUi, View.OnClickListener, AdapterView.OnItemClickListener{
    private final static String TAG = AddressListActivity.class.getSimpleName();
    private ArrayMap<String, String> map;
    private TextView mCancel;
    private CheckBox mAdd;
    private ListView mAddressListView;
    private AddressListAdapter mAddressListAdapter;
    private List<AddressListBean.IovBean.DataBean> mDataListBean;
    public final static String ADDRESS_DATA = "address_data";


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

    public void initView() {

        mAddressListAdapter = new AddressListAdapter(this);
        mAddressListView = findViewById(R.id.list_address);
        mAddressListView.setAdapter(mAddressListAdapter);
        mAddressListView.setOnItemClickListener(this);
        mCancel = findViewById(R.id.cancel_action);
        //mAdd = findViewById(R.id.img_add);
        //mAdd.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        map = new ArrayMap<>();
        getPresenter().requestData(map);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AddressListBean.IovBean.DataBean AddressData = mDataListBean.get(position);
        Intent intent = new Intent();
        intent.putExtra(ADDRESS_DATA, AddressData);
        setResult(RESULT_OK, intent);
        Lg.getInstance().e("zhangbing","---------click--------");
        finish();

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
