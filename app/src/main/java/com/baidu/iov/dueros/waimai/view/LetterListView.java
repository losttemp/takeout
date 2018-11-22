package com.baidu.iov.dueros.waimai.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.iov.dueros.waimai.R;


public class LetterListView extends View {

    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    String[] b = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "#"};
    int choose = -1;
    Paint paint = new Paint();


    public LetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LetterListView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            Paint mpaint = new Paint();
            paint.setColor(Color.parseColor("#99ffffff"));
            paint.setTextSize(getResources().getDimension(R.dimen.px30sp));
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAntiAlias(true);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            if (i == choose) {
                paint.setColor(Color.parseColor("#00df7b"));
                mpaint.setColor(Color.parseColor("#1A00df7b"));
                paint.setFakeBoldText(true);
                canvas.drawCircle(width / 2 - paint.measureText(b[i]) / 2 + 10,
                        singleHeight * i + singleHeight - 8, getResources().getDimension(R.dimen.px38dp), mpaint);
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;

            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onTouchingLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onTouchingLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                listener.onTouchexit();
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);

        void onTouchexit();
    }
}
