package com.baidu.iov.dueros.waimai.view;
import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.baidu.iov.dueros.waimai.adapter.SortPopWindowAdapter;
import com.baidu.iov.dueros.waimai.ui.BusinessActivity;
import com.baidu.iov.dueros.waimai.ui.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionsPopWindow extends PopupWindow {

	public ConditionsPopWindow(final Context context) {
		List<String> sortList = new ArrayList<String>();
		sortList.addAll(Arrays.asList(context.getResources().getStringArray(R.array.sort_type)));

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
		final SortPopWindowAdapter adapter = new SortPopWindowAdapter(sortList, context);
		lvSortClass.setAdapter(adapter);

		lvSortClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.updateSelected(position);
				((BusinessActivity) context).setSortType(position);
				dismiss();
			}
		});

	}

	
	public void showFilterPopup(View parentView) {
		if (!isShowing()) {
			showAsDropDown(parentView);
		} else {
			dismiss();
		}
	}
}
