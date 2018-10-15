package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.ui.R;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;

import java.util.List;

public class ListDropDownAdapter<T extends CinemaBean.DataBean> extends BaseAdapter {

    private Context mContext;
    private List<T> mData;
    private int mSelectedItem = -1;

    public void setSelectedItem(int selectedItem) {
        mSelectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }

    public ListDropDownAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<T> data) {
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
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_drop_down, null);
            viewHolder = new ViewHolder();
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.mNum = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(viewHolder);
        }

        viewHolder.mTitle.setText(mData.get(position).getName() + " (" + mData.get(position).getValue() + ")");
        if (mData.get(position).getValue() != 0) {
            viewHolder.mNum.setText(mData.get(position).getValue() + "");
        } else {
            viewHolder.mNum.setText("");
        }

        if (mSelectedItem != -1) {
            if (mSelectedItem == position) {
                viewHolder.mTitle.setTextColor(mContext.getResources().getColor(R.color.drop_down_selected));
                viewHolder.mNum.setTextColor(mContext.getResources().getColor(R.color.drop_down_selected));
            } else {
                viewHolder.mTitle.setTextColor(mContext.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.mNum.setTextColor(mContext.getResources().getColor(R.color.drop_down_unselected));
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView mTitle;
        TextView mNum;
    }
}
