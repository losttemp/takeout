package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressSelectModel;
import com.baidu.iov.dueros.waimai.model.IAddressSelectModel;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;

public class AddressSelectPresenter extends Presenter<AddressSelectPresenter.AddressSelectUi> {
    private static final String TAG = AddressSelectPresenter.class.getSimpleName();

    private IAddressSelectModel mAdressSelectModel;

    public AddressSelectPresenter() {
        this.mAdressSelectModel = new AddressSelectModel();
    }

    @Override
    public void onCommandCallback(String cmd, String extra) {

    }

    @Override
    public void registerCmd(Context context) {

    }

    @Override
    public void unregisterCmd(Context context) {

    }

    @Override
    public void onUiReady(AddressSelectUi ui) {
        super.onUiReady(ui);
    }

    @Override
    public void onUiUnready(AddressSelectUi ui) {
        super.onUiUnready(ui);
    }

    public void requestData(ArrayMap<String, String> map) {
        mAdressSelectModel.requestAdressList(map, new RequestCallback<CinemaBean>() {

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

    public interface AddressSelectUi extends Ui {
        void onSuccess(CinemaBean data);

        void onError(String error);
    }
}
