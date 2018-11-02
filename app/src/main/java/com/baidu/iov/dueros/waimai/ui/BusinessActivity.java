package com.baidu.iov.dueros.waimai.ui;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.adapter.BusinesAdapter;
import com.baidu.iov.dueros.waimai.adapter.TabSortTypeAdpater;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.PoilistReq;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.presenter.BusinessPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.view.ActivityFilterPopWindow;
import com.baidu.iov.dueros.waimai.view.ConditionsPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;
public class BusinessActivity extends BaseActivity<BusinessPresenter,BusinessPresenter.BusinessUi> implements BusinessPresenter.BusinessUi, View.OnClickListener{
    private static final String TAG = BusinessActivity.class.getSimpleName();
    private Button btnBack;
    private TextView tvTitle;
    private Button btnSearch;
    private BusinesAdapter mBusinesAdapter;
    private ListView mBusinessListView;
    private  TextView tvConditions;
    private  TextView tvFilter;
    
    private PoilistReq poilistReq;
    
    private String keyword="";
    private String title="";
    private ConditionsPopWindow mConditionsPopWindow;

    private FilterConditionsReq filterConditionsReq;

    private List<FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter> activityFilterList=new ArrayList<>();
    private List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> sortTypeList=new ArrayList<>();
    private List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> sortTypeTabs=new ArrayList<>();
    private List<BusinessBean.MeituanBean.Business.OpenPoiBaseInfo> mOpenPoiBaseInfoList = new ArrayList<>();

    private RecyclerView rvSortType;
    
    private TabSortTypeAdpater mTabSortTypeAdpater;

    private ActivityFilterPopWindow mActivityFilterPopWindow;

    private SmartRefreshLayout mRefreshLayout;

    private BusinessBean.MeituanBean.Business mBusiness;

