package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressListImpl;
import com.baidu.iov.dueros.waimai.model.IAddressList;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;

public class AddressListPresenter extends Presenter<AddressListPresenter.AddressListUi> {


    private IAddressList mAddressList;

    public AddressListPresenter() {
        mAddressList = new AddressListImpl();
    }

    @Override
    public void onUiUnready(AddressListUi ui) {
        super.onUiUnready(ui);
        mAddressList.onReady();
    }

    @Override
    public void onUiReady(AddressListUi ui) {
        super.onUiReady(ui);
        mAddressList.onDestroy();
    }

    public void requestData() {
        mAddressList.requestAddressList(new RequestCallback<AddressListBean>() {

            @Override
            public void onSuccess(AddressListBean data) {
                if (null != getUi()) {
                    getUi().onGetAddressListSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (null != getUi()) {
                    getUi().onGetAddressListFailure(msg);
                }
            }
        });

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

    public interface AddressListUi extends Ui {

        void onGetAddressListSuccess(AddressListBean data);

        void onGetAddressListFailure(String msg);


    }

}
