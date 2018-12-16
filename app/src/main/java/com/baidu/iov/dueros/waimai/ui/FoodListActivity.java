package com.baidu.iov.dueros.waimai.ui;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.DiscountAdaper;
import com.baidu.iov.dueros.waimai.adapter.PoifoodSpusListAdapter;
import com.baidu.iov.dueros.waimai.adapter.PoifoodSpusTagsAdapter;
import com.baidu.iov.dueros.waimai.adapter.ShoppingCartAdapter;
import com.baidu.iov.dueros.waimai.bean.PoifoodSpusTagsBean;
import com.baidu.iov.dueros.waimai.interfacedef.IShoppingCartToDetailListener;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListExtraBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListExtraPayloadBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoidetailinfoBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.presenter.PoifoodListPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.DoubleUtil;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.GlideApp;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.baidu.iov.dueros.waimai.view.FlowLayoutManager;
import com.baidu.iov.faceos.client.GsonUtil;
import com.domain.multipltextview.MultiplTextView;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

public class FoodListActivity extends BaseActivity<PoifoodListPresenter, PoifoodListPresenter.PoifoodListUi> implements PoifoodListPresenter.PoifoodListUi, View.OnClickListener, PoifoodSpusListAdapter.onCallBackListener, IShoppingCartToDetailListener {
    private static final String TAG = FoodListActivity.class.getSimpleName();
    public static final String POI_INFO = "poi_info";
    public static final String PRODUCT_LIST_BEAN = "product_list_bean";
    public static final String DISCOUNT = "discount";

    private boolean isScroll = true;
    private RecyclerView mFoodSpuTagsList;
    private RecyclerView mSpusList;
    private PoifoodSpusListAdapter mPoifoodSpusListAdapter;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList;
    private MultiplTextView shoppingPrise;
    private TextView shoppingNum;
    private Button settlement;
    private LinearLayout cardShopLayout;
    private ImageView shopping_cart;
    private int AnimationDuration = 500;
    private int number = 0;
    private boolean isClean = false;
    private FrameLayout animation_viewGroup;
    private MultiplTextView defaultText;
    private List<PoifoodSpusTagsBean> poifoodSpusTagsBeans;
    private RelativeLayout parentLayout;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans = new ArrayList<>();
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeanList;
    private ListView shoppingListView;
    private ShoppingCartAdapter shoppingCartAdapter;
    private ArrayMap<String, String> map;

