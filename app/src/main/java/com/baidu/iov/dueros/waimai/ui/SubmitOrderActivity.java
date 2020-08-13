package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.BuildConfig;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.DeliveryDateAdapter;
import com.baidu.iov.dueros.waimai.adapter.DeliveryTimeAdapter;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.presenter.SubmitInfoPresenter;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.BackgroundUtils;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.CompareDate;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.GlideApp;
import com.baidu.iov.dueros.waimai.utils.GsonUtil;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;

import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.baidu.iov.dueros.waimai.ui.AddressListActivity.ADDRESS_DATA;
import static com.baidu.iov.dueros.waimai.ui.FoodListActivity.POI_INFO;
import static com.baidu.iov.dueros.waimai.ui.FoodListActivity.PRODUCT_LIST_BEAN;

public class SubmitOrderActivity extends BaseActivity<SubmitInfoPresenter, SubmitInfoPresenter.SubmitInfoUi>
        implements SubmitInfoPresenter.SubmitInfoUi, View.OnClickListener {

    private static final String TAG = SubmitOrderActivity.class.getSimpleName();

    public static final int CAN_SHIPPING = 1;
    public static final int CANNOT_SHIPPING = 2;
    public static final int STOP_SHIPPING = 3;

    private RelativeLayout mArrivetimeLayout;
    private RelativeLayout mAddressUpdateLayout;
    private LinearLayout mDiscountsLayout;
    private LinearLayout mProductInfoListview;
    private TextView mPackingFee;
    private TextView mShippingFeeTv;
    private Button mToPayTv;
    private TextView mDiscountTv;
    private TextView mShopNameTv;
    private TextView mArriveTimeTv;
    private TextView mTypeTipTv;
    private TextView mDeliveryTypeTv;
    private TextView mAddressTv;
    private TextView mUserNameTv, mUserPhoneTv;
    private TextView mTotalTv;
    private TextView mDiscountWarnTipTv;
    private RelativeLayout mWarnTipParent;
    private ImageView mBackImg;

    private List<ArriveTimeBean.MeituanBean.DataBean> mDataBean;
    private AddressListBean.IovBean.DataBean mAddressData;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> mProductList;
    private PoifoodListBean.MeituanBean.DataBean.PoiInfoBean mPoiInfo;
    private OrderPreviewBean.MeituanBean.DataBean mOrderPreviewData;
    private OrderSubmitBean.MeituanBean.DataBean mOrderSubmitData;


    private ListView mListViewDate;
    private ListView mListViewTime;
    private DeliveryDateAdapter mDateAdapter;
    private DeliveryTimeAdapter mTimeAdapter;
    private int mCurTimeItem = 0;
    private int mCurDateItem = 0;
    private int mPreDateItem = 0;
    private NumberFormat mNumberFormat;
    private String mEstimateTime;
    private int mUnixtime = 0;
    private boolean isChoiceAddressBack;
    private View loadingView;
    private RelativeLayout mParentsLayout;
    private LinearLayout mNoNet;
    private Button mNoInternetButton;
    private String date_type_tip, date_time, view_time;
    boolean clicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);

        mNumberFormat = new DecimalFormat("##.##");
        SharedPreferences sharedPreferences = getSharedPreferences("_cache", MODE_PRIVATE);
        String addressDataJson = sharedPreferences.getString(Constant.ADDRESS_DATA, null);
        if (addressDataJson != null) {
            mAddressData = GsonUtil.fromJson(addressDataJson, AddressListBean.IovBean.DataBean.class);
        }

        Intent intent = getIntent();
        if (intent != null) {
            mProductList = (List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean>) intent.getSerializableExtra(PRODUCT_LIST_BEAN);
            mPoiInfo = (PoifoodListBean.MeituanBean.DataBean.PoiInfoBean) intent.getSerializableExtra(POI_INFO);
        }
