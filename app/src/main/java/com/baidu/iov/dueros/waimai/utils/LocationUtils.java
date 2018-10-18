package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.HashMap;
import java.util.Map;

public class LocationUtils {
    public static final int SPAN = 1000;
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

    public static Map mlcationMap = new HashMap();
    private Context context;
    private LocationClient mLocationClient;
    private static final String TAG = LocationUtils.class.getSimpleName();

    private static LocationUtils mInstance = null;
    public static BDLocation location;

    private LocationUtils(Context context) {
        this.context = context;

    }

    public static LocationUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationUtils(context);
        }
        return mInstance;
    }

    public void startLocation() {
        if ((mLocationClient != null) && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    public void stopLocation() {
        if ((mLocationClient != null) && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    public void getLcation(LocationClientOption.LocationMode locationMode, String coorType,
                           int ScanSpan, boolean GPSFlag) {
        mLocationClient = new LocationClient(context);
        LocationClientOption option = initLocationClientOption(locationMode,
                coorType, ScanSpan, GPSFlag);
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(option);
    }

    public void setLocationListener(BDAbstractLocationListener listener) {
        mLocationClient.registerLocationListener(listener);
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
        option.setPriority(LocationClientOption.GpsOnly);
        if (TextUtils.isEmpty(coorType)) {
            option.setCoorType("bd09ll");
        } else {
            option.setCoorType(coorType);
        }
        option.setScanSpan(ScanSpan);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);//
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
            Lg.getInstance().d(TAG, "onLocDiagnosticMessage: i==" + i + "i1==" + i1 + "s==" + s);
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            LocationUtils.location = location;
            int requestCode = location.getLocType();
            Lg.getInstance().d(TAG, "onReceiveLocation: " + requestCode);
            switch (requestCode) {
                case TypeGpsLocation:
                    mlcationMap.put(Constant.GPS, true);
                    mlcationMap.put(Constant.LATITUDE, location.getLatitude());
                    mlcationMap.put(Constant.LONGITUDE, location.getLongitude());
                    mlcationMap.put(Constant.STREET, location.getStreet());
                    mlcationMap.put(Constant.STREET_NUMBER, location.getStreetNumber());

                    mlcationMap.put(Constant.CITY, location.getCity());
                    mlcationMap.put(Constant.CITYCODE, location.getCityCode());
                    mlcationMap.put(Constant.PROVINCE, location.getProvince());
                    mlcationMap.put(Constant.COUNTRY, location.getCountry());
                    mlcationMap.put(Constant.DETAIL,
                            location.getAddrStr() + location.getLocationDescribe());
                    break;
                case TypeCriteriaException:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeNetWorkException:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeNetGps:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeOffLineLocation:
                    mlcationMap.put(Constant.GPS, true);
                    mlcationMap.put(Constant.CITYCODE, location.getCityCode());
                    mlcationMap.put(Constant.LATITUDE, location.getLatitude());
                    mlcationMap.put(Constant.LONGITUDE, location.getLongitude());
                    mlcationMap.put(Constant.STREET, location.getStreet());
                    mlcationMap.put(Constant.STREET_NUMBER, location.getStreetNumber());
                    mlcationMap.put(Constant.CITY, location.getCity());
                    mlcationMap.put(Constant.PROVINCE, location.getProvince());
                    mlcationMap.put(Constant.COUNTRY, location.getCountry());
                    mlcationMap.put(Constant.DETAIL,
                            location.getAddrStr() + location.getLocationDescribe());
                    break;
                case TypeOffLineLocationFail:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeOffLineLocationNetworkFail:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeNetWorkLocation:
                    mlcationMap.put(Constant.GPS, true);
                    mlcationMap.put(Constant.LATITUDE, location.getLatitude());
                    mlcationMap.put(Constant.LONGITUDE, location.getLongitude());
                    mlcationMap.put(Constant.STREET, location.getStreet());
                    mlcationMap.put(Constant.STREET_NUMBER, location.getStreetNumber());

                    mlcationMap.put(Constant.CITY, location.getCity());
                    mlcationMap.put(Constant.PROVINCE, location.getProvince());
                    mlcationMap.put(Constant.COUNTRY, location.getCountry());
                    mlcationMap.put(Constant.DETAIL,
                            location.getAddrStr() + location.getLocationDescribe());
                    mlcationMap.put(Constant.OPERATORS, location.getOperators());
                    mlcationMap.put(Constant.CITYCODE, location.getCityCode());
                    break;
                case TypeServerDecryptError:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeServerError:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeCheckKeyError:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeServerCheckKeyError:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeServerCheckKeyUnUsed:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                case TypeServerCheckSHA:
                    mlcationMap.put(Constant.GPS, false);
                    break;
                default:
                    break;
            }
        }
    };
}
