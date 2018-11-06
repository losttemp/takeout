package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;


import java.util.List;

public class ActivityFilterPopWindowAdapter extends BaseAdapter {

	private List<FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter> mData;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	public ActivityFilterPopWindowAdapter(Context context) {
		mContext = context;
		this.mLayoutInflater = LayoutInflater.from(mContext);
       
	}

	public void setData(List<FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter> activityFilterList){
		mData=activityFilterList;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_activity_filter, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.rvItems = (android.support.v7.widget.RecyclerView) convertView.findViewById(R.id.rv_items);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.rvItems.setLayoutManager(new GridLayoutManager(mContext, 3));
        FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter mActivityFilter=mData.get(position);
        viewHolder.tvName.setText(mActivityFilter.getGroup_title());
        final ActivityFilterItemsAdpater mActivityFilterItemsAdpater=new ActivityFilterItemsAdpater(mContext);
		mActivityFilterItemsAdpater.setData(mActivityFilter.getItems());
		viewHolder.rvItems.setAdapter(mActivityFilterItemsAdpater);
		mActivityFilterItemsAdpater.setOnItemClickListener(new ActivityFilterItemsAdpater.OnItemClickListener() {
			@Override
			public void onItemClick(int itemPosition) {
				FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter.Item item=mData.get(position).getItems().get(itemPosition);
				if (item.getBubble_info().isIs_show()) {
					item.getBubble_info().setIs_show(false);
				} else {
					if (mData.get(position).getSupport_multi_choice() == 0) {
						for (FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter.Item  data : mData.get(position)
								.getItems()) {
							if (data.getBubble_info().isIs_show()) {
								data.getBubble_info().setIs_show(false);
								break;
							}
						}
					}
					item.getBubble_info().setIs_show(true);
				}
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		private TextView tvName;
		private RecyclerView rvItems;
	}

	
}
