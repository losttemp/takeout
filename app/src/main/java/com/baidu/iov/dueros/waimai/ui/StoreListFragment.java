package com.baidu.iov.dueros.waimai.ui;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
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
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.FilterPopWindow;
import com.baidu.iov.dueros.waimai.view.SortPopWindow;
import com.baidu.iov.dueros.waimai.view.SortTypeTagListView;
import com.baidu.iov.faceos.client.GsonUtil;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;
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
	private RelativeLayout mRlTipNoResult;
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
	private List<FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean> mSortTypeTabs = new ArrayList<>();
	private List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean> mFilterList
			= new ArrayList<>();

	private StoreReq mStoreReq;
	private SortPopWindow mSortPopWindow;
	private FilterPopWindow mFilterPopWindow;
	private int mFromPageType;
	
	//语音选择下一页时跳动的item数目
	private static final int VOICE_STEP_HOME = 3,VOICE_STEP_SHOP=4,VOICE_STEP_SEARCH=3;
	private View mView;

	private FilterConditionReq filterConditionReq;

	private LinearLayout mLoading;

	private int  shopTotalNum;



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
		mContext = getActivity();
		iniView(view);
		iniData();

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerReceiver();
	}

	public static void getLocation(Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences("_cache", Context.MODE_PRIVATE);
		String addressDataJson = sharedPreferences.getString(Constant.ADDRESS_DATA, null);
		if (addressDataJson != null) {
			AddressListBean.IovBean.DataBean mAddressData = GsonUtil.fromJson(addressDataJson, AddressListBean.IovBean.DataBean.class);
			Constant.GOODS_LATITUDE = mAddressData.getLatitude() != null ? mAddressData.getLatitude() : -1;
			Constant.GOODS_LONGITUDE = mAddressData.getLongitude() != null ? mAddressData.getLongitude() : -1;
			Lg.getInstance().d(TAG, "latitude:" + Constant.GOODS_LATITUDE  + " longitude:" + Constant.GOODS_LONGITUDE );
		}
	}

	private void iniView(View view) {
		mLlFilter = view.findViewById(R.id.ll_filter);
		mRlSort = view.findViewById(R.id.rl_sort);
		mTvSort = view.findViewById(R.id.tv_sort);
		mIvSort = view.findViewById(R.id.iv_sort);
		mRlFilter = view.findViewById(R.id.rl_filter);
		mTvFilter = view.findViewById(R.id.tv_filter);
		mIvFilter = view.findViewById(R.id.iv_filter);
		mRefreshLayout = view.findViewById(R.id.refresh_layout);
		mRvStore = view.findViewById(R.id.rv_store);
		mViewBg = view.findViewById(R.id.view_bg);
		mView = view.findViewById(R.id.view);
		mTvTipNoResult = view.findViewById(R.id.tv_tip_no_result);
		mRlTipNoResult = view.findViewById(R.id.rl_tip_no_result);
		mWarnNoInternet = view.findViewById(R.id.warn_no_internet);
		mNoInternetBtn = view.findViewById(R.id.no_internet_btn);
		mLoading = view.findViewById(R.id.ll_loading);
		mTagLv = view.findViewById(R.id.tag_lv);
		mTagLv.setItemClickListener(new SortTypeTagListView.OnItemClickListener() {
			@Override
			public void onClick(int sortType) {
				mTvSort.setText(getResources().getString(R.string.store_sort));
				addTagLvItemClickEvent(sortType);
				if (sortType == Constant.COMPREHENSIVE) {
					mTvSort.setTextColor(getResources().getColor(R.color.filter_selected));
				} else {
					mTvSort.setTextColor(getResources().getColor(R.color.white_60));
				}
				mStoreReq.setSortType(sortType);
				mRvStore.scrollToPosition(0);
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

		mStoreAdaper = new StoreAdaper(mStoreList, mContext, mFromPageType);
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRvStore.setLayoutManager(layoutManager);
		mRvStore.setAdapter(mStoreAdaper);
		mStoreAdaper.setItemClickListener(new StoreAdaper.OnItemClickListener() {
			@Override
			public void onItemClick(int position) {
				addStoreItemClickEvent();
				jumpPage(position, false);
			}
		});

		setRefreshView();
		mRlSort.setOnClickListener(this);
		mRlFilter.setOnClickListener(this);
		mNoInternetBtn.setOnClickListener(this);
		mRlSort.setAccessibilityDelegate(new View.AccessibilityDelegate(){
			@Override
			public boolean performAccessibilityAction(View host, int action, Bundle args) {
				switch (action) {
					case AccessibilityNodeInfo.ACTION_CLICK:
						showFilterPop();
						StandardCmdClient.getInstance().playTTS(mContext,"BUBBLE");
						break;
					default:
						break;
				}
				return true;
			}});
		mRlFilter.setAccessibilityDelegate(new View.AccessibilityDelegate(){
			@Override
			public boolean performAccessibilityAction(View host, int action, Bundle args) {
				switch (action) {
					case AccessibilityNodeInfo.ACTION_CLICK:
						showFilterPop();
						StandardCmdClient.getInstance().playTTS(mContext,"BUBBLE");
						break;
					default:
						break;
				}
				return true;
			}});

		mStoreAdaper.setItemAccessibilityDelegate(new StoreAdaper.ItemAccessibilityDelegate() {
			@Override
			public void onItemAccessibilityDelegate(int position) {
				VoicesSelectListItem(position);
			}
		});

		mStoreReq = new StoreReq();
		mStoreReq.setLatitude(Constant.GOODS_LATITUDE);
		mStoreReq.setLongitude(Constant.GOODS_LONGITUDE );
		mStoreReq.setSortType(Constant.COMPREHENSIVE);
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME&!HomeActivity.fromLogin) {
			getLocation(mContext);
			homeLoadFirstPage();
		}
		filterConditionReq = new FilterConditionReq();
		getFilterList();
	}
	
	public void getFilterList(){
		filterConditionReq.setLatitude(Constant.GOODS_LATITUDE);
		filterConditionReq.setLongitude(Constant.GOODS_LONGITUDE );
		getPresenter().requestFilterList(filterConditionReq);
	}
	
	

	private void addStoreItemClickEvent(){
		if (mFromPageType==Constant.STORE_FRAGMENT_FROM_HOME){
			Entry.getInstance().onEvent(Constant.EVENT_SELECTE_STORE_CLICK_FROM_HOME,EventType.TOUCH_TYPE);
		}
		else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_FLOWER){
			Entry.getInstance().onEvent(Constant.EVENT_SELECTE_STORE_CLICK_FROM_FLOWER,EventType.TOUCH_TYPE);
		}else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_CAKE){
			Entry.getInstance().onEvent(Constant.EVENT_SELECTE_STORE_CLICK_FROM_CAKE,EventType.TOUCH_TYPE);
		}
	}

	private void addTagLvItemClickEvent(int sortType){
		if (sortType==Constant.SALES) {
			if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME) {
				Entry.getInstance().onEvent(Constant.EVENT_CLICK_SALES_FROM_HOME, EventType.TOUCH_TYPE);
			} else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_FLOWER) {
				Entry.getInstance().onEvent(Constant.EVENT_CLICK_SALES_FROM_FLOWER, EventType.TOUCH_TYPE);
			} else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_CAKE) {
				Entry.getInstance().onEvent(Constant.EVENT_CLICK_SALES_FROM_CAKE, EventType.TOUCH_TYPE);
			}
		}else  if (sortType==Constant.DISTANCE) {
			if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME) {
				Entry.getInstance().onEvent(Constant.EVENT_CLICK_DISTANCE_FROM_HOME, EventType.TOUCH_TYPE);
			} else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_FLOWER) {
				Entry.getInstance().onEvent(Constant.EVENT_CLICK_DISTANCE_FROM_FLOWER, EventType.TOUCH_TYPE);
			} else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_CAKE) {
				Entry.getInstance().onEvent(Constant.EVENT_CLICK_DISTANCE_FROM_CAKE, EventType.TOUCH_TYPE);
			}
		}
	}
	
	
	private void showSortPop(){
		if (mSortPopWindow == null) {
			mSortPopWindow = new SortPopWindow(mContext, mSortList, (new SortPopWindow
					.OnSelectedSortListener() {
				@Override
				public void OnSelectedSort(FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean type) {
					mStoreReq.setSortType((int) type.getCode());
					mTvSort.setText(type.getName());
					mRvStore.scrollToPosition(0);
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

		if (mFilterPopWindow==null||!mFilterPopWindow.isShowing()) {
			mTagLv.setTextViewDefaultColor();
			mTvSort.setTextColor(getResources().getColor(R.color.filter_selected));
			mIvSort.setImageResource(R.drawable.arrow_up);
			mSortPopWindow.showAsDropDown(mView);
			mViewBg.setVisibility(View.VISIBLE);
		}
	}

	private void showFilterPop(){
		if (mFilterPopWindow == null) {
			mFilterPopWindow = new FilterPopWindow(mContext, mFilterList, new
					FilterPopWindow.OnClickOkListener() {
						@Override
						public void onClickOk(String migFilter) {
							mStoreReq.setMigFilter(migFilter);
							if (!migFilter.isEmpty()) {
								mTvFilter.setTextColor(getResources().getColor(R.color.filter_selected));
							} else {
								mTvFilter.setTextColor(getResources().getColor(R.color.white_60));
							}
							mRvStore.scrollToPosition(0);
							loadFirstPage(mStoreReq);
						}
					});
			mFilterPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
				@Override
				public void onDismiss() {
					mIvFilter.setImageResource(R.drawable.arrow_down);
					mViewBg.setVisibility(View.GONE);
					if (mStoreReq.getMigFilter() == null || mStoreReq.getMigFilter().isEmpty()) {
						mTvFilter.setTextColor(getResources().getColor(R.color.white_60));
					} else {
						mTvFilter.setTextColor(getResources().getColor(R.color.filter_selected));
					}
				}
			});
		}

		if (mSortPopWindow==null||!mSortPopWindow.isShowing()) {
			mTvFilter.setTextColor(getResources().getColor(R.color.filter_selected));
			mIvFilter.setImageResource(R.drawable.arrow_up);
			mFilterPopWindow.showAsDropDown(mView);
			mViewBg.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_sort:
				addSortEvent();
				showSortPop();
				break;

			case R.id.rl_filter:
				addFliterEvent();
				showFilterPop();
				break;

			case R.id.no_internet_btn:
				refresh();
				break;

			default:
				break;
		}

	}

	private void addSortEvent(){
		if (mFromPageType==Constant.STORE_FRAGMENT_FROM_HOME){
			Entry.getInstance().onEvent(Constant.EVENT_CLICK_SORT_FROM_HOME,EventType.TOUCH_TYPE);
		}
		else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_FLOWER){
			Entry.getInstance().onEvent(Constant.EVENT_CLICK_SORT_FROM_FLOWER,EventType.TOUCH_TYPE);
		}else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_CAKE){
			Entry.getInstance().onEvent(Constant.EVENT_CLICK_SORT_FROM_CAKE,EventType.TOUCH_TYPE);
		}
	}

	private void addFliterEvent(){
		if (mFromPageType==Constant.STORE_FRAGMENT_FROM_HOME){
			Entry.getInstance().onEvent(Constant.EVENT_CLICK_FILTER_FROM_HOME,EventType.TOUCH_TYPE);
		}
		else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_FLOWER){
			Entry.getInstance().onEvent(Constant.EVENT_CLICK_FILTER_FROM_FLOWER,EventType.TOUCH_TYPE);
		}else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_CAKE){
			Entry.getInstance().onEvent(Constant.EVENT_CLICK_FILTER_FROM_CAKE,EventType.TOUCH_TYPE);
		}
	}
	
	private void refresh(){
		if (NetUtil.getNetWorkState(mContext)) {
			if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME) {
				mPresenter.requestFilterList(filterConditionReq);
				homeLoadFirstPage();
			} else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH) {
				mPresenter.requestFilterList(filterConditionReq);
				searchLoadFirstPage(mStoreReq);
			} else {
				mPresenter.requestFilterList(filterConditionReq);
				recommendShopLoadFirstPage(mStoreReq);
			}
		} else {
			ToastUtils.show(mContext, getResources().getString(R.string.is_network_connected), Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void update(StoreResponse data) {
		Lg.getInstance().d(TAG, "data:" + data);
		mStoreData = data.getMeituan().getData();
		shopTotalNum=mStoreData.getPoi_total_num();
		if (mStoreData.getCurrent_page_index() <= 1) {
			mStoreList.clear();
		}
		mWarnNoInternet.setVisibility(View.GONE);
		mLoading.setVisibility(View.GONE);
		mStoreList.addAll(data.getMeituan().getData().getOpenPoiBaseInfoList());
		mStoreAdaper.notifyDataSetChanged();
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
					((SearchActivity) mContext).setmEtTipNoResult();

				}
			} else if (mFromPageType == Constant.STORE_FRAGMENT_FROM_HOME || mFromPageType == Constant.STORE_FRAGMENT_FROM_FLOWER
					|| mFromPageType == Constant.STORE_FRAGMENT_FROM_CAKE|| mFromPageType == Constant.STORE_FRAGMENT_FROM_FOOD) {
				if (!TextUtils.isEmpty(mStoreReq.getMigFilter())) {
					mTvTipNoResult.setText(WaiMaiApplication.getInstance().getString(R.string
							.no_search_result_filter));
				} else {
					mTvTipNoResult.setText(WaiMaiApplication.getInstance().getString(R.string
							.no_search_result_position));
					mLlFilter.setVisibility(View.GONE);
					mView.setVisibility(View.GONE);
				}
			}
			mRlTipNoResult.setVisibility(View.VISIBLE);
			mRefreshLayout.setVisibility(View.GONE);
		} else {
			mView.setVisibility(View.VISIBLE);
			mRlTipNoResult.setVisibility(View.GONE);
			mRefreshLayout.setVisibility(View.VISIBLE);
			mLlFilter.setVisibility(View.VISIBLE);
			Lg.getInstance().d(TAG, "mStoreList:" + mStoreList.get(0));
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
		Lg.getInstance().d(TAG, "msg:" + msg);
		mLoading.setVisibility(View.GONE);
		mWarnNoInternet.setVisibility(View.VISIBLE);
		mView.setVisibility(View.GONE);
		mLlFilter.setVisibility(View.GONE);
		mRlTipNoResult.setVisibility(View.GONE);
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
				if (sortType.getPosition() == FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean.TABPOS) {
					sortTypeTabs.add(sortType);
				}
			}
		}
		return sortTypeTabs;
	}

	@Override
	public void updateFilterCondition(FilterConditionResponse data) {
		if (data == null || data.getMeituan() == null) {
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

	private void addStoreItemVoiceEvent(){
		if (mFromPageType==Constant.STORE_FRAGMENT_FROM_HOME){
			Entry.getInstance().onEvent(Constant.EVENT_SELECTE_STORE_VOICE_FROM_HOME,EventType.VOICE_TYPE);
		}
		else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_FLOWER){
			Entry.getInstance().onEvent(Constant.EVENT_SELECTE_STORE_VOICE_FROM_FLOWER,EventType.VOICE_TYPE);
		}else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_CAKE){
			Entry.getInstance().onEvent(Constant.EVENT_SELECTE_STORE_VOICE_FROM_CAKE,EventType.VOICE_TYPE);
		}
	}

	@Override
	public void selectListItem(int index) {
		if (index>=0) {
			VoicesSelectListItem(index);
		}
	}

	public void VoicesSelectListItem(int index){
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH && ((SearchActivity) mContext)
				.getStatus() != Constant.SEARCH_STATUS_FRAGMENT) {
			return;
		}
		addStoreItemVoiceEvent();
		if (index-1>=0) {
			jumpPage(index, true);
		}
	}

	@Override
	public void nextPage(boolean isNextPage) {
		if (mFromPageType == Constant.STORE_FRAGMENT_FROM_SEARCH && ((SearchActivity) mContext)
				.getStatus() != Constant.SEARCH_STATUS_FRAGMENT) {
			return;
		}

		if (mRlTipNoResult.getVisibility() == View.GONE) {
			addNextPageEvent();
			LinearLayoutManager manager = (LinearLayoutManager) mRvStore.getLayoutManager();
			assert manager != null;
			int currentItemPosition = manager.findFirstVisibleItemPosition();
			if (isNextPage) {
				if (currentItemPosition + getPageNum() * 2 > mStoreList.size() && mRefreshLayout != null) {
					mRefreshLayout.autoLoadmore(100);
				}
				manager.scrollToPositionWithOffset(currentItemPosition + getPageNum(), 0);
				if (currentItemPosition==shopTotalNum){
					StandardCmdClient.getInstance().playTTS(mContext, getString(R.string.last_page));
				}
			} else {
				if (currentItemPosition == 0) {
					StandardCmdClient.getInstance().playTTS(mContext, getString(R.string.first_page));
				}
				manager.scrollToPositionWithOffset(currentItemPosition - getPageNum() > 0 ? currentItemPosition - getPageNum() : 0, 0);
			}
		}

	}

	private int getPageNum(){
		if (mFromPageType==Constant.STORE_FRAGMENT_FROM_HOME){
			return  VOICE_STEP_HOME;
		}else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_SEARCH){
			return  VOICE_STEP_SEARCH;
		}
		return  VOICE_STEP_SHOP;
	}
	
	private void addNextPageEvent(){
		if (mFromPageType==Constant.STORE_FRAGMENT_FROM_HOME){
			Entry.getInstance().onEvent(Constant.EVENT_NEXT_PAGE_VOICE_FROM_HOME,EventType.VOICE_TYPE);
		}
		else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_FLOWER){
			Entry.getInstance().onEvent(Constant.EVENT_NEXT_PAGE_VOICE_FROM_FLOWER,EventType.VOICE_TYPE);
		}else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_CAKE){
			Entry.getInstance().onEvent(Constant.EVENT_NEXT_PAGE_VOICE_FROM_CAKE,EventType.VOICE_TYPE);
		}
	}

	private void setRefreshView() {

		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshLayout) {
				loadFirstPage(mStoreReq);
				mLoading.setVisibility(View.GONE);
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
			if (mStoreList.get(position).getStatus() == Constant.STROE_STATUS_BREAK) {
				return;
			}
			Intent intent = new Intent(mContext, FoodListActivity.class);
			intent.putExtra(Constant.STORE_ID, mStoreList.get(position).getWm_poi_id());
			intent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
			intent.putExtra("latitude", Constant.GOODS_LATITUDE);
			intent.putExtra("longitude", Constant.GOODS_LONGITUDE);
			startActivity(intent);
		}
	}

	private boolean netDataReque() {
		if (!NetUtil.getNetWorkState(mContext)) {
			mRefreshLayout.setVisibility(View.GONE);
			mWarnNoInternet.setVisibility(View.VISIBLE);
			mRlTipNoResult.setVisibility(View.GONE);
			mLlFilter.setVisibility(View.GONE);
			mView.setVisibility(View.GONE);
			return true;
		}
		return false;
	}

	public void loadFirstPage(StoreReq storeReq) {
		if (!netDataReque()) {
			mLoading.setVisibility(View.VISIBLE);
			mRlTipNoResult.setVisibility(View.GONE);
			storeReq.setPage_index(1);
			mPresenter.requestStoreList(storeReq);
		}
	}

	public void searchLoadFirstPage(StoreReq storeReq) {
		if (!netDataReque()) {
			mStoreList.clear();
			mStoreAdaper.notifyDataSetChanged();
			mRlTipNoResult.setVisibility(View.GONE);
			mLoading.setVisibility(View.VISIBLE);
			mLlFilter.setVisibility(View.GONE);
			mTvSort.setText(getResources().getString(R.string.store_sort));
			mStoreReq.setSortType(Constant.COMPREHENSIVE);
			mTvSort.setTextColor(getResources().getColor(R.color.filter_selected));
			mTagLv.setTextViewDefaultColor();

			storeReq.setPage_index(1);
			storeReq.setLatitude(Constant.GOODS_LATITUDE);
			storeReq.setLongitude(Constant.GOODS_LONGITUDE);
			mPresenter.requestStoreList(storeReq);
			mStoreReq = storeReq;

		}
	}

	public void recommendShopLoadFirstPage(StoreReq storeReq) {
		if (!netDataReque()) {
			mLoading.setVisibility(View.VISIBLE);
			mRlTipNoResult.setVisibility(View.GONE);
			storeReq.setPage_index(1);
			storeReq.setLatitude(Constant.GOODS_LATITUDE);
			storeReq.setLongitude(Constant.GOODS_LONGITUDE);
			mPresenter.requestStoreList(storeReq);
			mStoreReq = storeReq;
		}
	}

	public void homeLoadFirstPage() {
		if (!netDataReque()) {
			mLoading.setVisibility(View.VISIBLE);
			mStoreReq.setPage_index(1);
			mStoreReq.setLatitude(Constant.GOODS_LATITUDE);
			mStoreReq.setLongitude(Constant.GOODS_LONGITUDE);
			mPresenter.requestStoreList(mStoreReq);
		}
	}



	BroadcastReceiver mPullLocationBroadReceive =new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
				getLocation(mContext);
				if (!Constant.GOODS_LATITUDE.equals(mStoreReq.getLatitude())){
					filterConditionReq.setLatitude(Constant.GOODS_LATITUDE);
					filterConditionReq.setLongitude(Constant.GOODS_LONGITUDE);
					
					mStoreReq.setLatitude(Constant.GOODS_LATITUDE);
					mStoreReq.setLongitude(Constant.GOODS_LONGITUDE);
					mStoreReq.setSortType(Constant.COMPREHENSIVE);
					mTvSort.setText(getResources().getString(R.string.store_sort));
					mTvSort.setTextColor(getResources().getColor(R.color.white_60));
					mStoreReq.setMigFilter("");
					mTvFilter.setTextColor(getResources().getColor(R.color.white_60));
					if (mContext instanceof HomeActivity){
						((HomeActivity)mContext).setAddress();
					}
					refresh();
				}
			  
			
		}
	};

	private void registerReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.PULL_LOCATION);
		mContext.registerReceiver(mPullLocationBroadReceive, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mContext.unregisterReceiver(mPullLocationBroadReceive);
	}
}