package com.baidu.iov.dueros.waimai.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

import com.baidu.iov.dueros.waimai.R;

public class ConstraintHeightListView extends ListView{


    private float mMaxHeight = 100;

    public ConstraintHeightListView(Context context) {
        this(context, null);
    }

    public ConstraintHeightListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConstraintHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ConstraintHeightListView, 0, defStyleAttr);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int type = array.getIndex(i);
            if (type == R.styleable.ConstraintHeightListView_maxHeight) {
                mMaxHeight = array.getDimension(type, -1);
            }
        }
        array.recycle();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (mMaxHeight <= specSize && mMaxHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Float.valueOf(mMaxHeight).intValue(),
                    MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setmMaxHeight(float maxHeight) {
        this.mMaxHeight = maxHeight;
    }
}
