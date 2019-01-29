package com.baidu.iov.dueros.waimai.view;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.SortPopWindowAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse.MeituanBean
		.DataBean.SortTypeListBean;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;


import java.util.ArrayList;
import java.util.List;

public class SortPopWindow extends PopupWindow {
	private Context mContext;
	private List<SortTypeListBean> mSortList;
	private SortPopWindowAdapter mAdapter;
	private OnSelectedSortListener mOnSelectedSortListener;

	protected VoiceManager.CmdCallback mVoiceCallback;
	

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

		if (null == mVoiceCallback) {
			mVoiceCallback = new VoiceManager.CmdCallback(){

				@Override
				public void onCmdCallback(String cmd, String extra) {
					if (cmd.equals(VoiceManager.CMD_SELECT) && Integer.parseInt(extra) > 0) {
						if (listener != null) {
							listener.OnSelectedSort(mSortList.get(Integer.parseInt(extra)));
						}
						dismiss();
					}
				}
			};
		}

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
		ArrayList<String> cmdList = new ArrayList<String>();
		cmdList.add(VoiceManager.CMD_SELECT);
		VoiceManager.getInstance().registerCmd(mContext, cmdList, mVoiceCallback);
	}

	@Override
	public void dismiss() {
		VoiceManager.getInstance().unregisterCmd(mContext, mVoiceCallback);
		super.dismiss();
	}
}
