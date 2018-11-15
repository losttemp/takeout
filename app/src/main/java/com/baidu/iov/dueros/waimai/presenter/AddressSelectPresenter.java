package com.baidu.iov.dueros.waimai.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressSelectModel;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectPresenter extends Presenter<AddressSelectPresenter.AddressSelectUi> {
    private static final String TAG = AddressSelectPresenter.class.getSimpleName();

    private AddressSelectModel mAdressSelectModel;
    private List<AddressListBean.IovBean.DataBean> mDataBeans = new ArrayList<>();

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
        getDesBeans();

        mAdressSelectModel.requestAdressList(reqBean, new RequestCallback<AddressListBean>() {

            @Override
            public void onSuccess(AddressListBean data) {
                mDataBeans = data.getIov().getData();
                if (null != getUi()) {
                    getUi().onSuccess(mDataBeans);
                }
                for (int i = 0; i < mDataBeans.size(); i++) {
                    try {
                        AddressListBean.IovBean.DataBean dataBean = mDataBeans.get(i);
                        String user_phone = Encryption.desEncrypt(dataBean.getUser_phone());
                        String user_name = Encryption.desEncrypt(dataBean.getUser_name());
                        if (!MyApplicationAddressBean.USER_PHONES.contains(user_phone)) {
                            MyApplicationAddressBean.USER_PHONES.add(0, user_phone);
                        }
                        if (!MyApplicationAddressBean.USER_NAMES.contains(user_name)) {
                            MyApplicationAddressBean.USER_NAMES.add(0, user_name);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != getUi()) {
                    getUi().onFailure(msg);
                }
            }
        });
    }

    private void getDesBeans() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.baidu.naviauto.open.api");
        MReceiver mReceiver = new MReceiver();
        if (null != getUi()) {
            getUi().onRegisterReceiver(mReceiver, intentFilter);
        }
    }

    public interface AddressSelectUi extends Ui {
        void onRegisterReceiver(MReceiver mReceiver, IntentFilter intentFilter);

        void onSuccess(List<AddressListBean.IovBean.DataBean> data);

        void onFailure(String msg);
    }

    public class MReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == "com.baidu.naviauto.open.api") {
                String s = intent.getStringExtra("dest_json");
                Lg.getInstance().d("xss", "getAction   " + s);
                parseJSON(s);
                Lg.getInstance().d("xss", "AddOnSuccess");
                AddressListBean.IovBean.DataBean dataBean = new AddressListBean.IovBean.DataBean();
/*                if (navi != null) {
                    Lg.getInstance().d("xss", "AddOnSuccess =  " + navi.getAddress());
                    dataBean.setUser_name("xss");
                    dataBean.setUser_phone("1888888888");
                    dataBean.setAddress(navi.getAddress() + navi.getName());
                    mDataList.add(0, dataBean);
                }*/
            }
        }

    }

    private void parseJSON(String jsonData) {
        Gson gson = new Gson();
/*        navi = gson.fromJson(jsonData,
                new TypeToken<AddressEndBean>() {
                }.getType());*/
    }
}
