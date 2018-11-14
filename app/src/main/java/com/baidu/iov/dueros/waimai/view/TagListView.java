package com.baidu.iov.dueros.waimai.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class TagListView extends LinearLayout implements View.OnClickListener {
    private int mTextSize = 15;
    private int mTextColor = 0xFF43A7BC;
    //    private int mItemBackgroundResource = R.drawable.item_cinema_tag_bg;
    private int mDividerWidth = 30;
    private List<String> mTags;
    private String mTagValue;

    public TagListView(Context context) {
        this(context, null);
    }

    public TagListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setOrientation(HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagListView);

        mTextSize = a.getDimensionPixelSize(R.styleable.TagListView_textSize, mTextSize);
        mTextColor = a.getColor(R.styleable.TagListView_textColor, mTextColor);
//        mItemBackgroundResource = a.getResourceId(R.styleable.CinemaTagView_itemBackgroundResource, mItemBackgroundResource);
        mDividerWidth = a.getDimensionPixelOffset(R.styleable.TagListView_dividerWidth, mDividerWidth);
    }

    public TagListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTags(List<String> tags) {
        this.mTags = tags;
        removeAllViews();
        for (int i = 0; i <tags.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(tags.get(i));
            textView.setTextSize(mTextSize);
            textView.setTextColor(mTextColor);
            if (i == 0) {
                textView.setBackgroundColor(Color.BLACK);
            } else {
                textView.setBackgroundColor(Color.GRAY);
            }
//            textView.setBackgroundResource(mItemBackgroundResource);
            int padding = 20;
            textView.setPadding(30, 10, 30, 10);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, mDividerWidth, 0);
            textView.setLayoutParams(params);
            textView.setOnClickListener(this);
            textView.setTag(tags.get(i));
            addView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mTags.size(); i++) {
            if (v.getTag().equals(mTags.get(i))) {
                this.mTagValue = mTags.get(i);
                v.setBackgroundColor(Color.BLACK);
                v.setClickable(false);
            } else {
                View childAt = this.getChildAt(i);
                childAt.setBackgroundColor(Color.GRAY);
                childAt.setClickable(true);
            }
        }
    }

    public String getmTagValue() {
        return mTagValue;
    }
}
