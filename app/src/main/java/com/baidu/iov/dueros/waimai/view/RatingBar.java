package com.baidu.iov.dueros.waimai.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.iov.dueros.waimai.R;

import java.math.BigDecimal;

public class RatingBar extends LinearLayout {

    private boolean mClickable;
    private boolean halfstart;
    private int starCount;
    private int starNum;
    private OnRatingChangeListener onRatingChangeListener;
    private float starImageSize;
    private float starImageWidth;
    private float starImageHeight;
    private float starImagePadding;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private Drawable starHalfDrawable;
    private int y = 1;
    private boolean isEmpty = true;

    public void setStarHalfDrawable(Drawable starHalfDrawable) {
        this.starHalfDrawable = starHalfDrawable;
    }

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    public void setmClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public void halfStar(boolean halfstart) {
        this.halfstart = halfstart;
    }

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    public void setStarImageWidth(float starImageWidth) {
        this.starImageWidth = starImageWidth;
    }

    public void setStarImageHeight(float starImageHeight) {
        this.starImageHeight = starImageHeight;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public void setImagePadding(float starImagePadding) {
        this.starImagePadding = starImagePadding;
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);

        starHalfDrawable = a.getDrawable(R.styleable.RatingBar_starHalf);
        starEmptyDrawable = a.getDrawable(R.styleable.RatingBar_starEmpty);
        starFillDrawable = a.getDrawable(R.styleable.RatingBar_starFill);
        starImageSize = a.getDimension(R.styleable.RatingBar_starImageSize, 120);
        starImageWidth = a.getDimension(R.styleable.RatingBar_starImageWidth, 60);
        starImageHeight = a.getDimension(R.styleable.RatingBar_starImageHeight, 120);
        starImagePadding = a.getDimension(R.styleable.RatingBar_starImagePadding, 15);
        starCount = a.getInteger(R.styleable.RatingBar_starCount, 5);
        starNum = a.getInteger(R.styleable.RatingBar_starNum, 0);
        mClickable = a.getBoolean(R.styleable.RatingBar_clickable, true);
        halfstart = a.getBoolean(R.styleable.RatingBar_halfstart, false);
        a.recycle();
        for (int i = 0; i < starNum; ++i) {
            ImageView imageView = getStarImageView(context, false);
            addView(imageView);
        }

        for (int i = 0; i < starCount; ++i) {
            ImageView imageView = getStarImageView(context, isEmpty);
            addView(imageView);
        }
    }

    private ImageView getStarImageView(Context context, boolean isEmpty) {
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(
                Math.round(starImageWidth),
                Math.round(starImageHeight)
        );
        imageView.setLayoutParams(para);
        imageView.setPadding(0, 0, Math.round(starImagePadding), 0);
        if (isEmpty) {
            imageView.setImageDrawable(starEmptyDrawable);
        } else {
            imageView.setImageDrawable(starFillDrawable);
        }
        return imageView;
    }

    public void setStar(float starCount) {
        int fint = (int) starCount;
        BigDecimal b1 = new BigDecimal(Float.toString(starCount));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
        float fPoint = b1.subtract(b2).floatValue();

        starCount = fint > this.starCount ? this.starCount : fint;
        starCount = starCount < 0 ? 0 : starCount;

        // drawfullstar
        for (int i = 0; i < starCount; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }

        // drawhalfstar
        if (fPoint > 0) {
            ((ImageView) getChildAt(fint)).setImageDrawable(starHalfDrawable);

            // drawemptystar
            for (int i = this.starCount - 1; i >= starCount + 1; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }

        } else {
            // drawemptystar
            for (int i = this.starCount - 1; i >= starCount; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }
        }
    }

    /**
     * change start listener
     */
    public interface OnRatingChangeListener {
        void onRatingChange(float RatingCount);
    }
}
