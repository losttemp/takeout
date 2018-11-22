package com.baidu.iov.dueros.waimai.net;

import com.baidu.iov.dueros.waimai.net.entity.base.ResponseBase;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net
 * @time 2018/10/10 13:51
 * @change
 * @class describe
 */

public abstract class ApiCallBack<T> implements Callback<ResponseBase<T>> {
    private static final String TAG = ApiCallBack.class.getSimpleName();
    public static final int SUCCESS_CODE = 0;
    public static final int AuthFail_CODE = 10010;

    @Override
    public void onResponse(Call<ResponseBase<T>> call, Response<ResponseBase<T>> response) {
        
        try {
            ResponseBase<T> responseBase = response.body();
            if (responseBase.getErrno() == SUCCESS_CODE) {
                if (responseBase.getData() instanceof StoreResponse){
                    obToArry((StoreResponse)responseBase.getData());
                }
                onSuccess(responseBase.getData());
                Lg.getInstance().i(TAG, responseBase.getData().toString());
            } else if (responseBase.getErrno() == AuthFail_CODE){
                onSuccess(responseBase.getData());
                Lg.getInstance().i(TAG, responseBase.getData().toString());
            } else {
                onFailed(responseBase.getErr_msg());
                Lg.getInstance().e(TAG, responseBase.getErr_msg());
            }
        } catch (Exception e) {
            onFailed(e.getMessage());
            Lg.getInstance().e(TAG, e.getMessage());
        }
    }
    
    private void  obToArry(StoreResponse mStoreResponse){
        if (mStoreResponse.getMeituan().getData().getOpenPoiBaseInfoList()==null){
            List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean> mOpenPoiBaseInfoList=new ArrayList<>(0);
            mStoreResponse.getMeituan().getData().setOpenPoiBaseInfoList(mOpenPoiBaseInfoList);
        }
    }

    @Override
    public void onFailure(Call<ResponseBase<T>> call, Throwable throwable) {
        onFailed(throwable.getMessage());
        Lg.getInstance().e(TAG, throwable.getMessage());
    }

    public abstract void onSuccess(T data);

    public abstract void onFailed(String msg);
}
