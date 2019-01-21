package com.baidu.iov.dueros.waimai.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.presenter.HomePresenter;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;
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

    public static String address;

    private boolean init = false;

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
        if (getIntent().getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false)) {
            VoiceManager.getInstance().playTTS(HomeActivity.this, getString(R.string.please_choice_commodity));
        }
        iniView();
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
        if (TextUtils.isEmpty(address)) {
            if (!CacheUtils.getAddress().isEmpty()) {
                address = CacheUtils.getAddress();
            } else {
                address = "地址";
            }
        }
        mTvTitle.setText(address);
        GuidingAppear.INSTANCE.init(this, WaiMaiApplication.getInstance().getWaimaiBean().getShop().getList());
	}

	private void iniView() {
		mRlFood = findViewById(R.id.rl_food);
		mRlFlower = findViewById(R.id.rl_flower);
		mRlCake =  findViewById(R.id.rl_cake);
		mTvFood = findViewById(R.id.tv_food);
		mTvFlower = findViewById(R.id.tv_flower);
		mTvCake =  findViewById(R.id.tv_cake);
		mIvBack =  findViewById(R.id.iv_back);
		mIvRight =  findViewById(R.id.iv_right);
		mTvTitle =  findViewById(R.id.tv_title);
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
		if (mBDLocation!=null&&!init){
			String address=mBDLocation.getAddrStr();
			mTvTitle.setText(address);
			Constant.GOODS_LATITUDE = (int) (mBDLocation.getLatitude() * LocationManager.SPAN);
			Constant.GOODS_LONGITUDE  = (int) (mBDLocation.getLongitude() * LocationManager.SPAN);
			if (mStoreListFragment==null){
				initFragment();
			}
			mStoreListFragment.getFilterList();
			mStoreListFragment.homeLoadFirstPage();
			init=true;
		}
	}

	@Override
	public void getGPSAddressFail() {
		super.getGPSAddressFail();
		Lg.getInstance().e(TAG, "getGPSAddressFail:");
		StoreListFragment.getLocation(this);
		if (mStoreListFragment==null){
			initFragment();
		}
		mStoreListFragment.getFilterList();
		mStoreListFragment.homeLoadFirstPage();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back:
				Entry.getInstance().onEvent(Constant.EVENT_EXIT,EventType.TOUCH_TYPE);
				MyApplicationAddressBean.USER_NAMES.clear();
				MyApplicationAddressBean.USER_PHONES.clear();
				AtyContainer.getInstance().finishAllActivity();
				finish();
				break;

			case R.id.tv_title:
			case R.id.iv_title:
				Entry.getInstance().onEvent(Constant.EVENT_OPEN_ADDRESS_SELECT,EventType.TOUCH_TYPE);
				Intent addressIntent = new Intent(HomeActivity.this, AddressSelectActivity.class);
				startActivity(addressIntent);
				break;

			case R.id.iv_right:
				Entry.getInstance().onEvent(Constant.EVENT_OPEN_ORDER_LIST,EventType.TOUCH_TYPE);		
				Intent orderListIntent = new Intent(this, OrderListActivity.class);
				startActivity(orderListIntent);
				break;

			case R.id.rl_search:
				Entry.getInstance().onEvent(Constant.EVENT_OPEN_SEARCH_FROM_HOME,EventType.TOUCH_TYPE);
				Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
				searchIntent.putExtra(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, Constant.STORE_FRAGMENT_FROM_HOME);
				startActivity(searchIntent);
				break;

			case R.id.rl_flower:
				Entry.getInstance().onEvent(Constant.EVENT_FLOWER_CLICK,EventType.TOUCH_TYPE);
				Intent flowerIntent = new Intent(this, RecommendShopActivity.class);
				flowerIntent.putExtra("title", mTvFlower.getText().toString());
				startActivity(flowerIntent);
				break;

			case R.id.rl_cake:
				Entry.getInstance().onEvent(Constant.EVENT_CAKE_CLICK,EventType.TOUCH_TYPE);
				Intent cakeIntent = new Intent(this, RecommendShopActivity.class);
				cakeIntent.putExtra("title", mTvCake.getText().toString());
				startActivity(cakeIntent);
				break;

			case R.id.rl_food:
				Entry.getInstance().onEvent(Constant.EVENT_FOOD_CLICK,EventType.TOUCH_TYPE);
				Intent foodIntent = new Intent(this, FoodActivity.class);
				foodIntent.putExtra("title", mTvFood.getText().toString());
				foodIntent.putExtra("latitude", Constant.GOODS_LATITUDE);
				foodIntent.putExtra("longitude", Constant.GOODS_LONGITUDE);
				startActivity(foodIntent);
				break;

			default:
				break;
		}

	}
	
	
    }

    
    
