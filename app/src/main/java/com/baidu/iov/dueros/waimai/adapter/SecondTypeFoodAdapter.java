package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.bumptech.glide.Glide;

import java.util.List;

public class SecondTypeFoodAdapter extends BaseAdapter {

    private Context mContext;
    private List<FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean.SubCategoryListBean> mData;
    public void setData( List<FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean.SubCategoryListBean> data){
        mData = data;
        notifyDataSetChanged();
    }

    public SecondTypeFoodAdapter(Context context){
        mContext=context;
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
            convertView = View.inflate(mContext, R.layout.second_type_food_item, null);
            mViewHolder.tvName = convertView.findViewById(R.id.tv_name);
            mViewHolder.iv =convertView.findViewById(R.id.iv);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder=(ViewHolder)convertView.getTag();
        }
        FilterConditionResponse.MeituanBean.DataBean.CategoryFilterListBean.SubCategoryListBean mSubCategory =mData.get(position);
        mViewHolder.tvName.setText(mSubCategory.getName());
        Glide.with(mContext).load(mSubCategory.getIcon_url()).into(mViewHolder.iv);
        return convertView;
    }


    public class ViewHolder {

        private TextView tvName;
        private ImageView iv;

    }
}
