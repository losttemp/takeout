package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.OrderListAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListExtraBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListExtraPayloadBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.presenter.OrderListPresenter;

import java.util.ArrayList;
import java.util.List;

import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;

public class OrderListActivity extends BaseActivity<OrderListPresenter, OrderListPresenter.OrderListUi> implements
        OrderListPresenter.OrderListUi, View.OnClickListener {

    private AppCompatTextView mTvNoOrder;

    private RecyclerView mRvOrder;
    private AppCompatImageView mIvBack;

    private OrderListAdaper mOrderListAdaper;
    private List<OrderListResponse.IovBean.DataBean> mOrderList = new
            ArrayList<>();
    private OrderListReq mOrderListReq;
    OrderListExtraBean extraBean;
    private OrderCancelResponse.ErrorInfoBean mOrderCancel = new OrderCancelResponse.ErrorInfoBean();
    private OrderCancelReq mOrderCancelReq;
    private final String STATUS_PAY_EXPIRED = "5";

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
        mIvBack.setOnClickListener(this);

        mOrderListAdaper.setOnItemClickListener(new OrderListAdaper.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, OrderListExtraBean extraBean, OrderListExtraPayloadBean payloadBean) {
                switch (view.getId()) {
                    case R.id.tv_store_name:
                    case R.id.iv_click:
                        Intent storeintent = new Intent(OrderListActivity.this, FoodListActivity.class);
                        storeintent.putExtra(Constant.STORE_ID, payloadBean.getWm_ordering_list().getWm_poi_id());
                        startActivity(storeintent);
                        break;
                    case R.id.one_more_order:
                        Intent onemoreintent = new Intent(OrderListActivity.this, FoodListActivity.class);
                        onemoreintent.putExtra(Constant.STORE_ID, payloadBean.getWm_ordering_list().getWm_poi_id());
                        onemoreintent.putExtra(Constant.ORDER_LSIT_EXTRA_STRING, mOrderList.get(position).getExtra());
                        onemoreintent.putExtra(Constant.ONE_MORE_ORDER, true);
                        startActivity(onemoreintent);
                        break;
                    case R.id.pay_order:
                        Intent payintent = new Intent(OrderListActivity.this, PaymentActivity.class);
                        double total_price = 0;
                        for (int i = 0; i < extraBean.getOrderInfos().getFood_list().size(); i++) {
                            total_price = total_price + extraBean.getOrderInfos().getFood_list().get(i).getPrice() * extraBean.getOrderInfos().getFood_list().get(i).getCount();
                        }
                        payintent.putExtra("total_cost", total_price);
                        payintent.putExtra("order_id", Long.parseLong(mOrderList.get(position).getOut_trade_no()));
                        payintent.putExtra("shop_name", mOrderList.get(position).getOrder_name());
                        payintent.putExtra("pay_url", extraBean.getOrderInfos().getPay_url());
                        payintent.putExtra("pic_url", extraBean.getOrderInfos().getWm_pic_url());
                        payintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(payintent);
                        break;
                    case R.id.cancel_order:
                        mOrderCancelReq = new OrderCancelReq(Long.parseLong(mOrderList.get(position).getOut_trade_no()), payloadBean.getUser_phone());
                        ConfirmDialog dialog = new ConfirmDialog.Builder(OrderListActivity.this)
                                .setTitle(R.string.order_cancel_title)
                                .setMessage(R.string.order_cancel_message)
                                .setNegativeButton(R.string.order_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getPresenter().requestOrderCancel(mOrderCancelReq);
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
                        if (mOrderList.get(position).getOrder_status().equals(STATUS_PAY_EXPIRED)) {
                            Toast.makeText(OrderListActivity.this, R.string.order_expired, Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(OrderListActivity.this, OrderDetailsActivity.class);
                            intent.putExtra(Constant.ORDER_ID, Long.parseLong(mOrderList.get(position).getOut_trade_no()));
                            intent.putExtra("expected_time", payloadBean.getWm_ordering_list().getDelivery_time());
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
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
            mRvOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateOrderCancel(OrderCancelResponse data) {
        mOrderCancel = data.getErrorInfo();
        Toast.makeText(this, R.string.order_cancelled, Toast.LENGTH_LONG).show();

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
