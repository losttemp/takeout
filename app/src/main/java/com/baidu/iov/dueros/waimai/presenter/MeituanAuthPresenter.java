package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.AccountCallback;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.MeituanAuthModel;
import com.baidu.iov.dueros.waimai.model.IMeituanAuthModel;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.MeituanAuthorizeReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.MeituanAuthorizeResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_NO;
/**
 * Created by ubuntu on 18-10-27.
 */

public class MeituanAuthPresenter extends Presenter<MeituanAuthPresenter.MeituanLoginUi> {

    private static final String TAG = MeituanAuthPresenter.class.getSimpleName();

    private IMeituanAuthModel mModel;

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
            mVoiceManager.registerCmd(context, cmdList, mVoiceCallback);
        }
    }

    @Override
    public void unregisterCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mVoiceManager) {
            mVoiceManager.unregisterCmd(context, mVoiceCallback);
        }
    }

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

    public void requestAuthInfo(){
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
        });
    }

    public void requestAddressListData(AddressListReqBean reqBean) {
        mModel.requestAdressList(reqBean, new RequestCallback<AddressListBean>() {

            @Override
            public void onSuccess(AddressListBean data) {
                List<AddressListBean.IovBean.DataBean> dataBeans = data.getIov().getData();
                if (null != getUi()) {
                    getUi().getAddressListSuccess(dataBeans);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (null != getUi()) {
                    getUi().getAddressListFail(msg);
                }
            }
        });

    }

    public interface MeituanLoginUi extends Ui {
        void update(MeituanAuthorizeResponse data);

        void failure(String msg);

        void close();

        void accountSuccess(String msg);

        void accountFailure(String msg);

        void authSuccess(String msg);

        void authFailure(String msg);

        void getAddressListSuccess(List<AddressListBean.IovBean.DataBean> data);

        void getAddressListFail(String msg);
    }

}