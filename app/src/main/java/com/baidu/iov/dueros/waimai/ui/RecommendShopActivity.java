package com.baidu.iov.dueros.waimai.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.presenter.HomePresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
public class RecommendShopActivity extends BaseActivity<HomePresenter, HomePresenter.HomeUi> implements
        HomePresenter.HomeUi, View.OnClickListener {

    private Button btnBack;
    
    private TextView tvTitle;

    private RelativeLayout mRlSearch;

    private String title="";

    private String keyword="";

    private int categoryType;

    private int secondCategoryType;

    private StoreListFragment mStoreListFragment;

    private StoreReq mStoreReq;

    private boolean isload =false;

    @Override
    HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    HomePresenter.HomeUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        getIntentData();
        initView();
        initData();
        setListener();
    }
    

    public void getIntentData() {
        Intent intent=getIntent();
        if (intent!=null) {
            title = intent.getStringExtra("title");
            categoryType=(int)intent.getLongExtra("categoryType",0);
            secondCategoryType=(int)intent.getLongExtra("secondCategoryType",0);
        }
    }

    private  void initView(){
        btnBack=findViewById(R.id.btn_back);
        tvTitle=findViewById(R.id.tv_title);
        mRlSearch = findViewById(R.id.rl_search);
        tvTitle.setText(title);
       
    }

    private void initData (){
        if (getResources().getString(R.string.stroe_type_cake).equals(title)){
            keyword=getResources().getString(R.string.cake);
        }else if (getResources().getString(R.string.stroe_type_flower).equals(title)){
            keyword=getResources().getString(R.string.flower);
        }
      
        //fragment
        mStoreListFragment = new StoreListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, Constant.STORE_FRAGMENT_FROM_RECOMMENDSHOP);
        mStoreListFragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_store_list, mStoreListFragment);
        transaction.commit();
        
        mStoreReq = new StoreReq();
        mStoreReq.setSortType(Constant.COMPREHENSIVE);
        if (!keyword.isEmpty()){
            mStoreReq.setKeyword(keyword);
        }
        if (categoryType!=0) {
            mStoreReq.setCategoryType(categoryType);
        }
        if (secondCategoryType!=0) {
            mStoreReq.setSecondCategoryType(secondCategoryType);
        }
       
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isload){
            isload=true;
            mStoreListFragment.recommendShopLoadFirstPage(mStoreReq);
        }
      
    }

    @Override
    protected void onResume() {
        super.onResume();
        
    }

    private void setListener(){
        btnBack.setOnClickListener(this);
        mRlSearch.setOnClickListener(this);
     
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                onBackPressed();
                break;
            case R.id.rl_search:
                Intent intent = new Intent(RecommendShopActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
