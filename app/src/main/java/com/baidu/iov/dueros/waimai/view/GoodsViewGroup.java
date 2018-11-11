package com.baidu.iov.dueros.waimai.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;


public class GoodsViewGroup extends RadioGroup {

    private Context mContext;
    private int horInterval;
    private int verInterval;
    private int viewWidth;
    private int viewHeight;
    private int horPadding;
    private int verPadding;
    private float textSize;
    private int bgResoureNor;
    private int textColorNor;
    private int bgResoureSel;
    private int textColorSel;
    private boolean isSelector;

    public GoodsViewGroup(Context context) {
        this(context, null);
    }

    public GoodsViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        getResources().getColor(R.color.goods_item_text_normal);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet set) {
        mContext = context;
        TypedArray attrs = mContext.obtainStyledAttributes(set, R.styleable.GoodsViewGroup);
        isSelector = attrs.getBoolean(R.styleable.GoodsViewGroup_isSelector, true);
        textSize = attrs.getDimensionPixelSize(R.styleable.GoodsViewGroup_itemTextSize, 14);
        horInterval = attrs.getDimensionPixelSize(R.styleable.GoodsViewGroup_horInterval, 150);
        verInterval = attrs.getDimensionPixelSize(R.styleable.GoodsViewGroup_verInterval, 20);
        horPadding = attrs.getDimensionPixelSize(R.styleable.GoodsViewGroup_horPadding, 20);
        verPadding = attrs.getDimensionPixelSize(R.styleable.GoodsViewGroup_verPadding, 10);
        bgResoureNor = attrs.getResourceId(R.styleable.GoodsViewGroup_normal_drawable, R.drawable.goods_item_btn_normal);
        bgResoureSel = attrs.getResourceId(R.styleable.GoodsViewGroup_selected_drawable, R.drawable.goods_item_btn_selected);
        textColorNor = attrs.getColor(R.styleable.GoodsViewGroup_normal_textColor, getColorResoure(R.color.goods_item_text_normal));
        textColorSel = attrs.getColor(R.styleable.GoodsViewGroup_selected_textColor, getColorResoure(R.color.goods_item_text_selected));
    }

    private int getColorResoure(int resId) {
        return getResources().getColor(resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = measureWidth(widthMeasureSpec);
        viewHeight = measureHeight(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(viewWidth, getViewHeight());
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;
        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                result = getSuggestedMinimumHeight();
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    private int getViewHeight() {
        int viewwidth = horInterval;
        int viewheight = verInterval;
        if (getChildCount() > 0) {
            viewheight = getChildAt(0).getMeasuredHeight() + verInterval;
        }
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            if (viewwidth + getNextHorLastPos(i) > viewWidth) {
                viewwidth = horInterval;
                viewheight += (measureHeight + verInterval);
            } else {
                viewwidth += (measuredWidth + horInterval);
            }
        }
        return viewheight;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int posLeft = 0;
        int posTop = verInterval;
        int posRight = horInterval;
        int posBottom;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            if (posRight + getNextHorLastPos(i) > viewWidth) {
                posLeft = 0;
                posTop += (measureHeight + verInterval);
            }
            posRight = posLeft + measuredWidth;
            posBottom = posTop + measureHeight;
            childView.layout(posLeft, posTop, posRight, posBottom);
            posLeft += (measuredWidth + horInterval);
        }
    }

    private int getNextHorLastPos(int i) {
        return getChildAt(i).getMeasuredWidth() + horInterval;
    }

    private OnGroupItemClickListener onGroupItemClickListener;

    public void setGroupClickListener(OnGroupItemClickListener listener) {
        onGroupItemClickListener = listener;
        for (int i = 0; i < getChildCount(); i++) {
            final TextView childView = (TextView) getChildAt(i);
            final int itemPos = i;
        }
    }

    public void chooseItemStyle(int pos) {
        clearItemsStyle();
        if (pos < getChildCount()) {
            TextView childView = (TextView) getChildAt(pos);
            childView.setBackgroundResource(bgResoureSel);
            childView.setTextColor(textColorSel);
            setItemPadding(childView);
        }
    }

    private void clearItemsStyle() {
        for (int i = 0; i < getChildCount(); i++) {
            TextView childView = (TextView) getChildAt(i);
            childView.setBackgroundResource(bgResoureNor);
            childView.setTextColor(textColorNor);
            setItemPadding(childView);
        }
    }

    private void setItemPadding(TextView view) {
        view.setPadding(horPadding, verPadding, horPadding, verPadding);
    }

    public void setSelector(boolean selector) {
        isSelector = selector;
    }

    public interface OnGroupItemClickListener {
        void onGroupItemClick(int itemPos, String key, String value);
    }
}