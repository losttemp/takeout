package com.baidu.iov.dueros.waimai.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.ui.R;
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
    private static final int POI_MODEL_NEERBY=0;
    private static final int POI_MODEL_SEARCH=1;
    private int mPoiModle=0;

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
        Log.d("hhr","getview "+mPoiModle);
        View item= View.inflate(parent.getContext(),R.layout.activity_address_poi_item,null);
        TextView name= (TextView) item.findViewById(R.id.poi_item_name);
        TextView address= (TextView) item.findViewById(R.id.poi_item_address);
        TextView distance= (TextView) item.findViewById(R.id.poi_item_distance);

        switch (mPoiModle) {
            case POI_MODEL_NEERBY:
                PoiInfo poiInfo = mPoiInfos.get(position);
                name.setText(poiInfo.getName());
                address.setText(poiInfo.getAddress());
                distance.setVisibility(View.INVISIBLE);
                break;
            case POI_MODEL_SEARCH:
                SuggestionResult.SuggestionInfo suggestionInfo = mSuggestionInfos.get(position);
                name.setText(suggestionInfo.getKey());
                address.setText(suggestionInfo.getAddress()+"address");
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
