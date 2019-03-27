package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.OrderListAdaper;
import com.baidu.iov.dueros.waimai.net.Config;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderCancelReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListExtraBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListExtraPayloadBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.presenter.OrderListPresenter;

import java.util.ArrayList;
import java.util.List;

import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.BackgroundUtils;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.DeviceUtils;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.iov.faceos.client.GsonUtil;
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

    private boolean initTTS = false;
    private boolean isNeedVoice = false;
    private int oldListSize, selectPosition;
    private View loadingView;

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
        ArrayList<String> prefix = new ArrayList<>();
        prefix.add("选择");
        AccessibilityClient.getInstance().register(this, true, prefix, null);
        if (NetUtil.getNetWorkState(this)) {
            mRefreshLayout.setEnableLoadmore(false);
            mOrderListReq.setPage(START_PAGE);
            mRefreshLayout.autoRefresh();
            networkView.setVisibility(View.GONE);
            GuidingAppear.INSTANCE.init(this, WaiMaiApplication.getInstance().getWaimaiBean().getOrder().getOrder());
        } else {
            if (null != networkView) {
                networkView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AccessibilityClient.getInstance().unregister(this);
    }

    private void initView() {
        mIvBack = (AppCompatImageView) findViewById(R.id.iv_back);
        mRvOrder = (RecyclerView) findViewById(R.id.rv_order);
        mTvNoOrder = findViewById(R.id.order_list_empty_view);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        networkView = findViewById(R.id.network_view);
        findViewById(R.id.no_internet_btn).setOnClickListener(this);
        mTvNoOrder.setVisibility(View.GONE);
        loadingView = findViewById(R.id.loading_view);
    }

    private void initData() {
        Entry.getInstance().onEvent(Constant.ORDERLIST_TO_ORDERDETAIL_VOICE, EventType.TOUCH_TYPE);
        Entry.getInstance().onEvent(Constant.ORDERLIST_REFRESH_VOICE, EventType.TOUCH_TYPE);
        mOrderListAdaper = new OrderListAdaper(mOrderList, this) {
            @Override
            public void ttsCancelOrder(int position) {
                initTTS = true;
                pos = position;
                mOrderCancelReq = new OrderCancelReq(Long.parseLong(mOrderList.get(position).getOut_trade_no()));
                getPresenter().requestOrderCancel(mOrderCancelReq);
            }
        };
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
                OrderListActivity.this.selectPosition = position;
                OrderListActivity.this.isNeedVoice = isNeedVoice;
                switch (view.getId()) {
                    case R.id.tv_store_name:
                        //进入店铺
                        Intent storeintent = new Intent(OrderListActivity.this, FoodListActivity.class);
                        storeintent.putExtra(Constant.STORE_ID, payloadBean.getWm_ordering_list().getWm_poi_id());
                        startActivity(storeintent);
                        break;
                    case R.id.one_more_order:
                        //再来一单
                        Entry.getInstance().onEvent(Constant.ORDERLIST_TO_REPEAT_VOCIE, EventType.TOUCH_TYPE);
                        Intent onemoreintent = new Intent(OrderListActivity.this, FoodListActivity.class);
                        onemoreintent.putExtra(Constant.STORE_ID, payloadBean.getWm_ordering_list().getWm_poi_id());
                        onemoreintent.putExtra(Constant.ORDER_LSIT_EXTRA_STRING, mOrderList.get(position).getExtra());
                        onemoreintent.putExtra(Constant.ONE_MORE_ORDER, true);
                        onemoreintent.putExtra("flag",true);
                        onemoreintent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
                        startActivity(onemoreintent);
                        break;
                    case R.id.pay_order:
                        //去支付
                        if (CacheUtils.getAuth()) {
                            orderSubmit(position);
                        } else {
                            getPresenter().requestAuthInfo();
                        }
                        break;
                    case R.id.cancel_order:
                        //取消订单
                        pos = position;
                        mOrderCancelReq = new OrderCancelReq(Long.parseLong(mOrderList.get(position).getOut_trade_no()));
                        String message;
                        int trade_status = -99;
                        try {
                            trade_status = Integer.parseInt(mOrderList.get(position).getOut_trade_status());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (trade_status >= 2) {
                            message = getResources().getString(R.string.order_cancel_message);
                        } else {
                            message = "";
                        }
                        ConfirmDialog dialog = new ConfirmDialog.Builder(OrderListActivity.this)
                                .setTitle(R.string.order_cancel_title)
                                .setMessage(message)
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
                        //订单详情
                        Entry.getInstance().onEvent(Constant.ORDERLIST_TO_ORDERDETAIL_VOICE, EventType.TOUCH_TYPE);
                        Entry.getInstance().onEvent(Constant.ORDERLIST_TO_ORDERDETAIL, EventType.TOUCH_TYPE);
                        Intent intent = new Intent(OrderListActivity.this, OrderDetailsActivity.class);
                        intent.putExtra(Constant.ORDER_ID, Long.parseLong(mOrderList.get(position).getOut_trade_no()));
                        intent.putExtra(Constant.STORE_ID, Long.parseLong(payloadBean.getWm_ordering_list().getWm_poi_id()));
                        intent.putExtra(Constant.EXPECTED_TIME, payloadBean.getWm_ordering_list().getDelivery_time());
                        String status = mOrderList.get(position).getOut_trade_status();
                        if (IOV_STATUS_ZERO.equals(status) || IOV_STATUS_WAITING.equals(status)) {
                            intent.putExtra("pay_url", extraBean.getOrderInfos().getPay_url());
                            intent.putExtra("pic_url", extraBean.getOrderInfos().getWm_pic_url());
                        }
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void orderSubmit(int position) {
        loadingView.setVisibility(View.VISIBLE);
        OrderDetailsReq mOrderDetailsReq = new OrderDetailsReq();
        mOrderDetailsReq.setId(Long.parseLong(mOrderList.get(position).getOut_trade_no()));
        getPresenter().requestOrderDetails(mOrderDetailsReq);
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
        String mMessage = getResources().getString(R.string.remind_message);
        SpannableString spanColor = new SpannableString(mMessage);
        spanColor.setSpan(new ForegroundColorSpan(Color.parseColor("#10CBE5")), 5, 12, 0);
        ConfirmDialog dialog1 = new ConfirmDialog.Builder(this)
                .setTitle(R.string.order_cancel)
                .setSpannableMessage(spanColor)
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
                                } else {
                                    ActivityCompat.requestPermissions(OrderListActivity.this,
                                            new String[]{Manifest.permission.CALL_PHONE},
                                            REQUEST_CODE_CALL_PHONE);
                                }
                            } else {
                                if (!DeviceUtils.checkBluetooth(OrderListActivity.this)) return;
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
                Entry.getInstance().onEvent(Constant.GOBACK_TO_PREACTIVITY, EventType.TOUCH_TYPE);
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
        }
        if (mOrderList.size() == 0) {
            mTvNoOrder.setVisibility(View.VISIBLE);
            mRvOrder.setVisibility(View.GONE);
        }
        if (mTvNoOrder.getVisibility() != View.VISIBLE && initTTS && oldListSize == mOrderList.size()) {
            sendTTS(R.string.last_page);
        } else {
            initTTS = false;
        }
    }

    @Override
    public void updateOrderCancel(OrderCancelResponse data) {
        if (data.getMeituan().getCode() == 0) {
            sendTTS(R.string.close_order_success_text);
            ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_cancelled), Toast.LENGTH_LONG);
            mOrderList.get(pos).setOut_trade_status(IOV_STATUS_CANCELED);
            mOrderListAdaper.notifyItemChanged(pos);
        } else {
            showCancelDialog();
        }
    }

    private void sendTTS(int stringId) {
        if (initTTS) {
            initTTS = false;
            StandardCmdClient.getInstance().playTTS(mContext, getString(stringId));
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
    public void authFailure(String msg) {
        ToastUtils.show(this,"授权失败，请开启服务授权",Toast.LENGTH_SHORT);
    }

    @Override
    public void authSuccess(String msg) {
        boolean isBackground = BackgroundUtils.isBackground(getBaseContext());
        if (!isBackground){
            orderSubmit(pos);
        }
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void selectListItem(int i) {
        if (i > 0) {
            i = i - 1;
        }
        if (mOrderList == null || mOrderList.size() == 0) {
            StandardCmdClient.getInstance().playTTS(OrderListActivity.this, getString(R.string.have_no_order));
            return;
        }
        if (mOrderList.size() > i) {
            try {
                OrderListResponse.IovBean.DataBean order = mOrderList.get(i);
                String extra = order.getExtra();
                OrderListExtraBean extraBean = GsonUtil.fromJson(extra, OrderListExtraBean.class);
                String payload = Encryption.desEncrypt(extraBean.getPayload());
                OrderListExtraPayloadBean payloadBean = GsonUtil.fromJson(payload, OrderListExtraPayloadBean.class);
                Intent onemoreintent = new Intent(OrderListActivity.this, FoodListActivity.class);
                onemoreintent.putExtra(Constant.STORE_ID, payloadBean.getWm_ordering_list().getWm_poi_id());
                onemoreintent.putExtra(Constant.ORDER_LSIT_EXTRA_STRING, mOrderList.get(i).getExtra());
                onemoreintent.putExtra(Constant.ONE_MORE_ORDER, true);
                onemoreintent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, true);
                startActivity(onemoreintent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void nextPage(boolean isNextPage) {
        if (mTvNoOrder.getVisibility() == View.GONE) {
            LinearLayoutManager manager = (LinearLayoutManager) mRvOrder.getLayoutManager();
            assert manager != null;
            int currentItemPosition = manager.findFirstVisibleItemPosition();
            if (isNextPage) {
                StandardCmdClient.getInstance().playTTS(mContext, Config.DEFAULT_TTS);
                if (currentItemPosition + getPageNum() * 2 > mOrderList.size()) {
                    manager.scrollToPositionWithOffset(mOrderList.size() - 1, 0);
                    mRefreshLayout.autoLoadmore(1000);
                    oldListSize = mOrderList.size();
                    initTTS = true;
                    return;
                }
                manager.scrollToPositionWithOffset(currentItemPosition + getPageNum(), 0);
            } else {
                if (currentItemPosition == 0) {
                    StandardCmdClient.getInstance().playTTS(mContext, getString(R.string.first_page));
                } else {
                    StandardCmdClient.getInstance().playTTS(mContext, Config.DEFAULT_TTS);
                }
                manager.scrollToPositionWithOffset(currentItemPosition - getPageNum() > 0 ? currentItemPosition - getPageNum() : 0, 0);
            }
        }
    }

    @Override
    public void orderDetailsSuccess(OrderDetailsResponse data) {
        loadingView.setVisibility(View.GONE);
        OrderDetailsResponse.MeituanBean.DataBean mOrderDetails = data.getMeituan().getData();
        int status = mOrderDetails.getOut_trade_status();
        if (status == 0 || status == 1) {
            try {
                OrderListResponse.IovBean.DataBean order = mOrderList.get(selectPosition);
                String extra = order.getExtra();
                OrderListExtraBean extraBean = GsonUtil.fromJson(extra, OrderListExtraBean.class);
                String payload = Encryption.desEncrypt(extraBean.getPayload());
                OrderListExtraPayloadBean payloadBean = GsonUtil.fromJson(payload, OrderListExtraPayloadBean.class);
                Entry.getInstance().onEvent(Constant.ORDERSUBMIT_TOPAY, EventType.TOUCH_TYPE);
                Intent payintent = new Intent(OrderListActivity.this, PaymentActivity.class);
                double total_price = ((double) extraBean.getOrderInfos().getGoods_total_price()) / 100;
                payintent.putExtra("total_cost", total_price);
                payintent.putExtra("order_id", Long.parseLong(mOrderList.get(selectPosition).getOut_trade_no()));
                payintent.putExtra(Constant.STORE_ID, payloadBean.getWm_ordering_list().getWm_poi_id());
                payintent.putExtra("shop_name", mOrderList.get(selectPosition).getOrder_name());
                payintent.putExtra("pay_url", extraBean.getOrderInfos().getPay_url());
                payintent.putExtra("pic_url", extraBean.getOrderInfos().getWm_pic_url());
                payintent.putExtra("flag", true);
                payintent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
                payintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(payintent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.show(mContext, "订单已过期", Toast.LENGTH_SHORT);
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void orderDetailsFailure(String msg) {
        loadingView.setVisibility(View.GONE);
        ToastUtils.show(mContext, "订单已过期", Toast.LENGTH_SHORT);
        mRefreshLayout.autoRefresh();
    }

    private int getPageNum() {
        return 5;
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
                if (!DeviceUtils.checkBluetooth(OrderListActivity.this)) return;
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
