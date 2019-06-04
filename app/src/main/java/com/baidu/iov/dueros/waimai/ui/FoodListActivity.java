package com.baidu.iov.dueros.waimai.ui;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
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
import com.baidu.iov.dueros.waimai.net.entity.response.FoodDetailBean;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListExtraBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderOwnerBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoidetailinfoBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.presenter.PoifoodListPresenter;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.DoubleUtil;
import com.baidu.iov.dueros.waimai.utils.GlideApp;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.iov.dueros.waimai.view.ConstraintHeightListView;
import com.baidu.iov.dueros.waimai.view.FlowLayoutManager;
import com.baidu.iov.faceos.client.GsonUtil;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;
import com.domain.multipltextview.MultiplTextView;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

public class FoodListActivity extends BaseActivity<PoifoodListPresenter, PoifoodListPresenter.PoifoodListUi> implements PoifoodListPresenter.PoifoodListUi, View.OnClickListener, PoifoodSpusListAdapter.onCallBackListener, IShoppingCartToDetailListener {
    private static final String TAG = FoodListActivity.class.getSimpleName();
    public static final String POI_INFO = "poi_info";
    public static final String PRODUCT_LIST_BEAN = "product_list_bean";
    public static final String DISCOUNT = "discount";

    public static Map<Integer, PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> selectFoods = new HashMap<>();

