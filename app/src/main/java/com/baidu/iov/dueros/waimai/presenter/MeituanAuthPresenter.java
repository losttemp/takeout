package com.baidu.iov.dueros.waimai.presenter;

import com.baidu.iov.dueros.waimai.interfacedef.AccountCallback;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.IMeituanAuthModel;
import com.baidu.iov.dueros.waimai.model.MeituanAuthModel;
import com.baidu.iov.dueros.waimai.net.entity.request.MeituanAuthorizeReq;
import com.baidu.iov.dueros.waimai.net.entity.response.MeituanAuthorizeResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

/**
 * Created by ubuntu on 18-10-27.
 */

public class MeituanAuthPresenter extends Presenter<MeituanAuthPresenter.MeituanLoginUi> {

    private static final String TAG = MeituanAuthPresenter.class.getSimpleName();

    private IMeituanAuthModel mModel;

    public MeituanAuthPresenter() {
        mModel = new MeituanAuthModel();
    }

    @Override
    public void onUiReady(MeituanLoginUi ui) {
        super.onUiReady(ui);
        mModel.onReady();
    }

    @Override
    public void onUiUnready(MeituanLoginUi ui) {
        super.onUiUnready(ui);
        mModel.onDestroy();
    }

    public void requestAccountInfo() {
        mModel.requestAccountInfo(new AccountCallback() {
            @Override
            public void onSuccess(String msg) {
                if (null != getUi()) {
                    getUi().accountSuccess(msg);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != getUi()) {
                    getUi().accountFailure(msg);
                }
            }
        });
    }

    public void requestAuthInfo() {
        mModel.requestAuthInfo(new AccountCallback() {
            @Override
            public void onSuccess(String msg) {
                if (null != getUi()) {
                    getUi().authSuccess(msg);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != getUi()) {
                    getUi().authFailure(msg);
                }
            }
        });
    }

    public void requestMeituanAuth(MeituanAuthorizeReq meituanAuthorizeReq) {

        mModel.requestMeituanAuth(meituanAuthorizeReq, new RequestCallback<MeituanAuthorizeResponse>() {

            @Override
            public void onSuccess(MeituanAuthorizeResponse data) {
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

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestMeituanAuth getLogid: "+id);
            }
        });
    }

    public interface MeituanLoginUi extends Ui {
        void update(MeituanAuthorizeResponse data);

        void failure(String msg);

        void accountSuccess(String msg);

        void accountFailure(String msg);

        void authSuccess(String msg);

        void authFailure(String msg);

    }

}