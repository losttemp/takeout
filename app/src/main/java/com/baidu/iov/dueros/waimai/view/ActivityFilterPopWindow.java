package com.baidu.iov.dueros.waimai.view;
import android.app.ActionBar;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.adapter.ActivityFilterPopWindowAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.ui.BusinessActivity;
import com.baidu.iov.dueros.waimai.ui.R;
import java.util.List;
public class ActivityFilterPopWindow extends PopupWindow {

	private Context mContext;

	private ActivityFilterPopWindowAdapter mAdapter;

	private ListView lvActivityFilter;

	private TextView tvReset;

	private TextView tvOk;

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
		tvReset =  mContentView.findViewById(R.id.tv_reset);
		tvOk =  mContentView.findViewById(R.id.tv_ok);
		mAdapter = new ActivityFilterPopWindowAdapter(mContext);
		

	}

	public void setData(List<FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter> mData){
		mAdapter.setData(mData);
		activityFilterList=mData;
		lvActivityFilter.setAdapter(mAdapter);
		lvActivityFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				dismiss();
			}
		});
		tvOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuffer migFilter = new StringBuffer();
				for (FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter activityFilter : activityFilterList) {
					for (FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter.Item item : activityFilter.getItems()) {
						if (item.isChcked()) {
							if (!TextUtils.isEmpty(migFilter)) {
								migFilter.append(",");
							}
							migFilter.append(item.getCode());
						}
					}
				}
				((BusinessActivity) mContext).setFilterTypes(migFilter.toString());
				dismiss();
			}
		});

		tvReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter activityFilter : activityFilterList) {
					for (FilterConditionsResponse.MeituanBean.MeituanData.ActivityFilter.Item item : activityFilter.getItems()) {
						if (item.isChcked()) {
							item.setChcked(false);
						}
					}
				}
				mAdapter.notifyDataSetChanged();
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
