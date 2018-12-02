package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressSuggestionAdapter;
import com.baidu.iov.dueros.waimai.presenter.AddressSuggestionPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.baidu.iov.dueros.waimai.view.ClearEditText;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.List;

public class AddressSuggestionActivity extends BaseActivity<AddressSuggestionPresenter, AddressSuggestionPresenter.AddressSuggestionUi>
        implements TextWatcher, View.OnClickListener, AddressSuggestionPresenter.AddressSuggestionUi{
    private RecyclerView mRecyclerView;
    private ClearEditText mSearchEdit;
    private AddressSuggestionAdapter mAdapter;
    private List<SuggestionResult.SuggestionInfo> mAllSuggestions;
    private TextView mCityTV;
    private static final String TAG = AddressSuggestionActivity.class.getSimpleName();
    private SuggestionSearch mSuggestionSearch;
    private OnGetSuggestionResultListener mGetSuggestionResultListener;
    private LinearLayout mErrorLL;
    private String mCity;
    private boolean isEditModle;
    private TextView tv_title;
    private ImageView iv_back;
    private RelativeLayout selectCityView;

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
        initListener();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.address_title);
        iv_back = (ImageView) findViewById(R.id.address_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.address_search_rv);
        mSearchEdit = (ClearEditText) findViewById(R.id.address_search_edit);
        mCityTV = (TextView) findViewById(R.id.address_search_city);
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
        mAllSuggestions = new ArrayList<>();
        mAdapter = new AddressSuggestionAdapter(mAllSuggestions);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        selectCityView.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new AddressSuggestionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, SuggestionResult.SuggestionInfo dataBean) {
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
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initSuggestionInfo(String city, final String key) {
        if (!TextUtils.isEmpty(city)) {
            mSuggestionSearch = SuggestionSearch.newInstance();
            mGetSuggestionResultListener = new OnGetSuggestionResultListener() {
                @Override
                public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                    List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
                    if (allSuggestions != null && allSuggestions.size() > 0) {
                        mErrorLL.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAllSuggestions = allSuggestions;
                        mAdapter.setSuggestionInfos(mAllSuggestions);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mErrorLL.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                }
            };
            mSuggestionSearch.setOnGetSuggestionResultListener(mGetSuggestionResultListener);
            mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().city(city).keyword(key));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < 1) {
            mAllSuggestions.clear();
            mAdapter.notifyDataSetChanged();
            mErrorLL.setVisibility(View.GONE);
            return;
        }

        initSuggestionInfo(mCity, s + "");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSuggestionSearch != null) {
            mSuggestionSearch.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_back:
                finish();
                break;
            case R.id.address_search_city_layout:
                Intent intent = new Intent(AddressSuggestionActivity.this, CityListActivity.class);
                startActivityForResult(intent, Constant.CITY_REQUEST_CODE_CHOOSE);
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
            VoiceManager.getInstance().playTTS(AddressSuggestionActivity.this, String.format(getString(R.string.address_harvest), mAllSuggestions.get(i).getKey()));
            if (isEditModle) {
                setResult(Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE, intent);
            } else {
                intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, false);
                startActivity(intent);
            }
            finish();
        }
    }
}
