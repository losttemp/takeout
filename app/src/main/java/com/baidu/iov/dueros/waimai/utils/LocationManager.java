package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationManager {
    public static final int SPAN = 1000000;
    public static final int TypeGpsLocation = 61;
    public static final int TypeCriteriaException = 62;
    public static final int TypeNetWorkException = 63;
    public static final int TypeNetGps = 65;
    public static final int TypeOffLineLocation = 66;
    public static final int TypeOffLineLocationFail = 67;
    public static final int TypeOffLineLocationNetworkFail = 68;
    public static final int TypeNetWorkLocation = 161;
    public static final int TypeServerError = 167;
    public static final int TypeServerDecryptError = 162;
    public static final int TypeCheckKeyError = 502;
    public static final int TypeServerCheckKeyError = 505;
    public static final int TypeServerCheckKeyUnUsed = 601;
    public static final int TypeServerCheckSHA = 602;

    private Context context;
    private LocationClient mLocationClient;
    private static final String TAG = LocationManager.class.getSimpleName();

    private static LocationManager mInstance = null;

    private static BDLocation mBDLocation;

    private LocationManager(Context context) {
        this.context = context;
    }

    public static LocationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationManager(context);
        }
        return mInstance;
    }

    public void startLocation() {
        mLocationClient.requestLocation();
        if ((mLocationClient != null) && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    public void restartLocation() {
        if ((mLocationClient != null) && !mLocationClient.isStarted()) {
            mLocationClient.restart();
        }
    }

    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(myListener);
            locationCallBack = null;
            mLocationClient = null;
        }
    }

    public void initLocationClient(LocationClientOption.LocationMode locationMode, String coorType,
                                   int ScanSpan, boolean GPSFlag) {
        mLocationClient = new LocationClient(context);
        LocationClientOption option = initLocationClientOption(locationMode,
                coorType, ScanSpan, GPSFlag);
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(option);
    }

    public void requestLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }

    private LocationClientOption initLocationClientOption(
            LocationClientOption.LocationMode locationMode, String coorType, int ScanSpan,
            boolean GPSFlag) {
        LocationClientOption option = new LocationClientOption();
        //option.setPriority(LocationClientOption.GpsOnly);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //"bd09ll"
        if (TextUtils.isEmpty(coorType)) {
            option.setCoorType("gcj02");
        } else {
            option.setCoorType(coorType);
        }
        option.setScanSpan(ScanSpan);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        return option;
    }


    BDAbstractLocationListener myListener = new BDAbstractLocationListener() {
        @Override
        public void onLocDiagnosticMessage(int i, int i1, String s) {
            super.onLocDiagnosticMessage(i, i1, s);
            Lg.getInstance().d(TAG, "onLocDiagnosticMessage");
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            Lg.getInstance().d(TAG, "onReceiveLocation");
            if (locationCallBack != null && location != null) {
                mBDLocation = location;
                switch (location.getLocType()) {
                    case TypeGpsLocation:
                    case TypeOffLineLocation:
                    case TypeNetWorkLocation:
                        Constant.LONGITUDE = (int) (location.getLongitude() * SPAN);
                        Constant.LATITUDE = (int) (location.getLatitude() * SPAN);
                        locationCallBack.locationCallBack(true, mBDLocation);
                        break;
                    case TypeCriteriaException:
                    case TypeNetWorkException:
                    case TypeNetGps:
                    case TypeOffLineLocationFail:
                    case TypeOffLineLocationNetworkFail:
                    case TypeServerDecryptError:
                    case TypeServerError:
                    case TypeCheckKeyError:
                    case TypeServerCheckKeyError:
                    case TypeServerCheckKeyUnUsed:
                    case TypeServerCheckSHA:
                        locationCallBack.locationCallBack(false, mBDLocation);
                    default:
                        break;
                }
            }
            Lg.getInstance().d(TAG, "onReceiveLocation end");
            mLocationClient.stop();
        }
    };

    private LocationCallBack locationCallBack;

    public interface LocationCallBack {
        void locationCallBack(boolean isSuccess, BDLocation bdLocation);
    }

    public void setLocationCallBack(LocationCallBack locationCallBack) {
        this.locationCallBack = locationCallBack;
    }

    public BDLocation getLastKnownLocation() {
        return mLocationClient.getLastKnownLocation();
    }
}