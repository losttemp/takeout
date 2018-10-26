package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse.MeituanBean
		.DataBean.SortTypeListBean;
import com.baidu.iov.dueros.waimai.ui.R;

import java.util.List;

public class SortPopWindowAdapter extends BaseAdapter {

	private List<SortTypeListBean> mSortList;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int mCurrentSelect;

	public SortPopWindowAdapter(List<SortTypeListBean> sortList, Context context) {
		this.mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mSortList = sortList;
	}

	@Override
	public int getCount() {
		return mSortList.size();
	}

	@Override
	public SortTypeListBean getItem(int position) {
		return mSortList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.layout_pop_sort_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tvSortName = (TextView) convertView.findViewById(R.id.tv_sort_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSortName.setText(mSortList.get(position).getName());

		/*if (mCurrentSelect == position) {
			viewHolder.tvSortName.setTextColor(mContext.getResources().getColor(R.color.blue));
		} else {
			viewHolder.tvSortName.setTextColor(mContext.getResources().getColor(R.color.black));
		}*/

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
