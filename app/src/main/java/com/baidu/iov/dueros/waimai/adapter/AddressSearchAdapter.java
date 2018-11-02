package com.baidu.iov.dueros.waimai.adapter;

import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.sug.SuggestionResult;

import java.util.List;

public class AddressSearchAdapter extends BaseAdapter {
    private List<PoiInfo> mPoiInfos;

    public List<PoiInfo> getmPoiInfos() {
        return mPoiInfos;
    }

    public void setmPoiInfos(List<PoiInfo> mPoiInfos) {
        mPoiModle = POI_MODEL_NEERBY;
        this.mPoiInfos = mPoiInfos;
    }

    public List<SuggestionResult.SuggestionInfo> getmSuggestionInfos() {
        return mSuggestionInfos;
    }

    public void setmSuggestionInfos(List<SuggestionResult.SuggestionInfo> mSuggestionInfos) {
        mPoiModle = POI_MODEL_SEARCH;
        this.mSuggestionInfos = mSuggestionInfos;
    }

    private List<SuggestionResult.SuggestionInfo> mSuggestionInfos;
    private static final int POI_MODEL_NEERBY = 0;
    private static final int POI_MODEL_SEARCH = 1;
    private int mPoiModle = 0;

    public AddressSearchAdapter(List<SuggestionResult.SuggestionInfo> mAllSuggestions, List<PoiInfo> mPoiList) {
        this.mSuggestionInfos = mAllSuggestions;
        this.mPoiInfos = mPoiList;
    }

    @Override
    public int getCount() {
        switch (mPoiModle) {
            case POI_MODEL_NEERBY:
                return mPoiInfos.size();
            case POI_MODEL_SEARCH:
                return mSuggestionInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        switch (mPoiModle) {
            case POI_MODEL_NEERBY:
                return mPoiInfos.get(position);
            case POI_MODEL_SEARCH:
                return mSuggestionInfos.get(position);
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = View.inflate(parent.getContext(), R.layout.activity_address_poi_item, null);
        TextView name = (TextView) item.findViewById(R.id.poi_item_name);
        TextView address = (TextView) item.findViewById(R.id.poi_item_address);
        TextView distance = (TextView) item.findViewById(R.id.poi_item_distance);

        switch (mPoiModle) {
            case POI_MODEL_NEERBY:
                PoiInfo poiInfo = mPoiInfos.get(position);
                if (position == 0) {
                    MapView map = new MapView(parent.getContext());
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
                    map.setLayoutParams(params);
                    BaiduMap baiduMap = map.getMap();
                    LatLng point = poiInfo.getLocation();
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_menu);

                    OverlayOptions options = new MarkerOptions().position(point)
                            .icon(bitmap);
                    baiduMap.addOverlay(options);
 /*

                    double longitude = *//*poiInfo.getLocation().longitude*//*114436307f;
                    double latitude = *//*poiInfo.getLocation().latitude*//*30502147f;
                    MyLocationData locData = new MyLocationData.Builder()
                            .accuracy(180)
                            .direction(100)
                            .latitude(latitude)
                            .longitude(longitude).build();
                    baiduMap.setMyLocationData(locData);

                    MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, bitmapDescriptor);
                    baiduMap.setMyLocationConfiguration(myLocationConfiguration);
                    baiduMap.setMyLocationEnabled(true);*/
                    return map;
                } else {
                    name.setText(poiInfo.getName());
                    address.setText(poiInfo.getAddress());
                    distance.setVisibility(View.INVISIBLE);
                }
                break;
            case POI_MODEL_SEARCH:
                SuggestionResult.SuggestionInfo suggestionInfo = mSuggestionInfos.get(position);
                name.setText(suggestionInfo.getKey());
                address.setText(suggestionInfo.getAddress() + "address");
//                double distance = DistanceUtil.getDistance(location, pt);
//                LatLng pt = mAllSuggestions.get(i).getPt();
//                double distance = DistanceUtil.getDistance(location, pt);
                distance.setText("1.2 m");
                distance.setVisibility(View.VISIBLE);
                break;
        }

        return item;
    }

    @Override
    public int getItemViewType(int position) {
        return mPoiModle;
    }
}
