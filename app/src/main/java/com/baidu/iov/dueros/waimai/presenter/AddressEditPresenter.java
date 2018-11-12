package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressEditModel;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressDeleteReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
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


    public void requestUpdateAddressData(ArrayMap<String, String> map) {
        addressEditModel.updateAddressData(new AddressEditReq(), new RequestCallback<AddressEditBean>() {

            @Override
            public void onSuccess(AddressEditBean data) {
                if (null != getUi()){
                    getUi().updateAddressSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != getUi()){
                    getUi().updateAddressFail(msg);
                }
            }
        });
    }

    public void requestAddAddressData(ArrayMap<String, String> map) {
        addressEditModel.updateAddressData(new AddressEditReq(), new RequestCallback<AddressEditBean>() {

            @Override
            public void onSuccess(AddressEditBean data) {
                if (null != getUi()){
                    getUi().addAddressSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != getUi()){
                    getUi().addAddressFail(msg);
                }
            }
        });
    }

    public void requestDeleteAddressData(AddressDeleteReq addressDeleteReq) {
        addressEditModel.deleteAddressData(addressDeleteReq, new RequestCallback<AddressEditBean>() {

            @Override
            public void onSuccess(AddressEditBean data) {
                if (null != getUi()){
                    getUi().deleteAddressSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != getUi()){
                    getUi().deleteAddressFail(msg);
                }
            }
        });
    }

    public interface AddressEditUi extends Ui {
        void updateAddressSuccess(AddressEditBean data);
        void updateAddressFail(String error);
        void addAddressSuccess(AddressEditBean data);
        void addAddressFail(String error);
        void deleteAddressSuccess(AddressEditBean data);
        void deleteAddressFail(String error);
    }
}
