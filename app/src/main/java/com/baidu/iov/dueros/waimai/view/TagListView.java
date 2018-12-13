package com.baidu.iov.dueros.waimai.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class TagListView extends LinearLayout implements View.OnClickListener {
    private int mTextSize = 15;
    private int mTextColor = 0xFF43A7BC;
    private int mDividerWidth = 30;
    private List<String> mTags;

    public void setmTagValue(String mTagValue) {
        this.mTagValue = mTagValue;
    }

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
        mDividerWidth = a.getDimensionPixelOffset(R.styleable.TagListView_dividerWidth, mDividerWidth);
    }

    public TagListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTags(List<String> tags, String type) {
        this.mTags = tags;
        removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(tags.get(i));
            textView.getPaint().setTextSize(mTextSize);
            textView.setTextColor(mTextColor);
            if (!TextUtils.isEmpty(type)&&type.equals(tags.get(i))) {
                setmTagValue(type);
                textView.setBackgroundColor(this.getResources().getColor(R.color.address_select_type_color));
            } else {
                textView.setBackgroundColor(this.getResources().getColor(R.color.address_type_color));
            }
            int width = getResources().getDimensionPixelSize(R.dimen.px170dp);
            int height = getResources().getDimensionPixelSize(R.dimen.px58dp);
            LayoutParams params = new LayoutParams(width, height);
            params.setMargins(0, 0, mDividerWidth, 0);
            textView.setGravity(Gravity.CENTER);
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
                v.setBackgroundColor(this.getResources().getColor(R.color.address_select_type_color));
                v.setClickable(false);
            } else {
                View childAt = this.getChildAt(i);
                childAt.setBackgroundColor(this.getResources().getColor(R.color.address_type_color));
                childAt.setClickable(true);
            }
        }
    }

    public String getmTagValue() {
        return mTagValue;
    }
}
