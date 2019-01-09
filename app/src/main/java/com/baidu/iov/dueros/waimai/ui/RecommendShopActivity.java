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
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

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

    private int mFromPageType;

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
            mFromPageType=Constant.STORE_FRAGMENT_FROM_CAKE;
        }else if (getResources().getString(R.string.stroe_type_flower).equals(title)){
            keyword=getResources().getString(R.string.flower);
            mFromPageType=Constant.STORE_FRAGMENT_FROM_FLOWER;
        }else {
            mFromPageType=Constant.STORE_FRAGMENT_FROM_FOOD;
        }
        
      
        //fragment
        mStoreListFragment = new StoreListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, mFromPageType);
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
        if (getResources().getString(R.string.stroe_type_cake).equals(title)){
            GuidingAppear.INSTANCE.init(this, WaiMaiApplication.getInstance().getWaimaiBean().getShop().getCake());
        }else if (getResources().getString(R.string.stroe_type_flower).equals(title)){
            GuidingAppear.INSTANCE.init(this, WaiMaiApplication.getInstance().getWaimaiBean().getShop().getFlower());
        }
    }

    private void setListener(){
        btnBack.setOnClickListener(this);
        mRlSearch.setOnClickListener(this);
     
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Entry.getInstance().onEvent(Constant.EVENT_BACK,EventType.TOUCH_TYPE);
                finish();
                break;
            case R.id.rl_search:
                if (mFromPageType==Constant.STORE_FRAGMENT_FROM_FLOWER){
                    Entry.getInstance().onEvent(Constant.EVENT_OPEN_SEARCH_FROM_FLOWER,EventType.TOUCH_TYPE);
                }else if (mFromPageType==Constant.STORE_FRAGMENT_FROM_CAKE){
                    Entry.getInstance().onEvent(Constant.EVENT_OPEN_SEARCH_FROM_CAKE,EventType.TOUCH_TYPE);
                }
                Intent intent = new Intent(RecommendShopActivity.this, SearchActivity.class);
                intent.putExtra(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, mFromPageType);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
