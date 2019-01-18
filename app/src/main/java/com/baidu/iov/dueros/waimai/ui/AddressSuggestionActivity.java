package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressHintListAdapter;
import com.baidu.iov.dueros.waimai.adapter.AddressSuggestionAdapter;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.presenter.AddressSuggestionPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.baidu.iov.dueros.waimai.view.ClearEditText;
import com.baidu.iov.dueros.waimai.view.RollTextView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddressSuggestionActivity extends BaseActivity<AddressSuggestionPresenter, AddressSuggestionPresenter.AddressSuggestionUi>
        implements TextWatcher, View.OnClickListener, AddressSuggestionPresenter.AddressSuggestionUi, OnGetSuggestionResultListener, AdapterView.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private AutoCompleteTextView mSearchEdit;
    private AddressSuggestionAdapter mAdapter;
    private List<PoiInfo> mAllSuggestions;
    private RollTextView mCityTV;
    private static final String TAG = AddressSuggestionActivity.class.getSimpleName();
    private LinearLayout mErrorLL;
    private String mCity;
    private boolean isEditModle;
    private TextView tv_title;
    private ImageView iv_back;
    private ImageView closeView;
    private ImageView iv_arrow, iv_refresh;
    private RelativeLayout selectCityView;
    private PoiSearch poiSearch;
    private LatLng location;
    private SuggestionSearch mSuggestionSearch =null;
    private ArrayList<String> suggest = null;
    private AddressHintListAdapter sugAdapter;

    @Override
    AddressSuggestionPresenter createPresenter() {
        return new AddressSuggestionPresenter();
    }

    @Override
    AddressSuggestionPresenter.AddressSuggestionUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);
        suggest = new ArrayList<>();
        initView();
        initData();
        initPoiInfo();
        initListener();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.address_title);
        iv_back = (ImageView) findViewById(R.id.address_back);
        iv_arrow = (ImageView) findViewById(R.id.arrow);
        iv_refresh = (ImageView) findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.address_search_rv);
        mSearchEdit = (AutoCompleteTextView) findViewById(R.id.address_search_edit);
        closeView = (ImageView) findViewById(R.id.suggestion_close);
        mCityTV = (RollTextView) findViewById(R.id.address_search_city);
        mErrorLL = (LinearLayout) findViewById(R.id.address_search_error);
        selectCityView = (RelativeLayout) findViewById(R.id.address_search_city_layout);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);
    }

    private void initData() {
        tv_title.setText(getResources().getString(R.string.address_title_search));
        mCity = getIntent().getStringExtra(Constant.ADDRESS_EDIT_INTENT_EXTRE_CITY);
        isEditModle = getIntent().getBooleanExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
        mCityTV.setText(mCity);
        if (mCity.length() > 2) {
            mCityTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mCityTV.setSingleLine(true);
            mCityTV.setMarqueeRepeatLimit(-1);
        }
        if (mCity.equals(Constant.GET_CITY_ERROR)) {
            iv_arrow.setVisibility(View.GONE);
            iv_refresh.setVisibility(View.VISIBLE);
        } else {
            iv_arrow.setVisibility(View.VISIBLE);
            iv_refresh.setVisibility(View.GONE);
        }
        mAllSuggestions = new ArrayList<>();
        mAdapter = new AddressSuggestionAdapter(mAllSuggestions);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        closeView.setOnClickListener(this);
        selectCityView.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new AddressSuggestionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, PoiInfo dataBean) {
                Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_POIACT_SELECT, EventType.TOUCH_TYPE);
                Lg.getInstance().d(TAG, "setOnItemClickListener");
                Intent intent = new Intent(AddressSuggestionActivity.this, AddressEditActivity.class);
                intent.putExtra(Constant.ADDRESS_SEARCCH_INTENT_EXTRE_ADDSTR, dataBean);
                if (isEditModle) {
                    setResult(Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE, intent);
                } else {
                    intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, false);
                    startActivity(intent);
                }
                finish();
            }
        });
        sugAdapter = new AddressHintListAdapter(this, suggest);
        mSearchEdit.setThreshold(1);
        mSearchEdit.setAdapter(sugAdapter);
        mSearchEdit.addTextChangedListener(this);
        mSearchEdit.setOnItemClickListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        GuidingAppear.INSTANCE.init(this, WaiMaiApplication.getInstance().getWaimaiBean().getAddress().getSearch_result());
    }

    private void initPoiInfo() {
        poiSearch = PoiSearch.newInstance();
        double span = LocationManager.SPAN + 0.5f;
        location = new LatLng(Constant.LATITUDE / span, Constant.LONGITUDE / span);
        // 设置检索监听器
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                    mErrorLL.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    return;
                }
                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// success
                    Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_POIACT_EDIT, EventType.TOUCH_TYPE);
                    List<PoiInfo> poiAddrInfoList = poiResult.getAllPoi();
                    mAllSuggestions.clear();
                    if (poiAddrInfoList != null && poiAddrInfoList.size() > 0) {
                        Collections.sort(poiAddrInfoList, new Comparator<PoiInfo>() {
                            @Override
                            public int compare(PoiInfo o1, PoiInfo o2) {
                                double o11 = DistanceUtil.getDistance(location, o1.getLocation());
                                double o22 = DistanceUtil.getDistance(location, o2.getLocation());
                                if (o11 > o22) {
                                    return 1;
                                }
                                if (o11 == o22) {
                                    return 0;
                                }
                                return -1;
                            }
                        });
                        mErrorLL.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAllSuggestions.addAll(poiAddrInfoList);
                    } else {
                        mErrorLL.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
    }

    private void citySearch(String city, final String key) {
        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
        citySearchOption.city(city);
        citySearchOption.keyword(key);
        citySearchOption.pageCapacity(20);
        citySearchOption.pageNum(0);
        poiSearch.searchInCity(citySearchOption);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence cs, int start, int before, int count) {
        if (cs.length() > 0) {
            closeView.setVisibility(View.VISIBLE);
            mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                    .keyword(cs.toString())
                    .city(mCity));
        } else {
            mAllSuggestions.clear();
            mAdapter.notifyDataSetChanged();
            mErrorLL.setVisibility(View.GONE);
            closeView.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (poiSearch != null) {
            poiSearch.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_back:
                finish();
                break;
            case R.id.address_search_city_layout:
                Intent intent = new Intent(AddressSuggestionActivity.this, CityPickerActivity.class);
                startActivityForResult(intent, Constant.CITY_REQUEST_CODE_CHOOSE);
                break;
            case R.id.suggestion_close:
                mSearchEdit.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.CITY_REQUEST_CODE_CHOOSE && resultCode == Constant.CITY_RESULT_CODE_CHOOSE) {
            String cityName = data.getStringExtra(Constant.CITYCODE);
            mCityTV.setText(cityName);
            if (cityName.length() > 2) {
                mCityTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                mCityTV.setSingleLine(true);
                mCityTV.setMarqueeRepeatLimit(-1);
            }
            if (cityName.equals(Constant.GET_CITY_ERROR)) {
                iv_arrow.setVisibility(View.GONE);
                iv_refresh.setVisibility(View.VISIBLE);
            } else {
                iv_arrow.setVisibility(View.VISIBLE);
                iv_refresh.setVisibility(View.GONE);
            }
            mCity = cityName;
            mAllSuggestions.clear();
            mAdapter.notifyDataSetChanged();
            mSearchEdit.setText("");
        }
    }

    @Override
    public void selectListItem(int i) {
        if (null != mAllSuggestions && mAllSuggestions.size() >= i) {
            Intent intent = new Intent(AddressSuggestionActivity.this, AddressEditActivity.class);
            intent.putExtra(Constant.ADDRESS_SEARCCH_INTENT_EXTRE_ADDSTR, mAllSuggestions.get(i));
            VoiceManager.getInstance().playTTS(AddressSuggestionActivity.this, String.format(getString(R.string.address_harvest), mAllSuggestions.get(i).getName()));
            if (isEditModle) {
                setResult(Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE, intent);
            } else {
                intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, false);
                startActivity(intent);
            }
            finish();
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }

        List<String> s = new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                s.add(info.key);
            }
        }
        suggest.clear();
        suggest.addAll(s);
        sugAdapter = new AddressHintListAdapter(this, suggest);
        mSearchEdit.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        citySearch(mCity, suggest.get(position));
    }
}
