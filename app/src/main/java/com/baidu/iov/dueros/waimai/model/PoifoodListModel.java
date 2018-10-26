package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.PoifoodListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;

/**
 * Created by ubuntu on 18-10-18.
 */

public class PoifoodListModel implements IPoifoodListModel {
    private static final String TAG = PoifoodListModel.class.getSimpleName();

    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestPoifoodList(ArrayMap<String, String> params, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        if (params != null) {
            Lg.getInstance().d(TAG, "areaId:" + params.get(Constant.AREA_ID) + " aoiId:" + params.get(Constant.AOI_ID)
                    + " brandId:" + params.get(Constant.BRAND_ID));
        }
        PoifoodListReq poifoodListReq = new PoifoodListReq();
        poifoodListReq.setLongitude(95369826);
        poifoodListReq.setLatitude(29735952);
        poifoodListReq.setWm_poi_id(2868090);
        ApiUtils.getPoifoodList(poifoodListReq, new ApiCallBack<PoifoodListBean>() {
            @Override
            public void onSuccess(PoifoodListBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
