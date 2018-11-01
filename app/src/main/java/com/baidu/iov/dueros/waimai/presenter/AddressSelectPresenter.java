package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressSelectModel;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;

import java.util.List;

public class AddressSelectPresenter extends Presenter<AddressSelectPresenter.AddressSelectUi> {
    private static final String TAG = AddressSelectPresenter.class.getSimpleName();

    private AddressSelectModel mAdressSelectModel;

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

    public void requestData(AddressListReqBean reqBean) {

        mAdressSelectModel.requestAdressList(reqBean, new RequestCallback<AddressListBean>() {

            @Override
            public void onSuccess(AddressListBean data) {
                List<AddressListBean.MeituanBean.DataBean> dataBeans = data.getMeituan().getData();
                getUi().onSuccess(dataBeans);
            }

            @Override
            public void onFailure(String msg) {
                getUi().onError(msg);
            }
        });
    }

    public interface AddressSelectUi extends Ui {
        void onSuccess(List<AddressListBean.MeituanBean.DataBean> data);

        void onError(String error);
    }
}
