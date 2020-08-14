package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.presenter.Presenter;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CommonUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GuidingAppear;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.location.BDLocation;

public abstract class BaseActivity<T extends Presenter<U>, U extends Ui> extends AppCompatActivity implements LocationManager.LocationCallBack {

    private T mPresenter;
    private AlertDialog dialog;

    abstract T createPresenter();

    abstract U getUi();

    protected BaseActivity() {
        mPresenter = createPresenter();
    }

    protected BDLocation mBDLocation;

    public T getPresenter() {
        return mPresenter;
    }

    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 400;
    protected Context mContext;

    private String targetPath = "com.baidu.bodyguard.ui.activity.MainActivity";
    private String targetPackage = "com.baidu.bodyguard";
    private String locationFlag = "com.baidu.bodyguard-Location";
    private String intentKey = "privacy_action";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainer.getInstance().addActivity(this);
        setStatusBar(false, ContextCompat.getColor(this, R.color.notification_bar_bg));
        mPresenter.onUiReady(getUi());
        mContext = this;

        IntentFilter filter = new IntentFilter();
        registerReceiver(exitReceiver, filter);
        initLocationCity();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GuidingAppear.INSTANCE.exit(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog = null;
        }
        AtyContainer.getInstance().removeActivity(this);
        LocationManager.getInstance(this).stopLocation();
        mPresenter.onUiDestroy(getUi());
        unregisterReceiver(exitReceiver);
    }

    protected void initLocationCity() {
        LocationManager instance = LocationManager.getInstance(this);
        instance.initLocationClient(null, null, 1000, true);
        LocationManager.getInstance(this).setLocationCallBack(this);
        instance.startLocation();
        BDLocation lastKnownLocation = instance.getLastKnownLocation();
        if (lastKnownLocation != null) {
            mBDLocation = lastKnownLocation;
            getGPSAddressSuccess();
            Lg.getInstance().e("AddrStr", lastKnownLocation.getAddrStr());
            Constant.LATITUDE = (int) (mBDLocation.getLatitude() * LocationManager.SPAN);
            Constant.LONGITUDE = (int) (mBDLocation.getLongitude() * LocationManager.SPAN);
        }
    }

    public void setStatusBar(boolean translucent, @ColorInt int color) {
        CommonUtils.setTranslucentStatusBar(this, translucent);
        if (color != 0) {
            CommonUtils.setStatusBarColor(this, color);
        }
    }

    @Override
    public void locationCallBack(boolean isSuccess, BDLocation bdLocation) {
        if (isSuccess) {
            mBDLocation = bdLocation;
            Constant.LATITUDE = (int) (mBDLocation.getLatitude() * LocationManager.SPAN);
            Constant.LONGITUDE = (int) (mBDLocation.getLongitude() * LocationManager.SPAN);
            getGPSAddressSuccess();
        } else {
            getGPSAddressFail();
            LocationManager.getInstance(this).requestLocation();
        }
    }





    protected void doSearchAddress(final boolean isEditModle) {
        Intent intent = new Intent(mContext, AddressSuggestionActivity.class);
        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, isEditModle);
        startActivityForResult(intent, Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE);
    }

    public void getGPSAddressSuccess() {
    }

    public void getGPSAddressFail() {
    }

    public BroadcastReceiver exitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.finish();
        }
    };

}
