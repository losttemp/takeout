package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class FilterSubTypeAdapter extends BaseAdapter {

	private List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean>
			mItemsBeans;
	private Context mContext;
	private LayoutInflater mLayoutInflater;

	public FilterSubTypeAdapter(List<FilterConditionResponse.MeituanBean.DataBean
			.ActivityFilterListBean.ItemsBean> itemsBeans, Context context) {
		this.mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mItemsBeans = itemsBeans;
	}

	@Override
	public int getCount() {
		return mItemsBeans.size();
	}

	@Override
	public FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean getItem
			(int position) {
		return mItemsBeans.get(position);
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

		viewHolder.tvSubTypeName.setText(mItemsBeans.get(position).getName());
		if (mItemsBeans.get(position).isChcked()) {
			viewHolder.tvSubTypeName.setBackgroundResource(R.drawable.shape_filter_selected_bg);
		} else {
			viewHolder.tvSubTypeName.setBackgroundResource(R.drawable.shape_filter_unselected_bg);
		}
		if (viewHolder.tvSubTypeName.getText().toString().equals(mContext.getResources().getString(R.string.supporting_invoices))){
			viewHolder.tvSubTypeName.setVisibility(View.GONE);
		}else{
			viewHolder.tvSubTypeName.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	public static class ViewHolder {
		private TextView tvSubTypeName;
	}
}
