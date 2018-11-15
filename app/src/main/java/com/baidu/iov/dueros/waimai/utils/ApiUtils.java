package com.baidu.iov.dueros.waimai.utils;

import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.ApiInstance;
import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressAddBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaInfoResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.City;
import com.baidu.iov.dueros.waimai.net.entity.response.CityListResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.MeituanAuthorizeResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderCancelResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoidetailinfoBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.SearchSuggestResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;

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
     *
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
     *
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
     *
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
    
    /**
     * prepare for each request
     *
     * @param request
     * @param <D>
     * @return
     */
    private static <D extends RequestBase> Map<String, String> requestPrepare(D request) {
        Lg.getInstance().d(TAG, "LONGITUDE===" + Constant.LONGITUDE + "LATITUDE===" + Constant.LATITUDE);
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
    public static <D extends RequestBase> void getFilterConditions(D request, ApiCallBack<FilterConditionResponse> callBack) {
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
     * get order details request
     *
     * @param <D>
     * @param request
     * @param callBack
     */
    public static <D extends RequestBase> void getOrderDetails(D request, ApiCallBack<OrderDetailsResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getOrderDetails(requestMap).enqueue(callBack);
    }

    /**
     * get order cancel request
     *
     * @param <D>
     * @param request
     * @param callBack
     */
    public static <D extends RequestBase> void getOrderCancel(D request, ApiCallBack<OrderCancelResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getOrderCancel(requestMap).enqueue(callBack);
    }

    /**
     * get filter condition list request
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getFilterList(D request, ApiCallBack<FilterConditionResponse> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getFilterList(requestMap).enqueue(callBack);
    }


    /**
     * updateAddress
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void updateAddress(D request, ApiCallBack<AddressEditBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().updateAddress(requestMap).enqueue(callBack);
    }

    /**
     * addAddress
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void addAddress(D request, ApiCallBack<AddressAddBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().addAddress(requestMap).enqueue(callBack);
    }

    /**
     * deleteAddress
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void deleteAddress(D request, ApiCallBack<AddressEditBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().deleteAddress(requestMap).enqueue(callBack);
    }

    /**
     * get meituan Authorize
     *
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

    /**
     * get order preview request
     *
     * @param request
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getOrderPreview(D request, ApiCallBack<OrderPreviewBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getOrderPreview(requestMap).enqueue(callBack);
    }
}
