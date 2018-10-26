package com.baidu.iov.dueros.waimai.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.ui.R;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.List;
public class ActivityFilterItemsAdpater extends RecyclerView.Adapter<ActivityFilterItemsAdpater.ItemsViewHolder>implements View.OnClickListener{

    private Context mContext;

    private List<FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter.Item> mData;

    private ActivityFilterItemsAdpater.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public ActivityFilterItemsAdpater(Context context){
        mContext=context;
    }

    public void setData(List<FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter.Item> data){
        mData=data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout
                .item_activity_filter_items, viewGroup, false);
        final ActivityFilterItemsAdpater.ItemsViewHolder holder = new ActivityFilterItemsAdpater.ItemsViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder itemsViewHolder, int i) {
        FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter.Item  item=mData.get(i);
        itemsViewHolder.tvName.setText(mData.get(i).getName());
        itemsViewHolder.itemRl.setTag(i);
        Lg.getInstance().e("ActivityFilterItemsAdpater","item.isChcked():"+item.isChcked());
        if (item.isChcked()) {
             itemsViewHolder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
        }else {
            itemsViewHolder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.gray_bg));
        }
        itemsViewHolder.itemRl.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }



    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick((int)v.getTag());
        notifyDataSetChanged();

    }



    static class ItemsViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private RelativeLayout itemRl;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRl=itemView.findViewById(R.id.item_rl);
            tvName= itemView.findViewById(R.id.tv_name);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int itemPosition);
    }

}
