package com.baidu.iov.dueros.waimai.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.utils.DistanceUtil;

import java.text.DecimalFormat;
import java.util.List;

public class AddressSuggestionAdapter extends RecyclerView.Adapter<AddressSuggestionAdapter.AddressViewHolder> {

    private List<SuggestionResult.SuggestionInfo> mSuggestionInfos;
    private OnItemClickListener mItemClickListener;
    private LatLng location;

    public AddressSuggestionAdapter(List<SuggestionResult.SuggestionInfo> suggestionInfos) {
        mSuggestionInfos = suggestionInfos;
        double span = LocationManager.SPAN + 0.5f;
        location = new LatLng(Constant.LATITUDE / span, Constant.LONGITUDE / span);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setLocation(LatLng location) {
        this.location = location;
        notifyDataSetChanged();
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
            num.setText((position + 1) + "");
            name.setText(suggestionInfo.getKey());
            address.setText(TextUtils.isEmpty(suggestionInfo.getAddress()) ? "" : suggestionInfo.getAddress());
            LatLng pt = suggestionInfo.getPt();
            double distance = DistanceUtil.getDistance(location, pt);
            float distanceValue = Math.round((distance / 10f)) / 100f;
            if (distanceValue < 10000) {
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                String distanceString = decimalFormat.format(distanceValue) + "km";
                distance_tv.setText(distanceString);
            } else {
                distance_tv.setText("");
            }
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


}