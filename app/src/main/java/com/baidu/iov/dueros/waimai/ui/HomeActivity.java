package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.presenter.HomePresenter;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;
import com.sankuai.waimai.opensdk.MTWMApi;
import com.sankuai.waimai.opensdk.callback.LoadDataListener;
import com.sankuai.waimai.opensdk.response.model.PoiDetail;
import com.sankuai.waimai.opensdk.response.model.PoiListData;
import com.sankuai.waimai.opensdk.response.model.food.FoodInfo;

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
    private ImageView mIvBack;
    private ImageView mIvRight;
    private TextView mTvTitle;
    private RelativeLayout mRlSearch;
    private ImageView mIvTitle;

    private ImageView mExitLogin;

    private StoreListFragment mStoreListFragment;

    private static final String FRAGMENT_KEY = "storeListFragment";

    public static String address = "地址";

    private ArrayList<String> prefix = new ArrayList<>();


    private CountDownTimer mTimer;
    private boolean isLocation = false;

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
        iniView();
        setAddress();
        initDataTime();
        if (savedInstanceState != null) {
            mStoreListFragment = (StoreListFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
        } else {
            initFragment();
        }
        loadData();
    }

    public void loadData() {

        LoadDataListener<PoiListData> listDataLoadDataListener = new LoadDataListener<PoiListData>() {
            @Override
            public void onSuccess(PoiListData data) {
                Log.d(TAG, "成功了");
            }

            @Override
            public void onError(String errorMsg) {
                Log.d(TAG, "失败了" + errorMsg);

            }
        };

        MTWMApi.getPoiList(Constant.LONGITUDE, Constant.LATITUDE, "黄焖鸡", 0, 0, 20, listDataLoadDataListener);

    }

    public void loadPoiListData() {
        LoadDataListener<PoiListData> listDataLoadDataListener = new LoadDataListener<PoiListData>() {
            @Override
            public void onSuccess(PoiListData data) {
                Log.d(TAG, "成功了");
            }

            @Override
            public void onError(String errorMsg) {
                Log.d(TAG, "失败了");
            }
        };
        MTWMApi.getPoiList(Constant.LONGITUDE, Constant.LATITUDE, "黄焖鸡", 0, 0, 20, listDataLoadDataListener);
    }

    public void loadPoiFoodData() {
        LoadDataListener<FoodInfo> loadDataListener = new LoadDataListener<FoodInfo>() {
            @Override
            public void onSuccess(FoodInfo data) {
                Log.d(TAG, "成功了");
            }

            @Override
            public void onError(String errorMsg) {
                Log.d(TAG, "失败了");
            }
        };
        MTWMApi.getPoiFood(Constant.LONGITUDE, Constant.LATITUDE, 606737, loadDataListener);
    }

    public void loadPoiDetail() {

        // 参数 wm_poi_id  longitude  latitude  需要接入方传入
        MTWMApi.getPoiDetail(Constant.LONGITUDE, Constant.LATITUDE, 606737, new LoadDataListener<PoiDetail>() {

            @Override
            public void onSuccess(PoiDetail data) {
                Log.d(TAG, "成功了");
            }

            @Override
            public void onError(String errorMsg) {
                Log.d(TAG, "失败了");
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mStoreListFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAGMENT_KEY, mStoreListFragment);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getTakeout_list().getHints(), Constant.TTS_SHOP_LIST);
        AccessibilityClient.getInstance().register(this, true, prefix, null);
    }

    public void setAddress() {
        if (!CacheUtils.getAddress().isEmpty()) {
            address = CacheUtils.getAddress();
        }
        mTvTitle.setText(address);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        mExitLogin = findViewById(R.id.exit_login);

        mIvBack.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mRlFlower.setOnClickListener(this);
        mRlFood.setOnClickListener(this);
        mRlCake.setOnClickListener(this);
        mTvTitle.setOnClickListener(this);
        mRlSearch.setOnClickListener(this);
        mIvTitle.setOnClickListener(this);
        mExitLogin.setOnClickListener(this);
        prefix.add(getResources().getString(R.string.prefix_choice));
        prefix.add(getResources().getString(R.string.prefix_check));
        prefix.add(getResources().getString(R.string.prefix_open));
        mTvTitle.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        intentToAddress();
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
                        intentToOrderList();
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
                        intentToFood();
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
                        intentToFlower();
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
                        intentToCake();
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
//        if (isInitData() && mBDLocation != null) {
//            address = mBDLocation.getAddress().district + mBDLocation.getAddress().street + mBDLocation.getAddress().streetNumber;
//            mTvTitle.setText(address);
//            Constant.GOODS_LATITUDE = (int) (mBDLocation.getLatitude() * LocationManager.SPAN);
//            Constant.GOODS_LONGITUDE = (int) (mBDLocation.getLongitude() * LocationManager.SPAN);
//            Lg.getInstance().e(TAG, " Constant.GOODS_LATITUDE：" + Constant.GOODS_LATITUDE + "  Constant.GOODS_LONGITUDE:" + Constant.GOODS_LONGITUDE);
//            if (mStoreListFragment == null) {
//                initFragment();
//            }
//            mStoreListFragment.getFilterList();
//            mStoreListFragment.homeLoadFirstPage();
//        }
    }

    @Override
    public void getGPSAddressFail() {
        Lg.getInstance().e(TAG, "getGPSAddressFail:");
//        if (isInitData()) {
//            setAddress();
//            StoreListFragment.getLocation(this);
//            if (mStoreListFragment == null) {
//                initFragment();
//            }
//            mStoreListFragment.getFilterList();
//            mStoreListFragment.homeLoadFirstPage();
//        }
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
                intentToAddress();
                break;

            case R.id.iv_right:
                Entry.getInstance().onEvent(Constant.EVENT_OPEN_ORDER_LIST, EventType.TOUCH_TYPE);
                intentToOrderList();
                break;

            case R.id.rl_search:
                Entry.getInstance().onEvent(Constant.EVENT_OPEN_SEARCH_FROM_HOME, EventType.TOUCH_TYPE);
                Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                searchIntent.putExtra(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, Constant.STORE_FRAGMENT_FROM_HOME);
                startActivity(searchIntent);
                break;

            case R.id.rl_flower:
                Entry.getInstance().onEvent(Constant.EVENT_FLOWER_CLICK, EventType.TOUCH_TYPE);
                intentToFlower();
                break;

            case R.id.rl_cake:
                Entry.getInstance().onEvent(Constant.EVENT_CAKE_CLICK, EventType.TOUCH_TYPE);
                intentToCake();
                break;

            case R.id.rl_food:
                Entry.getInstance().onEvent(Constant.EVENT_FOOD_CLICK, EventType.TOUCH_TYPE);
                intentToFood();
                break;
            case R.id.exit_login:
                exitLogin();
                break;
            default:
                break;
        }

    }


    AlertDialog dialog;

    private void exitLogin() {
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.exit_login_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        Button btnOk = layout.findViewById(R.id.ok);
        Button btnCancel = layout.findViewById(R.id.cancel);
        dialog = builder.create();
        dialog.getWindow().setWindowAnimations(R.style.notAnimation);
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
                getPresenter().requesLogout();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void intentToAddress() {
        Intent addressIntent = new Intent(HomeActivity.this, AddressSelectActivity.class);
        startActivity(addressIntent);

    }


    private void intentToOrderList() {
        Intent orderListIntent = new Intent(this, OrderListActivity.class);
        startActivity(orderListIntent);
    }

    private void intentToFood() {
        Intent foodIntent = new Intent(this, FoodActivity.class);
        foodIntent.putExtra("title", mTvFood.getText().toString());
        startActivity(foodIntent);
    }

    private void intentToFlower() {
        Intent flowerIntent = new Intent(this, RecommendShopActivity.class);
        flowerIntent.putExtra("title", mTvFlower.getText().toString());
        startActivity(flowerIntent);
    }

    private void intentToCake() {
        Intent cakeIntent = new Intent(this, RecommendShopActivity.class);
        cakeIntent.putExtra("title", mTvCake.getText().toString());
        startActivity(cakeIntent);
    }

    private void initDataTime() {
        //当定位没反应的时候,定时2秒调用缓存的地址数据进行加载商店列表
        if (true) {
            mTimer = new CountDownTimer(2000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    Lg.getInstance().e(TAG, "onFinish");
                    if (isLocation) return;
                    isLocation = true;
                    setAddress();
                    StoreListFragment.getLocation(mContext);
                    if (mStoreListFragment == null) {
                        initFragment();
                    }
                    mStoreListFragment.getFilterList();
                    mStoreListFragment.homeLoadFirstPage();
                    if (null != mTimer) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                }

            };
            mTimer.start();
        }
    }

    private boolean isInitData() {
        if (!isLocation) {
            isLocation = true;
            if (null != mTimer) {
                mTimer.cancel();
                mTimer = null;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update(LogoutBean data) {
        CacheUtils.saveAddrTime(0);
        CacheUtils.saveAddress("");
        Intent intent = new Intent(HomeActivity.this, TakeawayLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void failure(String msg) {
        ToastUtils.show(HomeActivity.this, getResources().getText(R.string.logout_failed), Toast.LENGTH_SHORT);
        Lg.getInstance().e(TAG, "msg:" + msg);
        dialog.dismiss();
    }
}



