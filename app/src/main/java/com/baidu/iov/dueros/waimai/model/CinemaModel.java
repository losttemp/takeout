package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.net.entity.request.CinemaListReq;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;


public class CinemaModel implements ICinemaModel {
    private static final String TAG = CinemaModel.class.getSimpleName();

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

        if(params != null){
            Lg.getInstance().d(TAG,"areaId:" + params.get(Constant.AREA_ID) + " aoiId:" + params.get(Constant.AOI_ID)
                    + " brandId:" + params.get(Constant.BRAND_ID));
            //params = 0,no filters
        }

        CinemaListReq cinemaListReq = new CinemaListReq();
        cinemaListReq.setPageNum(0);
        cinemaListReq.setPageSize(20);
        ApiUtils.getCinemaList(cinemaListReq, new ApiCallBack<CinemaBean>() {
            @Override
            public void onSuccess(CinemaBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
