package com.baidu.iov.dueros.waimai.view;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.adapter.FilterSubTypeAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import java.util.ArrayList;
import java.util.List;

public class FilterPopWindow extends PopupWindow {

	private Context  mContext;
	
	private List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean> mFilterList;
	
	private FilterSubTypeAdapter mFilterSubTypeAdapter;

	private GridView gvFilterType;

	private RelativeLayout rlParent;

    private ArrayList<String> prefix = new ArrayList<>();

	private Handler handler = new Handler();

	private FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean mActivityFilterListBean;
	private List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean> itemsBeans= new ArrayList<>();

	public FilterPopWindow( Context context, List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean> filterList,
						   final OnClickOkListener listener) {

		mContext=context;
		mFilterList = filterList;

		final View mContentView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
				.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = new LinearLayout(context);
		mContentView = inflater.inflate(R.layout.pop_window_filter, linearLayout);
		setContentView(mContentView);
		gvFilterType =  mContentView.findViewById(R.id.gv_subType);
		TextView tvOk = mContentView.findViewById(R.id.tv_ok);
		rlParent =  mContentView.findViewById(R.id.rl_parent);
		setWidth(ActionBar.LayoutParams.MATCH_PARENT);
		setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setOutsideTouchable(true);

        prefix.add(mContext.getResources().getString(R.string.prefix_choice));
        prefix.add(mContext.getResources().getString(R.string.prefix_check));
        prefix.add(mContext.getResources().getString(R.string.prefix_open));
		
		update();
		mActivityFilterListBean =getActivityFilterListBean(mFilterList);
		itemsBeans=mActivityFilterListBean.getItems();
		mFilterSubTypeAdapter = new FilterSubTypeAdapter(itemsBeans, context);
		gvFilterType.setAdapter(mFilterSubTypeAdapter);
		gvFilterType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				itemFilter(pos);
			}
		});
		tvOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                toFilter(listener);
			}
		});

		mFilterSubTypeAdapter.setItemAccessibilityDelegate(new FilterSubTypeAdapter.ItemAccessibilityDelegate() {
			@Override
			public void onItemAccessibilityDelegate(int position) {
				itemFilter(position);
				dismiss();
			}
		});

        tvOk.setAccessibilityDelegate(new View.AccessibilityDelegate(){
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                switch (action) {
                    case AccessibilityNodeInfo.ACTION_CLICK:
                        toFilter(listener);
						StandardCmdClient.getInstance().playTTS(mContext,"BUBBLE");
                        break;
                    default:
                        break;
                }
                return true;
            }});

	}

	private  void itemFilter(int pos){
		ActivityFilterListBean.ItemsBean item = itemsBeans.get(pos);
		if (item.isChcked()) {
			item.setChcked(false);
		} else {
			if (mActivityFilterListBean.getSupport_multi_choice() == 0) {
				for (FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean data : mActivityFilterListBean.getItems()) {
					if (data.isChcked()) {
						data.setChcked(false);
						break;
					}
				}
			}
			item.setChcked(true);
		}

		mFilterSubTypeAdapter.notifyDataSetChanged();
	}
	
	private  void  toFilter(OnClickOkListener listener){
        StringBuffer migFilter = new StringBuffer();
        if(mActivityFilterListBean!=null&&mActivityFilterListBean.getItems()!=null){
        for (ActivityFilterListBean.ItemsBean subtype : mActivityFilterListBean.getItems()) {
            if (subtype.isChcked()) {
                if (!TextUtils.isEmpty(migFilter)) {
                    migFilter.append(",");
                }
                migFilter.append(subtype.getCode());
            }
        }}
        if (listener != null) {
            listener.onClickOk(migFilter.toString());
        }
        dismiss();
    }

	public  FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean getActivityFilterListBean(List<ActivityFilterListBean> filterList){
		FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean mActivityFilterListBean = new FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean();
		if (filterList==null||filterList.isEmpty()){
			return mActivityFilterListBean;
		}

		int size =filterList.size();
		for (int i = 0; i < size; i++) {
			String name = filterList.get(i).getGroup_title();
			if (!TextUtils.isEmpty(name)&&name.contains(mContext.getResources().getString(R.string.feature_business))){
				mActivityFilterListBean=filterList.get(i);
				int items=mActivityFilterListBean.getItems().size();
				List<FilterConditionResponse.MeituanBean.DataBean.ActivityFilterListBean.ItemsBean> deleteSubTypes = new ArrayList<>();
				for (int j = 0; j < items; j++) {
					if (mActivityFilterListBean.getItems().get(j).getName().equals(mContext.getResources().getString(R.string.new_bus))||mActivityFilterListBean.getItems().get(j).getName().equals(mContext.getResources().getString(R.string.booking))||mActivityFilterListBean.getItems().get(j).getName().equals(mContext.getResources().getString(R.string.supporting_invoices))){
						deleteSubTypes.add(mActivityFilterListBean.getItems().get(j));
					}

				}
				mActivityFilterListBean.getItems().removeAll(deleteSubTypes);
				return mActivityFilterListBean;
			} }

		return mActivityFilterListBean;
	}

	public void updateList() {
		mFilterSubTypeAdapter.notifyDataSetChanged();
	}

	public interface OnClickOkListener {
		void onClickOk(String migFilter);
	}

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
		//

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				rlParent.setBackgroundResource(R.drawable.bg_filter_pop);
				rlParent.setVisibility(View.VISIBLE);
			}
		}, 300);
        AccessibilityClient.getInstance().register(mContext,true,prefix, null);
    }

	public final void parentDismiss(){
		super.dismiss();
	}

    @Override
    public void dismiss() {
        AccessibilityClient.getInstance().unregister(mContext);
		rlParent.setBackground(null);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				rlParent.setVisibility(View.GONE);
				parentDismiss();
			}
		}, 50);
    }
}


