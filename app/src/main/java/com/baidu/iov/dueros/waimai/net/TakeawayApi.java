package com.baidu.iov.dueros.waimai.net;

import com.baidu.iov.dueros.waimai.net.entity.base.ResponseBase;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressAddBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressDeleteBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaInfoResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.City;
import com.baidu.iov.dueros.waimai.net.entity.response.CityListResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.GuidingBean;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
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

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net
 * @time 2018/10/10 9:02
 * @change
 * @class describe
 */

public interface TakeawayApi {

    @FormUrlEncoded
    @POST("/iovservice/api/leadwords")
    Call<ResponseBase<GuidingBean>> getGuiding(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/movie/citylist")
    Call<ResponseBase<CityListResponse>> getCityList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/movie/citylocation")
    Call<ResponseBase<City>> getCityByLocation(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/movie/cinemalist")
    Call<ResponseBase<CinemaBean>> getCinemaList(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("/iovservice/movie/cinemainfo")
    Call<ResponseBase<CinemaInfoResponse>> getCinemaInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/poifood")
    Call<ResponseBase<PoifoodListBean>> getPoifood(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/poidetailinfo")
    Call<ResponseBase<PoidetailinfoBean>> getPoidetailinfo(@FieldMap Map<String, String> map);

    /**
     * get store list request
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/iovservice/waimai/poilist")
    Call<ResponseBase<StoreResponse>> getStoreList(@FieldMap Map<String, String> map);
    

    @FormUrlEncoded
    @POST("/iovservice/waimai/getfilterconditions")
    Call<ResponseBase<FilterConditionResponse>> getFilterConditions(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/orderlist")
    Call<ResponseBase<OrderListResponse>> getOrderList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/orderquery")
    Call<ResponseBase<OrderDetailsResponse>> getOrderDetails(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/ordercancel")
    Call<ResponseBase<OrderCancelResponse>> getOrderCancel(@FieldMap Map<String, String> map);

    /**
     * get filter condition list request
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/iovservice/waimai/getfilterconditions")
    Call<ResponseBase<FilterConditionResponse>> getFilterList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/poiarrivetime")
    Call<ResponseBase<ArriveTimeBean>> getArriveTimeList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/addresslist")
    Call<ResponseBase<AddressListBean>> getAddressList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/ordersubmit")
    Call<ResponseBase<OrderSubmitBean>> getOrderSubmit(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/addressupdate")
    Call<ResponseBase<AddressEditBean>> updateAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/addressadd")
    Call<ResponseBase<AddressAddBean>> addAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/addressdelete")
    Call<ResponseBase<AddressDeleteBean>> deleteAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/authorize")
    Call<ResponseBase<MeituanAuthorizeResponse>> getMeituanAuth(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/suggest")
    Call<ResponseBase<SearchSuggestResponse>> getSearchSuggest(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/orderpreview")
    Call<ResponseBase<OrderPreviewBean>> getOrderPreview(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/iovservice/waimai/logout")
    Call<ResponseBase<LogoutBean>> logout(@FieldMap Map<String, String> map);

}
