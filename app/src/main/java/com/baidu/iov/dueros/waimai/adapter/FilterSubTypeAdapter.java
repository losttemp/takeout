package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.ui.R;

import java.util.List;

public class FilterSubTypeAdapter extends BaseAdapter {

	private List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean>
			mHistorys;
	private Context mContext;
	private LayoutInflater mLayoutInflater;

	public FilterSubTypeAdapter(List<FilterConditionResponse.MeituanBean.DataBean
			.ActivityFilterListBean.ItemsBean> historys, Context context) {
		this.mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mHistorys = historys;
	}

	@Override
	public int getCount() {
		return mHistorys.size();
	}

	@Override
	public FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean getItem
			(int position) {
		return mHistorys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.layout_filter_sub_type_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tvSubTypeName = (TextView) convertView.findViewById(R.id.tv_sub_type_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSubTypeName.setText(mHistorys.get(position).getName());
		if (mHistorys.get(position).isChcked()) {
			viewHolder.tvSubTypeName.setTextColor(mContext.getResources().getColor(R.color.blue));
		} else {
			viewHolder.tvSubTypeName.setTextColor(mContext.getResources().getColor(R.color.black));
		}

		return convertView;
	}

	public static class ViewHolder {
		private TextView tvSubTypeName;
	}
}
