package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.CYBChangeCityGridViewAdapter;
import com.baidu.iov.dueros.waimai.adapter.ContactAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.CityListBean;
import com.baidu.iov.dueros.waimai.utils.CheckUtils;
import com.baidu.iov.dueros.waimai.utils.CommonUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.view.QGridView;
import com.baidu.iov.faceos.client.GsonUtil;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableHeaderAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * Created by Administrator on 2017/11/25.
 */

public class CityPickerActivity extends AppCompatActivity {
    private ContactAdapter mAdapter;
    private BannerHeaderAdapter mBannerHeaderAdapter;
    private IndexableLayout indexableLayout;
    private CYBChangeCityGridViewAdapter cybChangeCityGridViewAdapter;
    private CityListBean cityListBean;
    private ImageView pic_contact_back;
    private LocationManager mlocationManager;
    private String mlocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(false, ContextCompat.getColor(this, R.color.base_color));
        setContentView(R.layout.activity_city_picker);
        getLocationCity();
        initview();
        initAdapter();
        onlisten();
    }

    public void initAdapter() {
        cityListBean = readCityJson();
        mAdapter = new ContactAdapter(this);
        indexableLayout.setAdapter(mAdapter);
        indexableLayout.setOverlayStyle_Center();
        mAdapter.setDatas(cityListBean.getAll());
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
        mBannerHeaderAdapter = new BannerHeaderAdapter("↑", null, bannerList);
        indexableLayout.addHeaderAdapter(mBannerHeaderAdapter);
    }

    private void getLocationCity() {
        mlocationManager = LocationManager.getInstance(getApplicationContext());
        mlocationManager.getLcation(null, null, 0, true);
        mlocationManager.setLocationListener(mLocationListener);
        mlocationManager.startLocation();
    }

    public void initview() {
        pic_contact_back = findViewById(R.id.pic_contact_back);
        indexableLayout = findViewById(R.id.indexableLayout);
        indexableLayout.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onlisten() {

        pic_contact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<CityListBean.AllBean>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, CityListBean.AllBean entity) {
                if (originalPosition >= 0) {
                    reFreshCityInfo(entity.getName(), 0);
                }
            }
        });
    }

    /**
     * 自定义的Banner Header
     */
    class BannerHeaderAdapter extends IndexableHeaderAdapter {
        private static final int TYPE = 1;

        public BannerHeaderAdapter(String index, String indexTitle, List datas) {
            super(index, indexTitle, datas);
        }

        @Override
        public int getItemViewType() {
            return TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(CityPickerActivity.this).inflate(R.layout.item_city_header, parent, false);
            VH holder = new VH(view);
            return holder;
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, Object entity) {
            final VH vh = (VH) holder;
            cybChangeCityGridViewAdapter = new CYBChangeCityGridViewAdapter(CityPickerActivity.this, cityListBean.getHot());
            vh.head_home_change_city_gridview.setAdapter(cybChangeCityGridViewAdapter);
            vh.head_home_change_city_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    reFreshCityInfo(cityListBean.getHot().get(position).getName(), 0);
                }
            });
            if (!CheckUtils.isEmpty(mlocation)) {
                vh.item_header_city_dw.setText(mlocation);
                vh.try_city.setVisibility(View.GONE);
                vh.item_header_city_dw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reFreshCityInfo(mlocation, 0);
                    }
                });
            } else {
                vh.item_header_city_dw.setText(R.string.position_fail);
                vh.try_city.setVisibility(View.VISIBLE);
                vh.item_header_city_dw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getLocationCity();
                    }
                });
            }
        }

        private class VH extends RecyclerView.ViewHolder {
            GridView head_home_change_city_gridview;
            Button item_header_city_dw;
            TextView try_city;

            public VH(View itemView) {
                super(itemView);
                head_home_change_city_gridview = (QGridView) itemView.findViewById(R.id.item_header_city_gridview);
                item_header_city_dw = (Button) itemView.findViewById(R.id.current_city);
                try_city = (TextView) itemView.findViewById(R.id.try_city);
            }
        }
    }

    private CityListBean readCityJson() {
        try {
            InputStreamReader isr = new InputStreamReader(getAssets().open("city.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject testjson = new JSONObject(builder.toString());
            String array = testjson.getString("data");
            CityListBean cityListBean = GsonUtil.fromJson(array, CityListBean.class);
            return cityListBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BDAbstractLocationListener mLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            int requestCode = bdLocation.getLocType();
            switch (requestCode) {
                case LocationManager.TypeGpsLocation:
                case LocationManager.TypeOffLineLocation:
                case LocationManager.TypeNetWorkLocation:
                    //Constant.LATITUDE = bdLocation.getLatitude();
                    //Constant.LONGITUDE = bdLocation.getLongitude();
                    mlocation = String.valueOf(bdLocation.getCity());
//                    getPresenter().requestData(null);
                    mBannerHeaderAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;

            }
        }
    };

    private void reFreshCityInfo(String name, int id) {
        Intent intent = new Intent();
        intent.putExtra(Constant.CITYCODE, name);
        setResult(Constant.CITY_RESULT_CODE_CHOOSE, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationManager.stopLocation();
        mLocationListener = null;
        mlocationManager = null;
        indexableLayout = null;
    }

    public void setStatusBar(boolean translucent, @ColorInt int color) {
        CommonUtils.setTranslucentStatusBar(this, translucent);
        if (color != 0) {
            CommonUtils.setStatusBarColor(this, color);
        }
    }

}

