package com.baidu.iov.dueros.waimai.presenter;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.model.ICinemaModel;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.model.CinemaModel;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;

public class CinemaPresenter extends Presenter<CinemaPresenter.CinemaUi> {
    private static final String TAG = CinemaPresenter.class.getSimpleName();

    private ICinemaModel mCinemaModel;

    public CinemaPresenter() {
        mCinemaModel = new CinemaModel();
    }

    @Override
    public void onUiReady(CinemaPresenter.CinemaUi ui) {
        super.onUiReady(ui);
        mCinemaModel.onReady();
    }

    @Override
    public void onUiUnready(CinemaPresenter.CinemaUi ui) {
        super.onUiUnready(ui);
        mCinemaModel.onDestroy();
    }

    public void requestData(ArrayMap<String, String> map) {
        mCinemaModel.requestCinemaList(map, new RequestCallback<CinemaBean>() {

            @Override
            public void onSuccess(CinemaBean data) {
                getUi().onSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                getUi().onError(msg);
            }
        });
    }

    public interface CinemaUi extends Ui {
        void onSuccess(CinemaBean data);
        void onError(String error);
    }

}
