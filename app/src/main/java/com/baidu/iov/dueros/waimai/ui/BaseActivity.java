package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.presenter.Presenter;
import com.baidu.iov.dueros.waimai.utils.CommonUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.location.BDLocation;

public abstract class BaseActivity<T extends Presenter<U>, U extends Ui> extends AppCompatActivity implements LocationManager.LocationCallBack {

    private T mPresenter;
    private int mBDLocationLocType;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(false, ContextCompat.getColor(this, R.color.base_color));
        mPresenter.onUiReady(getUi());
        initLocationCity();
        requestPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().registerCmd(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().unregisterCmd(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationManager.getInstance(this).stopLocation();
        mPresenter.onUiDestroy(getUi());
    }

    protected void initLocationCity() {
        LocationManager instance = LocationManager.getInstance(this);
        instance.initLocationClient(null, null, 0, true);
        LocationManager.getInstance(this).setLocationCallBack(this);
        instance.startLocation();
        BDLocation lastKnownLocation = instance.getLastKnownLocation();
        if (lastKnownLocation != null) {
            mBDLocation = lastKnownLocation;
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
        } else {
            mBDLocationLocType = bdLocation.getLocType();
            LocationManager.getInstance(this).requestLocation();
        }
    }

    private void requestPermission() {
        if (isNoRequest)return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.persion_err)
                            .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                    startActivity(intent);
                                    finish();

                                }
                            })
                            .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .create()
                            .show();

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ACCESS_COARSE_LOCATION);
                }


            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //permission was granted, yay! Do the contacts-related task you need to do.
                initLocationCity();
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void doSearchAddress(boolean isEditModle) {
        Intent intent = new Intent(this, AddressSuggestionActivity.class);
        if (mBDLocation != null) {
            intent.putExtra(Constant.ADDRESS_EDIT_INTENT_EXTRE_CITY, mBDLocation.getCity());
            intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, isEditModle);
            startActivityForResult(intent, Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE);
        } else {
            Toast.makeText(this, getResources().getString(R.string.location_error_toast), Toast.LENGTH_LONG).show();
            LocationManager.getInstance(this).requestLocation();
        }
    }

    private boolean isNoRequest = false;

    public void setNoRequestForPermissions(boolean isNoRequest){
        this.isNoRequest=isNoRequest;
    }
}
