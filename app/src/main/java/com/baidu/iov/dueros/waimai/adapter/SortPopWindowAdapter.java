package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean;
import com.domain.multipltextview.MultiplTextView;

import java.util.List;

public class SortPopWindowAdapter extends BaseAdapter {

	private List<SortTypeListBean> mSortList;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int mCurrentSelect;
    private  ItemAccessibilityDelegate mItemAccessibilityDelegate;
	public SortPopWindowAdapter(List<SortTypeListBean> sortList, Context context) {
		this.mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mSortList = sortList;
	}

	public void setData(List<SortTypeListBean>  data){
		mSortList = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mSortList == null ? 0 : mSortList.size();
	}

	@Override
	public SortTypeListBean getItem(int position) {
		return mSortList== null ? null : mSortList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.layout_pop_sort_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tvSortName = (MultiplTextView) convertView.findViewById(R.id.tv_sort_name);
			viewHolder.rl= (RelativeLayout) convertView.findViewById(R.id.rl);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSortName.setText(mSortList.get(position).getName());
		viewHolder.rl.setContentDescription(mSortList.get(position).getName());
		viewHolder.rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemAccessibilityDelegate != null) {
					mItemAccessibilityDelegate.onItemAccessibilityDelegate(position);
				}
			}
		});
		viewHolder.rl.setAccessibilityDelegate(new View.AccessibilityDelegate(){
			@Override
			public boolean performAccessibilityAction(View host, int action, Bundle args) {
				switch (action) {
					case AccessibilityNodeInfo.ACTION_CLICK:
						if (mItemAccessibilityDelegate != null) {
							mItemAccessibilityDelegate.onItemAccessibilityDelegate(position);
						}
						return true;
					default:
						break;
				}
				return false;
			}});

		return convertView;
	}

	public static class ViewHolder {
		private MultiplTextView tvSortName;
		private RelativeLayout rl;
	}

	public void updateSelected(int positon) {
		if (positon != mCurrentSelect) {
			mCurrentSelect = positon;
			notifyDataSetChanged();
		}
	}

	public interface ItemAccessibilityDelegate {
		void onItemAccessibilityDelegate(int position);
	}

	public void setItemAccessibilityDelegate(ItemAccessibilityDelegate itemAccessibilityDelegate) {
		mItemAccessibilityDelegate = itemAccessibilityDelegate;
	}
	
}
