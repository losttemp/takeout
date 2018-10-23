package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.HomeModel;
import com.baidu.iov.dueros.waimai.model.IHomeModel;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.ArrayList;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_NO;

public class HomePresenter extends Presenter<HomePresenter.HomeUi> {

    private static final String TAG = HomePresenter.class.getSimpleName();

    private IHomeModel mModel;

    @Override
    public void onCommandCallback(String cmd, String extra) {
        if (CMD_NO.equals(cmd) && null != getUi()) {
            getUi().close();
        }
    }

    @Override
    public void registerCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mVoiceManager) {
            ArrayList<String> cmdList = new ArrayList<String>();
            cmdList.add(CMD_NO);
            //mVoiceController.registerCmd(context, cmdList, mVoiceCallback);
            mVoiceManager.registerCmd(context,cmdList, mVoiceCallback);
        }
    }

    @Override
    public void unregisterCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mVoiceManager) {
            mVoiceManager.unregisterCmd(context, mVoiceCallback);
        }
    }

    public HomePresenter() {
        mModel = new HomeModel();
    }

    @Override
    public void onUiReady(HomeUi ui) {
        super.onUiReady(ui);
        mModel.onReady();
    }

    @Override
    public void onUiUnready(HomeUi ui) {
        super.onUiUnready(ui);
        mModel.onDestroy();
    }

    public void requestStoreList(StoreReq storeReq) {

        mModel.requestStoreList(storeReq, new RequestCallback<StoreResponse>() {
            @Override
            public void onSuccess(StoreResponse data) {
                if (getUi() != null) {
                    getUi().update(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().failure(msg);
                }
            }
        });
    }

    public interface HomeUi extends Ui {
        void update(StoreResponse data);

        void failure(String msg);
        void close();
    }

}
