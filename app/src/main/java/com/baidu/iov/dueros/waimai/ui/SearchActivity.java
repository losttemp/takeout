package com.baidu.iov.dueros.waimai.ui;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.SearchHistroyAdapter;
import com.baidu.iov.dueros.waimai.adapter.SearchSuggestAdapter;
import com.baidu.iov.dueros.waimai.net.entity.request.SearchSuggestReq;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.SearchSuggestResponse;
import com.baidu.iov.dueros.waimai.presenter.SearchPresenter;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.SharedPreferencesUtils;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.util.ArrayList;
import java.util.List;
public class SearchActivity extends BaseActivity<SearchPresenter, SearchPresenter.SearchUi>
		implements
		SearchPresenter.SearchUi, View.OnClickListener {
	private static final String TAG = "SearchActivity";
	private AppCompatEditText mEtSearch;
	private FrameLayout mFragmentStoreList;
	private LinearLayout mLlHistory;
	private AppCompatImageView mIvDelete;
	private AppCompatImageView mIvClean;
	private AppCompatTextView mTvCancel;
	private ListView mLvHistory;
	private ListView mLvSuggest;

	private  View mDivision;


	private SearchPresenter mPresenter;
	private StoreReq mStoreReq;
	private List<String> mHistorys;
	private SearchHistroyAdapter mSearchHistroyAdapter;
	private SearchSuggestAdapter mSearchSuggestAdapter;
	private StoreListFragment mStoreListFragment;
	private List<SearchSuggestResponse.MeituanBean.DataBean.SuggestBean> mSuggests;
	private int mCurrentStatus;

	private static final int HEAD_NUM=1;

	private RelativeLayout mRlSearch;

	private SearchSuggestReq searchSuggestReq;

	private int mFromPageType;

	private ArrayList<String> prefix = new ArrayList<>();


	@Override
	SearchPresenter createPresenter() {
		return new SearchPresenter();
	}

	@Override
	SearchPresenter.SearchUi getUi() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		getIntentData();
		iniView();
		iniData();

	}

    @Override
    protected void onResume() {
        super.onResume();
        GuidingAppear.INSTANCE.init(this, WaiMaiApplication.getInstance().getWaimaiBean().getSearch().getSearch());
		AccessibilityClient.getInstance().register(this,true,prefix, null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		AccessibilityClient.getInstance().unregister(this);

	}

    public void getIntentData() {
		Intent intent = getIntent();
		if (intent != null) {
			mFromPageType = intent.getIntExtra(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE,Constant.STORE_FRAGMENT_FROM_HOME);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	private void setEtSelection(EditText et){

		if (et!=null){
			String text=et.getText().toString();
			int index =et.length()>20?20:et.length();
			et.setSelection(index);
		}

	}

	public void setmEtTipNoResult(){
		mEtSearch.setHint(getResources().getString(R.string.this_keyword_is_complicated));
		mEtSearch.setText("");
		mLlHistory.setVisibility(View.GONE);
		mLvSuggest.setVisibility(View.GONE);
		mFragmentStoreList.setVisibility(View.VISIBLE);
	}

	private void iniView() {
		mEtSearch =  findViewById(R.id.et_search);
		mFragmentStoreList =  findViewById(R.id.fragment_store_list);
		mLlHistory =  findViewById(R.id.ll_history);
		mIvDelete =  findViewById(R.id.iv_delete);
		mLvHistory =  findViewById(R.id.lv_history);
		mIvClean =  findViewById(R.id.iv_clean);
		mTvCancel =  findViewById(R.id.tv_cancel);
		mLvSuggest =  findViewById(R.id.lv_suggest);
		mRlSearch=  findViewById(R.id.rl_search);

		mDivision=new View(this);
		mDivision.setBackgroundColor(getResources().getColor(R.color.split_view_color));
		mLvHistory.addHeaderView(mDivision);
		mLvHistory.addFooterView(mDivision);
		mLvSuggest.addHeaderView(mDivision);
		prefix.add(getResources().getString(R.string.prefix_choice));
	
	}


	public void setmLlHistoryVisibility(){
		if (mHistorys.isEmpty()){
			mLlHistory.setVisibility(View.GONE);
		}else{
			mLlHistory.setVisibility(View.VISIBLE);
		}
	}
	private void iniData() {
		mPresenter = getPresenter();
		mStoreReq = new StoreReq();



		//fragment
		mStoreListFragment = new StoreListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, Constant.STORE_FRAGMENT_FROM_SEARCH);
		mStoreListFragment.setArguments(bundle);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.fragment_store_list, mStoreListFragment);
		transaction.commit();

		//search history
		mHistorys = new ArrayList<>();
		SharedPreferencesUtils.getSearchHistory(mHistorys);
		mSearchHistroyAdapter = new SearchHistroyAdapter(mHistorys, SearchActivity.this);
		mLvHistory.setAdapter(mSearchHistroyAdapter);
		setmLlHistoryVisibility();

		//search suggest
		mSuggests = new ArrayList<>();
		mSearchSuggestAdapter = new SearchSuggestAdapter(mSuggests, SearchActivity.this);
		mLvSuggest.setAdapter(mSearchSuggestAdapter);

		mEtSearch.setOnClickListener(this);
		mIvDelete.setOnClickListener(this);
		mIvClean.setOnClickListener(this);
		mTvCancel.setOnClickListener(this);
		mRlSearch.setOnClickListener(this);

		mIvDelete.setAccessibilityDelegate(new View.AccessibilityDelegate(){
			@Override
			public boolean performAccessibilityAction(View host, int action, Bundle args) {
				switch (action) {
					case AccessibilityNodeInfo.ACTION_CLICK:
						deleteAll();
						StandardCmdClient.getInstance().playTTS(mContext, mContext.getString(R.string.tts_delete_address));
						break;
					default:
						break;
				}
				return true;
			}});

		mLvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Entry.getInstance().onEvent(Constant.EVENT_HISTORY_ITEM_CLICK,EventType.TOUCH_TYPE);
				searchKeyword(mHistorys.get(position-HEAD_NUM));
                GuidingAppear.INSTANCE.init(SearchActivity.this, WaiMaiApplication.getInstance().getWaimaiBean().getSearch().getResult());
			}
		});

		mLvSuggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SearchSuggestResponse.MeituanBean.DataBean.SuggestBean suggest = mSuggests.get(position-HEAD_NUM);
				String name = suggest.getSuggest_query();
				if (suggest.getType() == 0&&suggest.getPoi_addition_info() != null ) {
					 if (suggest.getPoi_addition_info().getStatus()==Constant.STROE_STATUS_BREAK){
						 searchKeyword(name);
					 }else{
						 SharedPreferencesUtils.saveSearchHistory(suggest.getSuggest_query(), mHistorys);
						 mSearchHistroyAdapter.notifyDataSetChanged();
						 mEtSearch.setText(name);
						 setEtSelection(mEtSearch);
						 changeStatus(Constant.SEARCH_STATUS_HISTORY);
						 Intent intent = new Intent(SearchActivity.this, FoodListActivity.class);
						 intent.putExtra(Constant.STORE_ID, suggest.getPoi_addition_info().getWm_poi_id());
						 intent.putExtra("flag",true);
						 intent.putExtra("latitude", Constant.GOODS_LATITUDE);
						 intent.putExtra("longitude", Constant.GOODS_LONGITUDE);
						 startActivity(intent);
					 }

				} else {
					searchKeyword(name);
				}
			}
		});

		mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String keyword= mEtSearch.getText().toString().trim();
					if (!keyword.isEmpty()) {
						searchKeyword(keyword);
						return true;
					}
				}
				return false;
			}
		});

		mEtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String keyWord =mEtSearch.getText().toString().trim();
				if (TextUtils.isEmpty(keyWord)) {
					mIvClean.setVisibility(View.GONE);
					changeStatus(Constant.SEARCH_STATUS_HISTORY);
				} else {
					Entry.getInstance().onEvent(Constant.EVENT_INPUT_SEARCH, EventType.TOUCH_TYPE);
					mIvClean.setVisibility(View.VISIBLE);
					mLvSuggest.setVisibility(View.VISIBLE);
					mLlHistory.setVisibility(View.GONE);
					mFragmentStoreList.setVisibility(View.GONE);
					if (searchSuggestReq==null) {
						searchSuggestReq = new SearchSuggestReq();
					}
					searchSuggestReq.setLatitude(Constant.GOODS_LATITUDE);
					searchSuggestReq.setLongitude(Constant.GOODS_LONGITUDE);
					searchSuggestReq.setQuery(keyWord);
					mPresenter.requestSuggestList(searchSuggestReq);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_cancel:
				cancel();
				break;

			case R.id.iv_delete:
				deleteAll();
				break;

			case R.id.iv_clean:
				View currentFocus = this.getCurrentFocus();
				if (currentFocus != null) {
					currentFocus.clearFocus();
				}
				mEtSearch.setText("");
				break;

			case R.id.rl_search:
				mEtSearch.setHint(getResources().getString(R.string.search_hint_text));
				showKeyboard();
				break;

			default:
				break;
		}

	}

	private void showKeyboard(){
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(mEtSearch, 0);
	}

	private void deleteAll() {
		ConfirmDialog dialog = new ConfirmDialog.Builder(this)
				.setTitle(R.string.delete_history_title)
				.setMessage(R.string.delete_history_message)
				.setNegativeButton(R.string.delete_history_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferencesUtils.clearSearchHistory();
						SharedPreferencesUtils.getSearchHistory(mHistorys);
						mSearchHistroyAdapter.notifyDataSetChanged();
						mLlHistory.setVisibility(View.GONE);
						dialog.dismiss();
					}
				})
				.setPositiveButton(R.string.delete_history_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.create();
		dialog.show();
	}

	@Override
	public void close() {
		cancel();
	}

	@Override
	public void selectListItem(int index) {
		if (index>=0) {
			VoicesSelectListItem(index);
		}
	}

	public void VoicesSelectListItem(int index){
		if (mCurrentStatus == Constant.SEARCH_STATUS_HISTORY && mHistorys.size() > index) {
			Entry.getInstance().onEvent(Constant.EVENT_HISTORY_ITEM_VOIVE,EventType.VOICE_TYPE);
			StandardCmdClient.getInstance().playTTS(SearchActivity.this, getString(R.string.tts_search_for_you));
			searchKeyword(mHistorys.get(index));
		}
	}

	@Override
	public void onSuggestSuccess(SearchSuggestResponse data) {
		mSuggests.clear();
		mSuggests.addAll(data.getMeituan().getData().getSuggest());
		mSearchSuggestAdapter.notifyDataSetChanged();
		Lg.getInstance().d(TAG,"mSuggests:"+mSuggests);
	}

	@Override
	public void onSuggestFailure(String msg) {

	}

	public void searchKeyword(String keyword) {
		mEtSearch.setText(keyword);
		setEtSelection(mEtSearch);
		mStoreReq.setSortType(null);
		if (!TextUtils.isEmpty(keyword)) {
			mStoreReq.setKeyword(keyword);
			SharedPreferencesUtils.saveSearchHistory(keyword, mHistorys);
			mSearchHistroyAdapter.notifyDataSetChanged();
		} else {
			mStoreReq.setKeyword(null);
		}
		mStoreListFragment.searchLoadFirstPage(mStoreReq);
		changeStatus(Constant.SEARCH_STATUS_FRAGMENT);
		hideSoftKeyboard();
	}

	private void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context
				.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
	}

	private void changeStatus(int status) {
		if (status == Constant.SEARCH_STATUS_SUGGEST) {
			mLlHistory.setVisibility(View.GONE);
			mFragmentStoreList.setVisibility(View.GONE);
			mLvSuggest.setVisibility(View.VISIBLE);
		} else if (status == Constant.SEARCH_STATUS_FRAGMENT) {
			mLlHistory.setVisibility(View.GONE);
			mLvSuggest.setVisibility(View.GONE);
			mFragmentStoreList.setVisibility(View.VISIBLE);
			if (mSuggests.size() > 0) {
				mSuggests.clear();
				mSearchSuggestAdapter.notifyDataSetChanged();
			}
		} else {
			setmLlHistoryVisibility();
			mLvSuggest.setVisibility(View.GONE);
			 if(mFragmentStoreList!=null) {
				 mFragmentStoreList.setVisibility(View.GONE);
			 }
			if (mSuggests.size() > 0) {
				mSuggests.clear();
				mSearchSuggestAdapter.notifyDataSetChanged();
			}
		}
		mCurrentStatus = status;

	}

	private void cancel() {
		if (mCurrentStatus == Constant.SEARCH_STATUS_FRAGMENT) {
			changeStatus(Constant.SEARCH_STATUS_HISTORY);
		} else {
			Entry.getInstance().onEvent(Constant.EVENT_BACK,EventType.TOUCH_TYPE);
			finish();
		}
	}

	public int getStatus() {
		return mCurrentStatus;
	}

}
