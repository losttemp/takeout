package com.baidu.iov.dueros.waimai.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;

public class ClearEditText extends AppCompatAutoCompleteTextView implements View.OnFocusChangeListener, TextWatcher {

    private int maxLength=-1;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);
        addTextChangedListener(this);
        setOnFocusChangeListener(this);

//        setFocusable(true);
    }

    private void showTextClearButton() {
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_close, 0);
        this.setOnTouchListener(null);
        this.setOnTouchListener(new ClearButtonOnTouchListener(this));
    }

    private void hideTextClearButton() {
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        this.setOnTouchListener(null);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (getText().length()>0) {
            showTextClearButton();
        } else {
            hideTextClearButton();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int lengthBefore, int lengthAfter) {
        ClearEditText.this.requestFocus();
        if (s == null || s.length() == 0 && hasFocus()) {
            hideTextClearButton();
        } else {
            showTextClearButton();
            if (maxLength>0&&s.length() > maxLength) {
                ToastUtils.show(getContext(), getContext().getResources().getString(R.string.edit_text_view_max_length_hint),Toast.LENGTH_SHORT);
                this.setText(s.toString().substring(0, maxLength));
                this.setSelection(this.getText().length());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public class ClearButtonOnTouchListener implements View.OnTouchListener {
        private Drawable mDrawable;

        public ClearButtonOnTouchListener(TextView view) {
            super();
            Drawable[] drawables = view.getCompoundDrawables();
            if (drawables != null && drawables.length == 4) {
                this.mDrawable = drawables[2];
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mDrawable != null) {
                final int x = (int) event.getX() + v.getLeft();
                final int y = (int) event.getY();
                final Rect bounds = mDrawable.getBounds();
                int x1 = (v.getRight() - bounds.width() - 20);
                int x2 = (v.getRight() - v.getPaddingRight() + 20);
                int y1 = (v.getTop() - 20);
                int y2 = (v.getBottom() - v.getPaddingBottom() + 20);
                if (x > x1 && x < x2 && y > y1 && y < y2) {
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN) {
                        ClearEditText.this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_close, 0);
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        ClearEditText.this.setText("");
                        if (!ClearEditText.this.isCursorVisible()) {
                            ClearEditText.this.setCursorVisible(true);
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        ClearEditText.this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_close, 0);
                    } else {
                        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                            ClearEditText.this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_launcher, 0);
                        }
                    }
                }
            }
            return false;
        }
    }

    public void setMaxLength(int maxLength){
        this.maxLength=maxLength;
    }
}
