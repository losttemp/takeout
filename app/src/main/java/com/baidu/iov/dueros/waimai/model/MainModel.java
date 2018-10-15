package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.CinemaListReq;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class MainModel implements IMainModel {

    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestCinemaList(ArrayMap<String, String> params, final RequestCallback callback) {
        if (callback == null) {
            return;
        }
        CinemaListReq cinemaListReq = new CinemaListReq();
        cinemaListReq.setPageNum(0);
        cinemaListReq.setPageSize(20);
        ApiUtils.getCinemaList(cinemaListReq, new ApiCallBack<CinemaBean>() {
            @Override
            public void onSuccess(CinemaBean data) {
                callback.onSuccess(data);
                Lg.getInstance().d("test", data.toString());
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void requestCityList(ArrayMap<String, String> params, final RequestCallback callback) {

    }
}
