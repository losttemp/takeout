package com.baidu.iov.dueros.waimai.ui;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baidu.iov.dueros.waimai.adapter.StoreAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.presenter.HomePresenter;
import com.baidu.iov.dueros.waimai.view.SortPopWindow;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<HomePresenter, HomePresenter.HomeUi> implements
		HomePresenter.HomeUi, View.OnClickListener {

	private static final int SALE_NUM_SORT_INDEX = 1;
	private static final int PRICE_SORT_INDEX = 5;

	private AppCompatTextView mTvLocation;
	private RelativeLayout mRlSearch;
	private AppCompatImageView mIvBack;
	private AppCompatEditText mEtSearch;
	private AppCompatTextView mTvSearch;
	private LinearLayoutCompat mLlType;
	private AppCompatTextView mTvFood;
	private AppCompatTextView mTvFlower;
	private AppCompatTextView mTvCake;
	private LinearLayoutCompat mLlFilter;
	private AppCompatTextView mTvSort;
	private AppCompatTextView mTvSales;
	private AppCompatTextView mTvDistance;
	private AppCompatTextView mTvFilter;
	private RecyclerView mRvStore;
	private LinearLayout mLlHistory;
	private RelativeLayout mRlHistory;
	private AppCompatTextView mTvHistory;
	private AppCompatImageView mIvDelete;
	private ListView mLvHistory;
	private LinearLayoutCompat mLlBottom;
	private AppCompatTextView mTvHomeBtn;
	private AppCompatTextView mTvOrderBtn;

	private StoreAdaper mStoreAdaper;
	private List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean> mStoreList = new
			ArrayList<>();
	private StoreReq mStoreReq;
	SortPopWindow mSortPopWindow;

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

		getPresenter().requestStoreList(mStoreReq);
	}

	private void iniView() {
		mTvLocation = (AppCompatTextView) findViewById(R.id.tv_location);
		mRlSearch = (RelativeLayout) findViewById(R.id.rl_search);
		mIvBack = (AppCompatImageView) findViewById(R.id.iv_back);
		mEtSearch = (AppCompatEditText) findViewById(R.id.et_search);
		mTvSearch = (AppCompatTextView) findViewById(R.id.tv_search);
		mLlType = (LinearLayoutCompat) findViewById(R.id.ll_type);
		mTvFood = (AppCompatTextView) findViewById(R.id.tv_food);
		mTvFlower = (AppCompatTextView) findViewById(R.id.tv_flower);
		mTvCake = (AppCompatTextView) findViewById(R.id.tv_cake);
		mLlFilter = (LinearLayoutCompat) findViewById(R.id.ll_filter);
		mTvSort = (AppCompatTextView) findViewById(R.id.tv_sort);
		mTvSales = (AppCompatTextView) findViewById(R.id.tv_sales);
		mTvDistance = (AppCompatTextView) findViewById(R.id.tv_distance);
		mTvFilter = (AppCompatTextView) findViewById(R.id.tv_filter);
		mRvStore = (RecyclerView) findViewById(R.id.rv_store);
		mLlHistory = (LinearLayout) findViewById(R.id.ll_history);
		mRlHistory = (RelativeLayout) findViewById(R.id.rl_history);
		mTvHistory = (AppCompatTextView) findViewById(R.id.tv_history);
		mIvDelete = (AppCompatImageView) findViewById(R.id.iv_delete);
		mLvHistory = (ListView) findViewById(R.id.lv_history);
		mLlBottom = (LinearLayoutCompat) findViewById(R.id.ll_bottom);
		mTvHomeBtn = (AppCompatTextView) findViewById(R.id.tv_home);
		mTvOrderBtn = (AppCompatTextView) findViewById(R.id.tv_order);
	}

	private void iniData() {
		mStoreAdaper = new StoreAdaper(mStoreList, this);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRvStore.setLayoutManager(layoutManager);
		mRvStore.setAdapter(mStoreAdaper);

		mStoreReq = new StoreReq();
		mStoreReq.setLongitude(95369826);
		mStoreReq.setLatitude(29735952);

		mEtSearch.setOnClickListener(this);
		mTvSort.setOnClickListener(this);
		mTvSales.setOnClickListener(this);
		mTvDistance.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.et_search:

				break;

			case R.id.tv_sort:
				if (mSortPopWindow == null) {
					mSortPopWindow = new SortPopWindow(this);
				}
				mSortPopWindow.showFilterPopup(mTvSort);
				break;

			case R.id.tv_sales:
				mStoreReq.setSortType(SALE_NUM_SORT_INDEX);
				getPresenter().requestStoreList(mStoreReq);
				break;

			case R.id.tv_distance:
				mStoreReq.setSortType(PRICE_SORT_INDEX);
				getPresenter().requestStoreList(mStoreReq);
				break;

			default:
				break;
		}

	}

	@Override
	public void update(StoreResponse data) {
		mStoreList.clear();
		mStoreList.addAll(data.getMeituan().getData().getOpenPoiBaseInfoList());
		mStoreAdaper.notifyDataSetChanged();
	}

	@Override
	public void failure(String error) {

	}

	public void setSortType(int position) {
		mStoreReq.setSortType(position);
		mTvSort.setText(getResources().getStringArray(R.array.sort_type)[position]);
		getPresenter().requestStoreList(mStoreReq);
	}
}
