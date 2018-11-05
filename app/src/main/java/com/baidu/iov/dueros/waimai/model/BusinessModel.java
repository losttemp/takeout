package com.baidu.iov.dueros.waimai.model;
import android.util.ArrayMap;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.PoilistReq;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.List;
import java.util.Map;
/**
 * 
 *
 * @author ping
 * @date 2018/10/17
 */
public class BusinessModel implements IBusinessModel {
    private static final String TAG = BusinessModel.class.getSimpleName();
    

    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestFilterConditions(FilterConditionsReq filterConditionsReq, final RequestCallback callback) {

        ApiUtils.getFilterConditions(filterConditionsReq, new ApiCallBack<FilterConditionsResponse>() {
            @Override
            public void onSuccess(FilterConditionsResponse data) {
                if (callback!=null) {
                    callback.onSuccess(data);
                }
                Lg.getInstance().e(TAG,"msg:"+data);
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
    public void requestBusinessBean( PoilistReq poilistReq, final RequestCallback callback) {
        poilistReq.setLongitude(Constant.LONGITUDE);
        poilistReq.setLatitude(Constant.LATITUDE);
        Lg.getInstance().e(TAG,"poilistReq:"+poilistReq);
        ApiUtils.getBusinessByLocation(poilistReq, new ApiCallBack<BusinessBean>() {
            @Override
            public void onSuccess(BusinessBean data) {
                if (callback!=null) {
                    callback.onSuccess(data);
                }
                Lg.getInstance().e(TAG,"msg:"+data);
            }

            @Override
            public void onFailed(String msg)
            {
                if (callback!=null) {
                    callback.onFailure(msg);
                }
                Lg.getInstance().e(TAG,"msg:"+msg);
            }
        });
    }
}
