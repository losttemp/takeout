package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.StoreAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.presenter.StoreListPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.view.FilterPopWindow;
import com.baidu.iov.dueros.waimai.view.SortPopWindow;
import com.domain.multipltextview.MultiplTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class StoreListFragment extends BaseFragment<StoreListPresenter, StoreListPresenter
		.StoreListUi> implements
		StoreListPresenter.StoreListUi, View.OnClickListener {

	private static final String TAG = StoreListFragment.class.getSimpleName();
	
	private static final int COMPREHENSIVE = 0;
	private static final int SALE_NUM_SORT_INDEX = 1;
	private static final int DISTANCE_SORT_INDEX = 5;

	private LinearLayoutCompat mLlFilter;
	private RelativeLayout mRlSort;
	private AppCompatTextView mTvSort;
	private AppCompatImageView mIvSort;
	private AppCompatTextView mTvSales;
	private AppCompatTextView mTvDistance;
	private RelativeLayout mRlFilter;
	private AppCompatTextView mTvFilter;
	private AppCompatImageView mIvFilter;
	private SmartRefreshLayout mRefreshLayout;
	private RecyclerView mRvStore;
	private View mViewBg;
	private AppCompatTextView mTvTipNoResult;
	
	private  RelativeLayout mRlTipNoResult;
	

	private Context mContext;
	private StoreListPresenter mPresenter;
	private StoreAdaper mStoreAdaper;
	private StoreResponse.MeituanBean.DataBean mStoreData = new StoreResponse.MeituanBean
			.DataBean();
	private List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean> mStoreList = new
			ArrayList<>();
	private List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> mSortList
			= new ArrayList<>();
	private List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> sortTypeTabs=new ArrayList<>();
	private List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean> mFilterList
			= new ArrayList<>();
	private StoreReq mStoreReq;
	private FilterConditionReq mFilterConditionReq;
	private SortPopWindow mSortPopWindow;
	private FilterPopWindow mFilterPopWindow;
	private int mFromPageType;
	
	private View mView;

	@Override
	StoreListPresenter createPresenter() {
		return new StoreListPresenter();
	}

	@Override
	StoreListPresenter.StoreListUi getUi() {
		return this;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_store_list, container, false);
		iniView(view);
		iniData();

		return view;
	}

	private void iniView(View view) {
		mLlFilter = (LinearLayoutCompat) view.findViewById(R.id.ll_filter);
		mRlSort = (RelativeLayout) view.findViewById(R.id.rl_sort);
		mTvSort = (AppCompatTextView) view.findViewById(R.id.tv_sort);
		mIvSort = (AppCompatImageView) view.findViewById(R.id.iv_sort);
		mTvSales = view.findViewById(R.id.tv_sales);
		mTvDistance =  view.findViewById(R.id.tv_distance);
		mRlFilter = (RelativeLayout) view.findViewById(R.id.rl_filter);
		mTvFilter = (AppCompatTextView) view.findViewById(R.id.tv_filter);
		mIvFilter = (AppCompatImageView) view.findViewById(R.id.iv_filter);
		mRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refresh_layout);
		mRvStore = (RecyclerView) view.findViewById(R.id.rv_store);
		mViewBg = (View) view.findViewById(R.id.view_bg);
		mView= (View) view.findViewById(R.id.view);
		mTvTipNoResult = (AppCompatTextView) view.findViewById(R.id.tv_tip_no_result);
		mRlTipNoResult = (RelativeLayout) view.findViewById(R.id.rl_tip_no_result);

	}

	private void iniData() {
		mContext = getActivity();
		mPresenter = getPresenter();

		Bundle bundle = getArguments();
		if (bundle == null) {
			return;
		}
		mFromPageType = bundle.getInt(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE);

		mStoreAdaper = new StoreAdaper(mStoreList, mContext);
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRvStore.setLayoutManager(layoutManager);
		mRvStore.setAdapter(mStoreAdaper);
		mStoreAdaper.setItemClickListener(new StoreAdaper.OnItemClickListener() {
			@Override
			public void onItemClick(int position) {
				jumpPage(position);
			}
		});

		setRefreshView();
		mRlSort.setOnClickListener(this);
		mRlFilter.setOnClickListener(this);
		mTvSales.setOnClickListener(this);
		mTvDistance.setOnClickListener(this);

		mStoreReq = new StoreReq();
		mStoreReq.setSortType(COMPREHENSIVE);
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME &&
				Constant.LONGITUDE > 0 && Constant.LATITUDE > 0) {
			loadFirstPage(mStoreReq);
		}

		requestFilterList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_sort:
				if (mSortPopWindow == null) {
					mSortPopWindow = new SortPopWindow(mContext, mSortList, (new SortPopWindow
							.OnSelectedSortListener() {
						@Override
						public void OnSelectedSort(FilterConditionResponse.MeituanBean.DataBean
														   .SortTypeListBean type) {
							mStoreReq.setSortType((int) type.getCode());
							mTvSort.setText(type.getName());
							loadFirstPage(mStoreReq);
							mTvSales.setTextColor(mContext.getResources().getColor(
									R.color.white));
							mTvDistance.setTextColor(mContext.getResources().getColor(
									R.color.white));
						}
					}));
					mSortPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
						@Override
						public void onDismiss() {
							mTvSort.setTextColor(getResources().getColor(R.color.black));
							mIvSort.setImageResource(R.drawable.arrow_down);
							mViewBg.setVisibility(View.GONE);
						}
					});
				}
				if (mSortList.size() == 0) {
					requestFilterList();
				}
				mTvSort.setTextColor(getResources().getColor(R.color.black));
				mIvSort.setImageResource(R.drawable.arrow_up);
				mSortPopWindow.showAsDropDown(mView);
				mViewBg.setVisibility(View.VISIBLE);
				break;

			case R.id.rl_filter:
				if (mFilterPopWindow == null) {
					mFilterPopWindow = new FilterPopWindow(mContext, mFilterList, new
							FilterPopWindow.OnClickOkListener() {
								@Override
								public void onClickOk(String migFilter) {
									mStoreReq.setMigFilter(migFilter);
									if (!migFilter.isEmpty()){
										mTvFilter.setTextColor(getResources().getColor(R.color.black));
									}else{
										mTvFilter.setTextColor(getResources().getColor(R.color.gray));
									}
									loadFirstPage(mStoreReq);
								}
							});
					mFilterPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
						@Override
						public void onDismiss() {
							//mTvFilter.setTextColor(getResources().getColor(R.color.dark_gray));
							mIvFilter.setImageResource(R.drawable.arrow_down);
							mViewBg.setVisibility(View.GONE);
						}
					});
				}
				if (mFilterList.size() == 0) {
					requestFilterList();
				}
				mTvFilter.setTextColor(getResources().getColor(R.color.black));
				mIvFilter.setImageResource(R.drawable.arrow_up);
				mFilterPopWindow.showAsDropDown(mView);
				mViewBg.setVisibility(View.VISIBLE);
				break;

			case R.id.tv_sales:
				int salesaSortType = mStoreReq.getSortType()==null?0:mStoreReq.getSortType();
				if (salesaSortType!=SALE_NUM_SORT_INDEX) {
					mTvSort.setText(getResources().getString(R.string.store_sort));
					mStoreReq.setSortType(SALE_NUM_SORT_INDEX);
					mTvSales.setTextColor(mContext.getResources().getColor(
							R.color.black));
					mTvDistance.setTextColor(mContext.getResources().getColor(
							R.color.white));
					mTvSort.setTextColor(getResources().getColor(R.color.white));
					loadFirstPage(mStoreReq);
				}else {
					mStoreReq.setSortType(COMPREHENSIVE);
					mTvSales.setTextColor(mContext.getResources().getColor(
							R.color.white));
					mTvDistance.setTextColor(mContext.getResources().getColor(
							R.color.white));
					mTvSort.setTextColor(getResources().getColor(R.color.black));
					loadFirstPage(mStoreReq);
				}
				break;

			case R.id.tv_distance:
				int distanceSortType = mStoreReq.getSortType()==null?0:mStoreReq.getSortType();
				if (distanceSortType!=DISTANCE_SORT_INDEX) {
					mTvSort.setText(getResources().getString(R.string.store_sort));
					mStoreReq.setSortType(DISTANCE_SORT_INDEX);
					mTvSales.setTextColor(mContext.getResources().getColor(
							R.color.white));
					mTvDistance.setTextColor(mContext.getResources().getColor(
							R.color.black));
					mTvSort.setTextColor(getResources().getColor(R.color.white));
					loadFirstPage(mStoreReq);
				}else {
					mStoreReq.setSortType(COMPREHENSIVE);
					mTvSales.setTextColor(mContext.getResources().getColor(
							R.color.white));
					mTvDistance.setTextColor(mContext.getResources().getColor(
							R.color.white));
					mTvSort.setTextColor(getResources().getColor(R.color.black));
					loadFirstPage(mStoreReq);
				}
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
		}
		mStoreList.addAll(data.getMeituan().getData().getOpenPoiBaseInfoList());
		mStoreAdaper.notifyDataSetChanged();

		//set emptey view
		if (mStoreList.size() == 0) {
			if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH) {
				mLlFilter.setVisibility(View.GONE);
				mView.setVisibility(View.GONE);
				mTvTipNoResult.setText(WaiMaiApplication.getInstance().getString(R.string
						.no_search_result_keyword));
				((SearchActivity)mContext).setmEtTipNoResult();
			} else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME) {
				if (!TextUtils.isEmpty(mStoreReq.getMigFilter())) {
					mTvTipNoResult.setText(WaiMaiApplication.getInstance().getString(R.string
							.no_search_result_filter));
				} else {
					mTvTipNoResult.setText(WaiMaiApplication.getInstance().getString(R.string
							.no_search_result_position));
				}
			}
			mRlTipNoResult.setVisibility(View.VISIBLE);
			mRefreshLayout.setVisibility(View.GONE);
		}else{
			mView.setVisibility(View.VISIBLE);
			mRlTipNoResult.setVisibility(View.GONE);
			mRefreshLayout.setVisibility(View.VISIBLE);
		}

		if (mRefreshLayout.isRefreshing()) {
			mRefreshLayout.finishRefresh();
		}
		if (mRefreshLayout.isLoading()) {
			mRefreshLayout.finishLoadmore();
		}

	}

	@Override
	public void failure(String msg) {
		if (mRefreshLayout.isRefreshing()) {
			mRefreshLayout.finishRefresh(false);
		}
		if (mRefreshLayout.isLoading()) {
			mRefreshLayout.finishLoadmore(1000, false);
		}

	}

	private List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> getSortTypeList(List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> sortTypes) {
		List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> sortTypeList = new ArrayList<>();
		if (sortTypes != null && !sortTypes.isEmpty()) {
			int size = sortTypes.size();
			for (int i = 0; i < size; i++) {
				FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean sortType = sortTypes.get(i);
				//1:list 
				if (sortType.getPosition() == FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean.LISTPOS) {
					sortTypeList.add(sortType);
				}
			}
		}
		return sortTypeList;
	}

	private List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> getSortTypeTab(List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> sortTypes) {
		List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> sortTypeTabs = new ArrayList<>();
		if (sortTypes != null && !sortTypes.isEmpty()) {
			int size = sortTypes.size();
			for (int i = 0; i < size; i++) {
				FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean sortType = sortTypes.get(i);
				//0:tab  
				if (sortType.getPosition() ==FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean.TABPOS) {
					sortTypeTabs.add(sortType);
				}
			}
		}
		return sortTypeTabs;
	}

	@Override
	public void updateFilterCondition(FilterConditionResponse data) {
		if (data==null||data.getMeituan()==null){
			return;
		}
		mSortList.clear();
		mSortList.addAll(getSortTypeList(data.getMeituan().getData().getSort_type_list()));
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

	@Override
	public void selectListItem(int index) {
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH && ((SearchActivity) mContext)
				.getStatus() != Constant.SEARCH_STATUS_FRAGMENT) {
			return;
		}

		jumpPage(index);
	}

	@Override
	public void nextPage() {
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH && ((SearchActivity) mContext)
				.getStatus() != Constant.SEARCH_STATUS_FRAGMENT) {
			return;
		}

		if (mRlTipNoResult.getVisibility() == View.GONE) {
			if (mRvStore != null) {
				mRvStore.smoothScrollToPosition(mStoreAdaper.getItemCount() - 1);
			}
			if (mRefreshLayout != null) {
				mRefreshLayout.autoLoadmore(100);
			}
		}

	}

	private void setRefreshView() {

		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshLayout) {
				loadFirstPage(mStoreReq);
			}
		});

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

	private void jumpPage(int position) {
		if (mStoreList.size() > position) {
			if (mStoreList.get(position).getStatus()==Constant.STROE_STATUS_BREAK){
				Toast.makeText(mContext,getResources().getString(R.string.tips_earliest_delivery_time),Toast.LENGTH_LONG).show();
			}
			Intent intent = new Intent(mContext, FoodListActivity.class);
			intent.putExtra(Constant.STORE_ID, mStoreList.get(position).getWm_poi_id());
			startActivity(intent);
		}
	}

	public void loadFirstPage(StoreReq storeReq) {
		Lg.getInstance().e(TAG,"storeReq:"+storeReq);
		storeReq.setPage_index(1);
		mPresenter.requestStoreList(storeReq);
		mStoreReq = storeReq;
		mRlTipNoResult.setVisibility(View.GONE);
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH &&
				mLlFilter.getVisibility() == View.GONE) {
			mLlFilter.setVisibility(View.VISIBLE);
		}
	}

	public void locationLoadFirstPage() {
		Lg.getInstance().e(TAG,"mStoreReq:"+mStoreReq);
		mStoreReq.setPage_index(1);
		mPresenter.requestStoreList(mStoreReq);
	}

	/**
	 * request filter condition list
	 */
	public void requestFilterList() {
		mPresenter.requestFilterList(new FilterConditionReq());
	}

}
