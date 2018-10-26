package com.baidu.iov.dueros.waimai.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.ui.R;

import java.util.List;

public class FirstTypeFoodAdapter extends BaseAdapter {
    
    private Context mContext;
    private int currentPos;
    private List<FilterConditionsResponse.MeituanBean.MeituanData.CategoryFilter> mData;
    public FirstTypeFoodAdapter(Context context){
        mContext=context;
    }
    
    public void setData( List<FilterConditionsResponse.MeituanBean.MeituanData.CategoryFilter> data){
        mData = data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? 0 : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView==null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.first_type_food_item, null);
            mViewHolder.tvName = convertView.findViewById(R.id.tv_name);
            mViewHolder.rl = convertView.findViewById(R.id.rl);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder=(ViewHolder)convertView.getTag();
        }
        FilterConditionsResponse.MeituanBean.MeituanData.CategoryFilter mCategoryFilter =mData.get(position);
        mViewHolder.tvName.setText(mCategoryFilter.getName());
        if (currentPos == position) {
            mViewHolder.rl.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            mViewHolder.rl.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        }
        return convertView;
    }


        public class ViewHolder {

            private RelativeLayout rl;
            private TextView tvName;

        }

    public void updateSelected(int positon) {
        if (positon != currentPos) {
            currentPos = positon;
            notifyDataSetChanged();
        }
    }
}
