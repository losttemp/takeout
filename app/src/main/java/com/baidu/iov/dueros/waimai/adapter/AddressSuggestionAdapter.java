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
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;

import java.text.DecimalFormat;
import java.util.List;

public class AddressSuggestionAdapter extends RecyclerView.Adapter<AddressSuggestionAdapter.AddressViewHolder> {

    private List<PoiInfo> mSuggestionInfos;
    private OnItemClickListener mItemClickListener;
    private LatLng location;

    public AddressSuggestionAdapter(List<PoiInfo> suggestionInfos) {
        mSuggestionInfos = suggestionInfos;
        double span = LocationManager.SPAN + 0.5f;
        location = new LatLng(Constant.LATITUDE / span, Constant.LONGITUDE / span);
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
        PoiInfo suggestionInfo = mSuggestionInfos.get(position);
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
        private PoiInfo mDataBean;

        private AddressViewHolder(View item) {
            super(item);
            num = (TextView) item.findViewById(R.id.poi_item_num);
            name = (TextView) item.findViewById(R.id.poi_item_name);
            address = (TextView) item.findViewById(R.id.poi_item_address);
            distance_tv = (TextView) item.findViewById(R.id.poi_item_distance);
            item.setOnClickListener(this);
        }

        public void bindData(int position, PoiInfo suggestionInfo) {
            this.mDataBean = suggestionInfo;
            suggestionInfo.getCity();
            num.setText((position + 1) + "");
            name.setText(suggestionInfo.getName());
            LatLng pt = suggestionInfo.getLocation();
            double distance = DistanceUtil.getDistance(location, pt);
            float distanceValue = Math.round((distance / 10f)) / 100f;
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            String distanceString = decimalFormat.format(distanceValue) + "km";
            distance_tv.setText(distanceString);
            address.setText(suggestionInfo.getAddress());
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.OnItemClick(v, mDataBean);
            }
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, PoiInfo dataBean);
    }



}