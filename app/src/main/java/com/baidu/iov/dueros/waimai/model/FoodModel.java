package com.baidu.iov.dueros.waimai.model;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;
/**
 *
 *
 * @author ping
 * @date 2018/10/22
 */
public class FoodModel implements IFoodModel {

    private static final String TAG = FoodModel.class.getSimpleName();
    @Override
    public void requestFilterConditions(FilterConditionsReq filterConditionsReq, final RequestCallback callback) {
        
        ApiUtils.getFilterConditions(filterConditionsReq, new ApiCallBack<FilterConditionsResponse>() {
            @Override
            public void onSuccess(FilterConditionsResponse data) {
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
        });
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }
}
