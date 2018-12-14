package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.model.IMainModel;
import com.baidu.iov.dueros.waimai.model.MainModel;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class MainPresenter extends Presenter<MainPresenter.MainUi> {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private IMainModel mModel;

    @Override
    public void onCommandCallback(String cmd, String extra) {

    }

    @Override
    public void registerCmd(Context context) {

    }

    @Override
    public void unregisterCmd(Context context) {

    }

    public MainPresenter() {
        mModel = new MainModel();
    }

    @Override
    public void onUiReady(MainUi ui) {
        super.onUiReady(ui);
        mModel.onReady();
    }

    @Override
    public void onUiUnready(MainUi ui) {
        super.onUiUnready(ui);
        mModel.onDestroy();
    }

    public void requestData() {
        ArrayMap<String, String> map = new ArrayMap<String, String>();
        map.put("head", "head");
        map.put("page", "8");

        mModel.requestCinemaList(map, new RequestCallback<CinemaBean>() {

            @Override
            public void onSuccess(CinemaBean data) {
                getUi().update(data);
                Lg.getInstance().d(TAG,"onSuccess");
            }

            @Override
            public void onFailure(String error) {
                getUi().failure(error);
            }

            @Override
            public void getLogid(String id) {
                Log.d(TAG, "getLogid: "+id);
            }
        });
    }

    public interface MainUi extends Ui {
        void update(CinemaBean data);
        void failure(String error);
    }

}
