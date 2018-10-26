package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.baidu.iov.dueros.waimai.adapter.SearchHistroyAdapter;
import com.baidu.iov.dueros.waimai.adapter.StoreAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.presenter.HomePresenter;
import com.baidu.iov.dueros.waimai.utils.SharedPreferencesUtils;
import com.baidu.iov.dueros.waimai.view.FilterPopWindow;
import com.baidu.iov.dueros.waimai.view.SortPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<HomePresenter, HomePresenter.HomeUi> implements
		HomePresenter.HomeUi, View.OnClickListener {

	private static final int SALE_NUM_SORT_INDEX = 1;
	private static final int DISTANCE_SORT_INDEX = 5;

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
	private RelativeLayout mRlSort;
	private RelativeLayout mRlFilter;
	private AppCompatImageView mIvSort;
	private AppCompatImageView mIvFilter;
	private SmartRefreshLayout mRefreshLayout;

	private HomePresenter mPresenter;
	private StoreAdaper mStoreAdaper;
	private StoreResponse.MeituanBean.DataBean mStoreData = new StoreResponse.MeituanBean
			.DataBean();
	private List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean> mStoreList = new
			ArrayList<>();
	private List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> mSortList
			= new ArrayList<>();
	private List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean> mFilterList
			= new ArrayList<>();
	private StoreReq mStoreReq;
	private FilterConditionReq mFilterConditionReq;
	SortPopWindow mSortPopWindow;
	FilterPopWindow mFilterPopWindow;
	private List<String> mHistorys;
	private SearchHistroyAdapter mSearchHistroyAdapter;

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

		mPresenter.requestStoreList(mStoreReq);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPresenter().registerCmd(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPresenter().unregisterCmd(this);
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
		mRlSort = (RelativeLayout) findViewById(R.id.rl_sort);
		mRlFilter = (RelativeLayout) findViewById(R.id.rl_filter);
		mIvSort = (AppCompatImageView) findViewById(R.id.iv_sort);
		mIvFilter = (AppCompatImageView) findViewById(R.id.iv_filter);
		mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
	}

	private void iniData() {
		mPresenter = getPresenter();
		mStoreAdaper = new StoreAdaper(mStoreList, this);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRvStore.setLayoutManager(layoutManager);
		mRvStore.setAdapter(mStoreAdaper);

		mStoreReq = new StoreReq();

		//search history
		mHistorys = new ArrayList<>();
		SharedPreferencesUtils.getSearchHistory(HomeActivity.this, mHistorys);
		mSearchHistroyAdapter = new SearchHistroyAdapter(mHistorys,
				HomeActivity.this);
		mLvHistory.setAdapter(mSearchHistroyAdapter);

		setSearchEditTextListener();
		setRefreshView();
		mEtSearch.setOnClickListener(this);
		mRlSort.setOnClickListener(this);
		mRlFilter.setOnClickListener(this);
		mTvSales.setOnClickListener(this);
		mTvDistance.setOnClickListener(this);
		mTvSearch.setOnClickListener(this);
		mIvBack.setOnClickListener(this);
		mIvDelete.setOnClickListener(this);
		mTvFlower.setOnClickListener(this);
		mTvCake.setOnClickListener(this);
		mTvFood.setOnClickListener(this);
		mTvOrderBtn.setOnClickListener(this);

		mLvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				searchKeyword(mHistorys.get(position));
			}
		});

		//request filter condition list
		mFilterConditionReq = new FilterConditionReq();
		mPresenter.requestFilterList(mFilterConditionReq);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.et_search:
				showSearchPage();
				break;

			case R.id.rl_sort:
				if (mSortPopWindow == null) {
					mSortPopWindow = new SortPopWindow(this, mSortList);
					mSortPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
						@Override
						public void onDismiss() {
							mTvSort.setTextColor(getResources().getColor(R.color.dark_gray));
							mIvSort.setImageResource(R.mipmap.arrow_down);
						}
					});
				}
				mTvSort.setTextColor(getResources().getColor(R.color.black));
				mIvSort.setImageResource(R.mipmap.arrow_up);
				mSortPopWindow.showAsDropDown(mTvSort);

				break;

			case R.id.rl_filter:
				if (mFilterPopWindow == null) {
					mFilterPopWindow = new FilterPopWindow(this, mFilterList);
					mFilterPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
						@Override
						public void onDismiss() {
							mTvFilter.setTextColor(getResources().getColor(R.color.dark_gray));
							mIvFilter.setImageResource(R.mipmap.arrow_down);
						}
					});
				}
				mTvFilter.setTextColor(getResources().getColor(R.color.black));
				mIvFilter.setImageResource(R.mipmap.arrow_up);
				mFilterPopWindow.showAsDropDown(mTvFilter);
				break;

			case R.id.tv_sales:
				mTvSort.setText(getResources().getString(R.string.store_sort));
				mStoreReq.setSortType(SALE_NUM_SORT_INDEX);
				mStoreReq.setPage_index(1);
				mPresenter.requestStoreList(mStoreReq);
				break;

			case R.id.tv_distance:
				mTvSort.setText(getResources().getString(R.string.store_sort));
				mStoreReq.setSortType(DISTANCE_SORT_INDEX);
				mStoreReq.setPage_index(1);
				mPresenter.requestStoreList(mStoreReq);
				break;

			case R.id.tv_search:
				String keyWord = mEtSearch.getText().toString();
				searchKeyword(keyWord);
				break;

			case R.id.iv_delete:
				SharedPreferencesUtils.clearSearchHistory(HomeActivity.this);
				mSearchHistroyAdapter.notifyDataSetChanged();
				break;

			case R.id.iv_back:
				onSearchBack();
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
				orderListIntent.putExtra("title", mTvOrderBtn.getText().toString());
				startActivity(orderListIntent);
				break;

			default:
				break;
		}

	}

	@Override
	public void update(StoreResponse data) {
		mStoreData = data.getMeituan().getData();
		if (mStoreData.getCurrent_page_index() == 1) {
			mStoreList.clear();
			if (mRefreshLayout.isRefreshing()) {
				mRefreshLayout.finishRefresh();
			}
		} else {
			if (mRefreshLayout.isLoading()) {
				mRefreshLayout.finishLoadmore();
			}
		}
		mStoreList.addAll(data.getMeituan().getData().getOpenPoiBaseInfoList());
		mStoreAdaper.notifyDataSetChanged();
	}

	@Override
	public void failure(String msg) {
		if (mRefreshLayout.isRefreshing()) {
			mRefreshLayout.finishRefresh();
		}
		if (mRefreshLayout.isLoading()) {
			mRefreshLayout.finishLoadmore();
		}

	}

	@Override
	public void close() {
		finish();
	}

	@Override
	public void updateFilterCondition(FilterConditionResponse data) {
		mSortList.clear();
		mSortList.addAll(data.getMeituan().getData().getSort_type_list());
		if (mSortPopWindow != null) {
			mSortPopWindow.updateList();
		}

		mFilterList.clear();
		mFilterList.addAll(data.getMeituan().getData().getActivity_filter_list());
		if (mFilterPopWindow != null) {
			mFilterPopWindow.updateList();
		}

	}

	@Override
	public void failureFilterCondition(String msg) {

	}

	private void setSearchEditTextListener() {

		mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				if (b) {
					showSearchPage();
				}
			}
		});

	}

	private void setRefreshView() {

		mRefreshLayout.setEnableRefresh(true);
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshLayout) {
				mStoreReq.setPage_index(1);
				mPresenter.requestStoreList(mStoreReq);
			}
		});

		mRefreshLayout.setEnableLoadmore(true);
		mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshLayout) {
				if (mStoreData.getHave_next_page() == 1) {
					mStoreReq.setPage_index(mStoreData.getCurrent_page_index() + 1);
					mPresenter.requestStoreList(mStoreReq);
				} else {
					mRefreshLayout.finishLoadmore();
				}
			}
		});

	}

	private void showSearchPage() {
		mIvBack.setVisibility(View.VISIBLE);
		mTvSearch.setVisibility(View.VISIBLE);
		mLlHistory.setVisibility(View.VISIBLE);
		mTvLocation.setVisibility(View.GONE);
		mLlBottom.setVisibility(View.GONE);
		mLlType.setVisibility(View.GONE);
	}

	private void searchKeyword(String keyword) {
		mStoreReq.setSortType(null);
		if (!TextUtils.isEmpty(keyword)) {

			mStoreReq.setKeyword(keyword);
			mEtSearch.setText("");
			SharedPreferencesUtils.saveSearchHistory(HomeActivity.this, keyword,
					mHistorys);
			mSearchHistroyAdapter.notifyDataSetChanged();
		}
		mLlHistory.setVisibility(View.GONE);
		mTvSearch.setVisibility(View.GONE);
		mStoreReq.setPage_index(1);
		mPresenter.requestStoreList(mStoreReq);
		hideSoftKeyboard();
	}

	private void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context
				.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
	}

	public void setSortType(FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean type) {
		mStoreReq.setSortType((int) type.getCode());
		mTvSort.setText(type.getName());
		mStoreReq.setPage_index(1);
		mPresenter.requestStoreList(mStoreReq);
	}

	public void setFilterTypes(String migFilter) {
		mStoreReq.setMigFilter(migFilter);
		mStoreReq.setPage_index(1);
		mPresenter.requestStoreList(mStoreReq);
	}

	private void onSearchBack() {
		mIvBack.setVisibility(View.GONE);
		mTvSearch.setVisibility(View.GONE);
		mLlHistory.setVisibility(View.GONE);
		mTvLocation.setVisibility(View.VISIBLE);
		mLlBottom.setVisibility(View.VISIBLE);
		mLlType.setVisibility(View.VISIBLE);
		mStoreReq.setKeyword(null);
		mPresenter.requestStoreList(mStoreReq);
		hideSoftKeyboard();
	}

	@Override
	public void onBackPressed() {
		if (mIvBack.getVisibility() == View.GONE) {
			super.onBackPressed();
		} else {
			onSearchBack();
		}
	}
}
