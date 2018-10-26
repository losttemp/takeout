package com.baidu.iov.dueros.waimai.presenter;

import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.IPoifoodListModel;
import com.baidu.iov.dueros.waimai.model.PoifoodListModel;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;

/**
 * Created by ubuntu on 18-10-18.
 */

public class PoifoodListPresenter extends Presenter<PoifoodListPresenter.PoifoodListUi> {
    private static final String TAG = PoifoodListPresenter.class.getSimpleName();
    private IPoifoodListModel mPoifoodListModel;

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
                getUi().onSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                getUi().onError(msg);
            }
        });
    }

    public interface PoifoodListUi extends Ui {
        void onSuccess(PoifoodListBean data);

        void onError(String error);
    }
}
