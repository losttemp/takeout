package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.adapter.DeliveryDateAdapter;
import com.baidu.iov.dueros.waimai.adapter.DeliveryTimeAdapter;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.presenter.SubmitInfoPresenter;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.R;
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

    public final static String TOTAL_COST = "total_cost";
    public final static String ORDER_ID = "order_id";
    public final static String SHOP_NAME = "shop_name";
    public final static String PAY_URL = "pay_url";
    public final static String PIC_URL = "pic_url";

    private final static int SELECT_DELIVERY_ADDRESS = 100;
    private final static int ORDER_PREVIEW_SUCCESS = 0;
    private final static int SUBMIT_ORDER_SUCCESS = 0;
    private RelativeLayout mArrivetimeLayout;
    private RelativeLayout mAddressUpdateLayout;
    private LinearLayout mProductInfoListview;
    private TextView mPackingFee;
    private TextView mShippingFeeTv;
    private TextView mDiscount;
    private TextView mToPayTv;
    private TextView mDiscountTv;
    private TextView mShopNameTv;
    private TextView mArriveTimeTv;
    private TextView mTypeTipTv;
    private TextView mDeliveryTypeTv;
    private TextView mAddressTv;
    private TextView mUserNameTv;
    private TextView mUserPhoneTv;
    private TextView mTotalTv;

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
    private int mPreAddressItem = -1;
    private NumberFormat mNumberFormat;
    private String mEstimateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);

        mNumberFormat = new DecimalFormat("#.#");
        Intent intent = getIntent();
        if (intent != null) {
            mProductList = (List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean>) intent.getSerializableExtra(PRODUCT_LIST_BEAN);
            mPoiInfo = (PoifoodListBean.MeituanBean.DataBean.PoiInfoBean) intent.getSerializableExtra(POI_INFO);

            if (mProductList != null && mPoiInfo != null) {
                getPresenter().requestArriveTimeData(mPoiInfo.getWm_poi_id());
                getPresenter().requestOrderPreview(mProductList, mPoiInfo);
            }
        }

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

        mArrivetimeLayout.setOnClickListener(this);
        mAddressUpdateLayout.setOnClickListener(this);

        if (mProductList != null && mPoiInfo != null) {

            showAllProduct(mProductList);
            String shopName = mPoiInfo.getName();
            mShopNameTv.setText(shopName);
            String deliveryType = mPoiInfo.getDelivery_type() == 1 ? getString(R.string.delivery_type1_text)
                    : getString(R.string.delivery_type2_text);
            mDeliveryTypeTv.setText(deliveryType);
        }
    }


    public void showAllProduct(List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList) {

        mProductInfoListview.removeAllViews();
        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean : productList) {

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

            String pictureUrl = spusBean.getPicture();
            String name = spusBean.getName();

            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrsBeanList;
            attrsBeanList = spusBean.getAttrs();
            StringBuilder attrs = new StringBuilder();

            if (attrsBeanList.size() > 0) {
                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean attrsBean : attrsBeanList) {
                    for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean valuesBean : attrsBean.getChoiceAttrs())
                        attrs.append(valuesBean.getValue() + " ");
                }
                tv_attrs.setText(attrs.toString());
                tv_attrs.setVisibility(View.VISIBLE);
            } else {
                tv_attrs.setVisibility(View.INVISIBLE);
            }


            NumberFormat nf = new DecimalFormat("#.#");
            int count = spusBean.getNumber();
            double price = spusBean.getSkus().get(0).getPrice();
            double origin_price = spusBean.getSkus().get(0).getOrigin_price();

            if (price > origin_price) {
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
            case R.id.address_info:

                Intent intent = new Intent(this, AddressListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SELECT_DELIVERY_ADDRESS);
                break;

            case R.id.delivery_info:

                showPopwindow();
                backgroundAlpha(0.5f);
                break;

            case R.id.to_pay:

                if (mOrderPreviewData != null && mOrderPreviewData.getCode() == ORDER_PREVIEW_SUCCESS && mAddressData != null) {
                    List<OrderPreviewBean.MeituanBean.DataBean.WmOrderingPreviewDetailVoListBean> wmOrderingPreviewDetailVoListBean;
                    wmOrderingPreviewDetailVoListBean = mOrderPreviewData.getWm_ordering_preview_detail_vo_list();
                    getPresenter().requestOrderSubmitData(mAddressData, mPoiInfo, wmOrderingPreviewDetailVoListBean);
                }

                break;

            default:
                break;
        }
    }

    private void showPopwindow() {

        TextView tvCancel;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.arrive_time_layout, null);

        final PopupWindow popWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, 500);
        popWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0808080);
        popWindow.setBackgroundDrawable(dw);
        popWindow.setAnimationStyle(R.style.Popupwindow);
        popWindow.showAtLocation(this.findViewById(R.id.address_info), Gravity.TOP, 0, 0);

        mListViewDate = view.findViewById(R.id.list_date);
        mListViewTime = view.findViewById(R.id.list_time);
        tvCancel = view.findViewById(R.id.tv_cancel);

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

                if (mDataBean != null) {
                    String type = mDataBean.get(mCurDateItem).getTimelist().get(position).getDate_type_tip();
                    mTypeTipTv.setText(type);
                    String time = mDataBean.get(mCurDateItem).getTimelist().get(position).getView_time();
                    if (!type.isEmpty() && type.equals(getString(R.string.delivery_immediately))) {
                        mArriveTimeTv.setText(String.format(getResources().getString(R.string.arrive_time), mEstimateTime));
                    } else {
                        mArriveTimeTv.setText(time);
                    }

                    String shippingFee = mDataBean.get(mCurDateItem).getTimelist().get(position).getView_shipping_fee().trim();
                    String value = StringUtils.substringBefore(shippingFee, getString(R.string.shipping_fee1_text));
                    if (!value.isEmpty()) {

                        mShippingFeeTv.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(Double.parseDouble(value))));
                    }

                }
                getPresenter().requestOrderPreview(mProductList, mPoiInfo);
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

            case SELECT_DELIVERY_ADDRESS:
                if (data != null) {
                    mAddressData = (AddressListBean.IovBean.DataBean) data.getSerializableExtra(ADDRESS_DATA);

                    try {
                        mAddressTv.setText(Encryption.desEncrypt(mAddressData.getAddress()));
                        mUserNameTv.setText(Encryption.desEncrypt(mAddressData.getUser_name()));
                        mUserPhoneTv.setText(Encryption.desEncrypt(mAddressData.getUser_phone()));
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
            String defaultType = mDataBean.get(0).getTimelist().get(0).getDate_type_tip();
            mTypeTipTv.setText(defaultType);

            String time = mDataBean.get(0).getTimelist().get(0).getView_time();
            mArriveTimeTv.setText(time);
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

        if (mOrderPreviewData.getCode() == ORDER_PREVIEW_SUCCESS) {
            double shippingFee = mOrderPreviewData.getWm_ordering_preview_order_vo().getShipping_fee();
            mShippingFeeTv.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(shippingFee)));

            double packingFee = mOrderPreviewData.getWm_ordering_preview_order_vo().getBox_total_price();
            mPackingFee.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(packingFee)));

            double total = mOrderPreviewData.getWm_ordering_preview_order_vo().getTotal();
            mTotalTv.setText(String.format(getResources().getString(R.string.submit_total), total));

            double original_price = mOrderPreviewData.getWm_ordering_preview_order_vo().getOriginal_price();
            double reduced = original_price - total;
            mDiscountTv.setText(String.format(getResources().getString(R.string.submit_discount), mNumberFormat.format(reduced)));

            int estimate = mOrderPreviewData.getWm_ordering_preview_order_vo().getEstimate_arrival_time();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date date = new Date((estimate + System.currentTimeMillis()));
            mEstimateTime = sdf.format(date);

        }


    }


    @Override
    public void onOrderPreviewFailure(String msg) {

    }

    @Override
    public void onOrderSubmitSuccess(OrderSubmitBean data) {
        if (data != null) {
            mOrderSubmitData = data.getMeituan().getData();
        }

        if (mOrderSubmitData.getCode() == SUBMIT_ORDER_SUCCESS) {
            double total = mOrderPreviewData.getWm_ordering_preview_order_vo().getTotal();
            long orderId = mOrderSubmitData.getOrder_id();
            String poiName = mOrderPreviewData.getWm_ordering_preview_order_vo().getPoi_name();
            String payUrl = mOrderSubmitData.getPayUrl();
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(TOTAL_COST, total);
            intent.putExtra(ORDER_ID, orderId);
            intent.putExtra(SHOP_NAME, poiName);
            intent.putExtra(PAY_URL, payUrl);
            intent.putExtra(PIC_URL, mPoiInfo.getPic_url());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }
}
