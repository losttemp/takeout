package com.baidu.iov.dueros.waimai.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.DeviceUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.List;

public class DropDownMenu extends LinearLayout {
    private static final String TAG = DropDownMenu.class.getSimpleName();

    private LinearLayout tabMenuView;
    private FrameLayout containerView;
    private FrameLayout popupMenuViews;
    private View maskView;
    private int currentTabPosition = -1;

    private int textSelectedColor = 0xff890c85;
    private int textUnselectedColor = 0xff111111;
    private int maskColor /*= 0x88888888*/;
    private int menuTextSize = 14;

    private int menuSelectedIcon;
    private int menuUnselectedIcon;

    private float menuHeighPercent = 0.5f;


    public DropDownMenu(Context context) {
        this(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor, getResources().getColor(R.color.def_mask_color));
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddmenuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuUnselectedIcon, menuUnselectedIcon);
        menuHeighPercent = a.getFloat(R.styleable.DropDownMenu_ddmenuMenuHeightPercent, menuHeighPercent);
        a.recycle();

        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //add underline for tabMenuView
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(1.0f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //init containerView and add it to DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);
    }

    /**
     * init DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(List<String> tabTexts, List<View> popupViews, View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            Lg.getInstance().d(TAG, "tabTexts:" + tabTexts.size() + " popWiews:" + popupViews.size());
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);
        if (containerView.getChildAt(2) != null) {
            containerView.removeViewAt(2);
        }

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (DeviceUtils.getScreenSize(getContext()).y * menuHeighPercent)));
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }
    }

    private void addTab(List<String> tabTexts, int i) {
        final LinearLayout tab = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.dropdown_tab, null);

        TextView tabTitle = (TextView) tab.findViewById(R.id.tab_title);
        tabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tabTitle.setTextColor(textUnselectedColor);
        tabTitle.setText(tabTexts.get(i));

        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tab);
            }
        });
        tabMenuView.addView(tab);
    }

    public void setTabText(String text) {
        if (currentTabPosition != -1) {
            ((TextView) tabMenuView.getChildAt(currentTabPosition).findViewById(R.id.tab_title)).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    public void closeMenu() {
        if (currentTabPosition != -1) {
            ((TextView) tabMenuView.getChildAt(currentTabPosition).findViewById(R.id.tab_title)).setTextColor(textUnselectedColor);
            ((ImageView) tabMenuView.getChildAt(currentTabPosition).findViewById(R.id.tab_arrow)).setImageResource(menuUnselectedIcon);

            popupMenuViews.setVisibility(View.GONE);
            //popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            //maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            currentTabPosition = -1;
        }

    }

    public boolean isShowing() {
        return currentTabPosition != -1;
    }

    private void switchMenu(View target) {
        Lg.getInstance().d(TAG, "switchMenu currentTabPosition：" + currentTabPosition);
        for (int i = 0; i < tabMenuView.getChildCount(); i++) {
            if (target == tabMenuView.getChildAt(i)) {
                if (currentTabPosition == i) {
                    closeMenu();
                } else {
                    if (currentTabPosition == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        //popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        //maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                    currentTabPosition = i;

                    ((TextView) tabMenuView.getChildAt(i).findViewById(R.id.tab_title)).setTextColor(textSelectedColor);
                    ((ImageView) tabMenuView.getChildAt(i).findViewById(R.id.tab_arrow)).setImageResource(menuSelectedIcon);
                }
            } else {
                ((TextView) tabMenuView.getChildAt(i).findViewById(R.id.tab_title)).setTextColor(textUnselectedColor);
                ((ImageView) tabMenuView.getChildAt(i).findViewById(R.id.tab_arrow)).setImageResource(menuUnselectedIcon);
                popupMenuViews.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}