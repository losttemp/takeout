package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.FirstTypeFoodAdapter;
import com.baidu.iov.dueros.waimai.adapter.SecondTypeFoodAdapter;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.presenter.FoodPresenter;
import com.domain.multipltextview.MultiplTextView;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends BaseActivity<FoodPresenter,FoodPresenter.FoodUi> implements FoodPresenter.FoodUi,View.OnClickListener{

    private static final String TAG = FoodActivity.class.getSimpleName();

    private ListView lvFirstType;
  
    private GridView gvSecondType;

    private MultiplTextView tvFirstCategory;

    private Button btnBack;
    
    private RelativeLayout mRlSearch;
    private FirstTypeFoodAdapter mFirstTypeFoodAdapter;
  
    private SecondTypeFoodAdapter mSecondTypeFoodAdapter;

    private FilterConditionReq filterConditionReq;
   
    private List<FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean> categoryFilterList;
    
    private int lvFirstTypePos;

    private MultiplTextView tvNoResult;

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
        filterConditionReq =new FilterConditionReq();
        getPresenter().requestFilterConditions(filterConditionReq);

    }

    private void initView (){
        lvFirstType=findViewById(R.id.lv_first_type);
        gvSecondType=findViewById(R.id.gv_second_type);
        tvFirstCategory=findViewById(R.id.tv_first_category);
        btnBack=findViewById(R.id.btn_back);
        mRlSearch = (RelativeLayout) findViewById(R.id.rl_search);
        tvNoResult=findViewById(R.id.tv_tip_no_result);
        tvNoResult.setVisibility(View.GONE);
        
        mFirstTypeFoodAdapter=new FirstTypeFoodAdapter(this);
        lvFirstType.setAdapter(mFirstTypeFoodAdapter);

        mSecondTypeFoodAdapter=new SecondTypeFoodAdapter(this);
        gvSecondType.setAdapter(mSecondTypeFoodAdapter);

        btnBack.setOnClickListener(this);
        mRlSearch.setOnClickListener(this);
        
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
                FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean categoryFilter=categoryFilterList.get(lvFirstTypePos);
                FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean.SubCategoryListBean subCategory=categoryFilter.getSub_category_list().get(position);
                String title = subCategory.getName();
                long categoryType=categoryFilter.getCode();
                long secondCategoryType=subCategory.getCode();
                if (lvFirstTypePos!=0&&position==0){
                    title=categoryFilter.getName();
                }
                if (lvFirstTypePos==0){
                    categoryType=getCategoryCode(secondCategoryType,categoryFilterList);
                }
                Intent itemIntent=new Intent(FoodActivity.this,RecommendShopActivity.class);
                itemIntent.putExtra("title",  title);
                itemIntent.putExtra("categoryType",  categoryType);
                itemIntent.putExtra("secondCategoryType", secondCategoryType);
                startActivity(itemIntent);
            }
        });
        
    }


    @Override
    public void onSuccess(FilterConditionResponse data) {
        if (data==null||data.getMeituan()==null||data.getMeituan().getData().getCategory_filter_list().isEmpty()){
            return;
        }
        categoryFilterList=data.getMeituan().getData().getCategory_filter_list();
        categoryFilterList.get(0).getSub_category_list().addAll(getAllSubCategory(categoryFilterList));
        mFirstTypeFoodAdapter.setData(categoryFilterList);
        tvFirstCategory.setText(categoryFilterList.get(0).getName());
        mSecondTypeFoodAdapter.setData(categoryFilterList.get(0).getSub_category_list());
        if (categoryFilterList.size() == 0) {
            tvNoResult.setVisibility(View.VISIBLE);
        }
    }

    private long getCategoryCode(long secondCategoryCode,List<FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean> categoryFilterList){
        long categoryCode =0;
        if (categoryFilterList==null||categoryFilterList.isEmpty()||categoryFilterList.size()==1){
            return categoryCode;
        }
        int size =categoryFilterList.size();
        for (int i = 1; i < size; i++) {
            if (categoryFilterList.get(i).getSub_category_list().isEmpty()){
                return categoryCode;
            }
            int subCategorySize =categoryFilterList.get(i).getSub_category_list().size();
            for (int j = 1; j < subCategorySize; j++) {
                if (secondCategoryCode==categoryFilterList.get(i).getSub_category_list().get(j).getCode()){
                    categoryCode=categoryFilterList.get(i).getCode();
                }
            }
        }
        return  categoryCode;
    }
    
    private List<FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean.SubCategoryListBean> getAllSubCategory(List<FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean> categoryFilterList){
        List<FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean.SubCategoryListBean> subCategorys=new ArrayList<>();
        if (categoryFilterList==null||categoryFilterList.isEmpty()||categoryFilterList.size()==1){
            return subCategorys;
        }
        
        int size =categoryFilterList.size();
        for (int i = 1; i < size; i++) {
            if (categoryFilterList.get(i).getSub_category_list().isEmpty()){
                return subCategorys;
            }
            int subCategorySize =categoryFilterList.get(i).getSub_category_list().size();
            for (int j = 1; j < subCategorySize; j++) {
                subCategorys.add(categoryFilterList.get(i).getSub_category_list().get(j));
            }
        }
        return  subCategorys;
    }

    @Override
    public void onError(String error) {
        tvNoResult.setText(error);
        tvNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.rl_search:
                Intent intent = new Intent(FoodActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