//        if(mPoiInfo!=null){
//            getPresenter().requestArriveTimeData(mPoiInfo.getWm_poi_id());
//            if (mProductList!=null){
//                getPresenter().requestOrderPreview(mProductList, mPoiInfo, mUnixtime, mAddressData, SubmitOrderActivity.this);
//            }
//        }
        initView();
    }

    public void initView() {

        mAddressTv = findViewById(R.id.tv_address);
        mUserNameTv = findViewById(R.id.tv_name);
        mUserPhoneTv = findViewById(R.id.tv_phone);
        mToPayTv = findViewById(R.id.to_pay);
        mToPayTv.setOnClickListener(this);
        mTypeTipTv = findViewById(R.id.type_tip);
        mArriveTimeTv = findViewById(R.id.arrive_time);
        mAddressUpdateLayout = findViewById(R.id.address_info);
        mArrivetimeLayout = findViewById(R.id.delivery_info);
        mProductInfoListview = findViewById(R.id.product_listview);
        mShopNameTv = findViewById(R.id.store_name);
        mDeliveryTypeTv = findViewById(R.id.delivery_type);
        mShippingFeeTv = findViewById(R.id.shipping_fee);
        mPackingFee = findViewById(R.id.packing_fee);
        mDiscountTv = findViewById(R.id.discount_exists);
        mTotalTv = findViewById(R.id.total);
        mDiscountWarnTipTv = findViewById(R.id.discount_WarnTip);
        mWarnTipParent = findViewById(R.id.warntip_parent);
        mDiscountsLayout = findViewById(R.id.discounts_layout);
        mBackImg = findViewById(R.id.back_action);
        loadingView = findViewById(R.id.submit_order_loading);
        mParentsLayout = findViewById(R.id.order_submit_rl);
        mNoNet = findViewById(R.id.no_net);
        mNoInternetButton = (Button) findViewById(R.id.no_internet_btn);
        mBackImg.setOnClickListener(this);
        mArrivetimeLayout.setOnClickListener(this);
        mAddressUpdateLayout.setOnClickListener(this);
        mNoInternetButton.setOnClickListener(this);


        if (mProductList != null && mPoiInfo != null) {

            String shopName = mPoiInfo.getName();
            mShopNameTv.setText(shopName);
            String deliveryType = mPoiInfo.getDelivery_type() == 1 ? getString(R.string.delivery_type1_text)
                    : getString(R.string.delivery_type2_text);
            if (mPoiInfo.getDelivery_type() == 1) {
                mDeliveryTypeTv.setText(deliveryType);
            } else {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mDeliveryTypeTv.getLayoutParams();
                lp.width = 142;
                mDeliveryTypeTv.setLayoutParams(lp);
                mDeliveryTypeTv.setText(deliveryType);
            }
        }
        if (mAddressData != null) {
            try {
                mAddressTv.setText(Encryption.desEncrypt(mAddressData.getAddress()));
                String name = "";
                String phone = "";
                if (!TextUtils.isEmpty(mAddressData.getUser_name())) {
                    name = Encryption.desEncrypt(mAddressData.getUser_name());
                }
                if (!TextUtils.isEmpty(mAddressData.getUser_phone())) {
                    phone = Encryption.desEncrypt(mAddressData.getUser_phone());
                }
                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(phone)) {
                    mUserNameTv.setText(name);
                    mUserPhoneTv.setText(phone);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            mAddressTv.setText(getApplicationContext().getResources().getString(R.string.please_select_address_again));
        }


        mToPayTv.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        intentToPay();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        netDataReque();
    }

    private void intentToPay() {
        if (mAddressData == null) {
            ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT);
        } else if (mAddressData.getCanShipping() != 1) {
            ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_submit_msg8), Toast.LENGTH_SHORT);
        } else {
            if (CacheUtils.getAuth()) {
                orderSubmit();
            } else {
                getPresenter().requestAuthInfo();
            }
        }
    }

    private void netDataReque() {
        if (NetUtil.getNetWorkState(this)) {
            getPresenter().requestArriveTimeData(mPoiInfo.getWm_poi_id());
            getPresenter().requestOrderPreview(mProductList, mPoiInfo, mUnixtime, mAddressData, SubmitOrderActivity.this);
        } else {
            mNoNet.setVisibility(View.VISIBLE);
            mParentsLayout.setVisibility(View.GONE);
        }
    }


    private void playVoice() {

        if (!isChoiceAddressBack && mOrderPreviewData.getWm_ordering_preview_detail_vo_list().size() > 0) {
            String oneFood = mOrderPreviewData.getWm_ordering_preview_detail_vo_list().get(0).getFood_name();
            String allPrice = String.format(getResources().getString(R.string.submit_total), mOrderPreviewData.getWm_ordering_preview_order_vo().getTotal());
            String deliveryTime = mEstimateTime;
            String address = "";
            String phone = "";
            try {
                address = Encryption.desEncrypt(mAddressData.getAddress());
                phone = Encryption.desEncrypt(mAddressData.getUser_phone());
                if (phone.length() > 3) phone = phone.substring(phone.length() - 4, phone.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mAddressData.getCanShipping() != 1) {
                ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_submit_msg8), Toast.LENGTH_SHORT);
            }
            String str = String.format(getString(R.string.submit_order), oneFood, allPrice, deliveryTime, address, phone);
        }
    }

    public void showAllProductItem(List<OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean> wmOrderingPreviewDetailVoListBeanList) {

        mProductInfoListview.removeAllViews();
        for (OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean wmOrderingPreviewDetailVoListBean : wmOrderingPreviewDetailVoListBeanList) {

            LayoutInflater inflater = this.getLayoutInflater();
            final RelativeLayout viewItem = (RelativeLayout) inflater.inflate(R.layout.product_info_item, null);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            ImageView img_photo = viewItem.findViewById(R.id.product_photo);
            TextView tv_name = viewItem.findViewById(R.id.product_name);
            TextView tv_attrs = viewItem.findViewById(R.id.product_attrs);
            TextView tv_count = viewItem.findViewById(R.id.product_count);
            TextView tv_price = viewItem.findViewById(R.id.product_price);
            TextView tv_origin_price = viewItem.findViewById(R.id.origin_price);
//            TextView tv_discounts = viewItem.findViewById(R.id.product_discount);

            String pictureUrl = wmOrderingPreviewDetailVoListBean.getPicture();
            String name = wmOrderingPreviewDetailVoListBean.getFood_name();

            if (wmOrderingPreviewDetailVoListBean.getWm_ordering_preview_food_spu_attr_list().size() > 0) {
                for (OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean.WmOrderingPreviewFoodSpuAttrListBean wmOrderingPreviewFoodSpuAttrListBean : wmOrderingPreviewDetailVoListBean.getWm_ordering_preview_food_spu_attr_list()) {
                    String value = wmOrderingPreviewFoodSpuAttrListBean.getValue();
                    StringBuilder attrs = new StringBuilder();
                    attrs.append(value + " ");

                    tv_attrs.setText(attrs.toString());
                }
                tv_attrs.setVisibility(View.VISIBLE);
            } else {
                tv_attrs.setVisibility(View.INVISIBLE);
            }


            NumberFormat nf = new DecimalFormat("##.##");
            int count = wmOrderingPreviewDetailVoListBean.getCount();
            double price = wmOrderingPreviewDetailVoListBean.getFood_price();
            double origin_price = wmOrderingPreviewDetailVoListBean.getOrigin_food_price();
            double total_pricie = price * count;
            if (price < origin_price) {
                tv_origin_price.setText(String.format(getResources().getString(R.string.cost_text), nf.format(origin_price)));
                tv_origin_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_origin_price.getPaint().setAntiAlias(true); //去掉锯齿
                tv_origin_price.setVisibility(View.VISIBLE);
//                tv_discounts.setVisibility(View.VISIBLE);
            } else {
                tv_origin_price.setVisibility(View.INVISIBLE);
//                tv_discounts.setVisibility(View.INVISIBLE);
            }
            GlideApp.with(mContext).load(pictureUrl).placeholder(R.drawable.default_goods_icon).skipMemoryCache(true).into(img_photo);
            tv_name.setText(name);
            tv_count.setText(String.format(getResources().getString(R.string.count_char), count));
            tv_price.setText(String.format(getResources().getString(R.string.cost_text), nf.format(total_pricie)));

            mProductInfoListview.addView(viewItem, params);

            String total = String.format(getResources().getString(R.string.submit_total), mOrderPreviewData.getWm_ordering_preview_order_vo().getTotal());
            String deliveryTime = mEstimateTime;
            String address = "";
            String phone = "";
            try {
                address = Encryption.desEncrypt(mAddressData.getAddress());
                phone = Encryption.desEncrypt(mAddressData.getUser_phone());
                if (phone.length() > 3) phone = phone.substring(phone.length() - 4, phone.length());
            } catch (Exception e) {
                e.printStackTrace();
            }

            String str = String.format(getString(R.string.submit_order), name, total, deliveryTime, address, phone);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        clicked = true;
        ArrayList<String> prefix = new ArrayList<>();
        prefix.add("选择");
        AccessibilityClient.getInstance().register(this, true, prefix, null);
        GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getTakeout_pay().getHints(), Constant.TTS_PAY_SUBMUT);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back_action:
                Entry.getInstance().onEvent(Constant.GOBACK_TO_PREACTIVITY, EventType.TOUCH_TYPE);
                finish();
//                onBackPressed();
                break;
            case R.id.address_info:
                try {
                    if (mAddressData == null || TextUtils.isEmpty(mAddressData.getAddress())) {
                        exitLogin();
                    } else {
                        Entry.getInstance().onEvent(Constant.ORDERSUBMIT_ADDRESS_DIALOG, EventType.TOUCH_TYPE);
                        Intent intent = new Intent(this, AddressListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constant.WM_POI_ID, mPoiInfo.getWm_poi_id());
                        intent.putExtra(Constant.ADDRESS_DATA, mAddressData);
                        startActivityForResult(intent, Constant.SELECT_DELIVERY_ADDRESS);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.delivery_info:
                Entry.getInstance().onEvent(Constant.ORDERSUBMIT_TIME_DIALOG, EventType.TOUCH_TYPE);
                showPopwindow();
                backgroundAlpha(0.5f);
                break;

            case R.id.to_pay:
                Entry.getInstance().onEvent(Constant.ORDERSUBMIT_TOPAY, EventType.TOUCH_TYPE);
                if (mAddressData == null) {
                    ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT);
                } else if (mAddressData.getCanShipping() != 1) {
                    ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_submit_msg9), Toast.LENGTH_SHORT);
                } else {
                    if (CacheUtils.getAuth()) {
                        orderSubmit();
                    } else {
                        getPresenter().requestAuthInfo();
                    }
                }
                break;
            case R.id.no_internet_btn:
                netDataReque();
                break;
            default:
                break;
        }
    }

    private void orderSubmit() {
        if (NetUtil.getNetWorkState(this)) {
            if (mAddressData.getType().equals(getResources().getString(R.string.address_destination))) {
                switch (getIntent().getIntExtra(Constant.STATUS, 1)) {
                    case SubmitOrderActivity.CAN_SHIPPING:
                        if (clicked) {
                            if (mOrderPreviewData != null && mOrderPreviewData.getCode() == Constant.ORDER_PREVIEW_SUCCESS && mAddressData != null) {
                                clicked = false;
                                List<OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean> wmOrderingPreviewDetailVoListBean;
                                wmOrderingPreviewDetailVoListBean = mOrderPreviewData.getWm_ordering_preview_detail_vo_list();
                                mToPayTv.setEnabled(false);
                                loadingView.setVisibility(View.VISIBLE);
                                getPresenter().requestOrderSubmitData(mAddressData, mPoiInfo, wmOrderingPreviewDetailVoListBean, mUnixtime, this, loadingView);
                            }
                        }
                        break;
                    case SubmitOrderActivity.CANNOT_SHIPPING:
                        ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_submit_msg9), Toast.LENGTH_SHORT);
                        break;
                    case SubmitOrderActivity.STOP_SHIPPING:
                        ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_submit_msg9), Toast.LENGTH_SHORT);
                        break;
                }
            } else if (mOrderPreviewData != null && mOrderPreviewData.getCode() == Constant.ORDER_PREVIEW_SUCCESS && mAddressData != null) {
                if (clicked) {
                    clicked = false;
                    List<OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean> wmOrderingPreviewDetailVoListBean;
                    wmOrderingPreviewDetailVoListBean = mOrderPreviewData.getWm_ordering_preview_detail_vo_list();
                    mToPayTv.setEnabled(false);
                    loadingView.setVisibility(View.VISIBLE);
                    getPresenter().requestOrderSubmitData(mAddressData, mPoiInfo, wmOrderingPreviewDetailVoListBean, mUnixtime, this, loadingView);
                }
            }
        } else {
            netDataReque();
            ToastUtils.show(this, getResources().getString(R.string.net_unconnect), Toast.LENGTH_LONG);
        }

    }


    private static final int MIN_DELAY_TIME = 1000; // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    private void showPopwindow() {

        ImageView tvCancel;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.arrive_time_layout, null);

        final PopupWindow popWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.px750dp));
        popWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0808080);
        popWindow.setBackgroundDrawable(dw);
        popWindow.setAnimationStyle(R.style.Popupwindow);
        popWindow.showAtLocation(this.findViewById(R.id.address_info), Gravity.TOP, 0, 0);

        mListViewDate = view.findViewById(R.id.list_date);
        mListViewTime = view.findViewById(R.id.list_time);
        tvCancel = view.findViewById(R.id.cancel_action);

        mDateAdapter = new DeliveryDateAdapter(this);
        mListViewDate.setAdapter(mDateAdapter);
        mDateAdapter.setData(mDataBean);

        mTimeAdapter = new DeliveryTimeAdapter(this);
        mListViewTime.setAdapter(mTimeAdapter);
        showFirstItem();

        mListViewDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entry.getInstance().onEvent(Constant.ORDERSUBMIT_CHANGE_TIME, EventType.TOUCH_TYPE);
                mCurDateItem = position;
                List<ArriveTimeBean.MeituanBean.DataBean.TimelistBean> timelistBeans = mDataBean.get(position).getTimelist();
                mTimeAdapter.setData(timelistBeans, mCurDateItem);
                mTimeAdapter.setCurrentItem(0, mCurDateItem);
                mTimeAdapter.notifyDataSetChanged();
                mDateAdapter.setCurrentItem(position);
                mDateAdapter.notifyDataSetChanged();
                if (mPreDateItem == mCurDateItem) {
                    mListViewTime.smoothScrollToPosition(mCurTimeItem);
                } else {
                    mListViewTime.setSelection(0);
                }
            }
        });


        mListViewTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entry.getInstance().onEvent(Constant.ORDERSUBMIT_CHANGE_TIME, EventType.TOUCH_TYPE);
                isChoiceAddressBack = true;
                if (mDataBean != null) {
                    date_type_tip = mDataBean.get(mCurDateItem).getTimelist().get(position).getDate_type_tip();
                    view_time = mDataBean.get(mCurDateItem).getTimelist().get(position).getView_time();
                    date_time = mDataBean.get(mCurDateItem).getDate();
                    mUnixtime = mDataBean.get(mCurDateItem).getTimelist().get(position).getUnixtime();
                    if (mUnixtime == 0) {
                        String date = mDataBean.get(mCurDateItem).getDate().substring(0, mDataBean.get(mCurDateItem).getDate().indexOf("日") + 1);
                        long sysDate = System.currentTimeMillis();
                        String sys = CompareDate.formatTime(sysDate);
                        if (!date.equals(sys)) {
                            mTypeTipTv.setText(getString(R.string.specify_time));
                            mArriveTimeTv.setText(date + " " + mEstimateTime);
                        } else {
                            mArriveTimeTv.setText(String.format(getResources().getString(R.string.arrive_time), mEstimateTime));
                            mTypeTipTv.setText(getString(R.string.delivery_immediately));
                        }
                    } else {
                        if (mCurDateItem != 0) {
                            mArriveTimeTv.setText(date_time + " " + view_time);
                            mTypeTipTv.setText(date_type_tip);
                        } else {
                            String date = mDataBean.get(mCurDateItem).getDate().substring(0, mDataBean.get(mCurDateItem).getDate().indexOf("日") + 1);
                            String today = mDataBean.get(mCurDateItem).getDate().substring(0, 2);
                            long sysDate = System.currentTimeMillis();
                            String sys = CompareDate.formatTime(sysDate);
                            if (today.equals("今天")) {
                                mTypeTipTv.setText(getString(R.string.specify_time));
                                mArriveTimeTv.setText(today + " " + view_time + " 送出");
                            } else {
                                if (!date.equals(sys)) {
                                    mTypeTipTv.setText(getString(R.string.specify_time));
                                    mArriveTimeTv.setText(date + " " + view_time);
                                } else {
                                    mArriveTimeTv.setText(String.format(getResources().getString(R.string.arrive_time), view_time));
                                    mTypeTipTv.setText(date_type_tip);
                                }
                            }
                        }
                    }

                    String shippingFee = mDataBean.get(mCurDateItem).getTimelist().get(position).getView_shipping_fee().trim();
                    String value = StringUtils.substringBefore(shippingFee, getString(R.string.shipping_fee1_text));
                    if (!value.isEmpty()) {

                        mShippingFeeTv.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(Double.parseDouble(value))));
                    }

                }
