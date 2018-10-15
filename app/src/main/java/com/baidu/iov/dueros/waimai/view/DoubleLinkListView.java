package com.baidu.iov.dueros.waimai.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baidu.iov.dueros.waimai.adapter.DoubleLinkAdapter;
import com.baidu.iov.dueros.waimai.adapter.FilterSingleAdapter;
import com.baidu.iov.dueros.waimai.ui.R;

public class DoubleLinkListView extends LinearLayout {
    private static final String TAG = DoubleLinkListView.class.getSimpleName();

    private FilterSingleAdapter mLeftAdapter;
    private DoubleLinkAdapter mRightAdapter;
    private ListView mRightList;
    private ListView mLeftList;

    public void setOnItemClickListener(AdapterView.OnItemClickListener leftItemClickListener,
                                       AdapterView.OnItemClickListener rightItemClickListener) {
        mLeftList.setOnItemClickListener(leftItemClickListener);
        mRightList.setOnItemClickListener(rightItemClickListener);
    }

    public FilterSingleAdapter getLeftAdapter() {
        return mLeftAdapter;
    }

    public DoubleLinkAdapter getRightAdapter() {
        return mRightAdapter;
    }

    public ListView getRightList() {
        return mRightList;
    }

    public DoubleLinkListView(Context context) {
        this(context, null);
    }

    public DoubleLinkListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleLinkListView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);

        mLeftList = new ListView(context);
        mLeftList.setDivider(new ColorDrawable(getResources().getColor(R.color.black)));
        mLeftList.setDividerHeight(1);
        mLeftAdapter = new FilterSingleAdapter(context);
        mLeftList.setAdapter(mLeftAdapter);
        mLeftList.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));


        mRightList = new ListView(context);
        mRightList.setDividerHeight(0);
        mRightAdapter = new DoubleLinkAdapter(context);
        mRightList.setAdapter(mRightAdapter);
        mRightList.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        mRightList.setVisibility(GONE);

        addView(mLeftList);
        addView(mRightList);
    }
}