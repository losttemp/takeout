package com.baidu.iov.dueros.waimai.utils;

import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.ApiInstance;
import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaInfoResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.City;
import com.baidu.iov.dueros.waimai.net.entity.response.CityListResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.PoidetailinfoBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.SearchSuggestResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.MeituanAuthorizeResponse;

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
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getCityList(D request, ApiCallBack<CityListResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getCityList(requestMap).enqueue(callBack);
    }

    /**
     * get city by location request
     *
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
     *
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
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getCinemaInfo(D request, ApiCallBack<CinemaInfoResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getCinemaInfo(requestMap).enqueue(callBack);
    }


    /**
     * get arrivetime list request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getArriveTimeList(D request, ApiCallBack<ArriveTimeBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getArriveTimeList(requestMap).enqueue(callBack);
    }
	
	    /**
     * get address list request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getAddressList(D request, ApiCallBack<AddressListBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getAddressList(requestMap).enqueue(callBack);
    }
	
	    /**
     * get order submit request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getOrderSubmit(D request, ApiCallBack<OrderSubmitBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getOrderSubmit(requestMap).enqueue(callBack);
    }
	
    /**
     * get poifood list request
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getPoifoodList(D request, ApiCallBack<PoifoodListBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getPoifood(requestMap).enqueue(callBack);
    }

    /**
     * get poidetailinfo request
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getPoidetailinfo(D request, ApiCallBack<PoidetailinfoBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getPoidetailinfo(requestMap).enqueue(callBack);
    }

    public static <D extends RequestBase> void getBusinessByLocation(D request, ApiCallBack<BusinessBean> callBack) {
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
        request.uuid = "1234";
        request.sign = CommonUtils.sign(request);
        return CommonUtils.getAllFields(request);
    }

    /**
     * get store list request
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getStoreList(D request, ApiCallBack<StoreResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getStoreList(requestMap).enqueue(callBack);
    }

    /**
     * get filterConditions list request
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getFilterConditions(D request, ApiCallBack<FilterConditionsResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getFilterConditions(requestMap).enqueue(callBack);
    }

    /**
     * get order list request
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getOrderList(D request, ApiCallBack<OrderListResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getOrderList(requestMap).enqueue(callBack);
    }

    /**
     * get filter condition list request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getFilterList(D request, ApiCallBack<FilterConditionResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getFilterList(requestMap).enqueue(callBack);
    }


    /**
     * get filter condition list request
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void addAddress(D request, ApiCallBack<AddressEditBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().addAddress(requestMap).enqueue(callBack);
    }
	
    /**
     * get meituan Authorize
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getMeituanAuth(D request, ApiCallBack<MeituanAuthorizeResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getMeituanAuth(requestMap).enqueue(callBack);
    }

    /**
     * get search suggest list request
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getSearchSuggest(D request, ApiCallBack<SearchSuggestResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getSearchSuggest(requestMap).enqueue(callBack);
    }
}
