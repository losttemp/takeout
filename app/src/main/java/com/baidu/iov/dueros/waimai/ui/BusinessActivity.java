package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.adapter.BusinesAdapter;
import com.baidu.iov.dueros.waimai.adapter.TabSortTypeAdpater;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.PoilistReq;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.presenter.BusinessPresenter;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.view.ActivityFilterPopWindow;
import com.baidu.iov.dueros.waimai.view.ConditionsPopWindow;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessActivity extends BaseActivity<BusinessPresenter,BusinessPresenter.BusinessUi> implements BusinessPresenter.BusinessUi, View.OnClickListener{
    private static final String TAG = BusinessActivity.class.getSimpleName();
    private Button btnBack;
    private TextView tvTitle;
    private Button btnSearch;
    private BusinesAdapter mBusinesAdapter;
    private ListView mBusinessListView;
    private  TextView tvConditions;
    private  TextView tvFilter;
    
    private ArrayMap<String, String> map;
    
    private String keyword="";
    private String title="";
    private ConditionsPopWindow mConditionsPopWindow;

    private FilterConditionsReq filterConditionsReq;

    private List<FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter> activityFilterList;
    
    private List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> sortTypeList=new ArrayList<>();
    private List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> sortTypeTabs=new ArrayList<>();

    private RecyclerView rvSortType;
    
    private TabSortTypeAdpater mTabSortTypeAdpater;

    private ActivityFilterPopWindow mActivityFilterPopWindow;
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        getIntentData();
        initView();
        setListener();
    }


   
    public void getIntentData() {
        Intent intent=getIntent();
        if (intent!=null) {
            title = intent.getStringExtra("title");
        }
    }

    private void initData (){
        filterConditionsReq =new FilterConditionsReq();
        filterConditionsReq.setLatitude(29735952);
        filterConditionsReq.setLongitude(95369826);
        getPresenter().requestFilterConditions(filterConditionsReq);

    }

    private  void initView(){
        btnBack=findViewById(R.id.btn_back);
        tvTitle=findViewById(R.id.tv_title);
        btnSearch=findViewById(R.id.btn_search);

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
        if (getResources().getString(R.string.stroe_type_cake).equals(title)){
            keyword= getResources().getString(R.string.cake);
        }else if (getResources().getString(R.string.flower).equals(title)){
            keyword= getResources().getString(R.string.flower);
        }else {
            keyword=title;
        }

        initData();
        
        
        map = new ArrayMap<>();
        map.put(PoilistReq.KEYWORD,keyword);
        map.put(PoilistReq.SORTTYPE,""+PoilistReq.BEST_SORT_INDEX);
       
       getPresenter().requestBusinessBean(map);
    }
    
    private void setListener(){
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        tvConditions.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
        mTabSortTypeAdpater.setOnItemClickListener(new TabSortTypeAdpater.OnItemClickListener() {
            @Override
            public void onItemClick(List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> data, int position) {
                if (!map.get(PoilistReq.SORTTYPE).equals(""+data.get(position).getCode())){
                    tvConditions.setText(getResources().getStringArray(R.array.sort_type)[PoilistReq.BEST_SORT_INDEX]);
                    tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                    map.put(PoilistReq.SORTTYPE,""+data.get(position).getCode());
                }else{
                    tvConditions.setText(getResources().getStringArray(R.array.sort_type)[PoilistReq.BEST_SORT_INDEX]);
                    tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                    map.put(PoilistReq.SORTTYPE,""+PoilistReq.BEST_SORT_INDEX);
                }
                getPresenter().requestBusinessBean(map);
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
    public void onBusinessBeanSuccess(Map<String, BusinessBean> data) {
        Lg.getInstance().e(TAG,"data:"+data);
        //mBusinesAdapter.
        mBusinesAdapter.setData(data.get("meituan").getmBusiness().getOpenPoiBaseInfoList());
        mBusinesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBusinessBeanError(String error) {
      
        //mBusinesAdapter.setData(null);
    }

    @Override
    public void onFilterConditionsSuccess(FilterConditionsResponse data) {
        Lg.getInstance().e(TAG,"msg:"+data);
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

        //mBusinesAdapter.setData(null);
    }
    
     

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_search:
               
                break;
            case R.id.tv_conditions:
                mConditionsPopWindow.showPop(tvConditions);
                break;
            case R.id.tv_filter:
                mActivityFilterPopWindow.showPop(tvFilter);
                break;
            default:
                break;
        }
    }

    public void setSortType(int position) {
        map.put(PoilistReq.SORTTYPE,""+sortTypeList.get(position).getCode());
        tvConditions.setText(sortTypeList.get(position).getName());
        tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.black));
        getPresenter().requestBusinessBean(map);
    }
}
