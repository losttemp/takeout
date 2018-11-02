package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class ConditionsPopWindowAdapter extends BaseAdapter {

	private List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> mData;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int mCurrentSelect;

	public ConditionsPopWindowAdapter(Context context) {
		mContext = context;
		this.mLayoutInflater = LayoutInflater.from(context);
	}

	public void setData(List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> sortTypeList){
		mData=sortTypeList;
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_conditions, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.tvSortName = (TextView) convertView.findViewById(R.id.tv_sort_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSortName.setText(mData.get(position).getName());

		if (mCurrentSelect == position) {
			viewHolder.tvSortName.setTextColor(mContext.getResources().getColor(R.color.blue));
		} else {
			viewHolder.tvSortName.setTextColor(mContext.getResources().getColor(R.color.black));
		}

		return convertView;
	}

	public static class ViewHolder {
		private TextView tvSortName;
	}

	public void updateSelected(int positon) {
		if (positon != mCurrentSelect) {
			mCurrentSelect = positon;
			notifyDataSetChanged();
		}
	}
}
