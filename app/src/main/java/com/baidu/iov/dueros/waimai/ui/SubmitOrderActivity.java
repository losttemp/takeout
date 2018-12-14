package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.adapter.DeliveryDateAdapter;
import com.baidu.iov.dueros.waimai.adapter.DeliveryTimeAdapter;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.presenter.SubmitInfoPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.faceos.client.GsonUtil;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.baidu.iov.dueros.waimai.ui.AddressListActivity.ADDRESS_DATA;
import static com.baidu.iov.dueros.waimai.ui.FoodListActivity.POI_INFO;
import static com.baidu.iov.dueros.waimai.ui.FoodListActivity.PRODUCT_LIST_BEAN;

public class SubmitOrderActivity extends BaseActivity<SubmitInfoPresenter, SubmitInfoPresenter.SubmitInfoUi>
        implements SubmitInfoPresenter.SubmitInfoUi, View.OnClickListener {

    private static final String TAG = SubmitOrderActivity.class.getSimpleName();

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
    private TextView mUserNameTv;
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

            if (mProductList != null && mPoiInfo != null) {
                getPresenter().requestArriveTimeData(mPoiInfo.getWm_poi_id());
                getPresenter().requestOrderPreview(mProductList, mPoiInfo, mUnixtime, mAddressData);
            }
        }

        initView();
    }

    public void initView() {

        mAddressTv = findViewById(R.id.tv_address);
        mUserNameTv = findViewById(R.id.tv_name);
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

        mBackImg.setOnClickListener(this);
        mArrivetimeLayout.setOnClickListener(this);
        mAddressUpdateLayout.setOnClickListener(this);

        if (mProductList != null && mPoiInfo != null) {

            String shopName = mPoiInfo.getName();
            mShopNameTv.setText(shopName);
            String deliveryType = mPoiInfo.getDelivery_type() == 1 ? getString(R.string.delivery_type1_text)
                    : getString(R.string.delivery_type2_text);
            mDeliveryTypeTv.setText(deliveryType);
        }

        if (mAddressData != null) {
            try {
                mAddressTv.setText(Encryption.desEncrypt(mAddressData.getAddress()));
                String address = Encryption.desEncrypt(mAddressData.getUser_name()) + " "
                        + Encryption.desEncrypt(mAddressData.getUser_phone());
                mUserNameTv.setText(address);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void playVoice() {
        boolean isNeedVoice = getIntent().getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false);
        if (isNeedVoice && !isChoiceAddressBack && mOrderPreviewData.getWm_ordering_preview_detail_vo_list().size() > 0) {
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

            String str = String.format(getString(R.string.submit_order), oneFood, allPrice, deliveryTime, address, phone);
            VoiceManager.getInstance().playTTS(SubmitOrderActivity.this, str);
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
            TextView tv_discounts = viewItem.findViewById(R.id.product_discount);

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
            if (price < origin_price) {
                tv_origin_price.setText(String.format(getResources().getString(R.string.cost_text), nf.format(origin_price)));
                tv_origin_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_origin_price.setVisibility(View.VISIBLE);
                tv_discounts.setVisibility(View.VISIBLE);
            } else {
                tv_origin_price.setVisibility(View.INVISIBLE);
                tv_discounts.setVisibility(View.INVISIBLE);
            }
            Glide.with(this).load(pictureUrl).into(img_photo);
            tv_name.setText(name);
            tv_count.setText(String.format(getResources().getString(R.string.count_char), count));
            tv_price.setText(String.format(getResources().getString(R.string.cost_text), nf.format(price)));

            mProductInfoListview.addView(viewItem, params);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back_action:
                onBackPressed();
                break;
            case R.id.address_info:

                Intent intent = new Intent(this, AddressListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constant.WM_POI_ID, mPoiInfo.getWm_poi_id());
                startActivityForResult(intent, Constant.SELECT_DELIVERY_ADDRESS);
                break;

            case R.id.delivery_info:

                showPopwindow();
                backgroundAlpha(0.5f);
                break;

            case R.id.to_pay:

                if (mAddressData == null) {
                    ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT);
                }
                if (NetUtil.getNetWorkState(this)) {
                    if (mOrderPreviewData != null && mOrderPreviewData.getCode() == Constant.ORDER_PREVIEW_SUCCESS && mAddressData != null) {
//                        mAddressData.setLongitude(95369826);
//                        mAddressData.setLatitude(29735952);
                        List<OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean> wmOrderingPreviewDetailVoListBean;
                        wmOrderingPreviewDetailVoListBean = mOrderPreviewData.getWm_ordering_preview_detail_vo_list();
                        getPresenter().requestOrderSubmitData(mAddressData, mPoiInfo, wmOrderingPreviewDetailVoListBean, mUnixtime);
                    }
                } else {
                    ToastUtils.show(this, getResources().getString(R.string.net_unconnect), Toast.LENGTH_LONG);
                }
                break;

            default:
                break;
        }
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

                mCurDateItem = position;
                List<ArriveTimeBean.MeituanBean.DataBean.TimelistBean> timelistBeans = mDataBean.get(position).getTimelist();
                mTimeAdapter.setData(timelistBeans, mCurDateItem);
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
                isChoiceAddressBack = true;
                if (mDataBean != null) {
                    String type = mDataBean.get(mCurDateItem).getTimelist().get(position).getDate_type_tip();
                    String time = mDataBean.get(mCurDateItem).getTimelist().get(position).getView_time();
                    mUnixtime = mDataBean.get(mCurDateItem).getTimelist().get(position).getUnixtime();
                    if (mUnixtime == 0) {
                        mArriveTimeTv.setText(String.format(getResources().getString(R.string.arrive_time), mEstimateTime));
                        mTypeTipTv.setText(getString(R.string.delivery_immediately));
                    } else {
                        mArriveTimeTv.setText(time);
                        mTypeTipTv.setText(type);
                    }

                    String shippingFee = mDataBean.get(mCurDateItem).getTimelist().get(position).getView_shipping_fee().trim();
                    String value = StringUtils.substringBefore(shippingFee, getString(R.string.shipping_fee1_text));
                    if (!value.isEmpty()) {

                        mShippingFeeTv.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(Double.parseDouble(value))));
                    }

                }
                getPresenter().requestOrderPreview(mProductList, mPoiInfo, mUnixtime, mAddressData);
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
                    getPresenter().requestOrderPreview(mProductList, mPoiInfo, mUnixtime, mAddressData);

                    boolean isNeedVoice = data.getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false);
                    try {
                        String address = Encryption.desEncrypt(mAddressData.getAddress());
                        mAddressTv.setText(address);
                        if (isNeedVoice) {
                            VoiceManager.getInstance().playTTS(SubmitOrderActivity.this,
                                    String.format(getString(R.string.commodity_address), address));
                        }
                        String name = Encryption.desEncrypt(mAddressData.getUser_name()) + " "
                                + Encryption.desEncrypt(mAddressData.getUser_phone());
                        mUserNameTv.setText(name);
                        sendBroadcast(new Intent(Constant.PULL_LOCATION));
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
    public void onArriveTimeSuccess(ArriveTimeBean arriveTimeBean) {
        if (arriveTimeBean != null) {
            mDataBean = arriveTimeBean.getMeituan().getData();
        } else {
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


        int code = mOrderPreviewData.getCode();
        if (code == Constant.ORDER_PREVIEW_SUCCESS) {
            showAllProductItem(mOrderPreviewData.getWm_ordering_preview_detail_vo_list());
            showAllDiscountItem();
            double shippingFee = mOrderPreviewData.getWm_ordering_preview_order_vo().getShipping_fee();
            mShippingFeeTv.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(shippingFee)));


            double packingFee = mOrderPreviewData.getWm_ordering_preview_order_vo().getBox_total_price();
            mPackingFee.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(packingFee)));

            double total = mOrderPreviewData.getWm_ordering_preview_order_vo().getTotal();
            mTotalTv.setText(String.format(getResources().getString(R.string.submit_total), total));

            double original_price = mOrderPreviewData.getWm_ordering_preview_order_vo().getOriginal_price();
            double reduced = original_price - total;
            mDiscountTv.setText(String.format(getResources().getString(R.string.submit_discount), mNumberFormat.format(reduced)));


            String discount_WarnTip = mOrderPreviewData.getDiscountWarnTip();
            if (discount_WarnTip != null) {
                mDiscountWarnTipTv.setText(discount_WarnTip);
                mWarnTipParent.setVisibility(View.VISIBLE);
            } else {
                mWarnTipParent.setVisibility(View.GONE);
            }

            int estimate = mOrderPreviewData.getWm_ordering_preview_order_vo().getEstimate_arrival_time();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            mEstimateTime = sdf.format(new Date(Long.valueOf(String.valueOf(estimate)) * 1000L/* + 8 * 60 * 60 * 1000L*/));
            if (mUnixtime == 0) {
                mTypeTipTv.setText(getString(R.string.delivery_immediately));
                mArriveTimeTv.setText(String.format(getResources().getString(R.string.arrive_time), mEstimateTime));
            }

        } else {
            handlePreviewMsg(code);
        }
        playVoice();
    }

    public void showAllDiscountItem() {
        List<OrderPreviewBean.MeituanBean.DataBean.DiscountsBean> discountsBeanList = mOrderPreviewData.getDiscounts();
        mDiscountsLayout.removeAllViews();
        for (OrderPreviewBean.MeituanBean.DataBean.DiscountsBean discountsBean : discountsBeanList) {
            LayoutInflater inflater = this.getLayoutInflater();
            final RelativeLayout discountItem = (RelativeLayout) inflater.inflate(R.layout.discount_list_item, mDiscountsLayout, false);
            TextView discount_name_tv = discountItem.findViewById(R.id.discount_name);
            discount_name_tv.setText(discountsBean.getName());
            TextView discount_info_tv = discountItem.findViewById(R.id.discount);
            discount_info_tv.setText(String.format(getString(R.string.discount_money), mNumberFormat.format(discountsBean.getReduceFree())));
            mDiscountsLayout.addView(discountItem);
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
        if (data != null) {
            mOrderSubmitData = data.getMeituan().getData();
        }

        int submitCode = mOrderSubmitData.getCode();
        if (submitCode == Constant.SUBMIT_ORDER_SUCCESS) {
            double total = mOrderPreviewData.getWm_ordering_preview_order_vo().getTotal();
            long orderId = mOrderSubmitData.getOrder_id();
            String poiName = mOrderPreviewData.getWm_ordering_preview_order_vo().getPoi_name();
            Long poiId = mOrderPreviewData.getWm_ordering_preview_order_vo().getWm_poi_id();
            String payUrl = mOrderSubmitData.getPayUrl();
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(Constant.STORE_ID, poiId);
            intent.putExtra(Constant.EXPECTED_TIME, mUnixtime);
            intent.putExtra(Constant.TOTAL_COST, total);
            intent.putExtra(Constant.ORDER_ID, orderId);
            intent.putExtra(Constant.SHOP_NAME, poiName);
            intent.putExtra(Constant.PAY_URL, payUrl);
            intent.putExtra(Constant.PIC_URL, mPoiInfo.getPic_url());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (submitCode == Constant.SERVICE_ERROR) {

            ToastUtils.show(this, getString(R.string.service_error), Toast.LENGTH_SHORT);
        } else if (submitCode == Constant.BEYOND_DELIVERY_RANGE) {
            ToastUtils.show(this, getString(R.string.order_submit_msg8), Toast.LENGTH_SHORT);
        }

    }

}
