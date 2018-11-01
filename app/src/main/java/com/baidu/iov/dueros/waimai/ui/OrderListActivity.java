package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baidu.iov.dueros.waimai.adapter.SearchHistroyAdapter;
import com.baidu.iov.dueros.waimai.adapter.OrderListAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.presenter.OrderListPresenter;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

//import com.baidu.iov.dueros.waimai.waimaiapplication.R;

public class OrderListActivity extends BaseActivity<OrderListPresenter, OrderListPresenter.OrderListUi> implements
        OrderListPresenter.OrderListUi, View.OnClickListener {


    private AppCompatTextView mTvtitle;

    private RecyclerView mRvOrder;
    private AppCompatTextView mTvHomeBtn;
    private AppCompatTextView mTvOrderBtn;
    private AppCompatImageView mIvBack;

    private OrderListAdaper mOrderListAdaper;
    private List<OrderListResponse.IovBean.DataBean> mOrderList = new
            ArrayList<>();
    private OrderListReq mOrderListReq;
    //SortPopWindow mSortPopWindow;
    private List<String> mHistorys;
    private SearchHistroyAdapter mSearchHistroyAdapter;

    @Override
    OrderListPresenter createPresenter() {
        return new OrderListPresenter();
    }

    @Override
    OrderListPresenter.OrderListUi getUi() {
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        initView();
        initData();

        getPresenter().requestOrderList(mOrderListReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().registerCmd(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().unregisterCmd(this);
    }

    private void initView() {

        mIvBack = (AppCompatImageView) findViewById(R.id.iv_back);
        mRvOrder = (RecyclerView) findViewById(R.id.rv_order);
        mTvHomeBtn = (AppCompatTextView) findViewById(R.id.tv_home);
        mTvOrderBtn = (AppCompatTextView) findViewById(R.id.tv_order);
    }

    private void initData() {
        mOrderListAdaper = new OrderListAdaper(mOrderList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvOrder.setLayoutManager(layoutManager);
        mRvOrder.setAdapter(mOrderListAdaper);

        mOrderListReq = new OrderListReq();
        mTvHomeBtn.setOnClickListener(this);
        mTvOrderBtn.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        mOrderListAdaper.setOnItemClickListener(new OrderListAdaper.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_order:
                break;

            case R.id.tv_home:
                Intent homeIntent = new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
                break;

            default:
                break;
        }

    }

    @Override
    public void update(OrderListResponse data) {
        mOrderList.clear();
        mOrderList.addAll(data.getIov().getData());
        mOrderListAdaper.notifyDataSetChanged();
    }

    @Override
    public void failure(String msg) {

    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
