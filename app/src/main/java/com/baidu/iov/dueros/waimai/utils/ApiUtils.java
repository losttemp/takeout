package com.baidu.iov.dueros.waimai.utils;

import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.ApiInstance;
import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaInfoResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.City;
import com.baidu.iov.dueros.waimai.net.entity.response.CityListResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.utils
 * @time 2018/10/10 10:43
 * @change
 * @class describe
 */

public class ApiUtils {
    private static final String TAG = "ApiUtils";

    /**
     * get city list request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase>  void getCityList( D request, ApiCallBack<CityListResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getCityList(requestMap).enqueue(callBack);
    }

    /**
     * get city by location request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getCityByLocation(D request, ApiCallBack<City> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getCityByLocation(requestMap).enqueue(callBack);
    }

    /**
     * get cinema list request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getCinemaList(D request, ApiCallBack<CinemaBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getCinemaList(requestMap).enqueue(callBack);
    }

    /**
     * get cinema list request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getCinemaInfo(D request, ApiCallBack<CinemaInfoResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getCinemaInfo(requestMap).enqueue(callBack);
    }

    /**
     * get cinema list request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getBusinessByLocation(D request, ApiCallBack<Map<String,BusinessBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getBusinessByLocation(requestMap).enqueue(callBack);
    }


    /**
     * prepare for each request
     *
     * @param request
     * @param <D>
     * @return
     */
    private static <D extends RequestBase> Map<String, String> requestPrepare(D request) {
        request.crd = "113.948913_22.530194";
        request.uuid = "qa_test_123123";
        request.sign = CommonUtils.sign(request);
        return CommonUtils.getAllFields(request);
    }

    /**
     * get store list request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getStoreList(D request, ApiCallBack<StoreResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getStoreList(requestMap).enqueue(callBack);
    }

}
