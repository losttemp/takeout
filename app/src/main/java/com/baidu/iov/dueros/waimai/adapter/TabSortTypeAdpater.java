package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.ui.R;

import java.util.List;

public class TabSortTypeAdpater extends RecyclerView.Adapter<TabSortTypeAdpater.TabSortTypeViewHolder> implements View.OnClickListener{
    
    private Context mContext;
    
    private int currentPos=-1;
    
    private List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> mData;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public TabSortTypeAdpater(Context context){
        mContext=context;
    }

    public void setData(List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> data){
        mData=data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TabSortTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout
                .tab_sort_item, viewGroup, false);
        final TabSortTypeAdpater.TabSortTypeViewHolder holder = new TabSortTypeAdpater.TabSortTypeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TabSortTypeViewHolder tabSortTypeViewHolder, int i) {
        FilterConditionsResponse.MeituanBean.MeituanData.SortType sortType=mData.get(i);
        tabSortTypeViewHolder.tvName.setText(sortType.getShort_name());
        tabSortTypeViewHolder.tvName.setTag(i);
        if (currentPos == i) {
            tabSortTypeViewHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            tabSortTypeViewHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.gray));
        }
        tabSortTypeViewHolder.tvName.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
          return mData == null ? 0 : mData.size();
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(mData,(int)v.getTag());
        updateSelected(v);
    }

    static class TabSortTypeViewHolder extends RecyclerView.ViewHolder {
        
        private TextView tvName;

        public TabSortTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName= itemView.findViewById(R.id.tv_name);
        }
    }

    public void updateSelected(View v) {
        if ((int)v.getTag() != currentPos) {
            currentPos = (int)v.getTag();
            notifyDataSetChanged();
        }else{
            if (((TextView)v).getCurrentTextColor()==mContext.getResources().getColor(R.color.gray)){
                ((TextView) v).setTextColor(mContext.getResources().getColor(R.color.black));
            }else{
                ((TextView) v).setTextColor(mContext.getResources().getColor(R.color.gray));
            }
                
        }
    }

    public void initView(int pos){
        if (pos!=currentPos){
            currentPos=pos;
            notifyDataSetChanged();
        }
    }

   
    public interface OnItemClickListener {
        void onItemClick(List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> data, int position);
    }
}
