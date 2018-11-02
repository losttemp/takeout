package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.baidu.iov.dueros.waimai.net.entity.response.SearchSuggestResponse.MeituanBean.DataBean
		.SuggestBean;
import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class SearchSuggestAdapter extends BaseAdapter {

	private List<SuggestBean> mSuggests;
	private Context mContext;
	private LayoutInflater mLayoutInflater;

	public SearchSuggestAdapter(List<SuggestBean> suggests, Context context) {
		this.mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mSuggests = suggests;
	}

	@Override
	public int getCount() {
		return mSuggests.size();
	}

	@Override
	public SuggestBean getItem(int position) {
		return mSuggests.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.layout_search_suggest_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tvSuggestName = (AppCompatTextView) convertView.findViewById(R.id
					.tv_suggest_name);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSuggestName.setText(mSuggests.get(position).getSuggest_query());

		return convertView;
	}

	public static class ViewHolder {
		private AppCompatTextView tvSuggestName;

	}
}