//                getPresenter().requestOrderPreview(mProductList, mPoiInfo, mUnixtime, mAddressData, SubmitOrderActivity.this);
                mCurTimeItem = position;
                mPreDateItem = mCurDateItem;
                mTimeAdapter.setCurrentItem(mCurTimeItem, mPreDateItem);
                mTimeAdapter.notifyDataSetChanged();
                popWindow.dismiss();


            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popWindow.dismiss();
            }
        });

        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private void showFirstItem() {

        List<ArriveTimeBean.MeituanBean.DataBean.TimelistBean> defaultListBeans = null;
        if (mDataBean != null) {
            defaultListBeans = mDataBean.get(mPreDateItem).getTimelist();
            mTimeAdapter.setData(defaultListBeans, mPreDateItem);
            mTimeAdapter.setCurrentItem(mCurTimeItem, mPreDateItem);
            mListViewTime.smoothScrollToPosition(mCurTimeItem);
            mDateAdapter.setCurrentItem(mPreDateItem);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case Constant.SELECT_DELIVERY_ADDRESS:
                if (data != null) {
                    isChoiceAddressBack = true;
                    mAddressData = (AddressListBean.IovBean.DataBean) data.getSerializableExtra(ADDRESS_DATA);
                    getPresenter().requestOrderPreview(mProductList, mPoiInfo, mUnixtime, mAddressData, SubmitOrderActivity.this);

                    try {
                        if (mAddressData.getCanShipping() != 1) {
                            mAddressTv.setTextColor(0x99ffffff);
                            mArriveTimeTv.setTextColor(0x99ffffff);
                        } else {
                            mAddressTv.setTextColor(this.getResources().getColor(R.color.white));
                            mArriveTimeTv.setTextColor(this.getResources().getColor(R.color.white));
                        }
                        String address = Encryption.desEncrypt(mAddressData.getAddress());

                        if (mAddressData.getCanShipping() != 1) {
                            ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_submit_msg8), Toast.LENGTH_SHORT);
                        }

                        String name = Encryption.desEncrypt(mAddressData.getUser_name());
                        String phone = Encryption.desEncrypt(mAddressData.getUser_phone());
                        mAddressTv.setText(address);
                        mUserNameTv.setText(name);
                        mUserPhoneTv.setText(phone);

                        Intent intent = new Intent(Constant.PULL_LOCATION);
                        intent.setPackage(BuildConfig.APPLICATION_ID);
                        sendBroadcast(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    SubmitInfoPresenter createPresenter() {
        return new SubmitInfoPresenter();
    }

    @Override
    SubmitInfoPresenter.SubmitInfoUi getUi() {
        return this;
    }


    @Override
    public void onGetAddressListSuccess(AddressListBean data) {
        List<AddressListBean.IovBean.DataBean> mDataListBean = data.getIov().getData();
        for (AddressListBean.IovBean.DataBean addressDataBean : mDataListBean) {
            if (addressDataBean.getAddress_id() != null && mAddressData.getAddress_id() != null) {
                if (addressDataBean.getAddress_id().equals(mAddressData.getAddress_id())) {
                    mAddressData.setCanShipping(addressDataBean.getCanShipping());
                    try {
                        if (mAddressData.getCanShipping() != 1) {
                            mAddressTv.setTextColor(0x99ffffff);
                            mArriveTimeTv.setTextColor(0x99ffffff);
                        }
                        mAddressTv.setText(Encryption.desEncrypt(mAddressData.getAddress()));
                        String address = Encryption.desEncrypt(mAddressData.getUser_name()) + " "
                                + Encryption.desEncrypt(mAddressData.getUser_phone());
                        if (mAddressData.getUser_name() != null && mAddressData.getUser_phone() != null) {
                            mUserNameTv.setText(address);
                        } else {
                            if (MyApplicationAddressBean.USER_PHONES.get(0) != null && MyApplicationAddressBean.USER_NAMES.get(0) != null) {
                                mUserNameTv.setText(MyApplicationAddressBean.USER_NAMES.get(0) + "  " + MyApplicationAddressBean.USER_NAMES.get(0));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                try {
                    mAddressTv.setText(Encryption.desEncrypt(mAddressData.getAddress()));
                    if (mAddressData.getUser_name() != null && mAddressData.getUser_phone() != null) {
                        String address = Encryption.desEncrypt(mAddressData.getUser_name()) + " "
                                + Encryption.desEncrypt(mAddressData.getUser_phone());
                        mUserNameTv.setText(address);
                    } else {
                        if (mAddressData.getType().equals(mContext.getString(R.string.address_destination))) {
                            if (MyApplicationAddressBean.USER_PHONES.get(0) != null && MyApplicationAddressBean.USER_NAMES.get(0) != null) {
                                mUserNameTv.setText(MyApplicationAddressBean.USER_NAMES.get(0) + "  " + MyApplicationAddressBean.USER_PHONES.get(0));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
        }

    }

    @Override
    public void onGetAddressListFailure(String msg) {
        mAddressTv.setText(R.string.no_internet);
    }

    @Override
    public void onArriveTimeSuccess(ArriveTimeBean arriveTimeBean) {
        if (arriveTimeBean != null) {
            mDataBean = arriveTimeBean.getMeituan().getData();
        } else {
            ToastUtils.show(SubmitOrderActivity.this, "加载数据失败，请稍后重试", Toast.LENGTH_SHORT);
            Lg.getInstance().d(TAG, "no find data !");
        }

    }

    @Override
    public void onOrderPreviewSuccess(OrderPreviewBean data) {
        if (data != null) {
            mOrderPreviewData = data.getMeituan().getData();
        } else {
            Lg.getInstance().d(TAG, "not find data !");
        }
        if (mOrderPreviewData != null) {
            int code = mOrderPreviewData.getCode();
            if (code == Constant.ORDER_PREVIEW_SUCCESS) {
                showAllProductItem(mOrderPreviewData.getWm_ordering_preview_detail_vo_list());

                double shippingFee = mOrderPreviewData.getWm_ordering_preview_order_vo().getShipping_fee();
                mShippingFeeTv.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(shippingFee)));

                double packingFee = mOrderPreviewData.getWm_ordering_preview_order_vo().getBox_total_price();
                mPackingFee.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(packingFee)));

                double total = mOrderPreviewData.getWm_ordering_preview_order_vo().getTotal();
                mTotalTv.setText(String.format(getResources().getString(R.string.submit_total), total));

                double original_price = mOrderPreviewData.getWm_ordering_preview_order_vo().getOriginal_price();
                double reduced = original_price - total;
                if (reduced == 0) {
                    mDiscountTv.setVisibility(View.GONE);
                    mDiscountsLayout.setVisibility(View.GONE);
                } else {
                    showAllDiscountItem();
                    mDiscountTv.setText(String.format(getResources().getString(R.string.submit_discount), mNumberFormat.format(reduced)));
                }


                String discount_WarnTip = mOrderPreviewData.getDiscountWarnTip();
                if (discount_WarnTip != null) {
                    mDiscountWarnTipTv.setText(discount_WarnTip);
                    mWarnTipParent.setVisibility(View.VISIBLE);
                } else {
                    mWarnTipParent.setVisibility(View.GONE);
                }

                if (mCurDateItem != 0) {
                    mTypeTipTv.setText(date_type_tip);
                    if (date_time != null || view_time != null) {
                        mArriveTimeTv.setText(date_time + " " + view_time);
                    }
                } else {
                    int estimate = mOrderPreviewData.getWm_ordering_preview_order_vo().getEstimate_arrival_time();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    mEstimateTime = sdf.format(new Date(Long.valueOf(String.valueOf(estimate)) * 1000L/* + 8 * 60 * 60 * 1000L*/));
                    mTypeTipTv.setText(getString(R.string.delivery_immediately));
                    if (mEstimateTime != null) {
                        mArriveTimeTv.setText(String.format(getResources().getString(R.string.arrive_time), mEstimateTime));
                    } else {
                        mArriveTimeTv.setText("请选择配送时间");
                    }
                }

            } else {
                handlePreviewMsg(code);
            }
            mParentsLayout.setVisibility(View.VISIBLE);
            mNoNet.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
        } else {
            if (data.getMeituan().getErrorInfo() != null) {
                exitLogin();
                mParentsLayout.setVisibility(View.GONE);
                mNoNet.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);
            }
        }
        playVoice();
    }

    AlertDialog dialog;

    private void exitLogin() {
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.exit_login_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        TextView tvTitle = layout.findViewById(R.id.tv_title);
        Button btnOk = layout.findViewById(R.id.ok);
        Button btnCancel = layout.findViewById(R.id.cancel);
        tvTitle.setText(getResources().getString(R.string.choose_address_again));
        btnCancel.setVisibility(View.GONE);
        dialog = builder.create();
        dialog.show();
        if (dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setLayout((int) getResources().getDimension(R.dimen.px912dp), (int) getResources().getDimension(R.dimen.px516dp));
            window.setBackgroundDrawableResource(R.drawable.permission_dialog_bg);
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER_HORIZONTAL);
            window.setGravity(Gravity.TOP);
            lp.y = (int) getResources().getDimension(R.dimen.px480dp);
            window.setAttributes(lp);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubmitOrderActivity.this, AddressSelectActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
    }

    public void showAllDiscountItem() {
        List<OrderPreviewBean.MeituanBean.DataBean.DiscountsBean> discountsBeanList = mOrderPreviewData.getDiscounts();
        mDiscountsLayout.removeAllViews();
        for (OrderPreviewBean.MeituanBean.DataBean.DiscountsBean discountsBean : discountsBeanList) {
            LayoutInflater inflater = this.getLayoutInflater();
            final RelativeLayout discountItem = (RelativeLayout) inflater.inflate(R.layout.discount_list_item, mDiscountsLayout, false);
            if (discountsBean.getReduceFree() == 0) {
                discountItem.setVisibility(View.INVISIBLE);
            } else {
                TextView discount_name_tv = discountItem.findViewById(R.id.discount_name);
//                discount_name_tv.setText(discountsBean.getName());
                discount_name_tv.setText("满减优惠");
                TextView discount_info_tv = discountItem.findViewById(R.id.discount);
                discount_info_tv.setText(String.format(getString(R.string.discount_money), mNumberFormat.format(discountsBean.getReduceFree())));
                mDiscountsLayout.addView(discountItem);
            }
        }

    }

    private void handlePreviewMsg(int code) {

        switch (code) {
            case Constant.STORE_CANT_NOT_BUY:
                ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_preview_msg2), Toast.LENGTH_SHORT);
                break;

            case Constant.FOOD_CANT_NOT_BUY:
                ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_preview_msg3), Toast.LENGTH_SHORT);
                break;
            case Constant.FOOD_COST_NOT_BUY:
                ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_preview_msg5), Toast.LENGTH_SHORT);
                break;
            case Constant.FOOD_COUNT_NOT_BUY:
                ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_preview_msg15), Toast.LENGTH_SHORT);
                break;

            case Constant.FOOD_LACK_NOT_BUY:
                ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.order_preview_msg20), Toast.LENGTH_SHORT);
                break;
            case Constant.SERVICE_ERROR:
                ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.service_error), Toast.LENGTH_SHORT);
                break;
        }

    }

    @Override
    public void onFailure(String msg) {
        mToPayTv.setEnabled(true);
        clicked = true;
        loadingView.setVisibility(View.GONE);
        mParentsLayout.setVisibility(View.GONE);
        mNoNet.setVisibility(View.VISIBLE);

//        if (!NetUtil.getNetWorkState(this)) {
//            mParentsLayout.setVisibility(View.GONE);
//            mNoNet.setVisibility(View.VISIBLE);
//        }
    }


    @Override
    public void toPay() {
        mToPayTv.performClick();
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void onOrderSubmitSuccess(OrderSubmitBean data) {
        mToPayTv.setEnabled(true);
        clicked = true;
        mNoNet.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        if (data != null) {
            mOrderSubmitData = data.getMeituan().getData();
            int submitCode = mOrderSubmitData.getCode();
            if (submitCode == Constant.SUBMIT_ORDER_SUCCESS) {
                double total = mOrderPreviewData.getWm_ordering_preview_order_vo().getTotal();
                long orderId = mOrderSubmitData.getOrder_id();
                String poiName = mOrderPreviewData.getWm_ordering_preview_order_vo().getPoi_name();
                Long poiId = mOrderPreviewData.getWm_ordering_preview_order_vo().getWm_poi_id();
                String payUrl = mOrderSubmitData.getPayUrl();
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(Constant.STORE_ID, poiId + "");
                intent.putExtra(Constant.EXPECTED_TIME, mUnixtime);
                intent.putExtra(Constant.TOTAL_COST, total);
                intent.putExtra(Constant.ORDER_ID, orderId);
                intent.putExtra(Constant.SHOP_NAME, poiName);
                intent.putExtra(Constant.PAY_URL, payUrl);
                intent.putExtra(Constant.PIC_URL, mPoiInfo.getPic_url());
                intent.putExtra("flag", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //延迟1S关闭，解决透底的问题
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            } else if (submitCode == Constant.SERVICE_ERROR) {
                ToastUtils.show(this, getString(R.string.order_submit_error), Toast.LENGTH_SHORT);
                finish();
            } else if (submitCode == Constant.SUBMIT_ORDER_FAIL) {
                ToastUtils.show(this, getString(R.string.order_submit_error), Toast.LENGTH_SHORT);
            } else if (submitCode == Constant.BEYOND_DELIVERY_RANGE) {
                clicked = true;
                ToastUtils.show(this, getString(R.string.order_submit_msg8), Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void authSuccess(String msg) {
        Entry.getInstance().onEvent(Constant.EVENT_CLICK_SERVICE_AUTH, EventType.TOUCH_TYPE);
        boolean isBackground = BackgroundUtils.isBackground(getBaseContext());
        if (!isBackground) {
            orderSubmit();
        }
    }

    @Override
    public void authFailure(String msg) {
        ToastUtils.show(this, "授权失败，请开启服务授权", Toast.LENGTH_SHORT);
    }
}
