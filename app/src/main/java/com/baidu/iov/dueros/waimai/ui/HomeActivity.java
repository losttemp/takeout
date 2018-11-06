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

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.presenter.HomePresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;

import java.util.List;

public class HomeActivity extends BaseActivity<HomePresenter, HomePresenter.HomeUi> implements
		HomePresenter.HomeUi, View.OnClickListener {

	private AppCompatTextView mTvFood;
	private AppCompatTextView mTvFlower;
	private AppCompatTextView mTvCake;
	private AppCompatTextView mTvOrder;
	private AppCompatImageView mIvBack;
	private AppCompatImageView mIvRight;
	private AppCompatTextView mTvTitle;

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
		mTvFood = (AppCompatTextView) findViewById(R.id.tv_food);
		mTvFlower = (AppCompatTextView) findViewById(R.id.tv_flower);
		mTvCake = (AppCompatTextView) findViewById(R.id.tv_cake);
		mTvOrder = (AppCompatTextView) findViewById(R.id.tv_order);
		mIvBack = (AppCompatImageView) findViewById(R.id.iv_back);
		mIvRight = (AppCompatImageView) findViewById(R.id.iv_right);
		mTvTitle = (AppCompatTextView) findViewById(R.id.tv_title);

	}

	private void iniData() {
		//address
		mTvTitle.setText("地址");
		initLocation();
		Drawable drawable = getResources().getDrawable(R.mipmap.arrow_down_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mTvTitle.setCompoundDrawables(null, null, drawable, null);

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
		mTvOrder.setOnClickListener(this);
		mTvTitle.setOnClickListener(this);

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
				Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
				startActivity(searchIntent);
				break;

			case R.id.tv_flower:
				Intent flowerIntent = new Intent(this, BusinessActivity.class);
				flowerIntent.putExtra("title", mTvFlower.getText().toString());
				startActivity(flowerIntent);
				break;

			case R.id.tv_cake:
				Intent cakeIntent = new Intent(this, BusinessActivity.class);
				cakeIntent.putExtra("title", mTvCake.getText().toString());
				startActivity(cakeIntent);
				break;

			case R.id.tv_food:
				Intent foodIntent = new Intent(this, FoodActivity.class);
				foodIntent.putExtra("title", mTvFlower.getText().toString());
				startActivity(foodIntent);
				break;

			case R.id.tv_order:
				Intent orderListIntent = new Intent(this, OrderListActivity.class);
				orderListIntent.putExtra("title", mTvOrder.getText().toString());
				startActivity(orderListIntent);
				break;

			default:
				break;
		}

	}

	@Override
	public void close() {
		finish();
	}

	@Override
	public void locationCallBack(boolean isSuccess, BDLocation bdLocation) {
		super.locationCallBack(isSuccess, bdLocation);
		mStoreListFragment.loadFirstPage(new StoreReq());
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
		LocationManager instance = LocationManager.getInstance(this);
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
