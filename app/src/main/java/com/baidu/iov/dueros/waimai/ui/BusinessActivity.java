package com.baidu.iov.dueros.waimai.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.adapter.BusinesAdapter;
import com.baidu.iov.dueros.waimai.net.entity.request.PoilistReq;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.baidu.iov.dueros.waimai.presenter.BusinessPresenter;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.view.ConditionsPopWindow;
import java.util.Map;
public class BusinessActivity extends BaseActivity<BusinessPresenter,BusinessPresenter.BusinessUi> implements BusinessPresenter.BusinessUi, View.OnClickListener{
    private static final String TAG = BusinessActivity.class.getSimpleName();
    private Button btnBack;
    private TextView tvTitle;
    private EditText etSearch;
    private BusinesAdapter mBusinesAdapter;
    private ListView mBusinessListView;
    private  TextView tvConditions;
    private  TextView tvDistance;
    private  TextView tvSales;
    private  TextView tvFilter;
    
    private ArrayMap<String, String> map;
    
    private String keyword="";
    private String title="";
    private ConditionsPopWindow mConditionsPopWindow;
    
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
       
    

    private  void initView(){
        btnBack=findViewById(R.id.btn_back);
        tvTitle=findViewById(R.id.tv_title);
        etSearch=findViewById(R.id.et_search);

        tvConditions=findViewById(R.id.tv_conditions);
        tvDistance=findViewById(R.id.tv_distance);
        tvSales=findViewById(R.id.tv_sales);
        tvFilter=findViewById(R.id.tv_filter);
        
        mBusinessListView = findViewById(R.id.lv_business);
        mBusinesAdapter = new BusinesAdapter(this);
        mBusinessListView.setAdapter(mBusinesAdapter);
        
        tvTitle.setText(title);
        map = new ArrayMap<>();
       // map.put(PoilistReq.KEYWORD,keyword);
        map.put(PoilistReq.SORTTYPE,""+PoilistReq.BEST_SORT_INDEX);
       
       getPresenter().requestData(map);
    }
    
    private void setListener(){
        btnBack.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        tvConditions.setOnClickListener(this);
        tvDistance.setOnClickListener(this);
        tvSales.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
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
    public void onSuccess(Map<String, BusinessBean> data) {
        Lg.getInstance().e(TAG,"data:"+data);
        //mBusinesAdapter.
        mBusinesAdapter.setData(data.get("meituan").getmBusiness().getOpenPoiBaseInfoList());
        mBusinesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {
      
        //mBusinesAdapter.setData(null);
    }
    
     

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.et_search:
               
                break;
            case R.id.tv_conditions:
                if (mConditionsPopWindow==null) {
                    mConditionsPopWindow = new ConditionsPopWindow(this);
                }
                mConditionsPopWindow.showFilterPopup(tvConditions);
                break;
            case R.id.tv_sales:
                if (!map.get(PoilistReq.SORTTYPE).equals(""+PoilistReq.SALE_NUM_SORT_INDEX)){
                tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                tvSales.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                tvDistance.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                tvConditions.setText(getResources().getStringArray(R.array.sort_type)[PoilistReq.BEST_SORT_INDEX]);
                map.put(PoilistReq.SORTTYPE,""+PoilistReq.SALE_NUM_SORT_INDEX);
                }else{
                    tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                    tvSales.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                    tvDistance.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                    tvConditions.setText(getResources().getStringArray(R.array.sort_type)[PoilistReq.BEST_SORT_INDEX]);
                    map.put(PoilistReq.SORTTYPE,""+PoilistReq.BEST_SORT_INDEX);
                }
                
                getPresenter().requestData(map);
                break;
            case R.id.tv_distance:
                if (!map.get(PoilistReq.SORTTYPE).equals(""+PoilistReq.PRICE_SORT_INDEX)) {
                    tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                    tvSales.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                    tvDistance.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                    tvConditions.setText(getResources().getStringArray(R.array.sort_type)[PoilistReq.BEST_SORT_INDEX]);
                    map.put(PoilistReq.SORTTYPE, "" + PoilistReq.PRICE_SORT_INDEX);
                }else {
                    tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                    tvSales.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                    tvDistance.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
                    tvConditions.setText(getResources().getStringArray(R.array.sort_type)[PoilistReq.BEST_SORT_INDEX]);
                    map.put(PoilistReq.SORTTYPE, "" + PoilistReq.BEST_SORT_INDEX);
                }
                getPresenter().requestData(map);
                break;
            case R.id.tv_filter:
                break;
            default:
                break;
        }
    }

    public void setSortType(int position) {
        map.put(PoilistReq.SORTTYPE,""+position);
        tvConditions.setText(getResources().getStringArray(R.array.sort_type)[position]);
        
        tvConditions.setTextColor(getBaseContext().getResources().getColor(R.color.black));
        tvSales.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
        tvDistance.setTextColor(getBaseContext().getResources().getColor(R.color.gray));
      
        
        getPresenter().requestData(map);
    }
}
