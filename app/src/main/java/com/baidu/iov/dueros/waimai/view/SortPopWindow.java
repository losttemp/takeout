package com.baidu.iov.dueros.waimai.view;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.iov.dueros.waimai.adapter.SortPopWindowAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean;
import com.baidu.iov.dueros.waimai.ui.HomeActivity;
import com.baidu.iov.dueros.waimai.ui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortPopWindow extends PopupWindow {

	private List<SortTypeListBean> mSortList;
	private SortPopWindowAdapter mAdapter;
	public SortPopWindow(final Context context, List<SortTypeListBean> sortList) {
		mSortList = sortList;

		View mContentView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
				.LAYOUT_INFLATER_SERVICE);
		mContentView = inflater.inflate(R.layout.pop_window_sort, null);
		setContentView(mContentView);
		setWidth(ActionBar.LayoutParams.MATCH_PARENT);
		setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setOutsideTouchable(false);
		update();

		ListView lvSortClass = (ListView) mContentView.findViewById(R.id.lv_sort_class);
		mAdapter = new SortPopWindowAdapter(mSortList, context);
		lvSortClass.setAdapter(mAdapter);

		lvSortClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				adapter.updateSelected(position);
				((HomeActivity) context).setSortType(mSortList.get(position));
				dismiss();
			}
		});

	}

	public void updateList() {
		mAdapter.notifyDataSetChanged();
	}

}
