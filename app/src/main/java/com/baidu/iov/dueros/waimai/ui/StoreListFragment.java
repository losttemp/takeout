package com.baidu.iov.dueros.waimai.ui;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.StoreAdaper;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.presenter.StoreListPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.view.FilterPopWindow;
import com.baidu.iov.dueros.waimai.view.SortPopWindow;
import com.baidu.iov.dueros.waimai.view.SortTypeTagListView;
import com.baidu.iov.faceos.client.GsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;
public class StoreListFragment extends BaseFragment<StoreListPresenter, StoreListPresenter.StoreListUi> implements StoreListPresenter.StoreListUi, View.OnClickListener {

	private static final String TAG = StoreListFragment.class.getSimpleName();
	
	private RelativeLayout mLlFilter;
	private RelativeLayout mRlSort;
	private AppCompatTextView mTvSort;
	private AppCompatImageView mIvSort;
	private RelativeLayout mRlFilter;
	private AppCompatTextView mTvFilter;
	private AppCompatImageView mIvFilter;
	private SmartRefreshLayout mRefreshLayout;
	private RecyclerView mRvStore;
	private View mViewBg;
	private AppCompatTextView mTvTipNoResult;
	private  RelativeLayout mRlTipNoResult;
	private SortTypeTagListView mTagLv;
	private LinearLayout mWarnNoInternet;
	private Button mNoInternetBtn;

	private Context mContext;
	private StoreListPresenter mPresenter;
	private StoreAdaper mStoreAdaper;
	private StoreResponse.MeituanBean.DataBean mStoreData = new StoreResponse.MeituanBean
			.DataBean();
	private List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean> mStoreList = new
			ArrayList<>();
	private List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> mSortList
			= new ArrayList<>();
	private List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> mSortTypeTabs=new ArrayList<>();
	private List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean> mFilterList
			= new ArrayList<>();
	
	private StoreReq mStoreReq;
	private SortPopWindow mSortPopWindow;
	private FilterPopWindow mFilterPopWindow;
	private int mFromPageType;
	private static final int VOICE_STEP = 5;//语音选择下一页时跳动的item数目
	private View mView;
	private Integer latitude;
	private Integer longitude;

	private FilterConditionReq filterConditionReq;
	
	

	@Override
	StoreListPresenter createPresenter() {
		return new StoreListPresenter();
	}

	@Override
	StoreListPresenter.StoreListUi getUi() {
		return this;
	}

	public int getLatitude() {
		return latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_store_list, container, false);
		mContext = getActivity();
		getLocation();
		iniView(view);
		iniData();

