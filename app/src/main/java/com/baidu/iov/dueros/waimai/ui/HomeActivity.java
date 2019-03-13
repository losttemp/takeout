package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.presenter.HomePresenter;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity<HomePresenter, HomePresenter.HomeUi> implements
        HomePresenter.HomeUi, View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private RelativeLayout mRlFood;
    private RelativeLayout mRlFlower;
    private RelativeLayout mRlCake;

    private TextView mTvFood;
    private TextView mTvFlower;
    private TextView mTvCake;
    private AppCompatImageView mIvBack;
    private AppCompatImageView mIvRight;
    private TextView mTvTitle;
    private RelativeLayout mRlSearch;
    private AppCompatImageView mIvTitle;

    private StoreListFragment mStoreListFragment;

    private static final String FRAGMENT_KEY = "storeListFragment";

    public static String address = "地址";

    private boolean init = false;

    public static boolean fromLogin = false;

    private ArrayList<String> prefix = new ArrayList<>();

    private String[] tts = {"全力以赴的你，别忘了让自己吃好一点哦",
            "工作辛苦啦，来份美食，犒劳一下自己吧",
            "不吃饱哪有力气减肥啊，来份美食吧",
            "所有人都关心你飞得高不高，只有我关心你吃得好不好,来份美食吧",
            "唯美食与爱不可辜负，客官，来份美食呗"};


    @Override
    HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    HomePresenter.HomeUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Lg.getInstance().e(TAG, "onCreate");
        fromLogin = getIntent().getBooleanExtra(Constant.IS_FROME_TAKEAWAYLOGIN, false);
        if (getIntent().getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false)) {
            int index = (int) (Math.random() * 5);
            StandardCmdClient.getInstance().playTTS(HomeActivity.this, tts[index]);
        }
        iniView();
        setAddress();
        if (savedInstanceState != null) {
            mStoreListFragment = (StoreListFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
        } else {
            initFragment();
        }

        if (getIntent().getIntExtra(Constant.START_APP, -1) == Constant.START_APP_CODE) {
            requestPermission();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mStoreListFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAGMENT_KEY, mStoreListFragment);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onStart() {
        Lg.getInstance().e(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Lg.getInstance().e(TAG, "onResume");
        GuidingAppear.INSTANCE.init(this, WaiMaiApplication.getInstance().getWaimaiBean().getShop().getList());
        AccessibilityClient.getInstance().register(this, true, prefix, null);
    }

    public void setAddress() {
        if (!CacheUtils.getAddress().isEmpty()) {
            address = CacheUtils.getAddress();
        }
        mTvTitle.setText(address);
    }


    @Override
    protected void onPause() {
        super.onPause();
        AccessibilityClient.getInstance().unregister(this);

    }

    private void iniView() {
        mRlFood = findViewById(R.id.rl_food);
        mRlFlower = findViewById(R.id.rl_flower);
        mRlCake = findViewById(R.id.rl_cake);
        mTvFood = findViewById(R.id.tv_food);
        mTvFlower = findViewById(R.id.tv_flower);
        mTvCake = findViewById(R.id.tv_cake);
        mIvBack = findViewById(R.id.iv_back);
        mIvRight = findViewById(R.id.iv_right);
        mTvTitle = findViewById(R.id.tv_title);
        mRlSearch = findViewById(R.id.rl_search);
        mIvTitle = findViewById(R.id.iv_title);

        mIvBack.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mRlFlower.setOnClickListener(this);
        mRlFood.setOnClickListener(this);
        mRlCake.setOnClickListener(this);
        mTvTitle.setOnClickListener(this);
        mRlSearch.setOnClickListener(this);
        mIvTitle.setOnClickListener(this);
        prefix.add(getResources().getString(R.string.prefix_choice));
        prefix.add(getResources().getString(R.string.prefix_check));
        prefix.add(getResources().getString(R.string.prefix_open));
        mTvTitle.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        intentToAddress(true);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mIvRight.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        intentToOrderList(true);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mRlFood.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        intentToFood(true);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mRlFlower.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        intentToFlower(true);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mRlCake.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        intentToCake(true);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    private void initFragment() {
        //fragment
        mStoreListFragment = new StoreListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, Constant.STORE_FRAGMENT_FROM_HOME);
        mStoreListFragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_store_list, mStoreListFragment);
        transaction.commit();
    }

    @Override
    public void getGPSAddressSuccess() {
        Lg.getInstance().e(TAG, "getGPSAddressSuccess:");
        if (mBDLocation != null && !init) {
            address = mBDLocation.getAddress().district + mBDLocation.getAddress().street + mBDLocation.getAddress().streetNumber;
            mTvTitle.setText(address);
            Constant.GOODS_LATITUDE = (int) (mBDLocation.getLatitude() * LocationManager.SPAN);
            Constant.GOODS_LONGITUDE = (int) (mBDLocation.getLongitude() * LocationManager.SPAN);
            if (mStoreListFragment == null) {
                initFragment();
            }
            mStoreListFragment.getFilterList();
            mStoreListFragment.homeLoadFirstPage();
            init = true;
        }
    }

    @Override
    public void getGPSAddressFail() {
        super.getGPSAddressFail();
        Lg.getInstance().e(TAG, "getGPSAddressFail:");
        setAddress();
        StoreListFragment.getLocation(this);
        if (mStoreListFragment == null) {
            initFragment();
        }
        mStoreListFragment.getFilterList();
        mStoreListFragment.homeLoadFirstPage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Entry.getInstance().onEvent(Constant.EVENT_EXIT, EventType.TOUCH_TYPE);
                MyApplicationAddressBean.USER_NAMES.clear();
                MyApplicationAddressBean.USER_PHONES.clear();
                AtyContainer.getInstance().finishAllActivity();
                finish();
                break;

            case R.id.tv_title:
            case R.id.iv_title:
                Entry.getInstance().onEvent(Constant.EVENT_OPEN_ADDRESS_SELECT, EventType.TOUCH_TYPE);
                intentToAddress(false);
                break;

            case R.id.iv_right:
                Entry.getInstance().onEvent(Constant.EVENT_OPEN_ORDER_LIST, EventType.TOUCH_TYPE);
                intentToOrderList(false);
                break;

            case R.id.rl_search:
                Entry.getInstance().onEvent(Constant.EVENT_OPEN_SEARCH_FROM_HOME, EventType.TOUCH_TYPE);
                Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                searchIntent.putExtra(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, Constant.STORE_FRAGMENT_FROM_HOME);
                startActivity(searchIntent);
                break;

            case R.id.rl_flower:
                Entry.getInstance().onEvent(Constant.EVENT_FLOWER_CLICK, EventType.TOUCH_TYPE);
                intentToFlower(false);
                break;

            case R.id.rl_cake:
                Entry.getInstance().onEvent(Constant.EVENT_CAKE_CLICK, EventType.TOUCH_TYPE);
                intentToCake(false);
                break;

            case R.id.rl_food:
                Entry.getInstance().onEvent(Constant.EVENT_FOOD_CLICK, EventType.TOUCH_TYPE);
                intentToFood(false);
                break;

            default:
                break;
        }

    }


    private void intentToAddress(boolean isNeedVoice) {
        Intent addressIntent = new Intent(HomeActivity.this, AddressSelectActivity.class);
        startActivity(addressIntent);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StandardCmdClient.getInstance().playTTS(HomeActivity.this, getString(R.string.tts_add_new_address));
            }
        }, 100);
    }


    private void intentToOrderList(boolean isNeedVoice) {
        Intent orderListIntent = new Intent(this, OrderListActivity.class);
        orderListIntent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
        startActivity(orderListIntent);
    }

    private void intentToFood(boolean isNeedVoice) {
        Intent foodIntent = new Intent(this, FoodActivity.class);
        foodIntent.putExtra("title", mTvFood.getText().toString());
        foodIntent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
        startActivity(foodIntent);
    }

    private void intentToFlower(boolean isNeedVoice) {
        Intent flowerIntent = new Intent(this, RecommendShopActivity.class);
        flowerIntent.putExtra("title", mTvFlower.getText().toString());
        flowerIntent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
        startActivity(flowerIntent);
    }

    private void intentToCake(boolean isNeedVoice) {
        Intent cakeIntent = new Intent(this, RecommendShopActivity.class);
        cakeIntent.putExtra("title", mTvCake.getText().toString());
        cakeIntent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
        startActivity(cakeIntent);
    }


}



