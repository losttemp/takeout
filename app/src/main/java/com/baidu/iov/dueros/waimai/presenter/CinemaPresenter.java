package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;

import com.baidu.iov.dueros.waimai.model.ICinemaModel;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.model.CinemaModel;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.ArrayList;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_NO;

public class CinemaPresenter extends Presenter<CinemaPresenter.CinemaUi> {
    private static final String TAG = CinemaPresenter.class.getSimpleName();

    private ICinemaModel mCinemaModel;

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

    public CinemaPresenter() {
        mCinemaModel = new CinemaModel();
    }

    @Override
    public void onUiReady(CinemaPresenter.CinemaUi ui) {
        super.onUiReady(ui);
        mCinemaModel.onReady();
    }

    @Override
    public void onUiUnready(CinemaPresenter.CinemaUi ui) {
        super.onUiUnready(ui);
        mCinemaModel.onDestroy();
    }

    public void requestData(ArrayMap<String, String> map) {
        mCinemaModel.requestCinemaList(map, new RequestCallback<CinemaBean>() {

            @Override
            public void onSuccess(CinemaBean data) {
                getUi().onSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                getUi().failure(msg);
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "getLogid: "+id);
            }
        });
    }

    public interface CinemaUi extends Ui {
        void onSuccess(CinemaBean data);
        void failure(String msg);
        void close();
    }

}