		return view;
	}
	
	
	private void getLocation(){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences("_cache", Context.MODE_PRIVATE);
		String addressDataJson = sharedPreferences.getString(Constant.ADDRESS_DATA, null);
		if (addressDataJson != null) {
			AddressListBean.IovBean.DataBean mAddressData = GsonUtil.fromJson(addressDataJson, AddressListBean.IovBean.DataBean.class);
			latitude=mAddressData.getLatitude()!=null?mAddressData.getLatitude():-1;
			longitude=mAddressData.getLongitude()!=null?mAddressData.getLongitude():-1;
			Lg.getInstance().d(TAG,"latitude:"+latitude+" longitude:"+longitude);
		}
	}

	private void iniView(View view) {
		mLlFilter =  view.findViewById(R.id.ll_filter);
		mRlSort =view.findViewById(R.id.rl_sort);
		mTvSort = view.findViewById(R.id.tv_sort);
		mIvSort = view.findViewById(R.id.iv_sort);
		mRlFilter = view.findViewById(R.id.rl_filter);
		mTvFilter = view.findViewById(R.id.tv_filter);
		mIvFilter =  view.findViewById(R.id.iv_filter);
		mRefreshLayout = view.findViewById(R.id.refresh_layout);
		mRvStore = view.findViewById(R.id.rv_store);
		mViewBg = view.findViewById(R.id.view_bg);
		mView= view.findViewById(R.id.view);
		mTvTipNoResult = view.findViewById(R.id.tv_tip_no_result);
		mRlTipNoResult = view.findViewById(R.id.rl_tip_no_result);
		mWarnNoInternet= view.findViewById(R.id.warn_no_internet);
		mNoInternetBtn= view.findViewById(R.id.no_internet_btn);
		mTagLv = view.findViewById(R.id.tag_lv);
		mTagLv.setItemClickListener(new SortTypeTagListView.OnItemClickListener() {
			@Override
			public void onClick(int sortType) {
				mTvSort.setText(getResources().getString(R.string.store_sort)); 
				if (sortType==Constant.COMPREHENSIVE){
					mTvSort.setTextColor(getResources().getColor(R.color.filter_selected));
				}else{
					mTvSort.setTextColor(getResources().getColor(R.color.white_60));
				}
				mStoreReq.setSortType(sortType);
				loadFirstPage(mStoreReq);
			}
		});
	}

	private void iniData() {
		mPresenter = getPresenter();

		Bundle bundle = getArguments();
		if (bundle == null) {
			return;
		}
		mFromPageType = bundle.getInt(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE);

		mStoreAdaper = new StoreAdaper(mStoreList, mContext,mFromPageType);
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRvStore.setLayoutManager(layoutManager);
		mRvStore.setAdapter(mStoreAdaper);
		mStoreAdaper.setItemClickListener(new StoreAdaper.OnItemClickListener() {
			@Override
			public void onItemClick(int position) {
				jumpPage(position, false);
			}
		});

		setRefreshView();
		mRlSort.setOnClickListener(this);
		mRlFilter.setOnClickListener(this);
		mNoInternetBtn.setOnClickListener(this);

		mStoreReq = new StoreReq();
		mStoreReq.setLatitude(latitude);
		mStoreReq.setLongitude(longitude);
		mStoreReq.setSortType(Constant.COMPREHENSIVE);
		if (mFromPageType==Constant.STORE_FRAGMENT_FROM_HOME){
			homeLoadFirstPage();
		}

		filterConditionReq =new FilterConditionReq();
		filterConditionReq.setLatitude(latitude);
		filterConditionReq.setLongitude(longitude);
		getPresenter().requestFilterList(filterConditionReq);
	}

	@Override
	public void onStart() {
		super.onStart();
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
						}
					}));
					mSortPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
						@Override
						public void onDismiss() {
							mIvSort.setImageResource(R.drawable.arrow_down);
							mViewBg.setVisibility(View.GONE);
						}
					});
				}
				if (mSortList.size() == 0) {
					mPresenter.requestFilterList(filterConditionReq);
				}
				mTagLv.setTextViewDefaultColor();
				mTvSort.setTextColor(getResources().getColor(R.color.filter_selected));
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
										mTvFilter.setTextColor(getResources().getColor(R.color.filter_selected));
									}else{
										mTvFilter.setTextColor(getResources().getColor(R.color.white_60));
									}
									loadFirstPage(mStoreReq);
								}
							});
					mFilterPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
						@Override
						public void onDismiss() {
							mIvFilter.setImageResource(R.drawable.arrow_down);
							mViewBg.setVisibility(View.GONE);
						}
					});
				}
				if (mFilterList.size() == 0) {
					mPresenter.requestFilterList(filterConditionReq);
				}
				mTvFilter.setTextColor(getResources().getColor(R.color.filter_selected));
				mIvFilter.setImageResource(R.drawable.arrow_up);
				mFilterPopWindow.showAsDropDown(mView);
				mViewBg.setVisibility(View.VISIBLE);
				break;

			case R.id.no_internet_btn:
				if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME) {
					mPresenter.requestFilterList(filterConditionReq);
					homeLoadFirstPage();
				}else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_RECOMMENDSHOP){
					mPresenter.requestFilterList(filterConditionReq);
					recommendShopLoadFirstPage(mStoreReq);
				}
				break;
				
			default:
				break;
		}

	}

	@Override
	public void update(StoreResponse data) {
		mStoreData = data.getMeituan().getData();
		if (mStoreData.getCurrent_page_index() <= 1) {
			mStoreList.clear();
		}
	
		mStoreList.addAll(data.getMeituan().getData().getOpenPoiBaseInfoList());
		mStoreAdaper.notifyDataSetChanged();
		Lg.getInstance().d(TAG,"mStoreList:"+mStoreList.get(0));
		//set emptey view
		if (mStoreList.size() == 0) {
			if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH) {
				if (!TextUtils.isEmpty(mStoreReq.getMigFilter())) {
					mTvTipNoResult.setText(WaiMaiApplication.getInstance().getString(R.string
							.no_search_result_filter));
				} else {
					mTvTipNoResult.setText(WaiMaiApplication.getInstance().getString(R.string.no_search_result_keyword));
					mLlFilter.setVisibility(View.GONE);
					mView.setVisibility(View.GONE);
					((SearchActivity)mContext).setmEtTipNoResult();
				}
			} else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME||mFromPageType == Constant.STORE_FRAGMENT_FROM_RECOMMENDSHOP) {
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
			mWarnNoInternet.setVisibility(View.GONE);
			mLlFilter.setVisibility(View.VISIBLE);
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
		Lg.getInstance().d(TAG,"msg:"+msg);
		if (!NetUtil.getNetWorkState(mContext)){
			mLlFilter.setVisibility(View.GONE);
			mView.setVisibility(View.GONE);
			mRefreshLayout.setVisibility(View.GONE);
			mWarnNoInternet.setVisibility(View.VISIBLE);
			return;
		}
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH) {
			if (!TextUtils.isEmpty(mStoreReq.getMigFilter())) {
				mTvTipNoResult.setText(WaiMaiApplication.getInstance().getString(R.string
						.no_search_result_filter));
			} else {
				mTvTipNoResult.setText(WaiMaiApplication.getInstance().getString(R.string.no_search_result_keyword));
				mLlFilter.setVisibility(View.GONE);
				mView.setVisibility(View.GONE);
				((SearchActivity)mContext).setmEtTipNoResult();
			}
		} else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME||mFromPageType == Constant.STORE_FRAGMENT_FROM_RECOMMENDSHOP) {
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

		mSortTypeTabs.clear();
		mSortTypeTabs.addAll(getSortTypeTab(data.getMeituan().getData().getSort_type_list()));
		mTagLv.setTags(mSortTypeTabs);

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

		jumpPage(index, true);
	}

	@Override
	public void nextPage(boolean isNextPage) {
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH && ((SearchActivity) mContext)
				.getStatus() != Constant.SEARCH_STATUS_FRAGMENT) {
			return;
		}

		if (mRlTipNoResult.getVisibility() == View.GONE) {
			LinearLayoutManager manager = (LinearLayoutManager) mRvStore.getLayoutManager();
			assert manager != null;
			int currentItemPosition  = manager.findFirstVisibleItemPosition();
			if (isNextPage) {
				if (currentItemPosition + VOICE_STEP * 2 > mStoreList.size() && mRefreshLayout != null) {
					mRefreshLayout.autoLoadmore(100);
				}
				manager.scrollToPositionWithOffset(currentItemPosition + VOICE_STEP, 0);
			} else {
				manager.scrollToPositionWithOffset(currentItemPosition - VOICE_STEP > 0 ? currentItemPosition - VOICE_STEP : 0, 0);
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

	private void jumpPage(int position, boolean isNeedVoice) {
		if (mStoreList.size() > position) {
			if (mStoreList.get(position).getStatus()==Constant.STROE_STATUS_BREAK){
				return;
			}
			Intent intent = new Intent(mContext, FoodListActivity.class);
			intent.putExtra(Constant.STORE_ID, mStoreList.get(position).getWm_poi_id());
			intent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			startActivity(intent);
		}
	}


	public void loadFirstPage(StoreReq storeReq) {
		storeReq.setPage_index(1);;
		mPresenter.requestStoreList(storeReq);
		mRlTipNoResult.setVisibility(View.GONE);
	}

	public void searchLoadFirstPage(StoreReq storeReq) {
		mStoreList.clear();
		mStoreAdaper.notifyDataSetChanged();
		storeReq.setPage_index(1);
		storeReq.setLatitude(latitude);
		storeReq.setLongitude(longitude);
		mPresenter.requestStoreList(storeReq);
		mStoreReq = storeReq;
		mRlTipNoResult.setVisibility(View.GONE);
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH) {
			mTvSort.setText(getResources().getString(R.string.store_sort));
			mStoreReq.setSortType(Constant.COMPREHENSIVE);
			mTvSort.setTextColor(getResources().getColor(R.color.filter_selected));
			mTagLv.setTextViewDefaultColor();
			if (mLlFilter.getVisibility() == View.GONE){
				mLlFilter.setVisibility(View.VISIBLE);
			}
			
		}
	}

	public void recommendShopLoadFirstPage(StoreReq storeReq) {
		storeReq.setPage_index(1);
		storeReq.setLatitude(latitude);
		storeReq.setLongitude(longitude);
		mPresenter.requestStoreList(storeReq);
		mStoreReq = storeReq;
		mRlTipNoResult.setVisibility(View.GONE);
	}

	public void homeLoadFirstPage() {
		mStoreReq.setPage_index(1);
		mPresenter.requestStoreList(mStoreReq);
	}
	
}
