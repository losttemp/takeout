package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.IPoifoodListModel;
import com.baidu.iov.dueros.waimai.model.PoifoodListModel;
import com.baidu.iov.dueros.waimai.net.entity.response.PoidetailinfoBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;

/**
 * Created by ubuntu on 18-10-18.
 */

public class PoifoodListPresenter extends Presenter<PoifoodListPresenter.PoifoodListUi> {
    private static final String TAG = PoifoodListPresenter.class.getSimpleName();
    private IPoifoodListModel mPoifoodListModel;

    @Override
    public void onCommandCallback(String cmd, String extra) {
    }

    @Override
    public void registerCmd(Context context) {

    }

    @Override
    public void unregisterCmd(Context context) {

    }

    public PoifoodListPresenter() {
        mPoifoodListModel = new PoifoodListModel();
    }

    @Override
    public void onUiReady(PoifoodListUi ui) {
        super.onUiReady(ui);
        mPoifoodListModel.onReady();
    }

    @Override
    public void onUiUnready(PoifoodListUi ui) {
        super.onUiUnready(ui);
        mPoifoodListModel.onDestroy();
    }

    public void requestData(ArrayMap<String, String> map) {
        mPoifoodListModel.requestPoifoodList(map, new RequestCallback<PoifoodListBean>() {

            @Override
            public void onSuccess(PoifoodListBean data) {
                getUi().onPoifoodListSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                getUi().onPoifoodListError(msg);
            }
        });
        mPoifoodListModel.requestPoidetailinfo(map, new RequestCallback<PoidetailinfoBean>() {

            @Override
            public void onSuccess(PoidetailinfoBean data) {
                getUi().onPoidetailinfoSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                getUi().onPoidetailinfoError(msg);
            }
        });
    }

    public interface PoifoodListUi extends Ui {
        void onPoifoodListSuccess(PoifoodListBean data);

        void onPoidetailinfoSuccess(PoidetailinfoBean data);

        void onPoifoodListError(String error);

        void onPoidetailinfoError(String error);
    }
}
