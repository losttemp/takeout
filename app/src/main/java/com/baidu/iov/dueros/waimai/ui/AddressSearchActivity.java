package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressSearchAdapter;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.List;

public class AddressSearchActivity extends AppCompatActivity {
    private ListView mListView;
    private AutoCompleteTextView mSearchEdit;
    private AddressSearchAdapter mAdapter;
    private List<SuggestionResult.SuggestionInfo> mAllSuggestions;
    private List<PoiInfo> mPoiList;
    private BDLocation mBDLocation;
    private static final int POI_MODEL_NEERBY = 0;
    private static final int POI_MODEL_SEARCH = 1;
    private TextView mCityTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);
        initView();
        initData();
    }

    private void initData() {
        mBDLocation= getIntent().getParcelableExtra("address_edit_bd_location");
        Log.d("hhr",mBDLocation.getLongitude()+"--"+mBDLocation.getLatitude());
        final LatLng location = new LatLng(mBDLocation.getLatitude(), mBDLocation.getLongitude());
//        final LatLng location = new LatLng(39.92235, 116.380338);
        initReverseGeoCode(location);
        mAllSuggestions = new ArrayList<>();
        mPoiList = new ArrayList<>();
        mAdapter = new AddressSearchAdapter(mAllSuggestions, mPoiList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddressSearchActivity.this, AddressEditActivity.class);
                int itemViewType = mAdapter.getItemViewType(position);
                LatLng pt;
                String addr;
                if (itemViewType == POI_MODEL_NEERBY) {
                    pt = mPoiList.get(position).getLocation();
                    addr = mPoiList.get(position).getName();
                } else {
                    pt = mAllSuggestions.get(position).getPt();
                    addr = mAllSuggestions.get(position).getKey();
                }

                intent.putExtra("address_search_lat", pt.longitude);
                intent.putExtra("addStr",addr);
                intent.putExtra("address_search_lat",pt.longitude);
                setResult(3, intent);
                finish();
            }
        });
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    mAllSuggestions.clear();
                }
                initSuggestionInfo(mBDLocation.getCity(), s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mSearchEdit.addTextChangedListener(watcher);
//        TODO
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.address_search_lv);
        mSearchEdit = (AutoCompleteTextView) findViewById(R.id.address_search_edit);
        mCityTV = (TextView) findViewById(R.id.address_search_city);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initReverseGeoCode(LatLng mLocation) {
        GeoCoder geoCoder = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
                if (poiList != null) {
                    mPoiList.addAll(poiList);
                    mAdapter.setmPoiInfos(mPoiList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        geoCoder.setOnGetGeoCodeResultListener(listener);
        ReverseGeoCodeOption location = new ReverseGeoCodeOption().location(mLocation);
        geoCoder.reverseGeoCode(location);
    }

    private void initSuggestionInfo(String city, final String key) {
        SuggestionSearch suggestionSearch = SuggestionSearch.newInstance();
        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (key.length() == 0) {
                    mAllSuggestions.clear();
                } else {
                    List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
                    if (allSuggestions != null) {
                        mAllSuggestions = allSuggestions;
                        mAdapter.setmSuggestionInfos(mAllSuggestions);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        };
        suggestionSearch.setOnGetSuggestionResultListener(listener);
        suggestionSearch.requestSuggestion(new SuggestionSearchOption().city(city).keyword(key));
    }
}
