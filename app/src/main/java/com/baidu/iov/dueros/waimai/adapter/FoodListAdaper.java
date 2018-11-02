package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.TestClass;
import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class FoodListAdaper extends BaseAdapter {
//    private List<OrderpreviewBean.DataBean> mData;
    private List<TestClass> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public FoodListAdaper(Context context) {
        mContext = context;
    }

    public void setData(List<TestClass> data) {
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
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = View.inflate(mContext,R.layout.item_order_details, null);
            viewHolder.tvfoodname = (TextView) convertView.findViewById(R.id.tv_food_name);
            viewHolder.tvfoodprice = (TextView) convertView.findViewById(R.id.tv_food_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvfoodname.setText(mData.get(position).getName());
        viewHolder.tvfoodprice.setText(mData.get(position).getPrice());
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private final class ViewHolder {
        TextView tvfoodname;
        TextView tvfoodprice;
    }

}