    private Handler myHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        animation_viewGroup.removeAllViews();
                    } catch (Exception e) {

                    }
                    isClean = false;

                    break;
                default:
                    break;
            }
            return true;
        }
    });

    private PoifoodSpusTagsAdapter mFoodSpuTagsListAdapter;
    private RelativeLayout mStoreDetails;
    private MultiplTextView mClearshopCart;
    private PoidetailinfoBean mPoidetailinfoBean;
    private PoifoodListBean.MeituanBean.DataBean.PoiInfoBean mPoiInfoBean;
    private ImageView mFinish;
    private MultiplTextView mShopTitle;
    private MultiplTextView mDelivery;
    private TextView mBulletin;
    private RecyclerView mDiscounts;
    private ImageView mShopPicture;
    private List<String> listFull;
    private List<String> listReduce;
    private MultiplTextView mDiscount;
    private Integer discount;
    private double mDiscountNumber;
    private TextView mDetailsNotice;
    private MultiplTextView mDetailsDistribution;
    private RecyclerView mDetailsDiscount;
    private MultiplTextView mDetailsShopName;
    private Dialog mBottomDialog;
    private TextView mShopCartNum;
    private MultiplTextView mCartDistributionFee;
    private MultiplTextView mCartShoppingPrise;
    private Button mCartSettlement;
    private TextView mCartDiscount;
    private MultiplTextView mCartClose;
    private MultiplTextView mDistributionFee;
    private RelativeLayout mRlDiscount;
    private MultiplTextView mNoProduct;
    private RelativeLayout mLlPrice;
    private List<String> foodSpuTagsBeanName;
    private RelativeLayout mToolBar;
    private String mFirstDiscount;
    private boolean mOneMoreOrder;
    private MultiplTextView mMtDistributionFee;
    private RelativeLayout mRlNoProduct;
    private List<Boolean> mIsDiscountList;
    private boolean alreadyToast;
    private boolean isNeedVoice;
    private long mWmPoiId;
    private LinearLayout mNoNet;
    private Button mNoInternetButton;
    private LinearLayout mLoading;

    @Override
    PoifoodListPresenter createPresenter() {
        return new PoifoodListPresenter();
    }

    @Override
    PoifoodListPresenter.PoifoodListUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        map = new ArrayMap<>();
        initView();
        initData();
    }

    private void initView() {
        animation_viewGroup = createAnimLayout();
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        shoppingPrise = (MultiplTextView) findViewById(R.id.shoppingPrise);
        shoppingNum = (TextView) findViewById(R.id.shoppingNum);
        settlement = (Button) findViewById(R.id.settlement);
        mFoodSpuTagsList = (RecyclerView) findViewById(R.id.classify_mainlist);
        mSpusList = (RecyclerView) findViewById(R.id.classify_morelist);
        shopping_cart = (ImageView) findViewById(R.id.shopping_cart);
        mStoreDetails = (RelativeLayout) findViewById(R.id.rl_store_details);
        mFinish = (ImageView) findViewById(R.id.iv_finish);
        mShopTitle = (MultiplTextView) findViewById(R.id.tv_shop_title);
        mDelivery = (MultiplTextView) findViewById(R.id.tv_delivery);
        mBulletin = (TextView) findViewById(R.id.tv_bulletin);
        mDiscounts = (RecyclerView) findViewById(R.id.tv_discounts);
        mShopPicture = (ImageView) findViewById(R.id.iv_shop);
        mDiscount = (MultiplTextView) findViewById(R.id.tv_discount);
        mDistributionFee = (MultiplTextView) findViewById(R.id.tv_distribution_fee);
        mRlDiscount = (RelativeLayout) findViewById(R.id.rl_discount);
        mNoProduct = (MultiplTextView) findViewById(R.id.tv_no_product);
        mLlPrice = (RelativeLayout) findViewById(R.id.ll_price);
        mToolBar = (RelativeLayout) findViewById(R.id.toolBar);
        mMtDistributionFee = (MultiplTextView) findViewById(R.id.mt_distribution_fee);
        mRlNoProduct = (RelativeLayout) findViewById(R.id.rl_no_product);
        mNoNet = (LinearLayout) findViewById(R.id.no_net);
        mNoInternetButton = (Button) findViewById(R.id.no_internet_btn);
        mLoading = (LinearLayout) findViewById(R.id.ll_loading);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent() != null) {
            boolean IsShowShopCart = getIntent().getBooleanExtra(Constant.TO_SHOW_SHOP_CART, false);
            if (IsShowShopCart) {
                showShopCartDialog();
            }
        }
    }

    public void initData() {
        Integer latitude = 0;
        Integer longitude = 0;
        productList = new ArrayList<>();
        foodSpuTagsBeanName = new ArrayList<>();
        poifoodSpusTagsBeans = new ArrayList<>();
        mPoifoodSpusListAdapter = new PoifoodSpusListAdapter(this, productList, foodSpuTagsBeans, getWindow());
        mPoifoodSpusListAdapter.SetOnSetHolderClickListener(new PoifoodSpusListAdapter.HolderClickListener() {
            @Override
            public void onHolderClick(Drawable drawable, int[] start_location) {
                doAnim(drawable, start_location);
            }
        });

        final LinearLayoutManager layoutManagerRight = new LinearLayoutManager(this);
        mSpusList.setLayoutManager(layoutManagerRight);
        mSpusList.setAdapter(mPoifoodSpusListAdapter);
        mPoifoodSpusListAdapter.setCallBackListener(this);

        mFoodSpuTagsListAdapter = new PoifoodSpusTagsAdapter(this, poifoodSpusTagsBeans);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mFoodSpuTagsList.setLayoutManager(mLinearLayoutManager);
        mFoodSpuTagsList.setAdapter(mFoodSpuTagsListAdapter);

        shoppingCartAdapter = new ShoppingCartAdapter(this, productList);

        mSpusList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastItemPosition = 0;
                int firstItemPosition = 0;
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    lastItemPosition = linearManager.findLastVisibleItemPosition();
                    firstItemPosition = linearManager.findFirstVisibleItemPosition();
                }
                mFoodSpuTagsListAdapter.setSelectedPosition(firstItemPosition);
                mFoodSpuTagsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isScroll = true;
                } else {
                    isScroll = false;
                }
            }
        });
        mFoodSpuTagsListAdapter.setOnItemClickListener(new PoifoodSpusTagsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ((LinearLayoutManager) mSpusList.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                mFoodSpuTagsListAdapter.setSelectedPosition(position);
                mFoodSpuTagsListAdapter.notifyDataSetChanged();
            }
        });

        settlement.setOnClickListener(this);
        shopping_cart.setOnClickListener(this);

        mFinish.setOnClickListener(this);
        mStoreDetails.setOnClickListener(this);

        mNoInternetButton.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(Constant.STORE_ID)) {
            mWmPoiId = Long.parseLong(String.valueOf(getIntent().getExtras().get(Constant.STORE_ID)));
        }
        if (extras.containsKey("latitude")) {
            latitude = (Integer) getIntent().getExtras().get("latitude");
            longitude = (Integer) getIntent().getExtras().get("longitude");
        } else {
            AddressListBean.IovBean.DataBean mAddressData = getLocation();
            if (mAddressData != null) {
                latitude = mAddressData.getLatitude() != null ? mAddressData.getLatitude() : -1;
                longitude = mAddressData.getLongitude() != null ? mAddressData.getLongitude() : -1;
            }
        }
        map.put(Constant.STORE_ID, String.valueOf(mWmPoiId));
        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));
        netDataReque();
    }

    private void netDataReque() {
        if (NetUtil.getNetWorkState(this)) {
            mNoNet.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
//            parentLayout.setVisibility(View.VISIBLE);
            getPresenter().requestPoidetailinfo(map);
        } else {
            mNoNet.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
        }
    }

    private AddressListBean.IovBean.DataBean getLocation() {
        AddressListBean.IovBean.DataBean mAddressData = null;
        SharedPreferences sharedPreferences = getSharedPreferences("_cache", Context.MODE_PRIVATE);
        String addressDataJson = sharedPreferences.getString(Constant.ADDRESS_DATA, null);
        if (addressDataJson != null) {
            mAddressData = GsonUtil.fromJson(addressDataJson, AddressListBean.IovBean.DataBean.class);
        }
        return mAddressData;
    }

    @Override
    public void updateProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, String tag, int selection, boolean increase) {
        String spusBeanTag = spusBean.getTag();
        boolean firstAdd = false;
        boolean inList = false;
        Lg.getInstance().d(TAG, "updateProduct tag = " + tag + "; spusBeanTag = " + spusBeanTag);
        if (tag.equals(spusBeanTag)) {
            Lg.getInstance().d(TAG, "productList.contains(spusBean) = " + productList.contains(spusBean));
            if (productList.contains(spusBean)) {
                if (spusBean.getNumber() == 0) {
                    productList.remove(spusBean);
                } else {
                    for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : productList) {
                        Lg.getInstance().d(TAG, "shopProduct.getId() = " + shopProduct.getId() + "; spusBean.getId() = " + spusBean.getId());
                        if (spusBean.getId() == shopProduct.getId()) {
                            Lg.getInstance().d(TAG, "shopProduct.getNumber() = " + shopProduct.getNumber());
                            if (shopProduct.getAttrs() != null && shopProduct.getAttrs().size() > 0) {
                                for (int i = 0; i < shopProduct.getAttrs().size(); i++) {
                                    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs = shopProduct.getAttrs().get(i).getChoiceAttrs();
                                    long id = choiceAttrs.get(0).getId();
                                    if (id == spusBean.getAttrs().get(i).getChoiceAttrs().get(0).getId()) {

                                        if (shopProduct.getSkus() != null && shopProduct.getSkus().size() > 1) {
                                            int skusId = shopProduct.getChoiceSkus().get(0).getId();
                                            if (skusId == spusBean.getChoiceSkus().get(0).getId()) {
                                                shopProduct.setNumber(spusBean.getNumber());
                                                firstAdd = false;
                                                inList = true;
                                                break;
                                            }
                                        } else {
                                            shopProduct.setNumber(spusBean.getNumber());
                                            firstAdd = false;
                                            inList = true;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (inList) {
                                    break;
                                }
                            } else {
                                if (shopProduct.getSkus() != null && shopProduct.getSkus().size() > 1) {
                                    for (int i = 0; i < shopProduct.getSkus().size(); i++) {
                                        int id = shopProduct.getChoiceSkus().get(0).getId();
                                        if (id == spusBean.getChoiceSkus().get(0).getId()) {
                                            int num = spusBean.getNumber();
                                            shopProduct.setNumber(num);
                                            firstAdd = false;
                                            inList = true;
                                            break;
                                        }
                                    }
                                } else {
                                    int num = spusBean.getNumber();
                                    shopProduct.setNumber(num);
                                    firstAdd = false;
                                    inList = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBeanNew = null;
                try {
                    spusBeanNew = (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean) spusBean.deepClone();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Lg.getInstance().d(TAG, "shopProduct else");
                spusBeanNew.setSection(selection);
                productList.add(spusBeanNew);
                inList = false;
                firstAdd = true;
            }
        }
        shoppingCartAdapter.notifyDataSetChanged();
        if (mBottomDialog != null && mBottomDialog.isShowing()) {
            setDialogHeight(mBottomDialog);
        }
        refreshSpusTagNum(selection, increase, spusBean, firstAdd);
        setPrise(increase);
    }

    private void refreshSpusTagNum(int selection, boolean increase,
                                   PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                   boolean firstAdd) {
        for (int i = 0; i < poifoodSpusTagsBeans.size(); i++) {
            if (selection == poifoodSpusTagsBeans.get(i).getIndex()) {
                Integer number = poifoodSpusTagsBeans.get(i).getNumber();
                int minOrderCount = getMinOrderCount(spusBean);
                if (increase) {
                    if (firstAdd && minOrderCount > 1) {
                        number += minOrderCount;
                    } else {
                        number++;
                    }
                } else {
                    if (minOrderCount > 1 && minOrderCount == spusBean.getNumber()) {
                        number -= minOrderCount;
                    } else {
                        number--;
                    }
                }
                poifoodSpusTagsBeans.get(i).setNumber(number);
            }
        }
        mFoodSpuTagsListAdapter.notifyDataSetChanged();
    }

    private int getMinOrderCount(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean) {
        int min_order_count = 1;
        if (spusBean.getSkus() != null) {
            if (spusBean.getSkus().size() > 1 && spusBean.getChoiceSkus() != null) {
                min_order_count = spusBean.getChoiceSkus().get(0).getMin_order_count();
            } else {
                min_order_count = spusBean.getSkus().get(0).getMin_order_count();
            }
        }
        return min_order_count;
    }

    @Override
    public void onUpdateDetailList(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, String tag, int section, boolean increase) {
        String spusBeanTag = spusBean.getTag();
        Lg.getInstance().d(TAG, "onUpdateDetailList tag = " + tag + "; spusBeanTag = " + spusBeanTag);
        if (tag.equals(spusBeanTag)) {
            for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
                spusBeanList = foodSpuTagsBeans.get(i).getSpus();
                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : spusBeanList) {
                    if (spusBean.getId() == shopProduct.getId()) {
                        shopProduct.setNumber(spusBean.getNumber());
                    }
                }
            }
        }
        mPoifoodSpusListAdapter.notifyDataSetChanged();
        refreshSpusTagNum(section, increase, spusBean, false);
        setPrise(increase);
    }

    @Override
    public void onRemovePriduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product) {
        for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
            spusBeanList = foodSpuTagsBeans.get(i).getSpus();
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : spusBeanList) {
                if (product.getId() == shopProduct.getId()) {
                    productList.remove(product);
                    shoppingCartAdapter.notifyDataSetChanged();
                    shopProduct.setNumber(shopProduct.getNumber());
                }
            }
        }
        mPoifoodSpusListAdapter.notifyDataSetChanged();
        shoppingCartAdapter.notifyDataSetChanged();
        if (mBottomDialog != null && mBottomDialog.isShowing()) {
            setDialogHeight(mBottomDialog);
        }
        setPrise(false);
    }

    public void setPrise(boolean increase) {
        double sum = 0;
        int shopNum = 0;
        int restrict = 0;
        double priceRestrict = 0;
        boolean isDiscount = false;
        boolean exceedingTheLimit = false;
        mIsDiscountList = new ArrayList<>();
        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean pro : productList) {
            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = pro.getSkus();
            double price = 0;
            if (skus != null) {
                if (skus.size() == 0) {
                    price = pro.getMin_price();
                } else if (skus.size() == 1) {
                    if (exceedingTheLimit) {
                        price = pro.getSkus().get(0).getOrigin_price();
                    } else {
                        price = pro.getSkus().get(0).getPrice();
                    }
                    if (pro.getSkus().get(0).getPrice() < pro.getSkus().get(0).getOrigin_price()) {
                        isDiscount = true;

                        restrict = pro.getSkus().get(0).getRestrict();

                        if (restrict > 0 && restrict < pro.getNumber() && !exceedingTheLimit) {
                            ToastUtils.show(getApplicationContext(), getString(R.string.limit_buy_toast, "" + restrict,
                                    "" + restrict), Toast.LENGTH_SHORT);
                            exceedingTheLimit = true;
                        } else {
                            exceedingTheLimit = false;
                        }
                        for (int i = 0; i < pro.getNumber(); i++) {
                            mIsDiscountList.add(isDiscount);

                            if (restrict > 0) {
                                if (i < restrict) {
                                    priceRestrict = pro.getSkus().get(0).getPrice();
                                } else {
                                    price = pro.getSkus().get(0).getOrigin_price();
                                }
                            }
                        }
                    }
                } else if (skus.size() > 1) {
                    price = pro.getChoiceSkus().get(0).getPrice();
                    if (pro.getChoiceSkus().get(0).getPrice() < pro.getChoiceSkus().get(0).getOrigin_price()) {
                        isDiscount = true;
                        for (int i = 0; i < pro.getNumber(); i++) {
                            mIsDiscountList.add(isDiscount);
                        }
                    }
                }
            } else {
                price = pro.getMin_price();
            }

            if (exceedingTheLimit) {
                double sumRestrict = DoubleUtil.sum(sum, DoubleUtil.mul((double) restrict, Double.parseDouble("" + priceRestrict)));
                sum = DoubleUtil.sum(sumRestrict, DoubleUtil.mul((double) (pro.getNumber() - restrict), Double.parseDouble("" + price)));
                exceedingTheLimit = false;
            } else {
                sum = DoubleUtil.sum(sum, DoubleUtil.mul((double) pro.getNumber(), Double.parseDouble("" + price)));
            }
            shopNum = shopNum + pro.getNumber();
        }
        if (shopNum > 0) {
            shoppingNum.setVisibility(View.VISIBLE);
            if (mShopCartNum != null) {
                mShopCartNum.setVisibility(View.VISIBLE);
            }
        } else {
            shoppingNum.setVisibility(View.GONE);
            if (mShopCartNum != null) {
                mShopCartNum.setVisibility(View.GONE);
            }
        }
        if (sum > 0) {
            mLlPrice.setVisibility(View.VISIBLE);
            mNoProduct.setVisibility(View.GONE);
            mRlNoProduct.setVisibility(View.GONE);
            shoppingPrise.setVisibility(View.VISIBLE);
            if (mCartShoppingPrise != null) {
                mCartShoppingPrise.setVisibility(View.VISIBLE);
            }
        } else {
            mLlPrice.setVisibility(View.GONE);
            mNoProduct.setVisibility(View.VISIBLE);
            mRlNoProduct.setVisibility(View.VISIBLE);
            shoppingPrise.setVisibility(View.GONE);
            if (mCartShoppingPrise != null) {
                mCartShoppingPrise.setVisibility(View.GONE);
            }
        }
        shoppingNum.setText(String.valueOf(shopNum));
        if (mShopCartNum != null) {
            mShopCartNum.setText(String.valueOf(shopNum));
        }

        if (isDiscount) {
            if (mRlDiscount.getVisibility() == View.VISIBLE) {
                mRlDiscount.setVisibility(View.GONE);
            }
            shoppingPrise.setText("¥" + " " + NumberFormat.getInstance().format(sum));
            if (mCartShoppingPrise != null) {
                mCartShoppingPrise.setText("¥" + " " + NumberFormat.getInstance().format(sum));
            }
            if (increase && mIsDiscountList.size() == 1 && !alreadyToast) {
                ToastUtils.show(getApplicationContext(), getApplicationContext().getResources().getString(R.string.discount_prompt), Toast.LENGTH_SHORT);
                alreadyToast = true;
            }
        } else {
            if (listFull != null && listReduce != null) {
                if (mRlDiscount.getVisibility() == View.GONE) {
                    mRlDiscount.setVisibility(View.VISIBLE);
                }
                if (listFull.size() > 0 && sum > Double.parseDouble(listFull.get(listFull.size() - 1))) {
                    mDiscount.setText(getString(R.string.already_reduced) + listReduce.get(listReduce.size() - 1) + getString(R.string.element));
                    if (mCartDiscount != null) {
                        mCartDiscount.setText(getString(R.string.already_reduced) + listReduce.get(listReduce.size() - 1) + getString(R.string.element));
                    }
                    double v = sum - Double.parseDouble(listReduce.get(listReduce.size() - 1));
                    java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                    String str = NumberFormat.getInstance().format(v);
                    shoppingPrise.setText("¥" + " " + str);
                    if (mCartShoppingPrise != null) {
                        mCartShoppingPrise.setText("¥" + " " + str);
                    }
                    mDiscountNumber = Double.parseDouble(listReduce.get(listReduce.size() - 1));
                } else {
                    for (int i = 0; i < listFull.size(); i++) {
                        Lg.getInstance().d(TAG, "listFull.get(0) = " + listFull.get(0));
                        if (Double.parseDouble(listFull.get(i)) >= sum) {
                            double v = Double.parseDouble(listFull.get(i)) - sum;
                            java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                            String str = NumberFormat.getInstance().format(v);
                            if (i == 0) {
                                mDiscount.setText(getString(R.string.buy_again) + str + getString(R.string.reduce) + listReduce.get(i) + getString(R.string.element));
                                if (mCartDiscount != null) {
                                    mCartDiscount.setText(getString(R.string.buy_again) + str + getString(R.string.reduce) + listReduce.get(i) + getString(R.string.element));
                                }
                                shoppingPrise.setText("¥" + " " + NumberFormat.getInstance().format(sum));
                                if (mCartShoppingPrise != null) {
                                    mCartShoppingPrise.setText("¥" + " " + NumberFormat.getInstance().format(sum));
                                }
                            } else {
                                mDiscount.setText(getString(R.string.already_reduced) + listReduce.get(i - 1) + getString(R.string.element) + "，" +
                                        getString(R.string.buy_again) + str + getString(R.string.reduce) + listReduce.get(i) + getString(R.string.element));
                                if (mCartDiscount != null) {
                                    mCartDiscount.setText(getString(R.string.already_reduced) + listReduce.get(i - 1) + getString(R.string.element) + "，" +
                                            getString(R.string.buy_again) + str + getString(R.string.reduce) + listReduce.get(i) + getString(R.string.element));
                                }
                                double newSum = sum - Double.parseDouble(listReduce.get(i - 1));
                                java.text.DecimalFormat newformat = new java.text.DecimalFormat("0.0");
                                String newNum = NumberFormat.getInstance().format(newSum);
                                shoppingPrise.setText("¥" + " " + newNum);
                                if (mCartShoppingPrise != null) {
                                    mCartShoppingPrise.setText("¥" + " " + newNum);
                                }
                                mDiscountNumber = Double.parseDouble(listReduce.get(i - 1));
                            }
                            break;
                        }
                    }
                }
            } else {
                shoppingPrise.setText("¥" + " " + NumberFormat.getInstance().format(sum));
                if (mCartShoppingPrise != null) {
                    mCartShoppingPrise.setText("¥" + " " + NumberFormat.getInstance().format(sum));
                }
            }
        }

        if (productList == null || productList.size() == 0) {
            mDiscount.setText(mFirstDiscount);
        }
        if (mPoidetailinfoBean != null) {
            double min_price = mPoidetailinfoBean.getMeituan().getData().getMin_price();
            if (sum > 0) {
                if (sum < mPoidetailinfoBean.getMeituan().getData().getMin_price()) {
                    double v = min_price - sum;
                    java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                    String str = myformat.format(v);
                    settlement.setText(String.format(getString(R.string.not_distribution), NumberFormat.getInstance().format(Double.parseDouble(str))));
                    settlement.setBackgroundResource(R.drawable.btn_grey);
                    settlement.setEnabled(false);
                } else {
                    settlement.setText(R.string.confirmation_of_the_order);
                    settlement.setBackgroundResource(R.drawable.btn_blue);
                    settlement.setEnabled(true);
                }
                if (mCartSettlement != null) {
                    if (sum < min_price) {
                        double v = min_price - sum;
                        java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                        String str = myformat.format(v);
                        mCartSettlement.setText(String.format(getString(R.string.not_distribution), NumberFormat.getInstance().format(Double.parseDouble(str))));
                        mCartSettlement.setBackgroundResource(R.drawable.btn_grey);
                        mCartSettlement.setEnabled(false);
                    } else {
                        mCartSettlement.setText(R.string.confirmation_of_the_order);
                        mCartSettlement.setBackgroundResource(R.drawable.btn_blue);
                        mCartSettlement.setEnabled(true);
                    }
                }
            } else {
                settlement.setText(String.format(getString(R.string.can_not_order), NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getMin_price())));
                settlement.setBackgroundResource(R.drawable.btn_grey);
                settlement.setEnabled(false);
            }
        }
    }

    private void showShopCartDialog() {
        mBottomDialog = new Dialog(this, R.style.DialogTheme);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_shop_cart, null);
        mBottomDialog.setContentView(v);
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        v.setLayoutParams(layoutParams);
        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.show();
        setDialogHeight(mBottomDialog);
        cardShopLayout = (LinearLayout) v.findViewById(R.id.cardShopLayout);
        mClearshopCart = (MultiplTextView) v.findViewById(R.id.tv_clear);
        shoppingListView = (ListView) v.findViewById(R.id.shopproductListView);
        mShopCartNum = (TextView) v.findViewById(R.id.shoppingNum);
        mCartDistributionFee = (MultiplTextView) v.findViewById(R.id.distribution_fee);
        mCartShoppingPrise = (MultiplTextView) v.findViewById(R.id.shoppingPrise);
        mCartClose = (MultiplTextView) v.findViewById(R.id.tv_close_cart);
        mCartDiscount = (TextView) v.findViewById(R.id.tv_discount);
        mCartSettlement = (Button) v.findViewById(R.id.settlement);
        mCartSettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomDialog.dismiss();
                if (NetUtil.getNetWorkState(FoodListActivity.this)) {
                    getPresenter().requestOrderPreview(productList, mPoiInfoBean, 0);
                } else {
                    ToastUtils.show(getApplicationContext(), getString(R.string.net_error), Toast.LENGTH_LONG);
                }
            }
        });
        mCartClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomDialog.dismiss();
            }
        });
        shoppingListView.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setShopToDetailListener(this);
        mClearshopCart.setOnClickListener(this);
        if (mPoidetailinfoBean != null) {
            mCartDistributionFee.setText(String.format(getString(R.string.distribution_fee), NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getShipping_fee())));
        }
        setPrise(false);
    }

    private void setDialogHeight(Dialog dialog) {
        WindowManager m = getWindowManager();
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
//        if (productList.size() >= 4) p.height = 840;
//        if (productList.size() < 4) p.height = ListView.LayoutParams.WRAP_CONTENT;
        if (productList.size() == 0) dialog.dismiss();
        dialog.getWindow().setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping_cart:
                if (productList == null || productList.size() == 0) {
                    return;
                }
                showShopCartDialog();
                break;

            case R.id.settlement:
                if (productList == null || productList.size() == 0) {
                    settlement.setEnabled(false);
                    return;
                }
                if (NetUtil.getNetWorkState(this)) {
                    getPresenter().requestOrderPreview(productList, mPoiInfoBean, 0);
                } else {
                    ToastUtils.show(getApplicationContext(), getString(R.string.net_error), Toast.LENGTH_LONG);
                }
                break;

            case R.id.tv_clear:
                if (productList != null && productList.size() > 0) {
                    productList.clear();
                    for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
                        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spus = foodSpuTagsBeans.get(i).getSpus();
                        for (int j = 0; j < spus.size(); j++) {
                            spus.get(j).setNumber(0);
                        }
                    }
                    mPoifoodSpusListAdapter.notifyDataSetChanged();
                    shoppingCartAdapter.notifyDataSetChanged();
                    if (mBottomDialog != null && mBottomDialog.isShowing()) {
                        setDialogHeight(mBottomDialog);
                    }
                    for (int i = 0; i < poifoodSpusTagsBeans.size(); i++) {
                        poifoodSpusTagsBeans.get(i).setNumber(number);
                    }
                    mFoodSpuTagsListAdapter.notifyDataSetChanged();
                    setPrise(false);
                }
                if (mBottomDialog != null && mBottomDialog.isShowing()) {
                    mBottomDialog.dismiss();
                }
                break;
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rl_store_details:
                View popView = mPoifoodSpusListAdapter.getPopView(R.layout.dialog_shop_details);
                mDetailsNotice = (TextView) popView.findViewById(R.id.tv_notice);
                mDetailsShopName = (MultiplTextView) popView.findViewById(R.id.tv_shop_name);
                mDetailsDistribution = (MultiplTextView) popView.findViewById(R.id.tv_details_distribution);
                mDetailsDiscount = (RecyclerView) popView.findViewById(R.id.tv_discount);
                StringBuffer stringBuffer = new StringBuffer();
                if (mPoidetailinfoBean != null) {
                    List<PoidetailinfoBean.MeituanBean.DataBean.DiscountsBean> discounts = mPoidetailinfoBean.getMeituan().getData().getDiscounts();
                    List<String> discountList = getDiscountList(discounts, true);
                    if (mDetailsDiscount.getItemDecorationCount() == 0) {
                        mDetailsDiscount.addItemDecoration(new SpaceItemDecoration(dp2px(3)));
                    }
                    final FlowLayoutManager layoutManager = new FlowLayoutManager();
                    mDetailsDiscount.setLayoutManager(layoutManager);
                    DiscountAdaper discountAdaper = new DiscountAdaper(this, discountList);
                    mDetailsDiscount.setAdapter(discountAdaper);
                    mDetailsShopName.setText(mPoidetailinfoBean.getMeituan().getData().getName());
                    mDetailsDistribution.setText(getString(R.string.distribution_situation, NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getMin_price()),
                            NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getShipping_fee()), NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getAvg_delivery_time())));
                }
                if (mPoiInfoBean != null) {
                    mDetailsNotice.setText(getString(R.string.notice, mPoiInfoBean.getBulletin()));
                }

                final PopupWindow window = new PopupWindow(popView,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, true);
                mPoifoodSpusListAdapter.showFoodListActivityDialog(v, popView, window);
                break;
            case R.id.no_internet_btn:
                netDataReque();
                break;
        }
    }

    private FrameLayout createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;

    }

    private void doAnim(Drawable drawable, int[] start_location) {
        if (!isClean) {
            setAnim(drawable, start_location);
        } else {
            try {
                animation_viewGroup.removeAllViews();
                isClean = false;
                setAnim(drawable, start_location);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isClean = true;
            }
        }
    }

    @SuppressLint("NewApi")
    private void setAnim(Drawable drawable, int[] start_location) {
        Animation mScaleAnimation = new ScaleAnimation(1.2f, 0.6f, 1.2f, 0.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mScaleAnimation.setFillAfter(true);

        final ImageView iview = new ImageView(this);
        iview.setImageDrawable(drawable);
        final View view = addViewToAnimLayout(animation_viewGroup, iview,
                start_location);

        view.setAlpha(0.6f);

        int[] end_location = new int[2];
        settlement.getLocationInWindow(end_location);

        int endX = 0 - start_location[0] + 40;
        int endY = end_location[1] - start_location[1];
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);


        Animation mRotateAnimation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF,
                0.3f);
        mRotateAnimation.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(mRotateAnimation);
        set.addAnimation(mScaleAnimation);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);
        view.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                number++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                number--;
                if (number == 0) {
                    isClean = true;
                    myHandler.sendEmptyMessage(0);
                }
                ObjectAnimator.ofFloat(shopping_cart, "translationY", 0, 4, -2, 0).setDuration(400).start();
                ObjectAnimator.ofFloat(shoppingNum, "translationY", 0, 4, -2, 0).setDuration(400).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private View addViewToAnimLayout(ViewGroup vg, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public void onLowMemory() {
        isClean = true;
        try {
            animation_viewGroup.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isClean = false;
        super.onLowMemory();
    }

    @Override
    public void onOrderPreviewSuccess(OrderPreviewBean data) {
        switch (data.getMeituan().getData().getCode()) {
            case Constant.SUBMIT_ORDER_SUCCESS:
                Intent intent = new Intent(this, SubmitOrderActivity.class);
                intent.putExtra(POI_INFO, mPoiInfoBean);
                intent.putExtra(PRODUCT_LIST_BEAN, (Serializable) productList);
                intent.putExtra(DISCOUNT, mDiscountNumber);
                intent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
                startActivity(intent);
                break;
            /*case Constant.SUBMIT_ORDER_FAIL:
                ToastUtils.show(getApplicationContext(), data.getMeituan().getMsg(), Toast.LENGTH_SHORT);
                break;
            case Constant.STORE_CANT_NOT_BUY:
                ToastUtils.show(getApplicationContext(), getApplicationContext().getResources().getString(R.string.order_preview_msg2), Toast.LENGTH_SHORT);
                break;

            case Constant.FOOD_CANT_NOT_BUY:
                ToastUtils.show(getApplicationContext(), getApplicationContext().getResources().getString(R.string.order_preview_msg3), Toast.LENGTH_SHORT);
                break;
            case Constant.FOOD_COST_NOT_BUY:
                ToastUtils.show(getApplicationContext(), getApplicationContext().getResources().getString(R.string.order_preview_msg5), Toast.LENGTH_SHORT);
                break;
            case Constant.FOOD_COUNT_NOT_BUY:
                ToastUtils.show(getApplicationContext(), getApplicationContext().getResources().getString(R.string.order_preview_msg15), Toast.LENGTH_SHORT);
                break;

            case Constant.FOOD_LACK_NOT_BUY:
                ToastUtils.show(getApplicationContext(), data.getMeituan().getMsg(), Toast.LENGTH_SHORT);
                break;
            case Constant.SERVICE_ERROR:
                ToastUtils.show(getApplicationContext(), getApplicationContext().getResources().getString(R.string.service_error), Toast.LENGTH_SHORT);
                break;*/
            default:
                ToastUtils.show(getApplicationContext(), data.getMeituan().getData().getMsg(), Toast.LENGTH_SHORT);
                break;
        }
    }

    @Override
    public void onArriveTimeSuccess(ArriveTimeBean data) {
        String view_time = data.getMeituan().getData().get(0).getTimelist().get(1).getView_time();
        ToastUtils.show(getApplicationContext(), String.format(getString(R.string.first_arrive_time), view_time), Toast.LENGTH_SHORT);
    }

    @Override
    public void onOrderPreviewFailure(String msg) {
        Lg.getInstance().d(TAG, "msg = " + msg);
    }

    @Override
    public void onPoifoodListSuccess(PoifoodListBean data) {
        foodSpuTagsBeans.clear();
        foodSpuTagsBeanName.clear();
        mPoiInfoBean = data.getMeituan().getData().getPoi_info();
        mShopTitle.setText(mPoiInfoBean.getName());
        mDelivery.setText(getString(R.string.distribution_situation, NumberFormat.getInstance().format(mPoiInfoBean.getMin_price()),
                NumberFormat.getInstance().format(mPoiInfoBean.getShipping_fee()), NumberFormat.getInstance().format(mPoiInfoBean.getAvg_delivery_time())));
        mBulletin.setText(getString(R.string.notice, mPoiInfoBean.getBulletin()));
        GlideApp.with(this)
                .load(mPoiInfoBean.getPic_url())
                .into(mShopPicture);
        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> food_spu_tags = data.getMeituan().getData().getFood_spu_tags();
        for (int i = 0; i < food_spu_tags.size(); i++) {
            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean foodSpuTagsBean = food_spu_tags.get(i);
            String foodSpuTagsName = foodSpuTagsBean.getName();
            PoifoodSpusTagsBean poifoodSpusTagsBean = new PoifoodSpusTagsBean();
            poifoodSpusTagsBean.setFoodSpuTagsBeanName(foodSpuTagsName);
            poifoodSpusTagsBean.setIndex(i);
            poifoodSpusTagsBean.setNumber(0);
            poifoodSpusTagsBeans.add(poifoodSpusTagsBean);
            foodSpuTagsBeanName.add(foodSpuTagsName);
            Lg.getInstance().d(TAG, "foodSpuTagsBeanName = " + foodSpuTagsName);
            spusBeanList = new ArrayList<>();
            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spus = foodSpuTagsBean.getSpus();
            for (int j = 0; j < spus.size(); j++) {
                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean = spus.get(j);
                String spusName = spusBean.getName();
                spusBeanList.add(spusBean);
                Lg.getInstance().d(TAG, "spusBeanName = " + spusName);
            }
            Lg.getInstance().d(TAG, "spusBeanList = " + spusBeanList.size());
            foodSpuTagsBean.setSpus(spusBeanList);
            foodSpuTagsBeans.add(foodSpuTagsBean);
            oneMoreOrder(i);
        }
        Lg.getInstance().d(TAG, "foodSpuTagsBeanName = " + foodSpuTagsBeanName.toString());
        Lg.getInstance().d(TAG, "spusBeanName = " + spusBeanList.toString());
        if (mOneMoreOrder) {
            showShopCartDialog();
        }
        mPoifoodSpusListAdapter.notifyDataSetChanged();
        mFoodSpuTagsListAdapter.notifyDataSetChanged();

        boolean isNeedVoiceFeedback = getIntent().getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false);
        if (isNeedVoiceFeedback) {
            if (mOneMoreOrder) {
                VoiceManager.getInstance().playTTS(FoodListActivity.this, String.format(getString(R.string.sure_order), mPoiInfoBean.getName()));
            } else {
                VoiceManager.getInstance().playTTS(FoodListActivity.this, getString(R.string.choose_you_commodity));
            }
        }

        mLoading.setVisibility(View.GONE);
        mNoNet.setVisibility(View.GONE);
        parentLayout.setVisibility(View.VISIBLE);

        int status = mPoiInfoBean.getStatus();
        if (status == 1) {
            getPresenter().requestArriveTimeData(mWmPoiId);
        }
    }

    private void oneMoreOrder(int section) {
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(Constant.ONE_MORE_ORDER)) {
            mOneMoreOrder = (boolean) getIntent().getExtras().get(Constant.ONE_MORE_ORDER);
            if (mOneMoreOrder) {
                if (extras.containsKey(Constant.ORDER_LSIT_EXTRA_STRING)) {
                    String orderListExtraPayLoadStr = (String) getIntent().getExtras().get(Constant.ORDER_LSIT_EXTRA_STRING);
                    String payload;
                    OrderListExtraPayloadBean payloadBean = null;
                    OrderListExtraBean.OrderInfos orderInfos = null;
                    OrderListExtraBean extraBean = GsonUtil.fromJson(orderListExtraPayLoadStr, OrderListExtraBean.class);
                    try {
                        payload = Encryption.desEncrypt(extraBean.getPayload());
                        orderInfos = extraBean.getOrderInfos();
                        payloadBean = GsonUtil.fromJson(payload, OrderListExtraPayloadBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    List<OrderListExtraBean.OrderInfos.Food_list> spusFoodList = orderInfos.getFood_list();
                    for (OrderListExtraBean.OrderInfos.Food_list spusFood : spusFoodList) {
                        String name = spusFood.getName();
                        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean : spusBeanList) {
                            if (spusBean.getName().equals(name)) {
                                spusBean.setNumber(spusFood.getCount());
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrs = spusBean.getAttrs();
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = spusBean.getSkus();
                                List<String> attrIds = spusFood.getAttrIds();
                                List<String> attrValues = spusFood.getAttrValues();
                                String spec = spusFood.getSpec();
                                SetAttrsAndSkus(spusBean, attrs, skus, attrIds, attrValues, spec);
                                int number = spusBean.getNumber();
                                int minOrderCount = getMinOrderCount(spusBean);
                                if (number > 1) {
                                    if (minOrderCount > 1) {
                                        spusBean.setNumber(minOrderCount);
                                        updateProduct(spusBean, spusBean.getTag(), section, true);
                                        for (int i = minOrderCount; i <= number; i++) {
                                            spusBean.setNumber(i + 1);
                                            updateProduct(spusBean, spusBean.getTag(), section, true);
                                        }
                                    } else {
                                        for (int i = minOrderCount; i <= number; i++) {
                                            spusBean.setNumber(i);
                                            updateProduct(spusBean, spusBean.getTag(), section, true);
                                        }
                                    }
                                } else {
                                    updateProduct(spusBean, spusBean.getTag(), section, true);
                                }
                            }
                        }
                    }
                } else {
                    OrderDetailsResponse.MeituanBean.DataBean orderLsitBean = (OrderDetailsResponse.MeituanBean.DataBean) getIntent().getExtras().getSerializable(Constant.ORDER_LSIT_BEAN);
                    List<OrderDetailsResponse.MeituanBean.DataBean.FoodListBean> food_list = orderLsitBean.getFood_list();
                    for (OrderDetailsResponse.MeituanBean.DataBean.FoodListBean spusFood : food_list) {
                        String name = spusFood.getName();
                        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean : spusBeanList) {
                            if (spusBean.getName().equals(name)) {
                                spusBean.setNumber(spusFood.getCount());
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrs = spusBean.getAttrs();
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = spusBean.getSkus();
                                List<String> attrIds = spusFood.getAttrIds();
                                List<String> attrValues = spusFood.getAttrValues();
                                String spec = spusFood.getSpec();
                                SetAttrsAndSkus(spusBean, attrs, skus, attrIds, attrValues, spec);
                                int number = spusBean.getNumber();
                                int minOrderCount = getMinOrderCount(spusBean);
                                if (number > 1) {
                                    if (minOrderCount > 1) {
                                        spusBean.setNumber(minOrderCount);
                                        updateProduct(spusBean, spusBean.getTag(), section, true);
                                        for (int i = minOrderCount; i <= number; i++) {
                                            spusBean.setNumber(i + 1);
                                            updateProduct(spusBean, spusBean.getTag(), section, true);
                                        }
                                    } else {
                                        for (int i = minOrderCount; i <= number; i++) {
                                            spusBean.setNumber(i);
                                            updateProduct(spusBean, spusBean.getTag(), section, true);
                                        }
                                    }
                                } else {
                                    updateProduct(spusBean, spusBean.getTag(), section, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void SetAttrsAndSkus(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                 List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrs,
                                 List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus,
                                 List<String> attrIds, List<String> attrValues, String spec) {
        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusList;
        for (int i = 0; i < attrIds.size(); i++) {
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean attrsBean : attrs) {
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrsList = attrsBean.getChoiceAttrs();
                choiceAttrsList.get(0).setId(Long.parseLong(attrIds.get(i)));
                choiceAttrsList.get(0).setValue(String.valueOf(attrValues.get(i)));
            }
        }
        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean skusBean : skus) {
            if (!TextUtils.isEmpty(spec) && spec.equals(skusBean.getSpec())) {
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkusList = spusBean.getChoiceSkus();
                if (choiceSkusList == null) {
                    skusList = new ArrayList<>();
                    skusList.add(skusBean);
                    spusBean.setChoiceSkus(skusList);
                } else {
                    choiceSkusList.clear();
                    choiceSkusList.add(skusBean);
                }
            }
        }
    }

    @Override
    public void onPoifoodListError(String error) {
        Lg.getInstance().d(TAG, "error = " + error);
        mLoading.setVisibility(View.GONE);
        mNoNet.setVisibility(View.VISIBLE);
        parentLayout.setVisibility(View.GONE);
    }

    @Override
    public void onPoidetailinfoSuccess(PoidetailinfoBean data) {
        mPoidetailinfoBean = data;
        if (mPoidetailinfoBean != null) {
            List<PoidetailinfoBean.MeituanBean.DataBean.DiscountsBean> discounts = mPoidetailinfoBean.getMeituan().getData().getDiscounts();
            List<String> discountList = getDiscountList(discounts, false);
            if (mDiscounts.getItemDecorationCount() == 0) {
                mDiscounts.addItemDecoration(new SpaceItemDecoration(dp2px(3)));
            }
            final FlowLayoutManager layoutManager = new FlowLayoutManager();
            mDiscounts.setLayoutManager(layoutManager);
            DiscountAdaper discountAdaper = new DiscountAdaper(this, discountList);
            mDiscounts.setAdapter(discountAdaper);
            settlement.setText(String.format(getString(R.string.can_not_order), NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getMin_price())));
            mDistributionFee.setText(String.format(getString(R.string.distribution_fee), NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getShipping_fee())));
            mMtDistributionFee.setText(String.format(getString(R.string.distribution_fee), NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getShipping_fee())));
            if (discounts.size() == 0) {
                mRlDiscount.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < discounts.size(); i++) {
                    String info = discounts.get(i).getInfo();
                    if (info.contains(getString(R.string.full)) && info.contains(getString(R.string.reduce))) {
                        mRlDiscount.setVisibility(View.VISIBLE);
                        if (info.startsWith(getString(R.string.full))) {
                            String[] split = info.split(";");
                            listFull = new ArrayList();
                            listReduce = new ArrayList();
                            Lg.getInstance().d(TAG, "split.length = " + split.length);
                            for (int j = 0; j < split.length; j++) {
                                String splitString = split[j];
                                int y = 0;
                                for (int k = 0; k < splitString.length(); k++) {
                                    String s = splitString.charAt(k) + "";
                                    if (s.equals(getString(R.string.reduce))) {
                                        y = k;
                                    }
                                }
                                String substring = splitString.substring(1, y);
                                Lg.getInstance().d(TAG, "substring = " + substring);
                                String lastString = splitString.substring(y + 1, splitString.length());
                                Lg.getInstance().d(TAG, "lastString = " + lastString);
                                listFull.add(NumberFormat.getInstance().format(Double.parseDouble(substring)));
                                listReduce.add(NumberFormat.getInstance().format(Double.parseDouble(lastString)));
                                mFirstDiscount = getString(R.string.full) + listFull.get(0) + getString(R.string.element) + getString(R.string.reduce)
                                        + listReduce.get(0) + getString(R.string.element);
                                mDiscount.setText(mFirstDiscount);
                            }
                        }
                    }
                }
            }
        }

        getPresenter().requestPoifoodList(map);
        Lg.getInstance().d(TAG, "onPoidetailinfoSuccess data = " + data);
    }

    @Override
    public void onPoidetailinfoError(String error) {
        Lg.getInstance().d(TAG, "onPoidetailinfoError = " + error);
    }

    @Override
    public void onArriveTimeError(String error) {
    }

    @Override
    public void sureOrder() {
        if (null != mBottomDialog && mBottomDialog.isShowing() && null != mCartSettlement) {
            isNeedVoice = true;
            mCartSettlement.performClick();
            return;
        }

        if (null == mBottomDialog || (null != mBottomDialog && !mBottomDialog.isShowing() && null != settlement)) {
            isNeedVoice = true;
            settlement.performClick();
        }

    }

    @Override
    public void selectListItem(int i) {
        int flag = i + 1;
        int section = 0;
        int position = 0;
        boolean ok = true;
        while (ok) {
            int oldFlag = flag;
            flag -= foodSpuTagsBeans.get(section).getSpus().size();
            if (flag <= 0 && section == 0) {
                position = i;
                ok = false;
                continue;
            }
            if (flag <= 0) {
                position = oldFlag - 1;
                ok = false;
                continue;
            }
            section++;
        }

        LinearLayoutManager manager = (LinearLayoutManager) mSpusList.getLayoutManager();
        if (null != manager) {
            int firstItemPosition = manager.findFirstVisibleItemPosition();
            int lastItemPosition = manager.findLastVisibleItemPosition();

            if (firstItemPosition <= section && lastItemPosition >= section) {
                View view = mSpusList.getChildAt(section - firstItemPosition);
                if (null != mSpusList.getChildViewHolder(view)) {
                    PoifoodSpusListAdapter.MyViewHolder viewHolder = (PoifoodSpusListAdapter.MyViewHolder) mSpusList.getChildViewHolder(view);
                    RecyclerView recyclerView = viewHolder.getRecyclerView();
                    LinearLayoutManager m = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (null != m) {
                        int f = m.findFirstVisibleItemPosition();
                        int l = m.findLastVisibleItemPosition();
                        if (f <= position && l >= position) {
                            View v = recyclerView.getChildAt(position - f);
                            if (null != recyclerView.getChildViewHolder(v)) {
                                PoifoodSpusListAdapter.MyViewHolder.GridViewAdapter.ViewHolder holder = (PoifoodSpusListAdapter.MyViewHolder.GridViewAdapter.ViewHolder) recyclerView.getChildViewHolder(v);
                                holder.autoClick(position);
                            }
                        }
                    }
                }
            }
        }
    }

    private List<String> getDiscountList(List<PoidetailinfoBean.MeituanBean.DataBean.DiscountsBean> discounts, boolean allShow) {
        List<String> list = new ArrayList<>();
        List<String> discountList = new ArrayList<>();
        for (PoidetailinfoBean.MeituanBean.DataBean.DiscountsBean bean : discounts) {
            String[] name = bean.getInfo().split(";");
            list.addAll(Arrays.asList(name));
        }
        if (list.size() > 3 && !allShow) {
            discountList.add(list.get(0));
            discountList.add(list.get(1));
            discountList.add(list.get(2));
            return discountList;
        } else {
            return list;
        }
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull
                RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.top = 0;
            outRect.left = 0;
            outRect.right = space;
            outRect.bottom = space;
        }
    }

}
