package com.baidu.iov.dueros.waimai.model;
import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

/**
 *
 *
 * @author ping
 * @date 2018/10/22
 */
public class FoodModel implements IFoodModel {

    private static final String TAG = FoodModel.class.getSimpleName();
    @Override
    public void requestFilterConditions(FilterConditionReq filterConditionReq, final RequestCallback callback) {
        
        ApiUtils.getFilterConditions(filterConditionReq, new ApiCallBack<FilterConditionResponse>() {
            @Override
            public void onSuccess(FilterConditionResponse data) {
                if (callback!=null) {
                    callback.onSuccess(data);
                }
            }

            @Override
            public void onFailed(String msg) {
                if (callback!=null) {
                    callback.onFailure(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Log.d(TAG, "getLogid: "+id);
                }
            }
        });
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }
}
