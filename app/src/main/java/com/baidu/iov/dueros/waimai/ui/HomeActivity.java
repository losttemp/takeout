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
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
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

    //语音播报内容
    private String waimaiTts[];
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
        waimaiTts = getResources().getStringArray(R.array.waimai_watch);
        iniView();
        if (savedInstanceState != null) {
            mStoreListFragment = (StoreListFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
        } else {
            initFragment();
            if (getIntent().getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false)) {
                int index = (int) (Math.random() * 5);
                StandardCmdClient.getInstance().playTTS(HomeActivity.this, waimaiTts[index]);
            }
        }
        if (getIntent().getIntExtra(Constant.START_APP, -1) == Constant.START_APP_CODE) {
            //initDataTime();
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
    protected void onResume() {
        super.onResume();
        setAddress();
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


    private void intentToAddress(boolean isNeedVoice) {
        Intent addressIntent = new Intent(HomeActivity.this, AddressSelectActivity.class);
        startActivity(addressIntent);
        Handler handler = new Handler();
        if (isNeedVoice) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    StandardCmdClient.getInstance().playTTS(HomeActivity.this, getString(R.string.tts_add_new_address));
                }
            }, 100);
        }
    }


    private void intentToOrderList(boolean isNeedVoice) {
        Intent orderListIntent = new Intent(this, OrderListActivity.class);
        orderListIntent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
        startActivity(orderListIntent);
        Handler handler = new Handler();
        if (isNeedVoice) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    StandardCmdClient.getInstance().playTTS(HomeActivity.this, getString(R.string.tts_look_order_list));
                }
            }, 100);
        }
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

    private void initDataTime() {
        //当定位没反应的时候,定时2秒调用缓存的地址数据进行加载商店列表
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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



