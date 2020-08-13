package com.baidu.iov.dueros.waimai.presenter;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.FoodModel;
import com.baidu.iov.dueros.waimai.model.IFoodModel;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;
/**
 *
 *
 * @author ping
 * @date 2018/10/22
 */
public class FoodPresenter extends Presenter< FoodPresenter.FoodUi> {

    private static final String TAG = FoodPresenter.class.getSimpleName();

    private IFoodModel mFoodModel;

    public FoodPresenter() {
        this.mFoodModel = new FoodModel();
    }



    @Override
    public void onUiReady(FoodPresenter.FoodUi ui) {
        super.onUiReady(ui);
        mFoodModel.onReady();
    }

    public void requestFilterConditions(FilterConditionReq filterConditionReq) {
        mFoodModel.requestFilterConditions(filterConditionReq,new RequestCallback<FilterConditionResponse>(){
            @Override
            public void onSuccess(FilterConditionResponse data) {
                if ( getUi()!=null) {
                    getUi().onSuccess(data);
                }
                Lg.getInstance().e(TAG,"msg:"+data);
            }

            @Override
            public void onFailure(String msg) {
                if ( getUi()!=null) {
                    getUi().onError(msg);
                }
                Lg.getInstance().e(TAG,"msg:"+msg);
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "getLogid: "+id);
            }
        });
    }

    @Override
    public void onUiUnready(FoodPresenter.FoodUi ui) {
        super.onUiUnready(ui);
        mFoodModel.onDestroy();
    }

    public interface FoodUi extends Ui {
        void onSuccess(FilterConditionResponse data);
        void onError(String error);
    }
}
