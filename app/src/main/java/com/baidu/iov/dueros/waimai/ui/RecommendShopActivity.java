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
import com.baidu.iov.dueros.waimai.presenter.ShopPresenter;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;
import java.util.ArrayList;
public class RecommendShopActivity extends BaseActivity<ShopPresenter, ShopPresenter.ShopUi> implements
        ShopPresenter.ShopUi, View.OnClickListener {

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

    private ArrayList<String> prefix = new ArrayList<>();

    @Override
    ShopPresenter createPresenter() {
        return new ShopPresenter();
    }

    @Override
    ShopPresenter.ShopUi getUi() {
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
            if (intent.getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false)) {
                if (getResources().getString(R.string.stroe_type_flower).equals(title)){
                    StandardCmdClient.getInstance().playTTS(RecommendShopActivity.this, getString(R.string.tts_into_flower));
                }else if (getResources().getString(R.string.stroe_type_cake).equals(title)){
                    StandardCmdClient.getInstance().playTTS(RecommendShopActivity.this, getString(R.string.tts_into_cake));
                }
            }
        }
    }

    private  void initView(){
        btnBack=findViewById(R.id.btn_back);
        tvTitle=findViewById(R.id.tv_title);
        mRlSearch = findViewById(R.id.rl_search);
        tvTitle.setText(title);
        prefix.add(getResources().getString(R.string.prefix_choice));
        prefix.add(getResources().getString(R.string.prefix_check));
        prefix.add(getResources().getString(R.string.prefix_open));
       
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

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isload){
            isload=true;
            mStoreListFragment.recommendShopLoadFirstPage(mStoreReq);
        }
        if (getResources().getString(R.string.stroe_type_cake).equals(title)){
            GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getTakeout_cake().getHints(),Constant.TTS_SHOP_CAKE);
        }else if (getResources().getString(R.string.stroe_type_flower).equals(title)){
            GuidingAppear.INSTANCE.showtTips(this, WaiMaiApplication.getInstance().getWaimaiBean().getTakeout_flower().getHints(),Constant.TTS_SHOP_FLOWER);
        }
        AccessibilityClient.getInstance().register(this,true,prefix, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isload = false;
        AccessibilityClient.getInstance().unregister(this);
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
