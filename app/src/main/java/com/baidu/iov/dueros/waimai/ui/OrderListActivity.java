package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.OrderListAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListExtraBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.presenter.OrderListPresenter;

import java.util.ArrayList;
import java.util.List;

import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.iov.faceos.client.GsonUtil;

public class OrderListActivity extends BaseActivity<OrderListPresenter, OrderListPresenter.OrderListUi> implements
        OrderListPresenter.OrderListUi, View.OnClickListener {

    private AppCompatTextView mTvNoOrder;

    private RecyclerView mRvOrder;
    private AppCompatTextView mTvHomeBtn;
    private AppCompatTextView mTvOrderBtn;
    private AppCompatImageView mIvBack;

    private OrderListAdaper mOrderListAdaper;
    private List<OrderListResponse.IovBean.DataBean> mOrderList = new
            ArrayList<>();
    private OrderListReq mOrderListReq;
    OrderListExtraBean extraBean;
    private OrderCancelResponse.ErrorInfoBean mOrderCancel= new OrderCancelResponse.ErrorInfoBean();
    private OrderDetailsReq mOrderDetailsReq;

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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {

        mIvBack = (AppCompatImageView) findViewById(R.id.iv_back);
        mRvOrder = (RecyclerView) findViewById(R.id.rv_order);
        mTvHomeBtn = (AppCompatTextView) findViewById(R.id.tv_home);
        mTvOrderBtn = (AppCompatTextView) findViewById(R.id.tv_order);
        mTvNoOrder = (AppCompatTextView) findViewById(R.id.tv_tip_no_order);
        mTvNoOrder.setVisibility(View.GONE);
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
            public void onItemClick(View view, OrderListAdaper.ViewName viewname, int position) {
                extraBean = GsonUtil.fromJson(mOrderList.get(position).getExtra(), OrderListExtraBean.class);
                switch (view.getId()) {
                    case R.id.tv_store_name:
                    case R.id.iv_click:
                        Intent storeintent = new Intent(getApplicationContext(), FoodListActivity.class);
                        storeintent.putExtra(Constant.STORE_ID, extraBean.getPayload().getWm_ordering_list().getWm_poi_id());
                        startActivity(storeintent);
                        break;
                    case R.id.one_more_order:

                        break;
                    case R.id.cancel_order:
                        mOrderDetailsReq = new OrderDetailsReq(Long.parseLong(mOrderList.get(position).getOut_trade_no()), extraBean.getPayload().getUser_phone());
                        ConfirmDialog dialog = new ConfirmDialog.Builder(OrderListActivity.this)
                                .setTitle(R.string.order_cancel_title)
                                .setMessage(R.string.order_cancel_message)
                                .setNegativeButton(R.string.order_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getPresenter().requestOrderCancel(mOrderDetailsReq);
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(R.string.order_cancel_positive, new DialogInterface.OnClickListener() {
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
                        break;
                    default:
                        Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                        intent.putExtra(Constant.ORDER_ID, mOrderList.get(position).getOut_trade_no());
                        intent.putExtra(Constant.USER_PHONE, extraBean.getPayload().getUser_phone());
                        startActivity(intent);
                        break;
                }
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
        if (mOrderList.size() == 0) {
            mTvNoOrder.setText(R.string.no_order);
            mTvNoOrder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateOrderCancel(OrderCancelResponse data) {
        mOrderCancel = data.getErrorInfo();
        Toast.makeText(this,R.string.order_cancelled,Toast.LENGTH_LONG).show();

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
