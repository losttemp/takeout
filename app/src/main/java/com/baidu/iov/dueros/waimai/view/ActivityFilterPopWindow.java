package com.baidu.iov.dueros.waimai.view;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.iov.dueros.waimai.adapter.ActivityFilterPopWindowAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.ui.R;

import java.util.List;

public class ActivityFilterPopWindow extends PopupWindow {

	private Context mContext;

	private ActivityFilterPopWindowAdapter mAdapter;

	private ListView lvActivityFilter;

	private  List<FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter> activityFilterList;

	public ActivityFilterPopWindow(final Context context) {
		mContext=context;
		View mContentView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentView = inflater.inflate(R.layout.pop_window_activity_filter, null);
		setContentView(mContentView);
		setWidth(ActionBar.LayoutParams.MATCH_PARENT);
		setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setOutsideTouchable(false);
		update();
		lvActivityFilter = (ListView) mContentView.findViewById(R.id.lv_activity_fliter);
		mAdapter = new ActivityFilterPopWindowAdapter(mContext);
		

	}

	public void setData(List<FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter> activityFilterList){
		mAdapter.setData(activityFilterList);
		lvActivityFilter.setAdapter(mAdapter);
		lvActivityFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mAdapter.updateSelected(position);
				dismiss();
			}
		});
	}

	
	public void showPop(View parentView) {
		if (!isShowing()) {
			showAsDropDown(parentView);
		} else {
			dismiss();
		}
	}
}
