package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
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

import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class OrderListActivity extends BaseActivity<OrderListPresenter, OrderListPresenter.OrderListUi> implements
        OrderListPresenter.OrderListUi, View.OnClickListener {
    private final String IOV_STATUS_ZERO = "0"; //待支付
    private final String IOV_STATUS_WAITING = "1"; //待支付
    private View mTvNoOrder;

    private RecyclerView mRvOrder;
    private AppCompatImageView mIvBack;

    private OrderListAdaper mOrderListAdaper;
    private List<OrderListResponse.IovBean.DataBean> mOrderList = new
            ArrayList<>();
    private OrderListReq mOrderListReq;
    private OrderCancelReq mOrderCancelReq;
    private int pos;
    private final String IOV_STATUS_CANCELED = "8";
    private static final int REQUEST_CODE_CALL_PHONE = 500;
    private static final int EVERY_TIME_PULL_COUNT = 20;
    private static final int START_PAGE = 0;

    private SmartRefreshLayout mRefreshLayout;
    private View networkView;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtil.getNetWorkState(this)) {
            mRefreshLayout.setEnableLoadmore(false);
            mOrderListReq.setPage(START_PAGE);
            mRefreshLayout.autoRefresh();
            networkView.setVisibility(View.GONE);
        } else {
            if (null!=networkView){
                networkView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        mIvBack = (AppCompatImageView) findViewById(R.id.iv_back);
        mRvOrder = (RecyclerView) findViewById(R.id.rv_order);
        mTvNoOrder = findViewById(R.id.order_list_empty_view);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        networkView = findViewById(R.id.network_view);
        findViewById(R.id.no_internet_btn).setOnClickListener(this);
        mTvNoOrder.setVisibility(View.GONE);
    }

    private void initData() {
        mOrderListAdaper = new OrderListAdaper(mOrderList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvOrder.setLayoutManager(layoutManager);
        mRvOrder.setAdapter(mOrderListAdaper);

        mOrderListReq = new OrderListReq();
        mOrderListReq.setPage_num(EVERY_TIME_PULL_COUNT);
        mOrderListReq.setPage(START_PAGE);
        setRefreshView();
        mIvBack.setOnClickListener(this);

        mOrderListAdaper.setOnItemClickListener(new OrderListAdaper.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, OrderListExtraBean extraBean, OrderListExtraPayloadBean payloadBean, boolean isNeedVoice) {
                switch (view.getId()) {
                    case R.id.tv_store_name:
                    case R.id.iv_click:
                        Intent storeintent = new Intent(OrderListActivity.this, FoodListActivity.class);
                        storeintent.putExtra(Constant.STORE_ID, payloadBean.getWm_ordering_list().getWm_poi_id());
                        startActivity(storeintent);
                        break;
                    case R.id.one_more_order:
                        Entry.getInstance().onEvent(31300095,EventType.TOUCH_TYPE);
                        Intent onemoreintent = new Intent(OrderListActivity.this, FoodListActivity.class);
                        onemoreintent.putExtra(Constant.STORE_ID, payloadBean.getWm_ordering_list().getWm_poi_id());
                        onemoreintent.putExtra(Constant.ORDER_LSIT_EXTRA_STRING, mOrderList.get(position).getExtra());
                        onemoreintent.putExtra(Constant.ONE_MORE_ORDER, true);
                        onemoreintent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
//                        onemoreintent.putExtra("store_id","494377518250692");
//                        onemoreintent.putExtra("order_list_extra_string","{\"payload\":\"0dWBbLaL+4OYCKxoAKuzG2ZrnaIH+33+5XHIFShz680aQk+nQY6+1Gmzl8hVSB3kI2oGgQjGK6YBGTR2jlQQ2aT16EHBrgs4O2h0t4gNR6oM2mDngfAZ2ycJRqBqwUJgHEPVYRYK+Eug8Q0u8LXij5HffWFxzsUlFQ1Na70VPajq3XfryZdnzmRZtyH8AN4OfetnShd0GOk5i8DEp39jvMISigQeyNRPPJ6whqqkON0WxZzRI59IoK9lXx2fWxk7heSkclkq0XgkS6AgdxPTaHW2Evlz7Vb2CyaU6+u0QukNEQJLw1MK\\/5L2GrjzQCPqa6oiZcRguaL5\\/0akjb8ASOR8w51WVXLaC7Y3Fbhf\\/3SPgyRJeBFPIhjRfGc9NC9mOVCilIu5AqZ0Kc6drA7mEd0CSSVIeTHHsRwTUZvEJE3HBQcmGg\\/vBxGKIFIjsxHYfTUoDOjaW8ePwkFjepKWKv1QWjRAo+5J6H+2\\/sjdkcghbcE4KHSVF5inZzJtvzHG\",\"orderInfos\":{\"goods_name\":\"\\u9999\\u7f8e\\u5473\\u86cb\\u7cd5\",\"goods_total_price\":20300,\"transport_price\":3500,\"food_list\":[{\"food_id\":1187542485,\"spu_id\":1077555922,\"name\":\"\\u6d6a\\u6f2b\\u4e03\\u5915\\u3010\\u516b\\u5bf8\\u3011\",\"price\":198,\"original_price\":198,\"count\":1,\"spec\":\"\",\"box_num\":1,\"box_price\":0,\"attrIds\":[],\"attrValues\":[]}],\"wm_pic_url\":\"http:\\/\\/p0.meituan.net\\/waimaipoi\\/9d8e1d1895c8dee3c549a01be9246ce352921.jpg\",\"pay_url\":\"https:\\/\\/i.waimai.meituan.com\\/openh5\\/logincheck?back_url=https%3A%2F%2Fmpay.meituan.com%2Fi%2Fcashier%2Fshow%2Findex%3Fauth%3Dv2%26pay_token%3D5e10b939ea99aa805fc418ef4069008a%26tradeno%3D1812198622953284396&pay_success_url=https%3A%2F%2Fwww.meituan.com&redr_url=https%3A%2F%2Fwww.meituan.com\",\"recipient_address\":\"3l+G3MtJliAWpeZAkxL2BoffDmMe2YLyIbVDh0nJgzY=\",\"recipient_name\":\"wnZVyGCp\\/XBhacSeBDz1CA==\",\"recipient_phone\":\"lC3TTgfK0oM5g78Tn0Bm5w==\",\"original_price\":233,\"shipping_fee\":35,\"box_total_price\":0,\"night_shipping_fee\":0,\"total\":203,\"pay_method\":\"\\u5728\\u7ebf\\u652f\\u4ed8\",\"delivery_method\":\"\\u7f8e\\u56e2\\u914d\\u9001\",\"estimate_arrival_time\":1545222265,\"wm_poi_id\":494377518250692},\"logistics_status\":0}");
//                        onemoreintent.putExtra("one_more_order",true);
//                        onemoreintent.putExtra("isNeedVoiceFeedback",false);
                        startActivity(onemoreintent);
                        break;
                    case R.id.pay_order:
                        Entry.getInstance().onEvent(31300081,EventType.TOUCH_TYPE);
                        Intent payintent = new Intent(OrderListActivity.this, PaymentActivity.class);
                        double total_price = ((double) extraBean.getOrderInfos().getGoods_total_price()) / 100;
                        payintent.putExtra("total_cost", total_price);
                        payintent.putExtra("order_id", Long.parseLong(mOrderList.get(position).getOut_trade_no()));
                        payintent.putExtra(Constant.STORE_ID,payloadBean.getWm_ordering_list().getWm_poi_id());
                        payintent.putExtra("shop_name", mOrderList.get(position).getOrder_name());
                        payintent.putExtra("pay_url", extraBean.getOrderInfos().getPay_url());
                        payintent.putExtra("pic_url", extraBean.getOrderInfos().getWm_pic_url());
                        payintent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
                        payintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(payintent);
                        break;
                    case R.id.cancel_order:
                        pos = position;
                        Entry.getInstance().onEvent(31300081,EventType.TOUCH_TYPE);
                        mOrderCancelReq = new OrderCancelReq(Long.parseLong(mOrderList.get(position).getOut_trade_no()));
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
                        Entry.getInstance().onEvent(31300097,EventType.TOUCH_TYPE);
                        Intent intent = new Intent(OrderListActivity.this, OrderDetailsActivity.class);
                        intent.putExtra(Constant.ORDER_ID, Long.parseLong(mOrderList.get(position).getOut_trade_no()));
                        intent.putExtra(Constant.STORE_ID,Long.parseLong(payloadBean.getWm_ordering_list().getWm_poi_id()));
                        intent.putExtra(Constant.EXPECTED_TIME, payloadBean.getWm_ordering_list().getDelivery_time());
                        String status=  mOrderList.get(position).getOut_trade_status();
                        if (IOV_STATUS_ZERO.equals(status) || IOV_STATUS_WAITING.equals(status)){
                            intent.putExtra("pay_url", extraBean.getOrderInfos().getPay_url());
                            intent.putExtra("pic_url", extraBean.getOrderInfos().getWm_pic_url());
                        }
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void setRefreshView() {

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mRefreshLayout.setEnableLoadmore(false);
                mOrderListReq.setPage(START_PAGE);
                getPresenter().requestOrderList(mOrderListReq);
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                mRefreshLayout.setEnableRefresh(false);
                mOrderListReq.setPage(mOrderListReq.getPage() + 1);
                getPresenter().requestOrderList(mOrderListReq);
            }
        });

    }

    private void showCancelDialog() {
        ConfirmDialog dialog1 = new ConfirmDialog.Builder(this)
                .setTitle(R.string.remind_title)
                .setMessage(R.string.remind_message)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.remind_phone, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(OrderListActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(OrderListActivity.this,
                                        Manifest.permission.CALL_PHONE)) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }else{
                                    ActivityCompat.requestPermissions(OrderListActivity.this,
                                            new String[]{Manifest.permission.CALL_PHONE},
                                            REQUEST_CODE_CALL_PHONE);
                                }
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                Uri data = Uri.parse("tel:" + "10109777");
                                intent.setData(data);
                                startActivity(intent);
                            }
                        }
                        dialog.dismiss();
                    }
                })
                .setCloseButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog1.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Entry.getInstance().onEvent(31300102,EventType.TOUCH_TYPE);
                onBackPressed();
                break;
            case R.id.no_internet_btn:
                if (NetUtil.getNetWorkState(this)) {
                    mRefreshLayout.setEnableLoadmore(false);
                    mOrderListReq.setPage(START_PAGE);
                    mRefreshLayout.autoRefresh();
                    networkView.setVisibility(View.GONE);
                } else {
                    ToastUtils.show(this, getResources().getString(R.string.is_network_connected), Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void update(OrderListResponse data) {

        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableRefresh(true);

        if (mRefreshLayout.isRefreshing()) {
            mOrderList.clear();
            mRefreshLayout.finishRefresh();
        }
        if (mRefreshLayout.isLoading()) {
            mRefreshLayout.finishLoadmore();
        }


        if (null != data.getIov() && null != data.getIov().getData() && data.getIov().getData().size() != 0) {
            mOrderList.addAll(data.getIov().getData());
            mOrderListAdaper.notifyDataSetChanged();
        } else {
            mRefreshLayout.setEnableLoadmore(false);
        }
        if (mOrderList.size() == 0) {
            mTvNoOrder.setVisibility(View.VISIBLE);
            mRvOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateOrderCancel(OrderCancelResponse data) {
        if (data.getMeituan().getCode() == 0) {
            ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_cancelled),Toast.LENGTH_LONG);
            mOrderList.get(pos).setOut_trade_status(IOV_STATUS_CANCELED);
            mOrderListAdaper.notifyItemChanged(pos);
        } else {
            showCancelDialog();
        }
    }

    @Override
    public void failure(String msg) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh(false);
        }
        if (mRefreshLayout.isLoading()) {
            mRefreshLayout.finishLoadmore(1000, false);
        }
    }

    @Override
    public void orderCancelfail(String msg) {
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void selectListItem(int i) {
        if (mOrderList == null || mOrderList.size() == 0) {
            VoiceManager.getInstance().playTTS(OrderListActivity.this, getString(R.string.have_no_order));
        }
        LinearLayoutManager manager = (LinearLayoutManager) mRvOrder.getLayoutManager();
        assert manager != null;
        int firstItemPosition = manager.findFirstVisibleItemPosition();
        int lastItemPosition = manager.findLastVisibleItemPosition();

        if (firstItemPosition <= i && lastItemPosition >= i) {
            View view = mRvOrder.getChildAt(i - firstItemPosition);
            if (null != mRvOrder.getChildViewHolder(view)) {
                OrderListAdaper.ViewHolder viewHolder = (OrderListAdaper.ViewHolder) mRvOrder.getChildViewHolder(view);
                viewHolder.autoClick();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission was granted, yay! Do the contacts-related task you need to do.
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "10109777");
                intent.setData(data);
                startActivity(intent);
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
                finish();
            }
        }
    }
}
