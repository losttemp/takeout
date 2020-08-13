package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressSuggestionAdapter;
import com.baidu.iov.dueros.waimai.presenter.AddressSuggestionPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.RollTextView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.util.ArrayList;
import java.util.List;

public class AddressSuggestionActivity extends BaseActivity<AddressSuggestionPresenter, AddressSuggestionPresenter.AddressSuggestionUi>
        implements TextWatcher, View.OnClickListener, AddressSuggestionPresenter.AddressSuggestionUi, OnGetSuggestionResultListener {
    private RecyclerView mRecyclerView;
    private EditText mSearchEdit;
    private AddressSuggestionAdapter mAdapter;
    private List<SuggestionResult.SuggestionInfo> mAllSuggestions;
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
    //    private PoiSearch poiSearch;
    private SuggestionSearch mSuggestionSearch = null;

    private LocationManager mlocationManager;
    private int scanSpan = 1000;//请求定位间隔时间直到请求成功

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
        initView();
        initData();
//        initPoiInfo();
        initListener();
        requestCity();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.address_title);
        iv_back = (ImageView) findViewById(R.id.address_back);
        iv_arrow = (ImageView) findViewById(R.id.arrow);
        iv_refresh = (ImageView) findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.address_search_rv);
        mSearchEdit = (EditText) findViewById(R.id.address_search_edit);
        closeView = (ImageView) findViewById(R.id.suggestion_close);
        mCityTV = (RollTextView) findViewById(R.id.address_search_city);
        mErrorLL = (LinearLayout) findViewById(R.id.address_search_error);
        selectCityView = (RelativeLayout) findViewById(R.id.address_search_city_layout);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);
    }

    private void initData() {
        tv_title.setText(getResources().getString(R.string.address_title_search));
        isEditModle = getIntent().getBooleanExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
        mAllSuggestions = new ArrayList<>();
        mAdapter = new AddressSuggestionAdapter(mAllSuggestions);
        mRecyclerView.setAdapter(mAdapter);

        mCity = getString(R.string.city_loading);
        mCityTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mCityTV.setSingleLine(true);
        mCityTV.setMarqueeRepeatLimit(-1);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        closeView.setOnClickListener(this);
        selectCityView.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new AddressSuggestionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, SuggestionResult.SuggestionInfo dataBean) {
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
        mSearchEdit.addTextChangedListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getTakeout_out().getHints(), Constant.TTS_ADDRESS_SEARCH_RESULT);
    }

    private void initPoiInfo() {
//        poiSearch = PoiSearch.newInstance();
//        double span = LocationManager.SPAN + 0.5f;
//        location = new LatLng(Constant.LATITUDE / span, Constant.LONGITUDE / span);
//        // 设置检索监听器
//        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
//            @Override
//            public void onGetPoiResult(PoiResult poiResult) {
//                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
//                    mErrorLL.setVisibility(View.VISIBLE);
//                    mRecyclerView.setVisibility(View.GONE);
//                    return;
//                }
//                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// success
//                    Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_POIACT_EDIT, EventType.TOUCH_TYPE);
//                    List<PoiInfo> poiAddrInfoList = poiResult.getAllPoi();
//                    mAllSuggestions.clear();
//                    if (poiAddrInfoList != null && poiAddrInfoList.size() > 0) {
//                        Collections.sort(poiAddrInfoList, new Comparator<PoiInfo>() {
//                            @Override
//                            public int compare(PoiInfo o1, PoiInfo o2) {
//                                double o11 = DistanceUtil.getDistance(location, o1.getLocation());
//                                double o22 = DistanceUtil.getDistance(location, o2.getLocation());
//                                if (o11 > o22) {
//                                    return 1;
//                                }
//                                if (o11 == o22) {
//                                    return 0;
//                                }
//                                return -1;
//                            }
//                        });
//                        mErrorLL.setVisibility(View.GONE);
//                        mRecyclerView.setVisibility(View.VISIBLE);
//                        if (!TextUtils.isEmpty(mSearchEdit.getText().toString())){
//                            mAllSuggestions.addAll(poiAddrInfoList);
//                        }
//                    } else {
//                        mErrorLL.setVisibility(View.VISIBLE);
//                        mRecyclerView.setVisibility(View.GONE);
//                    }
//                    mAdapter.notifyDataSetChanged();
//                } else {
//                    mErrorLL.setVisibility(View.VISIBLE);
//                    mRecyclerView.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//
//            }
//
//            @Override
//            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
//
//            }
//
//            @Override
//            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
//
//            }
//        });
    }

    private void citySearch(String city, String key) {
        if (TextUtils.isEmpty(key)) {
            ToastUtils.show(mContext, getResources().getString(R.string.poi_search_hint_text), Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(city) ||
                getResources().getString(R.string.city_error).equals(city) ||
                getResources().getString(R.string.city_no_permission).equals(city) ||
                getResources().getString(R.string.city_loading).equals(city)) {
            ToastUtils.show(mContext, getResources().getString(R.string.poi_select_hint_text), Toast.LENGTH_SHORT);
            return;
        }
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(key)
                .city(city));
//        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
//        citySearchOption.city(city);
//        citySearchOption.keyword(key);
//        citySearchOption.pageCapacity(20);
//        citySearchOption.pageNum(0);
//        if (poiSearch != null) {
//            poiSearch.searchInCity(citySearchOption);
//        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence cs, int start, int before, int count) {
        if (cs.length() > 0) {
            closeView.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(cs.toString().trim())) {
                ToastUtils.show(mContext, getResources().getString(R.string.poi_search_hint_text), Toast.LENGTH_SHORT);
                return;
            }
            citySearch(mCity, mSearchEdit.getText().toString().trim());
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
        if (mlocationManager != null) {
            mlocationManager.stopLocation();
            mlocationManager = null;
        }
        mLocationListener = null;
//        if (poiSearch != null) {
//            poiSearch.destroy();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_back:
                hideSoftKeyboard();
                //防止键盘没有收回闪白
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);
                break;
            case R.id.address_search_city_layout:
                if (mlocationManager != null) {
                    mlocationManager.stopLocation();
                }
                if (mCity.equals(getString(R.string.city_loading))) {
                    mCity = getString(R.string.city_error);
                    mCityTV.setText(mCity);
                }
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
        if (i > 0) {
            i = i - 1;
        }
        if (null != mAllSuggestions && mAllSuggestions.size() > i) {
            Entry.getInstance().onEvent(Constant.ENTRY_ADDRESS_POIACT_YUYIN, EventType.VOICE_TYPE);
            Intent intent = new Intent(AddressSuggestionActivity.this, AddressEditActivity.class);
            intent.putExtra(Constant.ADDRESS_SEARCCH_INTENT_EXTRE_ADDSTR, mAllSuggestions.get(i));
            if (isEditModle) {
                setResult(Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE, intent);
            } else {
                intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, false);
                startActivity(intent);
            }
            finish();
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    //判断定位权限,权限没开给个提示
    public void requestCity() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Lg.getInstance().e("LocationManager", "AndPermission true");
            getLocationCity();
        } else {
            mCity = getString(R.string.city_no_permission);
            mCityTV.setText(mContext.getString(R.string.city_no_permission));
            mCityTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mCityTV.setSingleLine(true);
            mCityTV.setMarqueeRepeatLimit(-1);
            Constant.CITY = "city";
        }
    }

    private void getLocationCity() {
        if (mlocationManager == null) {
            mlocationManager = LocationManager.getInstance(getApplicationContext());
            mlocationManager.getLcation(null, null, scanSpan, true);
            mlocationManager.setLocationListener(mLocationListener);
        }
        mlocationManager.startLocation();
    }

    private BDAbstractLocationListener mLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                ToastUtils.show(mContext, "定位失败", Toast.LENGTH_SHORT);
                return;
            }
            int requestCode = bdLocation.getLocType();
            Lg.getInstance().d(TAG, "LocType:" + requestCode);
            switch (requestCode) {
                case LocationManager.TypeGpsLocation:
                case LocationManager.TypeOffLineLocation:
                case LocationManager.TypeNetWorkLocation:
                    Constant.LONGITUDE = (int) (bdLocation.getLongitude() * LocationManager.SPAN);
                    Constant.LATITUDE = (int) (bdLocation.getLatitude() * LocationManager.SPAN);
                    Lg.getInstance().d(TAG, "LocationListener success");
                    Lg.getInstance().d(TAG, "LocationListener success" + " ; bdLocation.getCity() = " + bdLocation.getCity());
                    String city = String.valueOf(bdLocation.getCity());
                    if ("null".equalsIgnoreCase(city)) {
                        city = mContext.getString(R.string.city_error);
                    }
                    Constant.CITY = city;
                    mCity = city;
                    mCityTV.setText(city);
                    double span = LocationManager.SPAN + 0.5f;
                    LatLng location = new LatLng(Constant.LATITUDE / span, Constant.LONGITUDE / span);
                    mAdapter.setLocation(location);
                    if (mlocationManager != null && !"null".equalsIgnoreCase(String.valueOf(bdLocation.getCity()))) {
                        mlocationManager.stopLocation();
                        mlocationManager = null;
                    }
                    break;
                default:
                    Lg.getInstance().d(TAG, "LocationListener error" + " : mlocationManager:" + mlocationManager);
                    if (mlocationManager != null) {
                        mlocationManager.requestLocation();
                    }
                    mCityTV.setText(mContext.getString(R.string.city_error));
                    iv_arrow.setVisibility(View.GONE);
                    iv_refresh.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
        mAllSuggestions.clear();
        if (allSuggestions != null && allSuggestions.size() > 0) {
            mErrorLL.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAllSuggestions.addAll(allSuggestions);
            mAdapter.notifyDataSetChanged();
        } else {
            mErrorLL.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

    }
}
