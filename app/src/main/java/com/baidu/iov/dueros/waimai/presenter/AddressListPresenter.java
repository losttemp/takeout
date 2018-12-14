package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressListImpl;
import com.baidu.iov.dueros.waimai.model.IAddressList;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;

import java.util.ArrayList;

public class AddressListPresenter extends Presenter<AddressListPresenter.AddressListUi> {
    private static final String TAG = AddressListPresenter.class.getSimpleName();

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

    public void requestData(long wm_poi_id) {
        mAddressList.requestAddressList(wm_poi_id, new RequestCallback<AddressListBean>() {

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

            @Override
            public void getLogid(String id) {
                Log.d(TAG, "getLogid: "+id);
            }
        });

    }

    @Override
    public void onCommandCallback(String cmd, String extra) {
        if (getUi() == null) {
            return;
        }

        switch (cmd) {
            case VoiceManager.CMD_SELECT:
                getUi().selectListItem(Integer.parseInt(extra));
                break;
            default:
                break;
        }
    }

    @Override
    public void registerCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mVoiceManager) {
            ArrayList<String> cmdList = new ArrayList<String>();
            cmdList.add(VoiceManager.CMD_SELECT);
            mVoiceManager.registerCmd(context, cmdList, mVoiceCallback);
        }
    }

    @Override
    public void unregisterCmd(Context context) {
        Lg.getInstance().d(TAG, "unregisterCmd");
        if (null != mVoiceManager) {
            mVoiceManager.unregisterCmd(context, mVoiceCallback);
        }
    }

    public interface AddressListUi extends Ui {

        void onGetAddressListSuccess(AddressListBean data);

        void onGetAddressListFailure(String msg);

        void selectListItem(int i);
    }

}
