package com.baidu.iov.dueros.waimai.ui;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.presenter.HomePresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.domain.multipltextview.MultiplTextView;

import java.util.List;
public class HomeActivity extends BaseActivity<HomePresenter, HomePresenter.HomeUi> implements
		HomePresenter.HomeUi, View.OnClickListener {

	private static final String TAG = StoreListFragment.class.getSimpleName();
	private TextView mTvFood;
	private TextView mTvFlower;
	private TextView mTvCake;
	private AppCompatImageView mIvBack;
	private AppCompatImageView mIvRight;
	private MultiplTextView mTvTitle;
	private RelativeLayout mRlSearch;

	private StoreListFragment mStoreListFragment;
	private BDLocation mBDLocation;

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
		iniData();

	}

	private void iniView() {
		mTvFood = findViewById(R.id.tv_food);
		mTvFlower = findViewById(R.id.tv_flower);
		mTvCake =  findViewById(R.id.tv_cake);
		mIvBack =  findViewById(R.id.iv_back);
		mIvRight =  findViewById(R.id.iv_right);
		mTvTitle =  findViewById(R.id.tv_title);
		mRlSearch = findViewById(R.id.rl_search);

	}

	private void iniData() {
		//address
		mTvTitle.setText("地址");
		initLocation();
		
		//fragment
		mStoreListFragment = new StoreListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, Constant.STORE_FRAGMENT_FROM_HOME);
		mStoreListFragment.setArguments(bundle);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.fragment_store_list, mStoreListFragment);
		transaction.commit();

		mIvBack.setOnClickListener(this);
		mIvRight.setOnClickListener(this);
		mTvFlower.setOnClickListener(this);
		mTvCake.setOnClickListener(this);
		mTvFood.setOnClickListener(this);
		mTvTitle.setOnClickListener(this);
		mRlSearch.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back:
				finish();
				break;

			case R.id.tv_title:
				Intent addressIntent = new Intent(HomeActivity.this, AddressSelectActivity.class);
				startActivity(addressIntent);
				break;

			case R.id.iv_right:
				Intent orderListIntent = new Intent(this, OrderListActivity.class);
				startActivity(orderListIntent);
				break;

			case R.id.rl_search:
				Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
				startActivity(searchIntent);
				break;

			case R.id.tv_flower:
				Intent flowerIntent = new Intent(this, RecommendShopActivity.class);
				flowerIntent.putExtra("title", mTvFlower.getText().toString());
				startActivity(flowerIntent);
				break;

			case R.id.tv_cake:
				Intent cakeIntent = new Intent(this, RecommendShopActivity.class);
				cakeIntent.putExtra("title", mTvCake.getText().toString());
				startActivity(cakeIntent);
				break;

			case R.id.tv_food:
				Intent foodIntent = new Intent(this, FoodActivity.class);
				foodIntent.putExtra("title", mTvFlower.getText().toString());
				startActivity(foodIntent);
				break;

			default:
				break;
		}

	}

	@Override
	public void locationCallBack(boolean isSuccess, BDLocation bdLocation) {
		super.locationCallBack(isSuccess, bdLocation);
		Lg.getInstance().d(TAG,"locationCallBack");
		mStoreListFragment.locationLoadFirstPage();
		mStoreListFragment.requestFilterList();

		mBDLocation = bdLocation;
		if (isSuccess && mBDLocation != null) {
			List<Poi> addrList = mBDLocation.getPoiList();
			if (addrList != null && addrList.size() > 0
					&& (!TextUtils.isEmpty(addrList.get(0).getName()))) {
				String addr = addrList.get(0).getName();
				mTvTitle.setText(addr);
			}
		}

	}

	private void initLocation() {
		LocationManager instance = LocationManager.getInstance(getApplicationContext());
		mBDLocation = instance.getLastKnownLocation();
		if (mBDLocation != null) {
			List<Poi> addrList = mBDLocation.getPoiList();
			if (addrList != null && addrList.size() > 0
					&& (!TextUtils.isEmpty(addrList.get(0).getName()))) {
				String addr = addrList.get(0).getName();
				mTvTitle.setText(addr);
			}
		}
	}

}
