package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.R;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;

import java.util.List;

public class DeliveryDateAdapter extends BaseAdapter {

    private Context mContext;
    private List<ArriveTimeBean.MeituanBean.DataBean> mData;
    private int mPreDatePos;

    public DeliveryDateAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setData(List<ArriveTimeBean.MeituanBean.DataBean> data) {
        mData = data;
        notifyDataSetChanged();
    }


    public void setCurrentItem(int currentDateItem) {
        this.mPreDatePos = currentDateItem;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_date_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_date = convertView.findViewById(R.id.tv_date);
            viewHolder.tv_dividerRight = convertView.findViewById(R.id.divider_right);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_dividerRight.setVisibility(View.VISIBLE);
        if (mPreDatePos == position){
            viewHolder.tv_date.setTextColor(mContext.getResources().getColor(R.color.select_date_color));
            viewHolder.tv_dividerRight.setVisibility(View.GONE);
        }else{
            viewHolder.tv_date.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        String date = mData.get(position).getDate();
        viewHolder.tv_date.setMinWidth(214);
        viewHolder.tv_date.setText(date);

        return convertView;
    }

    public class ViewHolder {

        TextView tv_date;
        View tv_dividerRight;

    }
}
