package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.adapter.FirstTypeFoodAdapter;
import com.baidu.iov.dueros.waimai.adapter.SecondTypeFoodAdapter;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.presenter.FoodPresenter;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.List;

public class FoodActivity extends BaseActivity<FoodPresenter,FoodPresenter.FoodUi> implements FoodPresenter.FoodUi,View.OnClickListener{

    private static final String TAG = FoodActivity.class.getSimpleName();

    private ListView lvFirstType;
  
    private GridView gvSecondType;

    private TextView tvFirstCategory;

    private Button btnBack;
    
    private Button btnSearch;
    private FirstTypeFoodAdapter mFirstTypeFoodAdapter;
  
    private SecondTypeFoodAdapter mSecondTypeFoodAdapter;

    private FilterConditionsReq filterConditionsReq;
   
    private List<FilterConditionsResponse.MeituanBean.MeituanData.CategoryFilter> categoryFilterList;
    
    private int lvFirstTypePos;

    @Override
    FoodPresenter createPresenter() {
        return new FoodPresenter();
    }

    @Override
    FoodPresenter.FoodUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_type);
        initView();
        initData();
      
   }

    private void initData (){
        filterConditionsReq =new FilterConditionsReq();
        filterConditionsReq.setLatitude(29735952);
        filterConditionsReq.setLongitude(95369826);
        getPresenter().requestFilterConditions(filterConditionsReq);

    }

    private void initView (){
        lvFirstType=findViewById(R.id.lv_first_type);
        gvSecondType=findViewById(R.id.gv_second_type);
        tvFirstCategory=findViewById(R.id.tv_first_category);
        btnBack=findViewById(R.id.btn_back);
        btnSearch=findViewById(R.id.btn_search);
        
        mFirstTypeFoodAdapter=new FirstTypeFoodAdapter(this);
        lvFirstType.setAdapter(mFirstTypeFoodAdapter);

        mSecondTypeFoodAdapter=new SecondTypeFoodAdapter(this);
        gvSecondType.setAdapter(mSecondTypeFoodAdapter);

        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        
        lvFirstType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvFirstTypePos=position;
                mFirstTypeFoodAdapter.updateSelected(position);
                tvFirstCategory.setText(categoryFilterList.get(position).getName());
                mSecondTypeFoodAdapter.setData(categoryFilterList.get(position).getSub_category_list());
            }
        });
        
        gvSecondType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              
                Intent cakeIntent=new Intent(FoodActivity.this,BusinessActivity.class);
                cakeIntent.putExtra("title",  categoryFilterList.get(lvFirstTypePos).getSub_category_list().get(position).getName());
                startActivity(cakeIntent);
            }
        });
        
    }


    @Override
    public void onSuccess(FilterConditionsResponse data) {
        categoryFilterList=data.getMeituan().getData().getCategory_filter_list();
        categoryFilterList.remove(0);
        mFirstTypeFoodAdapter.setData(categoryFilterList);
        tvFirstCategory.setText(categoryFilterList.get(0).getName());
        mSecondTypeFoodAdapter.setData(categoryFilterList.get(0).getSub_category_list());
        Lg.getInstance().e(TAG,"msg:"+data);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_search:
                Intent intent = new Intent(FoodActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
