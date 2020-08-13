package com.baidu.iov.dueros.waimai.utils;

import android.text.TextUtils;

import com.baidu.iov.dueros.waimai.net.Config;
import com.baidu.iov.dueros.waimai.ui.WaiMaiApplication;

/**
 * Created by ubuntu on 18-10-24.
 */

public class AccountManager {

   /* private static final String TAG = AccountManager.class.getSimpleName();
    private FaceOSClient mClient;
    private AccountCallBack callback;

    public static AccountManager getInstance() {
        return SingletonHandler.accountManager;
    }

    private static class SingletonHandler {
        private static final AccountManager accountManager = new AccountManager();
    }

    private AccountManager() {
        init();
    }

    private void init() {
        mClient = FaceOSClient.instance();
        mClient.setAppContext(WaiMaiApplication.getInstance().getApplicationContext());
        mClient.setDebug(true);
        mClient.bindAccountService();
        mClient.setStateListener(mClientListener);
        mClient.registerAccountReceiver(mAbsAccountReceiver);
    }


    public void getAccountInfo(AccountCallBack callBack) {
        Lg.getInstance().d(TAG, "getAccountInfo");
        this.callback = callBack;
        mClient.getAccountSender().sendGetAccountData(null);
    }

    public void getAuthInfo(AccountCallBack callBack) {
        Lg.getInstance().d(TAG, "getAuthInfo");
        this.callback = callBack;
//        mClient.getAccountSender().sendRequestUnifyAuthBotService();
        FaceOSClient.instance().getAccountSender().sendGetAuthData(ExAuthData.KEY_OUT_ORDER);
    }

    public interface AccountCallBack {
        void onAccountSuccess(String msg);

        void onAccountFailed(String msg);
    }

    private AbsAccountReceiver mAbsAccountReceiver = new AbsAccountReceiver() {
        @Override
        public void receiveGetAccountData(ExAccountData exAccountData) {
            if (exAccountData != null && exAccountData.isLogin()) {
                if (mClient.isServiceBinded()) {
                    CacheUtils.saveBduss(exAccountData.getBduss());
                    if (!TextUtils.isEmpty(exAccountData.getBduss())) {
                        Config.COOKIE_VALUE = "BDUSS=" + exAccountData.getBduss();
//                        Lg.getInstance().d(TAG, Config.COOKIE_VALUE);
                    }
                    callback.onAccountSuccess(Constant.ACCOUNT_LOGIN_SUCCESS);
                } else {
                    Lg.getInstance().d(TAG, "service is not bind");
                    mClient.bindAccountService();
                }
            } else {
                mClient.getAccountSender().sendRequestAccountLogin(false);
            }
        }

        @Override
        public void receiveGetAuthData(ExAuthData exAuthData) {
            Lg.getInstance().d(TAG, "receiveGetAuthData : " + exAuthData);
            if (exAuthData != null && exAuthData.isAuthedOutOrder()) {
                CacheUtils.saveAuth(true);
                callback.onAccountSuccess(Constant.ACCOUNT_AUTH_SUCCESS);
            } else {
                CacheUtils.saveAuth(false);
                mClient.getAccountSender().sendRequestUnifyAuthBotService();
            }
        }

        @Override
        public void receiveResponseAccountLogin(ExAccountData exAccountData) {
            Lg.getInstance().d(TAG, "receiveResponseAccountLogin : " + exAccountData);
            if (exAccountData != null && exAccountData.isLogin()) {
                if (mClient.isServiceBinded()) {
                    CacheUtils.saveBduss(exAccountData.getBduss());
                    callback.onAccountSuccess(Constant.ACCOUNT_LOGIN_SUCCESS);
                } else {
                    Lg.getInstance().d(TAG, "service is not bind");
                    mClient.bindAccountService();
                }
            } else {
                CacheUtils.saveBduss("");
                callback.onAccountFailed(Constant.ACCOUNT_LOGIN_FAIL);
                Lg.getInstance().d(TAG, "account login fail");
            }
        }

        @Override
        public void receiveResponseAuthService(ExAuthData exAuthData) {
            Lg.getInstance().d(TAG, "receiveResponseAuthService : " + exAuthData);
            if (exAuthData != null && exAuthData.isAuthedOutOrder()) {
                CacheUtils.saveAuth(true);
                callback.onAccountSuccess(Constant.ACCOUNT_AUTH_SUCCESS);
            } else {
                CacheUtils.saveAuth(false);
                callback.onAccountFailed(Constant.ACCOUNT_AUTH_FAIL);
                Lg.getInstance().d(TAG, "account auth fail");
            }
        }

        @Override
        public void receiveResponseAccountLogout(ExAccountData exAccountData) {
            Lg.getInstance().d(TAG, "receiveResponseAccountLogout : " + exAccountData);
            if (callback != null) {
                callback.onAccountFailed(Constant.ACCOUNT_LOGIN_FAIL);
            }
            CacheUtils.saveBduss("");
            CacheUtils.saveAddrTime(0);
            AtyContainer.getInstance().finishAllActivity();
        }
    };

    private FaceOSClientStateListener mClientListener = new FaceOSClientStateListener() {
        @Override
        public void onServiceConnected() {
            Lg.getInstance().d(TAG, "Service Connected");
        }

        @Override
        public void onServiceDisconnected() {
            Lg.getInstance().d(TAG, "Service Disconnected");
        }

        @Override
        public void onBindServiceFailure() {
            Lg.getInstance().d(TAG, "Bind Service Failure");
        }

        @Override
        public void onBindServiceSuccess() {
            Lg.getInstance().d(TAG, "Bind Service Success");
        }

        @Override
        public void onUnbindService() {
            Lg.getInstance().d(TAG, "Unbind Service");
        }

        @Override
        public void onSendSuccess(int serviceType, int funcType, String data) {
            Lg.getInstance().d(TAG, "Send Success");
        }

        @Override
        public void onSendFailure(int serviceType, int funcType, String data) {
            Lg.getInstance().d(TAG, "Send Failure");
        }

        @Override
        public void onRegisterClientServiceFailure() {
            Lg.getInstance().d(TAG, "Register Client Service Failure");
        }
    };*/
}
