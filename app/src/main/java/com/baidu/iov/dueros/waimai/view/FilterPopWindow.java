package com.baidu.iov.dueros.waimai.view;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.iov.dueros.waimai.adapter.FilterPopWindowAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse.MeituanBean
		.DataBean.ActivityFilterListBean;
import com.baidu.iov.dueros.waimai.ui.R;

import java.util.List;

public class FilterPopWindow extends PopupWindow {
	private List<ActivityFilterListBean>
			mFilterList;
	private FilterPopWindowAdapter mFilterAdapter;

	public FilterPopWindow(final Context context, List<ActivityFilterListBean> filterList,
						   final OnClickOkListener listener) {
		mFilterList = filterList;

		final View mContentView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
				.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = new LinearLayout(context);
		mContentView = inflater.inflate(R.layout.pop_window_filter, linearLayout);
		setContentView(mContentView);
		ListView lvFilterType = (ListView) mContentView.findViewById(R.id.lv_filter_type);
		AppCompatTextView tvReset = (AppCompatTextView) mContentView.findViewById(R.id.tv_reset);
		AppCompatTextView tvOk = (AppCompatTextView) mContentView.findViewById(R.id.tv_ok);

		setWidth(ActionBar.LayoutParams.MATCH_PARENT);
		setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setOutsideTouchable(true);
		update();

		mFilterAdapter = new FilterPopWindowAdapter(mFilterList, context);
		lvFilterType.setAdapter(mFilterAdapter);

		tvOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuffer migFilter = new StringBuffer();
				for (ActivityFilterListBean type : mFilterList) {
					for (ActivityFilterListBean.ItemsBean subtype : type.getItems()) {
						if (subtype.isChcked()) {
							if (!TextUtils.isEmpty(migFilter)) {
								migFilter.append(",");
							}
							migFilter.append(subtype.getCode());
						}
					}
				}
				if (listener != null) {
					listener.onClickOk(migFilter.toString());
				}
				dismiss();
			}
		});

		tvReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ActivityFilterListBean type : mFilterList) {
					for (ActivityFilterListBean.ItemsBean subtype : type.getItems()) {
						if (subtype.isChcked()) {
							subtype.setChcked(false);
						}
					}
				}
				mFilterAdapter.notifyDataSetChanged();
			}
		});
	}

	public void updateList() {
		mFilterAdapter.notifyDataSetChanged();
	}

	public interface OnClickOkListener {
		void onClickOk(String migFilter);
	}
}


