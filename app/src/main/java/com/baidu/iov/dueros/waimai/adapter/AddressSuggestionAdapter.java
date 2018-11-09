package com.baidu.iov.dueros.waimai.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.utils.DistanceUtil;

import java.text.DecimalFormat;
import java.util.List;

public class AddressSuggestionAdapter extends RecyclerView.Adapter<AddressSuggestionAdapter.AddressViewHolder> {

    private List<SuggestionResult.SuggestionInfo> mSuggestionInfos;
    private OnItemClickListener mItemClickListener;
    private LatLng location;
    private GeoCoder mSearch;

    public AddressSuggestionAdapter(List<SuggestionResult.SuggestionInfo> suggestionInfos) {
        mSuggestionInfos = suggestionInfos;
        double span = LocationManager.SPAN + 0.5f;
        location = new LatLng(Constant.LATITUDE / span, Constant.LONGITUDE / span);
    }

    public void setSuggestionInfos(List<SuggestionResult.SuggestionInfo> suggestionInfos) {
        this.mSuggestionInfos = suggestionInfos;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                .activity_address_poi_item, viewGroup, false);
        AddressViewHolder holder = new AddressViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder viewHolder, int position) {
        SuggestionResult.SuggestionInfo suggestionInfo = mSuggestionInfos.get(position);
        if (suggestionInfo != null) {
            viewHolder.bindData(position, suggestionInfo);
        }
    }

    @Override
    public int getItemCount() {
        return mSuggestionInfos.size();
    }


    class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView num;
        private TextView name;
        private TextView address;
        private TextView distance_tv;
        private SuggestionResult.SuggestionInfo mDataBean;

        private AddressViewHolder(View item) {
            super(item);
            num = (TextView) item.findViewById(R.id.poi_item_num);
            name = (TextView) item.findViewById(R.id.poi_item_name);
            address = (TextView) item.findViewById(R.id.poi_item_address);
            distance_tv = (TextView) item.findViewById(R.id.poi_item_distance);
            item.setOnClickListener(this);
        }

        public void bindData(int position, SuggestionResult.SuggestionInfo suggestionInfo) {
            this.mDataBean = suggestionInfo;
            suggestionInfo.getCity();
            num.setText((position + 1) + "");
            name.setText(suggestionInfo.getKey());

            LatLng pt = suggestionInfo.getPt();
            double distance = DistanceUtil.getDistance(location, pt);

            float distanceValue = Math.round((distance / 10f)) / 100f;
            suggestionInfo.getAddress();
//            getAddress(pt);
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            String distanceString = decimalFormat.format(distanceValue) + "km";
            distance_tv.setText(distanceString);
        }

        private void getAddress(LatLng pt) {
            mSearch = GeoCoder.newInstance();
            OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult geoCodeResult) {
                    if (geoCodeResult != null && geoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
//                        TODO
                        address.setText(geoCodeResult.getAddress());

                    }
                }
            };
            mSearch.setOnGetGeoCodeResultListener(listener);
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(pt));
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.OnItemClick(v, mDataBean);
            }
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, SuggestionResult.SuggestionInfo dataBean);
    }

    public void destroy() {
        if (mSearch!=null)
        mSearch.destroy();
    }
}