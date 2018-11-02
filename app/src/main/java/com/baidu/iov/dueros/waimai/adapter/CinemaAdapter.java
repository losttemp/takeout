package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class CinemaAdapter extends BaseAdapter {
    private Context mContext;
    private List<CinemaBean.ListBean> mData;

    public CinemaAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<CinemaBean.ListBean> data) {
        mData = data;
        notifyDataSetChanged();
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
            convertView = View.inflate(mContext, R.layout.item_cinema, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_id = (TextView) convertView.findViewById(R.id.tv_cinema_id);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_cinema_name);
            viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_cinema_address);
            viewHolder.tv_distance = (TextView) convertView.findViewById(R.id.tv_cinema_distance);
            viewHolder.tv_others = (TextView) convertView.findViewById(R.id.tv_cinema_tag);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_cinema_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CinemaBean.ListBean listBean = mData.get(position);
        viewHolder.tv_id.setText("" + (position + 1));
        viewHolder.tv_name.setText(listBean.getName());
        viewHolder.tv_address.setText(listBean.getAddress());
        viewHolder.tv_distance.setText(listBean.getDistance() + "m");
        //viewHolder.tv_others.setText(listBean.getTag().toString());
        viewHolder.tv_price.setText(listBean.getMinPrice());

        return convertView;
    }

    public class ViewHolder {
        public TextView tv_id;
        public TextView tv_name;
        public TextView tv_address;
        public TextView tv_distance;
        public TextView tv_others;
        public TextView tv_price;
    }
}