    private RecyclerView mFoodSpuTagsList;
    private RecyclerView mSpusList;
    private PoifoodSpusListAdapter mPoifoodSpusListAdapter;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList;
    private MultiplTextView shoppingPrise;
    private TextView shoppingNum;
    private Button settlement;
    private ImageView shopping_cart;
    private int number = 0;
    private boolean isClean = false;
    private FrameLayout animation_viewGroup;
    private int status = 1;
    //商品分类信息
    private List<PoifoodSpusTagsBean> poifoodSpusTagsBeans;
    private RelativeLayout parentLayout;
    //商铺整体数据(分类>分类中的商品)
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans = new ArrayList<>();
    //某分类下的商品
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeanList;
    private ListView shoppingListView;
    private ShoppingCartAdapter shoppingCartAdapter;
    private ArrayMap<String, String> map;
    private PoifoodSpusTagsAdapter mFoodSpuTagsListAdapter;
    private RelativeLayout mStoreDetails;
    private MultiplTextView mClearshopCart;
    private PoidetailinfoBean mPoidetailinfoBean;
    private PoifoodListBean.MeituanBean.DataBean.PoiInfoBean mPoiInfoBean;
    private ImageView mFinish;
    private TextView mShopTitle;
    private MultiplTextView mDelivery;
    private TextView mBulletin;
    private RecyclerView mDiscounts;
    private ImageView mShopPicture;
    private List<String> listFull;
    private List<String> listReduce;
    private TextView mDiscount;
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
    private RelativeLayout mCartDiscountLayout;
    private MultiplTextView mCartClose;
    private MultiplTextView mDistributionFee;
    private RelativeLayout mRlDiscount;
    private MultiplTextView mNoProduct;
    private RelativeLayout mLlPrice;
    //分类的名称列表
    private List<String> foodSpuTagsBeanName;
    private String mFirstDiscount;
    public static boolean mOneMoreOrder;
    private MultiplTextView mMtDistributionFee;
    private RelativeLayout mRlNoProduct;
    private List<Boolean> mIsDiscountList;
    private boolean alreadyToast;
    private boolean isNeedVoice;
    private String mWmPoiId;
    private LinearLayout mNoNet;
    private Button mNoInternetButton;
    private LinearLayout mLoading;
    private RelativeLayout mShopCartPic;
    private LinearLayout shoppingNumLayout;
    private InnerRecevier mInnerReceiver;
    private TextView mTextOpenCart;
    private boolean mTagsListOnclick;
    private int mTagsOnclicPosition;
    private boolean booleanExtra;
    private List<String> tags = new ArrayList<>();
    private Map<Integer, String> cache = new HashMap<>();

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
        booleanExtra = getIntent().getBooleanExtra("flag", false);
        initView();
        if (booleanExtra) {
            initData();
        } else {
            //bduss为空
            if (TextUtils.isEmpty(CacheUtils.getBduss())) {
                Intent intent = new Intent(mContext, TakeawayLoginActivity.class);
                intent.putExtra(Constant.ORDER_LSIT_EXTRA_STRING, getIntent().getStringExtra(Constant.ORDER_LSIT_EXTRA_STRING));
                intent.putExtra(Constant.STORE_ID, getIntent().getStringExtra(Constant.STORE_ID));
                startActivity(intent);
                finish();
            }else{
                initData();
            }
//            OrderDetailsResponse.MeituanBean.DataBean orderLsitBean = (OrderDetailsResponse.MeituanBean.DataBean) getIntent().getSerializableExtra(Constant.ORDER_LSIT_BEAN);
//            String json =  getIntent().getStringExtra(Constant.ORDER_LSIT_EXTRA_STRING);
//            if (!TextUtils.isEmpty(json)) {
//                mLoading.setVisibility(View.VISIBLE);
//                getPresenter().requestCheckOrderOwner(orderLsitBean.getOrder_id());
//            }else{
//                mLoading.setVisibility(View.GONE);
//            }
        }
        //创建广播
        mInnerReceiver = new InnerRecevier();
        //动态注册广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //启动广播
        registerReceiver(mInnerReceiver, intentFilter);
    }

    class InnerRecevier extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        finish();
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isNeedVoice = false;
        Constant.ANIMATION_END = true;
        mmHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOneMoreOrder = false;
        tags = null;
        cache = null;
        unregisterReceiver(mInnerReceiver);
        selectFoods.clear();
        //失去焦点时，需要隐藏购物车
        if (mBottomDialog != null) {
            mBottomDialog.dismiss();
            mBottomDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> prefix = new ArrayList<>();
        prefix.add(getString(R.string.open));
        AccessibilityClient.getInstance().register(this,
                true, prefix, null);
        GuidingAppear.INSTANCE.showtTips(FoodListActivity.this, WaiMaiApplication.getInstance().getWaimaiBean().getTakeout_shopping().getHints(), Constant.TTS_CART_SHOP_DETAIL);
        Constant.ANIMATION_END = true;
    }

    private void initView() {
        animation_viewGroup = getPresenter().createAnimLayout(this);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        shoppingPrise = (MultiplTextView) findViewById(R.id.shoppingPrise);
        shoppingNum = (TextView) findViewById(R.id.shoppingNum);
        shoppingNumLayout = (LinearLayout) findViewById(R.id.ll_shoppingNum_layout);
        settlement = (Button) findViewById(R.id.settlement);
        mFoodSpuTagsList = (RecyclerView) findViewById(R.id.classify_mainlist);
        mSpusList = (RecyclerView) findViewById(R.id.classify_morelist);
        shopping_cart = (ImageView) findViewById(R.id.shopping_cart);
        mStoreDetails = (RelativeLayout) findViewById(R.id.rl_store_details);
        mFinish = (ImageView) findViewById(R.id.iv_finish);
        mShopTitle = (TextView) findViewById(R.id.tv_shop_title);
        mDelivery = (MultiplTextView) findViewById(R.id.tv_delivery);
        mBulletin = (TextView) findViewById(R.id.tv_bulletin);
        mDiscounts = (RecyclerView) findViewById(R.id.tv_discounts);
        mShopPicture = (ImageView) findViewById(R.id.iv_shop);
        mDiscount = (TextView) findViewById(R.id.tv_discount);
        mDistributionFee = (MultiplTextView) findViewById(R.id.tv_distribution_fee);
        mRlDiscount = (RelativeLayout) findViewById(R.id.rl_discount);
        mNoProduct = (MultiplTextView) findViewById(R.id.tv_no_product);
        mLlPrice = (RelativeLayout) findViewById(R.id.ll_price);
        mMtDistributionFee = (MultiplTextView) findViewById(R.id.mt_distribution_fee);
        mRlNoProduct = (RelativeLayout) findViewById(R.id.rl_no_product);
        mNoNet = (LinearLayout) findViewById(R.id.no_net);
        mNoInternetButton = (Button) findViewById(R.id.no_internet_btn);
        mLoading = (LinearLayout) findViewById(R.id.ll_loading);
        mShopCartPic = (RelativeLayout) findViewById(R.id.rl_shop_cart);
        mTextOpenCart = (TextView) findViewById(R.id.tv_open_shop_cart);
        mFinish.setOnClickListener(this);
        shopping_cart.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                openShopCart();
                StandardCmdClient.getInstance().playTTS(FoodListActivity.this, "BUBBLE");
                return true;
            }
        });
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

    private Handler mmHandler = new Handler();

    public void initData() {
        Integer latitude = 0;
        Integer longitude = 0;
        productList = new ArrayList<>();
        foodSpuTagsBeanName = new ArrayList<>();
        poifoodSpusTagsBeans = new ArrayList<>();
        mPoifoodSpusListAdapter = new PoifoodSpusListAdapter(this, productList, foodSpuTagsBeans, getWindow());
        mPoifoodSpusListAdapter.SetOnSetHolderClickListener(new PoifoodSpusListAdapter.HolderClickListener() {
            @Override
            public void onHolderClick(final Drawable drawable, final int[] start_location) {
                // 数据量较大时，由于遍历导致主线程阻塞
                mmHandler.removeCallbacksAndMessages(null);
                mmHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doAnim(drawable, start_location);
                    }
                }, 50);
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
                int firstItemPosition = 0;
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    firstItemPosition = linearManager.findFirstVisibleItemPosition();
                }
                if (mTagsListOnclick && mTagsOnclicPosition != firstItemPosition) {
                    mFoodSpuTagsListAdapter.setSelectedPosition(mTagsOnclicPosition);
                    ((LinearLayoutManager) mFoodSpuTagsList.getLayoutManager()).scrollToPositionWithOffset(mTagsOnclicPosition, 0);
                    mTagsListOnclick = false;
                } else {
                    if (mFoodSpuTagsListAdapter.getSelectedPosition() != firstItemPosition) {
                        refreshData();
                    }
                    mFoodSpuTagsListAdapter.setSelectedPosition(firstItemPosition);
                    ((LinearLayoutManager) mFoodSpuTagsList.getLayoutManager()).scrollToPositionWithOffset(firstItemPosition, 0);
                }
                mFoodSpuTagsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        mFoodSpuTagsListAdapter.setOnItemClickListener(new PoifoodSpusTagsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                mTagsListOnclick = true;
                mTagsOnclicPosition = position;
                Entry.getInstance().onEvent(Constant.POIFOODLIST_CLICK_ON_THE_TYPE_OF_GOODS, EventType.TOUCH_TYPE);
                ((LinearLayoutManager) mSpusList.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                mFoodSpuTagsListAdapter.setSelectedPosition(position);
                mFoodSpuTagsListAdapter.notifyDataSetChanged();
                refreshData();
            }
        });

        settlement.setOnClickListener(this);
        settlement.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                settlement.performClick();
                isNeedVoice = true;
                return true;
            }
        });
        shopping_cart.setOnClickListener(this);
        mTextOpenCart.setOnClickListener(this);

        mStoreDetails.setOnClickListener(this);

        mNoInternetButton.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(Constant.STORE_ID)) {
            mWmPoiId = String.valueOf(getIntent().getExtras().get(Constant.STORE_ID));
        }
        if (extras.containsKey("latitude")) {
            latitude = (Integer) getIntent().getExtras().get("latitude");
            longitude = (Integer) getIntent().getExtras().get("longitude");
        } else {
            AddressListBean.IovBean.DataBean mAddressData = getPresenter().getLocation(mContext);
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
            getPresenter().requestPoidetailinfo(map);
        } else {
            mNoNet.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
            mShopCartPic.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, String tag, boolean increase, boolean refreshList) {
        String spusBeanTag = spusBean.getTag();
        boolean firstAdd = false;
        FoodListActivity.this.refreshList = refreshList;
        Lg.getInstance().d(TAG, "updateProduct tag = " + tag + "; spusBeanTag = " + spusBeanTag);
        if (tag.equals(spusBeanTag)) {
            Lg.getInstance().d(TAG, "productList.contains(spusBean) = " + productList.contains(spusBean));
            if (productList.contains(spusBean)) {
                getPresenter().setProduct(spusBean, productList, mOneMoreOrder, increase);
                firstAdd = false;
            } else {
                //判断再来一单 是否有同一商品
                getPresenter().judgeTheSameThing(spusBean, productList, mOneMoreOrder);
                if (spusBean.getNumber() > 0) {
                    PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBeanNew = null;
                    try {
                        spusBeanNew = (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean) spusBean.deepClone();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Lg.getInstance().d(TAG, "shopProduct else");
                    productList.add(spusBeanNew);
                    firstAdd = true;
                    alreadyToast = false;
                }
            }
        }
        shoppingCartAdapter.notifyDataSetChanged();
        if (mBottomDialog != null && mBottomDialog.isShowing()) {
            setDialogHeight(mBottomDialog);
        }
        tags.clear();
        getPresenter().refreshSpusTagNum(spusBean, poifoodSpusTagsBeans, tags, mFoodSpuTagsListAdapter, increase, firstAdd, false);
        setPrise(increase);
    }

    public void updateoneMoreOrder(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, String tag,
                                   List<String> spusFoodAttrIds, int spusFoodCount, boolean increase, boolean refreshList, String groupTag) {
        String spusBeanTag = spusBean.getTag();
        FoodListActivity.this.refreshList = refreshList;
        Lg.getInstance().d(TAG, "updateProduct tag = " + tag + "; spusBeanTag = " + spusBeanTag);
        if (tag.equals(spusBeanTag)) {
            Lg.getInstance().d(TAG, "productList.contains(spusBean) = " + productList.contains(spusBean));
            if (productList.contains(spusBean)) {
                getPresenter().setOneMoreOrderProduct(spusBean, productList, cache, groupTag, mOneMoreOrder, increase);
            } else {
                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBeanNew = null;
                try {
                    spusBeanNew = (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean) spusBean.deepClone();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Lg.getInstance().d(TAG, "shopProduct else");
                if (spusFoodAttrIds != null && spusFoodAttrIds.size() > 0) {
                    spusBeanNew.setSpecificationsNumber(spusFoodCount);
                    spusBeanNew.setNumber(spusFoodCount);
                }
                cache.put(spusBeanNew.getId(), groupTag);
                productList.add(spusBeanNew);
                alreadyToast = false;
            }
        }
        shoppingCartAdapter.notifyDataSetChanged();
        if (mBottomDialog != null && mBottomDialog.isShowing()) {
            setDialogHeight(mBottomDialog);
        }
        getPresenter().refreshSpusTagNum(spusBean, poifoodSpusTagsBeans, tags, mFoodSpuTagsListAdapter, increase, false, false);
        setPrise(increase);
    }

    @Override
    public void removeProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product,
                              String tag, int section, boolean increase, boolean spec) {
        String spusBeanTag = product.getTag();
        if (tag.equals(spusBeanTag)) {
            for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
                spusBeanList = foodSpuTagsBeans.get(i).getSpus();
                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : spusBeanList) {
                    if (product.getId() == shopProduct.getId()) {
                        if (spec) {
                            int shopProductNumber = 0;
                            for (int j = 0; j < productList.size(); j++) {
                                if (productList.get(j).getId() == shopProduct.getId()) {
                                    shopProductNumber += productList.get(j).getNumber();
                                    productList.remove(j);
                                }
                            }
                            shopProduct.setNumber(shopProductNumber);
                        } else {
                            productList.remove(product);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                        getPresenter().refreshSpusTagNum(shopProduct, poifoodSpusTagsBeans, tags, mFoodSpuTagsListAdapter, increase, false, true);
                        shopProduct.setNumber(0);
                        shopProduct.setSpecificationsNumber(0);
                        mPoifoodSpusListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
            tags.clear();
        }
        if (mBottomDialog != null && mBottomDialog.isShowing()) {
            setDialogHeight(mBottomDialog);
        }
        setPrise(false);
    }

    @Override
    public void onUpdateDetailList(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                   String tag, int section, boolean increase, boolean isMinOrderCount) {
        String spusBeanTag = spusBean.getTag();
        Lg.getInstance().d(TAG, "onUpdateDetailList tag = " + tag + "; spusBeanTag = " + spusBeanTag);
        if (tag.equals(spusBeanTag)) {
            for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
                spusBeanList = foodSpuTagsBeans.get(i).getSpus();
                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : spusBeanList) {
                    if (spusBean.getId() == shopProduct.getId()) {
                        int specificationsNumber = shopProduct.getSpecificationsNumber();
                        shopProduct.setNumber(spusBean.getNumber());
                        if (shopProduct.getSpecificationsNumber() > 0) {
                            if (increase) {
                                specificationsNumber += 1;
                            } else {
                                if (isMinOrderCount) {
                                    specificationsNumber -= getPresenter().getMinOrderCount(spusBean);
                                } else {
                                    specificationsNumber -= 1;
                                }
                            }
                        }
                        shopProduct.setSpecificationsNumber(specificationsNumber);
                    }
                }
            }
        }
        mPoifoodSpusListAdapter.notifyDataSetChanged();
        getPresenter().refreshSpusTagNum(spusBean, poifoodSpusTagsBeans, tags, mFoodSpuTagsListAdapter, false, increase, false);
        setPrise(increase);
    }

    @Override
    public void onRemovePriduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product, String tag, int section, boolean increase) {
        String spusBeanTag = product.getTag();
        if (tag.equals(spusBeanTag)) {
            for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
                spusBeanList = foodSpuTagsBeans.get(i).getSpus();
                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : spusBeanList) {
                    if (product.getId() == shopProduct.getId()) {
                        productList.remove(product);
                        shoppingCartAdapter.notifyDataSetChanged();
                        getPresenter().refreshSpusTagNum(shopProduct, poifoodSpusTagsBeans, tags, mFoodSpuTagsListAdapter, increase, false, true);
                        shopProduct.setNumber(product.getNumber());
                        mPoifoodSpusListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
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
                            ToastUtils.show(mContext, getString(R.string.limit_buy_toast, "" + restrict,
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
            shoppingNumLayout.setVisibility(View.VISIBLE);
            if (mShopCartNum != null) {
                mShopCartNum.setVisibility(View.VISIBLE);
            }
        } else {
            shoppingNum.setVisibility(View.GONE);
            shoppingNumLayout.setVisibility(View.GONE);
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
                ToastUtils.show(mContext, getResources().getString(R.string.discount_prompt), Toast.LENGTH_SHORT);
                alreadyToast = true;
            }
        } else {
            if (listFull != null && listReduce != null) {
                if (mRlDiscount.getVisibility() == View.GONE) {
                    mRlDiscount.setVisibility(View.VISIBLE);
                }
                Lg.getInstance().d(TAG, "sum:" + listFull.get(listFull.size() - 1));
                if (listFull.size() > 0 && sum >= Double.parseDouble(listFull.get(listFull.size() - 1))) {
                    mDiscount.setText(getString(R.string.already_reduced) + listReduce.get(listReduce.size() - 1) + getString(R.string.element));
                    if (mCartDiscount != null) {
                        mCartDiscount.setText(getString(R.string.already_reduced) + listReduce.get(listReduce.size() - 1) + getString(R.string.element));
                    }
                    double v = sum - Double.parseDouble(listReduce.get(listReduce.size() - 1));
                    String str = NumberFormat.getInstance().format(v);
                    shoppingPrise.setText("¥" + " " + str);
                    if (mCartShoppingPrise != null) {
                        mCartShoppingPrise.setText("¥" + " " + str);
                    }
                    mDiscountNumber = Double.parseDouble(listReduce.get(listReduce.size() - 1));
                } else {
                    for (int i = 0; i < listFull.size(); i++) {
                        Lg.getInstance().d(TAG, "listFull.get(0) = " + listFull.get(0));
                        if (Double.parseDouble(listFull.get(i)) > sum) {
                            double v = Double.parseDouble(listFull.get(i)) - sum;
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
                    java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.00");
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
                        java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.00");
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

                if (mCartSettlement != null) {
                    mCartSettlement.setText(String.format(getString(R.string.not_distribution), NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getMin_price())));
                    mCartSettlement.setBackgroundResource(R.drawable.btn_grey);
                    mCartSettlement.setEnabled(false);
                }
            }
        }
        if (mBottomDialog != null && mBottomDialog.isShowing() && mCartDiscountLayout != null) {
            mCartDiscountLayout.setVisibility(TextUtils.isEmpty(mCartDiscount.getText().toString()) ? View.GONE : View.VISIBLE);
        }
    }

    private void showShopCartDialog() {
        GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getTakeout_shopping().getHints(), Constant.TTS_CART_VIEW);
        mBottomDialog = new Dialog(this, R.style.NoTitleDialog);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_shop_cart, null);
        mBottomDialog.setContentView(v);
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        v.setLayoutParams(layoutParams);
        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.show();
        setDialogHeight(mBottomDialog);
        mClearshopCart = (MultiplTextView) v.findViewById(R.id.tv_clear);
        shoppingListView = (ConstraintHeightListView) v.findViewById(R.id.shopproductListView);
        mShopCartNum = (TextView) v.findViewById(R.id.shoppingNum);
        mCartDistributionFee = (MultiplTextView) v.findViewById(R.id.distribution_fee);
        mCartShoppingPrise = (MultiplTextView) v.findViewById(R.id.shoppingPrise);
        mCartClose = (MultiplTextView) v.findViewById(R.id.tv_close_cart);
        mCartDiscount = (TextView) v.findViewById(R.id.tv_discount);
        mCartDiscountLayout = v.findViewById(R.id.rl_discount);
        mCartSettlement = (Button) v.findViewById(R.id.settlement);
        mClearshopCart.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                clearShopCart();
                StandardCmdClient.getInstance().playTTS(FoodListActivity.this, getString(R.string.already_clear_shop_cart));
                return true;
            }
        });
        mCartSettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Entry.getInstance().onEvent(Constant.POIFOODLIST_CONFIRM_THE_ORDER, EventType.TOUCH_TYPE);
                mBottomDialog.dismiss();
                if (NetUtil.getNetWorkState(FoodListActivity.this)) {
                    if (!isFastClick()) {
                        mLoading.setVisibility(View.VISIBLE);
                        getPresenter().requestOrderPreview(productList, mPoiInfoBean, 0);
                    }
                } else {
                    ToastUtils.show(mContext, getString(R.string.net_error), Toast.LENGTH_SHORT);
                }
            }
        });
        mCartSettlement.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                mCartSettlement.performClick();
                isNeedVoice = true;
                return true;
            }
        });

        mCartClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Entry.getInstance().onEvent(Constant.POIFOODLIST_CLOSE_THE_SHOPPING_CART, EventType.TOUCH_TYPE);
                mBottomDialog.dismiss();
            }
        });
        mCartClose.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                mBottomDialog.dismiss();
                StandardCmdClient.getInstance().playTTS(FoodListActivity.this, "BUBBLE");
                return true;
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
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        if (productList.size() == 0) dialog.dismiss();
        dialog.getWindow().setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping_cart:
            case R.id.tv_open_shop_cart:
                Entry.getInstance().onEvent(Constant.POIFOODLIST_CHECK_THE_SHOPPING_CART, EventType.TOUCH_TYPE);
                openShopCart();
                break;

            case R.id.settlement:
                Entry.getInstance().onEvent(Constant.POIFOODLIST_CONFIRM_THE_ORDER, EventType.TOUCH_TYPE);
                if (productList == null || productList.size() == 0) {
                    settlement.setEnabled(false);
                    return;
                }
                if (NetUtil.getNetWorkState(this)) {
                    if (!isFastClick()) {
                        mLoading.setVisibility(View.VISIBLE);
                        getPresenter().requestOrderPreview(productList, mPoiInfoBean, 0);
                    }
                } else {
                    ToastUtils.show(this, getString(R.string.net_error), Toast.LENGTH_SHORT);
                }
                break;

            case R.id.tv_clear:
                Entry.getInstance().onEvent(Constant.POIFOODLIST_EMPTY_CART, EventType.TOUCH_TYPE);
                clearShopCart();
                break;
            case R.id.iv_finish:
                if (booleanExtra) {
                    finish();
                } else {
                    AtyContainer.getInstance().finishAllActivity();
                }
                Entry.getInstance().onEvent(Constant.POIFOODLIST_FINISH, EventType.TOUCH_TYPE);
                break;
            case R.id.rl_store_details:
                Entry.getInstance().onEvent(Constant.POIFOODLIST_SEE_NOTICE, EventType.TOUCH_TYPE);
                View popView = mPoifoodSpusListAdapter.getPopView(R.layout.dialog_shop_details);
                mDetailsNotice = (TextView) popView.findViewById(R.id.tv_notice);
                mDetailsShopName = (MultiplTextView) popView.findViewById(R.id.tv_shop_name);
                mDetailsDistribution = (MultiplTextView) popView.findViewById(R.id.tv_details_distribution);
                mDetailsDiscount = (RecyclerView) popView.findViewById(R.id.tv_discount);
                StringBuffer stringBuffer = new StringBuffer();
                if (mPoidetailinfoBean != null) {
                    List<PoidetailinfoBean.MeituanBean.DataBean.DiscountsBean> discounts = mPoidetailinfoBean.getMeituan().getData().getDiscounts();
                    List<String> discountList = getPresenter().getDiscountList(mContext, discounts, true);
                    if (mDetailsDiscount.getItemDecorationCount() == 0) {
                        mDetailsDiscount.addItemDecoration(new SpaceItemDecoration(mContext.getResources().getDimensionPixelSize(R.dimen.px10dp)));
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
                window.setBackgroundDrawable(new ColorDrawable(0x9a000000));
                mPoifoodSpusListAdapter.showFoodListActivityDialog(v, popView, window);
                break;
            case R.id.no_internet_btn:
                netDataReque();
                break;
        }
    }

    private void clearShopCart() {
        if (selectFoods != null) {
            selectFoods.clear();
        }
        if (productList != null && productList.size() > 0) {
            productList.clear();
            for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spus = foodSpuTagsBeans.get(i).getSpus();
                for (int j = 0; j < spus.size(); j++) {
                    spus.get(j).setNumber(0);
                    spus.get(j).setSpecificationsNumber(0);
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
        alreadyToast = false;
    }

    private void openShopCart() {
        if (productList == null || productList.size() == 0) {
            return;
        }
        showShopCartDialog();
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

        int endX = 0 - start_location[0] + 112;
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
                Constant.ANIMATION_END = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                number--;
                if (number == 0) {
                    isClean = true;
                    mFoodSpuTagsList.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                animation_viewGroup.removeAllViews();
                            } catch (Exception e) {

                            }
                            isClean = false;
                        }
                    });
                }
                ObjectAnimator.ofFloat(shopping_cart, "translationY", 0, 4, -2, 0).setDuration(400).start();
                ObjectAnimator.ofFloat(shoppingNum, "translationY", 0, 4, -2, 0).setDuration(400).start();

                Constant.ANIMATION_END = true;
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
                intent.putExtra(Constant.STATUS, status);
                intent.putExtra(POI_INFO, mPoiInfoBean);
                intent.putExtra(PRODUCT_LIST_BEAN, (Serializable) productList);
                intent.putExtra(DISCOUNT, mDiscountNumber);
                intent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
                startActivity(intent);
                break;
            case Constant.STORE_CANT_NOT_BUY:
                ToastUtils.show(mContext, getResources().getString(R.string.order_preview_msg2), Toast.LENGTH_SHORT);
                break;

            case Constant.FOOD_CANT_NOT_BUY:
                ToastUtils.show(mContext, getResources().getString(R.string.order_preview_msg3), Toast.LENGTH_SHORT);
                break;
            case Constant.BEYOND_THE_SCOPE_OF_DISTRIBUTION:
                ToastUtils.show(mContext, getResources().getString(R.string.order_preview_msg4), Toast.LENGTH_SHORT);
                break;
            case Constant.FOOD_COST_NOT_BUY:
                ToastUtils.show(mContext, getResources().getString(R.string.order_preview_msg5), Toast.LENGTH_SHORT);
                break;
            case Constant.FOOD_COUNT_NOT_BUY:
                ToastUtils.show(mContext, getResources().getString(R.string.order_preview_msg15), Toast.LENGTH_SHORT);
                break;
            case Constant.FOOD_LACK_TO_BUY:
                ToastUtils.show(mContext
                        , data.getMeituan().getData().getWm_ordering_unavaliable_food_vo_list().get(0).getWm_food_name() + " "
                                + getResources().getString(R.string.order_preview_msg29)
                        , Toast.LENGTH_SHORT);
                break;
            case Constant.FOOD_LACK_NOT_BUY:
                ConfirmDialog dialog = new ConfirmDialog.Builder(this)
                        .setTitle(R.string.bit_tip)
                        .setMessage(data.getMeituan().getData().getMsg())
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                break;
            case Constant.SERVICE_ERROR:
                ToastUtils.show(mContext, getResources().getString(R.string.order_preview_msg26), Toast.LENGTH_SHORT);
                break;
            default:
                ToastUtils.show(mContext, getResources().getString(R.string.service_error), Toast.LENGTH_SHORT);
                break;
        }
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public void onArriveTimeSuccess(ArriveTimeBean data) {
        String view_time = data.getMeituan().getData().get(0).getTimelist().get(1).getView_time();
        ToastUtils.show(mContext, String.format(getString(R.string.first_arrive_time), view_time), Toast.LENGTH_SHORT);
    }

    @Override
    public void onOrderPreviewFailure(String msg) {
        Lg.getInstance().d(TAG, "msg = " + msg);
        mLoading.setVisibility(View.GONE);
        ToastUtils.show(mContext, getResources().getString(R.string.is_network_connected), Toast.LENGTH_SHORT);
    }

    @Override
    public void onPoifoodListSuccess(PoifoodListBean data) {
        foodSpuTagsBeans.clear();
        foodSpuTagsBeanName.clear();
        mPoiInfoBean = data.getMeituan().getData().getPoi_info();
        mShopTitle.setText(mPoiInfoBean.getName());
        mBulletin.setText(getString(R.string.notice, mPoiInfoBean.getBulletin()));
        GlideApp.with(mContext).load(mPoiInfoBean.getPic_url()).placeholder(R.drawable.default_goods_icon).skipMemoryCache(true).into(mShopPicture);
        //商品分类
        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> food_spu_tags = data.getMeituan().getData().getFood_spu_tags();
        for (int i = 0; i < food_spu_tags.size(); i++) {
            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean foodSpuTagsBean = food_spu_tags.get(i);
            String foodSpuTagsName = foodSpuTagsBean.getName();
            PoifoodSpusTagsBean poifoodSpusTagsBean = new PoifoodSpusTagsBean();
            poifoodSpusTagsBean.setFoodSpuTagsBeanName(foodSpuTagsName);
            poifoodSpusTagsBean.setTag(Integer.valueOf(food_spu_tags.get(i).getTag()));
            poifoodSpusTagsBean.setNumber(0);
            poifoodSpusTagsBeans.add(poifoodSpusTagsBean);
            foodSpuTagsBeanName.add(foodSpuTagsName);
            spusBeanList = new ArrayList<>();
            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spus = foodSpuTagsBean.getSpus();
            for (int j = 0; j < spus.size(); j++) {
                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean = spus.get(j);
                spusBeanList.add(spusBean);
            }
            foodSpuTagsBean.setSpus(spusBeanList);
            foodSpuTagsBeans.add(foodSpuTagsBean);
            oneMoreOrder(foodSpuTagsBean.getTag());
        }
        if (mOneMoreOrder) {
            showShopCartDialog();
        }
        mPoifoodSpusListAdapter.notifyDataSetChanged();
        mFoodSpuTagsListAdapter.notifyDataSetChanged();

        boolean isNeedVoiceFeedback = getIntent().getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false);
        if (isNeedVoiceFeedback) {
            if (mOneMoreOrder) {
                StandardCmdClient.getInstance().playTTS(FoodListActivity.this, String.format(getString(R.string.sure_order), mPoiInfoBean.getName()));
            } else {
                StandardCmdClient.getInstance().playTTS(FoodListActivity.this, getString(R.string.choose_you_commodity));
            }
        }

        mLoading.setVisibility(View.GONE);
        mNoNet.setVisibility(View.GONE);
        parentLayout.setVisibility(View.VISIBLE);
        mShopCartPic.setVisibility(View.VISIBLE);
    }

    private void oneMoreOrder(String groupTag) {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constant.ONE_MORE_ORDER)) {
            mOneMoreOrder = (boolean) getIntent().getExtras().get(Constant.ONE_MORE_ORDER);
            if (mOneMoreOrder) {
                List<FoodDetailBean> spusFoodList = null;
                if (extras.containsKey(Constant.ORDER_LSIT_EXTRA_STRING)) {
                    String orderListExtraPayLoadStr = (String) getIntent().getExtras().get(Constant.ORDER_LSIT_EXTRA_STRING);
                    OrderListExtraBean extraBean = GsonUtil.fromJson(orderListExtraPayLoadStr, OrderListExtraBean.class);
                    if (extraBean != null && extraBean.getOrderInfos() != null) {
                        OrderListExtraBean.OrderInfos orderInfos = extraBean.getOrderInfos();
                        spusFoodList = orderInfos.getFood_list();
                    }
                } else {
                    OrderDetailsResponse.MeituanBean.DataBean orderLsitBean = (OrderDetailsResponse.MeituanBean.DataBean) getIntent().getExtras().getSerializable(Constant.ORDER_LSIT_BEAN);
                    spusFoodList = orderLsitBean.getFood_list();
                }
                if (spusFoodList == null && spusFoodList.size() < 1) return;
                oneMoreOrderSetFoodDetail(spusFoodList, groupTag);
            }
        }
    }

    public void oneMoreOrderSetFoodDetail(List<FoodDetailBean> spusFoodList, String groupTag) {
        for (FoodDetailBean spusFood : spusFoodList) {
            long spuId = spusFood.getSpu_id();
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean : spusBeanList) {
                if (spusBean.getId() == spuId && (spusBean.getStatus() == 1 || spusBean.getStatus() == 2 || spusBean.getStatus() == 3)) {
                    //商品售完提示
                    ToastUtils.show(mContext, spusBean.getName() + getString(R.string.sold_out), Toast.LENGTH_SHORT);
                } else if (spusBean.getId() == spuId) {
                    //同一商品
                    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrs = spusBean.getAttrs();
                    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = spusBean.getSkus();
                    List<String> attrIds = spusFood.getAttrIds();
                    List<String> attrValues = spusFood.getAttrValues();
                    String spec = spusFood.getSpec();
                    //设置规格
                    getPresenter().SetAttrsAndSkus(spusBean, attrs, skus, attrIds, attrValues, spec);
                    //返回有规格但是匹配不到数据，提示用户
                    if (attrIds.size() > 0 && spusBean.getAttrs() != null && spusBean.getAttrs().get(0).getChoiceAttrs() == null) {
                        ToastUtils.show(mContext, getString(R.string.insufficient_inventory_text), Toast.LENGTH_SHORT);
                        return;
                    }
                    if (attrIds.size() > 0) {
                        spusBean.setSpecificationsNumber(spusFood.getCount());
                    }
                    int number = spusFood.getCount();
                    spusBean.setNumber(number);
                    for (int i = 0; i < number; i++) {
                        spusBean.setNumber(i + 1);
                        updateoneMoreOrder(spusBean, spusBean.getTag(), spusFood.getAttrIds(), spusBean.getNumber(), true, false, groupTag);
                    }
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
        mShopCartPic.setVisibility(View.GONE);
    }

    @Override
    public void onPoidetailinfoSuccess(PoidetailinfoBean data) {
        mPoidetailinfoBean = data;
        if (mPoidetailinfoBean != null) {
            List<PoidetailinfoBean.MeituanBean.DataBean.DiscountsBean> discounts = mPoidetailinfoBean.getMeituan().getData().getDiscounts();
            status = mPoidetailinfoBean.getMeituan().getData().getStatus();
            List<String> discountList = getPresenter().getDiscountList(mContext, discounts, false);
            if (mDiscounts.getItemDecorationCount() == 0) {
                mDiscounts.addItemDecoration(new SpaceItemDecoration(dp2px(3)));
            }
            final FlowLayoutManager layoutManager = new FlowLayoutManager();
            mDiscounts.setLayoutManager(layoutManager);
            DiscountAdaper discountAdaper = new DiscountAdaper(this, discountList);
            mDiscounts.setAdapter(discountAdaper);
            mDelivery.setText(getString(R.string.distribution_situation, NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getMin_price()),
                    NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getShipping_fee()),
                    NumberFormat.getInstance().format(mPoidetailinfoBean.getMeituan().getData().getAvg_delivery_time())));
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
                                String lastString = splitString.substring(y + 1, splitString.length());
                                listFull.add(substring);
                                listReduce.add(lastString);
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
            if (mCartSettlement.isEnabled()) {
                mCartSettlement.performClick();
            }
            return;
        }
        if (null == mBottomDialog || (null != mBottomDialog && !mBottomDialog.isShowing() && null != settlement)) {
            isNeedVoice = true;
            if (settlement.isEnabled()) {
                settlement.performClick();
            }
        }

    }

    @Override
    public void nextPage(boolean isNextPage) {
        Entry.getInstance().onEvent(Constant.POIFOODLIST_VOICE_PAGING, EventType.TOUCH_TYPE);
        if (isNextPage) {
            mSpusList.scrollBy(0, getResources().getDimensionPixelSize(R.dimen.px828dp));
            if (mSpusList.canScrollVertically(1)) {
            } else {
                StandardCmdClient.getInstance().playTTS(mContext, getString(R.string.last_page));
            }
        } else {
            mSpusList.scrollBy(0, -getResources().getDimensionPixelSize(R.dimen.px966dp));
            if (mSpusList.canScrollVertically(-1)) {
            } else {
                StandardCmdClient.getInstance().playTTS(mContext, getString(R.string.first_page));
            }
        }
    }

    @Override
    public void onCheckOrderOwnerSuccess(OrderOwnerBean bean) {
        mLoading.setVisibility(View.GONE);
        if (bean.getIov() != null && bean.getIov().getEnabled() == 1) {
            initData();
        } else {
            //订单不属于该账号提示用户
            getPresenter().startCheckOrderOwnerDialog(mContext);
        }
    }

    @Override
    public void onCheckOrderOwnerError(String error) {
        mLoading.setVisibility(View.GONE);
        startHomeActivity();
    }

    @Override
    public void onLogoutSuccess(LogoutBean data) {
        CacheUtils.saveAddrTime(0);
        Intent intent = new Intent(mContext, TakeawayLoginActivity.class);
        intent.putExtra(Constant.ORDER_LSIT_EXTRA_STRING, getIntent().getStringExtra(Constant.ORDER_LSIT_EXTRA_STRING));
        intent.putExtra(Constant.STORE_ID, mWmPoiId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLogoutError(String msg) {
        ToastUtils.show(mContext, getResources().getText(R.string.logout_failed), Toast.LENGTH_SHORT);
        Lg.getInstance().e(TAG, "msg:" + msg);
    }

    @Override
    public void selectListItem(int position) {
        Entry.getInstance().onEvent(Constant.POIFOODLIST_ADD_GOODS_BY_VOICE, EventType.TOUCH_TYPE);
        getPresenter().selectDuerOSItem(mContext, position, foodSpuTagsBeans, mSpusList);
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

    private static final int MIN_DELAY_TIME = 2000; // 两次点击间隔不能少于2000ms
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

    private boolean refreshList = false;

    private void refreshData() {
        if (refreshList && selectFoods != null && selectFoods.size() > 0) {
            mPoifoodSpusListAdapter.notifyDataSetChanged();
            refreshList = false;
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}