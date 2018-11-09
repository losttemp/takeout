package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.adapter.DeliveryDateAdapter;
import com.baidu.iov.dueros.waimai.adapter.DeliveryTimeAdapter;
import com.baidu.iov.dueros.waimai.adapter.ProductInfoAdapter;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.presenter.SubmitInfoPresenter;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.R;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    private final static int SELECT_DELIVERY_ADDRESS = 100;
    private RelativeLayout mArrivetimeLayout;
    private RelativeLayout mAddressUpdateLayout;
    private ListView mProductInfoListview;
    private TextView mPackingFee;
    private TextView mShippingFeeTv;
    private TextView mDiscount;
    private TextView mToPayTv;
    private TextView mDiscountExists;
    private TextView mShopNameTv;
    private TextView mArriveTimeTv;
    private TextView mTypeTipTv;
    private TextView mDeliveryTypeTv;
    private TextView mAddressTv;
    private TextView mUserNameTv;
    private TextView mUserPhoneTv;

    private static List<ArriveTimeBean.MeituanBean.DataBean> mDataBean;
    private AddressListBean.IovBean.DataBean mAddressData;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> mProductList;
    private PoifoodListBean.MeituanBean.DataBean.PoiInfoBean mPoiInfo;
    private OrderPreviewBean.MeituanBean.DataBean mOrderPreview;


    private ListView mListViewDate;
    private ListView mListViewTime;
    private DeliveryDateAdapter mDateAdapter;
    private DeliveryTimeAdapter mTimeAdapter;
    private ProductInfoAdapter mProductInfoAdapter;
    private int mCurTimeItem = 0;
    private int mCurDateItem = 0;
    private int mPreDateItem = 0;
    private NumberFormat mNumberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        initView();
    }

    public void initView() {

/*

        mDiscount = findViewById(R.id.discount);

        mDiscountExists = findViewById(R.id.discount_exists);


        mPackingFee.setText(String.format(getResources().getString(R.string.cost), 2));
        mDeliveryFee.setText(String.format(getResources().getString(R.string.cost), 5));
        mDiscount.setText(String.format(getResources().getString(R.string.discount), 5));
        mToPay.setText(String.format(getResources().getString(R.string.total_cost), 35));
        String text = String.format(getResources().getString(R.string.discount_exist), 5);
        mDiscountExists.setText(Html.fromHtml(text));
*/

        Intent intent = getIntent();

        if (intent != null) {
            mProductList = (List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean>) intent.getSerializableExtra(PRODUCT_LIST_BEAN);
            mPoiInfo = (PoifoodListBean.MeituanBean.DataBean.PoiInfoBean) intent.getSerializableExtra(POI_INFO);
        }

        mNumberFormat = new DecimalFormat("#.#");

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

        mArrivetimeLayout.setOnClickListener(this);
        mAddressUpdateLayout.setOnClickListener(this);
        mProductInfoAdapter = new ProductInfoAdapter(this);
        mProductInfoListview.setAdapter(mProductInfoAdapter);

        if (mProductList != null && mPoiInfo != null) {

            mProductInfoAdapter.setData(mProductList, mPoiInfo);
            String shopName = mPoiInfo.getName();
            mShopNameTv.setText(shopName);
            String deliveryType = mPoiInfo.getDelivery_type() == 1 ? getString(R.string.delivery_type1_text)
                    : getString(R.string.delivery_type2_text);
            mDeliveryTypeTv.setText(deliveryType);

            double shippingFee = mPoiInfo.getShipping_fee();
            mShippingFeeTv.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(shippingFee)));


            double packingFee = 0;
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean : mProductList) {

                packingFee = packingFee + spusBean.getSkus().get(0).getBox_num() * spusBean.getSkus().get(0).getBox_price();
            }
            mPackingFee.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(packingFee)));

            getPresenter().requestArriveTimeData(mPoiInfo.getWm_poi_id());


        }

        if (mDataBean != null) {
            String defaultType = mDataBean.get(0).getTimelist().get(0).getDate_type_tip();
            mTypeTipTv.setText(defaultType);
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

                if (mPoiInfo != null && mProductList != null) {

                    Intent data = new Intent(this, PaymentActivity.class);
                    data.putExtra(POI_INFO, (Serializable) mPoiInfo);
                    data.putExtra(ADDRESS_DATA, mAddressData);
                    data.putExtra(PRODUCT_LIST_BEAN, (Serializable) mProductList);
                    data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(data);
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
                    if (!type.isEmpty() && type.equals(time)) {
                        mArriveTimeTv.setText(String.format(getResources().getString(R.string.arrive_time), "14:20"));
                    } else {
                        mArriveTimeTv.setText(time);
                    }

                    String shippingFee = mDataBean.get(mCurDateItem).getTimelist().get(position).getView_shipping_fee().trim();
                    String value = StringUtils.substringBefore(shippingFee, getString(R.string.shipping_fee1_text));
                    if (!value.isEmpty()) {

                        mShippingFeeTv.setText(String.format(getResources().getString(R.string.cost_text), mNumberFormat.format(Double.parseDouble(value))));
                    }

                }
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

                    getPresenter().requestOrderPreview(mProductList, mPoiInfo, mAddressData);
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
            mOrderPreview = data.getMeituan().getData();
        } else {
            Lg.getInstance().d(TAG, "not find data !");
        }

    }

    @Override
    public void onError(String error) {

    }
}
