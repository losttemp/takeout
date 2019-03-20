package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;

import java.util.List;

public class FilterSubTypeAdapter extends BaseAdapter {

	private List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean>
			mItemsBeans;
	private Context mContext;
	private LayoutInflater mLayoutInflater;

	private  ItemAccessibilityDelegate mItemAccessibilityDelegate;

	public FilterSubTypeAdapter(List<FilterConditionResponse.MeituanBean.DataBean
			.ActivityFilterListBean.ItemsBean> itemsBeans, Context context) {
		this.mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mItemsBeans = itemsBeans;
	}

	public void setData(List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean> data){
		mItemsBeans = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mItemsBeans == null ? 0 : mItemsBeans.size();
	}

	@Override
	public FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean getItem
			(int position) {
		return mItemsBeans == null ? null :mItemsBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
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
		viewHolder.tvSubTypeName.setContentDescription(mItemsBeans.get(position).getName());
		viewHolder.tvSubTypeName.setAccessibilityDelegate(new View.AccessibilityDelegate(){
			@Override
			public boolean performAccessibilityAction(View host, int action, Bundle args) {
				switch (action) {
					case AccessibilityNodeInfo.ACTION_CLICK:
						if (mItemAccessibilityDelegate != null) {
							mItemAccessibilityDelegate.onItemAccessibilityDelegate(position);
							StandardCmdClient.getInstance().playTTS(mContext,"BUBBLE");
						}
						break;
					default:
						break;
				}
				return true;
			}});
	
		return convertView;
	}

	public static class ViewHolder {
		private TextView tvSubTypeName;
	}

	public interface ItemAccessibilityDelegate {
		void onItemAccessibilityDelegate(int position);
	}

	public void setItemAccessibilityDelegate(ItemAccessibilityDelegate itemAccessibilityDelegate) {
		mItemAccessibilityDelegate = itemAccessibilityDelegate;
	}
}
