package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressEditModel;
import com.baidu.iov.dueros.waimai.model.IAddressEditModel;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;

public class AddressEditPresenter extends Presenter<AddressEditPresenter.AddressEditUi> {
    private static final String TAG = AddressEditPresenter.class.getSimpleName();

    private AddressEditModel addressEditModel;

    @Override
    public void onCommandCallback(String cmd, String extra) {

    }

    @Override
    public void registerCmd(Context context) {

    }

    @Override
    public void unregisterCmd(Context context) {

    }

    public AddressEditPresenter() {
        this.addressEditModel = new AddressEditModel();
    }

    @Override
    public void onUiReady(AddressEditUi ui) {
        super.onUiReady(ui);
    }

    @Override
    public void onUiUnready(AddressEditUi ui) {
        super.onUiUnready(ui);
    }


    public void requestData(ArrayMap<String, String> map) {
        addressEditModel.requestAdressList(map, new RequestCallback<AddressEditBean>() {

            @Override
            public void onSuccess(AddressEditBean data) {
                getUi().onSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                getUi().onError(msg);
            }
        });
    }

    public interface AddressEditUi extends Ui {
        void onSuccess(AddressEditBean data);
        void onError(String error);
    }
}
