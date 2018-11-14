package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;

import java.util.List;

public class BusDiscountAdpater extends RecyclerView.Adapter<BusDiscountAdpater.ItemViewHolder>{

    private static final String TAG = BusDiscountAdpater.class.getSimpleName();
    
    private Context mContext;

    private List<BusinessBean.MeituanBean.Business.OpenPoiBaseInfo.Discount> mData;

    public BusDiscountAdpater(Context context){
        mContext=context;
    }

    public void setData(List<BusinessBean.MeituanBean.Business.OpenPoiBaseInfo.Discount> data){
        mData=data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout
                .item_busdiscount, viewGroup, false);
        final ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemsViewHolder, int i) {
        BusinessBean.MeituanBean.Business.OpenPoiBaseInfo.Discount  mDiscount=mData.get(i);
        itemsViewHolder.tvName.setText(mDiscount.getInfo());
        itemsViewHolder.ivDiscount.setVisibility(View.GONE);
        //Glide.with(mContext).load(mDiscount.getIconUrl()).into(itemsViewHolder.ivDiscount);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
    
    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ImageView ivDiscount;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName= itemView.findViewById(R.id.tv_name);
            ivDiscount= itemView.findViewById(R.id.iv_discount);
        }
    }
    

}
