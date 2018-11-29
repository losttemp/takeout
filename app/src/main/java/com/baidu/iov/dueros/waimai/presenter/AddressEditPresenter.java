package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressEditModel;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressAddReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressDeleteReq;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressAddBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressDeleteBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.utils.Lg;

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
        addressEditModel = new AddressEditModel();
    }

    @Override
    public void onUiReady(AddressEditUi ui) {
        super.onUiReady(ui);
        addressEditModel.onReady();
    }

    @Override
    public void onUiUnready(AddressEditUi ui) {
        super.onUiUnready(ui);
        addressEditModel.onDestroy();
    }


    public void requestUpdateAddressData(AddressEditReq addressEditreq) {
        addressEditModel.updateAddressData(addressEditreq, new RequestCallback<AddressEditBean>() {

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

    public void requestAddAddressData(AddressAddReq addrAddreq) {
        addressEditModel.addAddressData(addrAddreq, new RequestCallback<AddressAddBean>() {

            @Override
            public void onSuccess(AddressAddBean data) {
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
        addressEditModel.deleteAddressData(addressDeleteReq, new RequestCallback<AddressDeleteBean>() {

            @Override
            public void onSuccess(AddressDeleteBean data) {
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
        void updateAddressFail(String msg);
        void addAddressSuccess(AddressAddBean data);
        void addAddressFail(String msg);
        void deleteAddressSuccess(AddressDeleteBean data);
        void deleteAddressFail(String msg);
    }
}