    private TextView tvNoResult;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        getIntentData();
        initView();
        initData();
        setListener();
    }
    
    public void getIntentData() {
        Intent intent=getIntent();
        if (intent!=null) {
            title = intent.getStringExtra("title");
            Lg.getInstance().e(TAG,"title:"+title);
        }
    }

    private void initData (){
        filterConditionsReq =new FilterConditionsReq();
        filterConditionsReq.setLatitude(29735952);
        filterConditionsReq.setLongitude(95369826);

        if (getResources().getString(R.string.stroe_type_cake).equals(title)){
            keyword= getResources().getString(R.string.cake);
        }else if (getResources().getString(R.string.stroe_type_flower).equals(title)){
            keyword= getResources().getString(R.string.flower);
        }else {
            keyword=title;
        }
        poilistReq=new PoilistReq();
        poilistReq.setKeyword(keyword);
        poilistReq.setPage_index(1);
     
        
        getPresenter().requestFilterConditions(filterConditionsReq);
        getPresenter().requestBusinessBean(poilistReq);

    }

    private  void initView(){
        btnBack=findViewById(R.id.btn_back);
        tvTitle=findViewById(R.id.tv_title);
        btnSearch=findViewById(R.id.btn_search);
        mRefreshLayout =  findViewById(R.id.refresh_layout);
        tvNoResult=findViewById(R.id.tv_tip_no_result);
        tvConditions=findViewById(R.id.tv_conditions);
        mConditionsPopWindow = new ConditionsPopWindow(this);
        mActivityFilterPopWindow= new ActivityFilterPopWindow(this);
        
        
        tvFilter=findViewById(R.id.tv_filter);
        rvSortType=findViewById(R.id.rv_sort_type);
        //横向列表布局
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSortType.setLayoutManager(manager);
        mTabSortTypeAdpater=new TabSortTypeAdpater(this);
        rvSortType.setAdapter(mTabSortTypeAdpater);
        
        mBusinessListView = findViewById(R.id.lv_business);
        mBusinesAdapter = new BusinesAdapter(this);
        mBusinessListView.setAdapter(mBusinesAdapter);
        
        tvTitle.setText(title);
        tvNoResult.setVisibility(View.GONE);
    }
    
    private void setListener(){
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        tvConditions.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
        setRefreshView();
        mTabSortTypeAdpater.setOnItemClickListener(new TabSortTypeAdpater.OnItemClickListener() {
            @Override
            public void onItemClick(List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> data, int position) {
                if (!String.valueOf(poilistReq.getSortType()).equals(""+data.get(position).getCode())){
                    tvConditions.setText(sortTypeList.get(0).getName());
                    tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                    poilistReq.setSortType((int) data.get(position).getCode());
                    poilistReq.setPage_index(1);
                }else{
                    tvConditions.setText(sortTypeList.get(0).getName());
                    tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                    poilistReq.setSortType((int) sortTypeList.get(0).getCode());
                    poilistReq.setPage_index(1);
                }
                getPresenter().requestBusinessBean(poilistReq);
            }
        });
        mBusinessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusinessBean.MeituanBean.Business.OpenPoiBaseInfo mOpenPoiBaseInfo=mOpenPoiBaseInfoList.get(position);
                if (mOpenPoiBaseInfo.getStatus()==Constant.STROE_STATUS_BREAK){
                    Toast.makeText(BusinessActivity.this,getResources().getString(R.string.tips_earliest_delivery_time),Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(BusinessActivity.this, FoodListActivity.class);
                    intent.putExtra(Constant.STORE_ID, mOpenPoiBaseInfo.getWmPoiId());
                    startActivity(intent);
                }
                
            }
        });
    }

    @Override
    BusinessPresenter createPresenter() {
        return new BusinessPresenter();
    }

    @Override
    BusinessPresenter.BusinessUi getUi() {
        return  this;
    }


    @Override
    public void onBusinessBeanSuccess(BusinessBean data) {
        Lg.getInstance().e(TAG,"data:"+data);
        mBusiness=data.getMeituan().getmBusiness();
        if (mBusiness.getCurrentPageIndex() == 1) {
            mOpenPoiBaseInfoList.clear(); 
        }
        
        mOpenPoiBaseInfoList.addAll(mBusiness.getOpenPoiBaseInfoList());
        mBusinesAdapter.setData(mOpenPoiBaseInfoList);
        if (mOpenPoiBaseInfoList.size() == 0) {
            if (!TextUtils.isEmpty(poilistReq.getMigFilter())) {
                tvNoResult.setText(WaiMaiApplication.getInstance().getString(R.string
                        .no_search_result_filter));
            } else {
                tvNoResult.setText(WaiMaiApplication.getInstance().getString(R.string
                        .no_search_result_keyword));
            }
            tvNoResult.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
        }else {
            tvNoResult.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);
        }

        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }
        if (mRefreshLayout.isLoading()) {
            mRefreshLayout.finishLoadmore();
        }
    }

    private void setRefreshView() {
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                poilistReq.setPage_index(1);
                getPresenter().requestBusinessBean(poilistReq);
            }
        });

        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                if (mBusiness!=null&&mBusiness.getHaveNextPage() == 1) {
                    poilistReq.setPage_index((mBusiness.getCurrentPageIndex() + 1));
                    Lg.getInstance().e(TAG,"data:"+poilistReq.getPage_index());
                    getPresenter().requestBusinessBean(poilistReq);
                } else {
                    mRefreshLayout.finishLoadmore();
                }
            }
        });
    }

    @Override
    public void onBusinessBeanError(String error) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }
        if (mRefreshLayout.isLoading()) {
            mRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onFilterConditionsSuccess(FilterConditionsResponse data) {
        activityFilterList =data.getMeituan().getData().getActivity_filter_list();
        mActivityFilterPopWindow.setData(activityFilterList);
        
        sortTypeTabs.clear();
        sortTypeTabs=getSortTypeTab(data.getMeituan().getData().getSort_type_list());
        mTabSortTypeAdpater.setData(sortTypeTabs);

        sortTypeList.clear();
        sortTypeList=getSortTypeList(data.getMeituan().getData().getSort_type_list());
        mConditionsPopWindow.setData(sortTypeList);
    }
    
    private List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> getSortTypeTab(List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> sortTypes) {
        if (sortTypes != null && !sortTypes.isEmpty()) {
            int size = sortTypes.size();
            for (int i = 0; i < size; i++) {
                FilterConditionsResponse.MeituanBean.MeituanData.SortType sortType = sortTypes.get(i);
                //0:tab  
                if (sortType.getPosition() ==FilterConditionsResponse.MeituanBean.MeituanData.SortType.TABPOS) {
                    sortTypeTabs.add(sortType);
                }
            }
        }
        return sortTypeTabs;
    }

    private List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> getSortTypeList(List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> sortTypes) {
        if (sortTypes != null && !sortTypes.isEmpty()) {
            int size = sortTypes.size();
            for (int i = 0; i < size; i++) {
                FilterConditionsResponse.MeituanBean.MeituanData.SortType sortType = sortTypes.get(i);
                //1:list 
                if (sortType.getPosition() == FilterConditionsResponse.MeituanBean.MeituanData.SortType.LISTPOS) {
                    sortTypeList.add(sortType);
                }
            }
        }
        return sortTypeList;
    }
    

    @Override
    public void onFilterConditionsError(String error) {

        Lg.getInstance().e(TAG,"error:"+error);
    }

   

    @Override
    public void close() {
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_search:
                Intent intent = new Intent(BusinessActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_conditions:
                mConditionsPopWindow.showPop(tvConditions);
                mConditionsPopWindow.setBackgroundAlpha(0.5f);
                mConditionsPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Drawable downDrawable =getResources().getDrawable(R.mipmap.arrow_down);
                        downDrawable.setBounds(0, 0, downDrawable.getIntrinsicWidth(), downDrawable.getIntrinsicHeight());
                        tvConditions.setCompoundDrawables(null,null,downDrawable,null);
                        mConditionsPopWindow.setBackgroundAlpha(1.0f);
                    }
                });
                Drawable upDrawable =getResources().getDrawable(R.mipmap.arrow_up);
                upDrawable.setBounds(0, 0, upDrawable.getIntrinsicWidth(), upDrawable.getIntrinsicHeight());
                tvConditions.setCompoundDrawables(null,null,upDrawable,null);
                break;
            case R.id.tv_filter:
                tvFilter.setTextColor(getResources().getColor(R.color.black));
                mActivityFilterPopWindow.showPop(tvFilter);
                mActivityFilterPopWindow.setBackgroundAlpha(0.5f);
                mActivityFilterPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Drawable downDrawable =getResources().getDrawable(R.mipmap.arrow_down);
                        downDrawable.setBounds(0, 0, downDrawable.getIntrinsicWidth(), downDrawable.getIntrinsicHeight());
                        tvFilter.setCompoundDrawables(null,null,downDrawable,null);
                        mActivityFilterPopWindow.setBackgroundAlpha(1.0f);
                    }
                });
                Drawable upFilterDrawable =getResources().getDrawable(R.mipmap.arrow_up);
                upFilterDrawable.setBounds(0, 0, upFilterDrawable.getIntrinsicWidth(), upFilterDrawable.getIntrinsicHeight());
                tvFilter.setCompoundDrawables(null,null,upFilterDrawable,null);
                break;
            default:
                break;
        }
    }

    public void setSortType(int position) {
        poilistReq.setSortType((int) sortTypeList.get(position).getCode());
        poilistReq.setPage_index(1);
        tvConditions.setText(sortTypeList.get(position).getName());
        tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.black));
        mTabSortTypeAdpater.initView(-1);
        getPresenter().requestBusinessBean(poilistReq);
    }

    public void setFilterTypes(String migFilter) {
        poilistReq.setMigFilter(migFilter);
        poilistReq.setPage_index(1);
        if (!migFilter.isEmpty()){
            tvFilter.setTextColor(getResources().getColor(R.color.black));
        }else{
            tvFilter.setTextColor(getResources().getColor(R.color.gray));
        }
        
        getPresenter().requestBusinessBean(poilistReq);
    }
}
