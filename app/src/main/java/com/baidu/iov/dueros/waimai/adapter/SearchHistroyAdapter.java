package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.ui.SearchActivity;
import com.baidu.iov.dueros.waimai.utils.SharedPreferencesUtils;

import java.util.List;
public class SearchHistroyAdapter extends BaseAdapter {

	private List<String> mHistorys;
	private Context mContext;
	private LayoutInflater mLayoutInflater;

	private ItemAccessibilityDelegate mItemAccessibilityDelegate;

	public SearchHistroyAdapter(List<String> historys, Context context) {
		this.mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mHistorys = historys;
	}

	@Override
	public int getCount() {
		return mHistorys == null ? 0 : mHistorys.size();
	}

	@Override
	public String getItem(int position) {
		return mHistorys == null ?null:mHistorys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mHistorys == null ? 0 :position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.layout_search_history_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tvHistoryNum = convertView.findViewById(R.id.tv_history_num);
			viewHolder.tvHistoryName =  convertView.findViewById(R.id.tv_history_name);
			viewHolder.ivDelete =  convertView.findViewById(R.id.iv_delete);
			viewHolder.rlHistory= (RelativeLayout) convertView.findViewById(R.id.rl_history);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvHistoryNum.setText(position + 1 + "");
		viewHolder.tvHistoryName.setText(mHistorys.get(position));

		viewHolder.rlHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemAccessibilityDelegate != null) {
					mItemAccessibilityDelegate.onItemAccessibilityDelegate(position);
				}
			}
		});
		viewHolder.rlHistory.setAccessibilityDelegate(new View.AccessibilityDelegate(){
			@Override
			public boolean performAccessibilityAction(View host, int action, Bundle args) {
				switch (action) {
					case AccessibilityNodeInfo.ACTION_CLICK:
						((SearchActivity)mContext).VoicesSelectListItem(position);
						break;
					default:
						break;
				}
				return true;
			}});

		viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				removeHistory(position);
			}
		});


		viewHolder.ivDelete.setAccessibilityDelegate(new View.AccessibilityDelegate(){
			@Override
			public boolean performAccessibilityAction(View host, int action, Bundle args) {
				switch (action) {
					case AccessibilityNodeInfo.ACTION_CLICK:
						removeHistory(position);
						break;
					default:
						break;
				}
				return true;
			}});
		return convertView;
	}
	
	private  void removeHistory(int position){
		mHistorys.remove(position);
		SharedPreferencesUtils.deleteSearchHistory(mHistorys);
		notifyDataSetChanged();
		if (mHistorys.isEmpty()){
			((SearchActivity)mContext).setmLlHistoryVisibility();
		}
	}

	public interface ItemAccessibilityDelegate {
		void onItemAccessibilityDelegate(int position);
	}

	public void setItemAccessibilityDelegate(ItemAccessibilityDelegate itemAccessibilityDelegate) {
		mItemAccessibilityDelegate = itemAccessibilityDelegate;
	}

	public static class ViewHolder {
		private TextView tvHistoryNum;
		private TextView tvHistoryName;
		private AppCompatImageView ivDelete;
		private RelativeLayout rlHistory;
	}
}
