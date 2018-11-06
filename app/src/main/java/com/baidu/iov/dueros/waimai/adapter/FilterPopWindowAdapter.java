package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse.MeituanBean
		.DataBean.ActivityFilterListBean;
import com.baidu.iov.dueros.waimai.R;

import java.util.ArrayList;
import java.util.List;

public class FilterPopWindowAdapter extends BaseAdapter {

	private List<ActivityFilterListBean> mFilterList;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int mCurrentSelect;

	public FilterPopWindowAdapter(List<ActivityFilterListBean> filterList, Context context) {
		this.mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mFilterList = filterList;
	}

	@Override
	public int getCount() {
		return mFilterList.size();
	}

	@Override
	public ActivityFilterListBean getItem(int position) {
		return mFilterList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.layout_pop_filter_type_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tvTypeName = (AppCompatTextView) convertView.findViewById(R.id
					.tv_type_name);
			viewHolder.gvSubType = (GridView) convertView.findViewById(R.id.gv_subType);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String name = mFilterList.get(position).getGroup_title();
		if (!TextUtils.isEmpty(name)) {
			viewHolder.tvTypeName.setText(mFilterList.get(position).getGroup_title());
			viewHolder.tvTypeName.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tvTypeName.setVisibility(View.GONE);
		}

		final List<ActivityFilterListBean.ItemsBean> subTypeList = new ArrayList<>();
		subTypeList.addAll(mFilterList.get(position).getItems());

		final FilterSubTypeAdapter adapter = new FilterSubTypeAdapter(subTypeList,
				mContext);
		viewHolder.gvSubType.setAdapter(adapter);
		viewHolder.gvSubType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				ActivityFilterListBean.ItemsBean item = mFilterList.get(position).getItems().get
						(pos);

				if (item.isChcked()) {
					item.setChcked(false);
				} else {
					if (mFilterList.get(position).getSupport_multi_choice() == 0) {
						for (ActivityFilterListBean.ItemsBean data : mFilterList.get(position)
								.getItems()) {
							if (data.isChcked()) {
								data.setChcked(false);
								break;
							}
						}
					}
					item.setChcked(true);
				}

				adapter.notifyDataSetChanged();
			}
		});

		return convertView;
	}

	public static class ViewHolder {
		private AppCompatTextView tvTypeName;
		private GridView gvSubType;
	}

	public void updateSelected(int positon) {
		if (positon != mCurrentSelect) {
			mCurrentSelect = positon;
			notifyDataSetChanged();
		}
	}

}
