package com.baidu.iov.dueros.waimai.view;
import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.iov.dueros.waimai.adapter.ConditionsPopWindowAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.ui.BaseActivity;
import com.baidu.iov.dueros.waimai.ui.BusinessActivity;
import com.baidu.iov.dueros.waimai.ui.R;

import java.util.List;

public class ConditionsPopWindow extends PopupWindow {

	private Context mContext;

	private ConditionsPopWindowAdapter mAdapter;

	private ListView lvSort;

	private  List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> sortTypeList;

	public ConditionsPopWindow(final Context context) {
		mContext=context;
		View mContentView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentView = inflater.inflate(R.layout.pop_window_conditions, null);
		setContentView(mContentView);
		setWidth(ActionBar.LayoutParams.MATCH_PARENT);
		setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setOutsideTouchable(false);
		update();
		lvSort = (ListView) mContentView.findViewById(R.id.lv_conditions);
        mAdapter = new ConditionsPopWindowAdapter(mContext);
		

	}

	public void setData(List<FilterConditionsResponse.MeituanBean.MeituanData.SortType> sortTypeList){
        mAdapter.setData(sortTypeList);
		lvSort.setAdapter(mAdapter);
		lvSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.updateSelected(position);
				((BusinessActivity) mContext).setSortType(position);
				dismiss();
			}
		});
	}

	public void setBackgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = ((BaseActivity)mContext).getWindow().getAttributes();  
        lp.alpha = bgAlpha;
		((BaseActivity)mContext).getWindow().setAttributes(lp);
	}  

	
	public void showPop(View parentView) {
		if (!isShowing()) {
			showAsDropDown(parentView);
		} else {
			dismiss();
		}
	}
}
