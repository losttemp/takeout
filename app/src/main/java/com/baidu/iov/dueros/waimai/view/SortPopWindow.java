package com.baidu.iov.dueros.waimai.view;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.SortPopWindowAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse.MeituanBean
		.DataBean.SortTypeListBean;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;


import java.util.ArrayList;
import java.util.List;

public class SortPopWindow extends PopupWindow {
	private Context mContext;
	private List<SortTypeListBean> mSortList;
	private SortPopWindowAdapter mAdapter;
	private OnSelectedSortListener mOnSelectedSortListener;

	private ArrayList<String> prefix = new ArrayList<>();
	

	public SortPopWindow(Context context, List<SortTypeListBean> sortList,
						 final OnSelectedSortListener listener) {
		mContext=context;
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
		prefix.add(mContext.getResources().getString(R.string.prefix_choice));
		prefix.add(mContext.getResources().getString(R.string.prefix_check));
		prefix.add(mContext.getResources().getString(R.string.prefix_open));
		ListView lvSortClass = (ListView) mContentView.findViewById(R.id.lv_sort_class);
		mAdapter = new SortPopWindowAdapter(mSortList, context);
		lvSortClass.setAdapter(mAdapter);
		lvSortClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (listener != null) {
					listener.OnSelectedSort(mSortList.get(position));
				}
				dismiss();
			}
		});

		mAdapter.setItemAccessibilityDelegate(new SortPopWindowAdapter.ItemAccessibilityDelegate() {
			@Override
			public void onItemAccessibilityDelegate(int position) {
				if (listener != null) {
					listener.OnSelectedSort(mSortList.get(position));
				}
				dismiss();
			}
		});

		
	}

	public void updateList() {
		mAdapter.notifyDataSetChanged();
	}

	public interface OnSelectedSortListener {
		void OnSelectedSort(FilterConditionResponse.MeituanBean.DataBean.SortTypeListBean type);
		
	}

	@Override
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
		AccessibilityClient.getInstance().register(mContext,true,prefix, null);
	}

	@Override
	public void dismiss() {
		AccessibilityClient.getInstance().unregister(mContext);
		super.dismiss();
	}
}
