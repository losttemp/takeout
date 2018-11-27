package com.baidu.iov.dueros.waimai.utils;

import com.baidu.iov.dueros.waimai.ui.WaiMaiApplication;
import com.baidu.iov.faceos.client.FaceOSClient;
import com.baidu.iov.faceos.client.FaceOSClientStateListener;
import com.baidu.iov.faceos.client.account.AbsAccountReceiver;
import com.baidu.iov.faceos.client.account.ExAccountData;
import com.baidu.iov.faceos.client.account.ExAuthData;

/**
 * Created by ubuntu on 18-10-24.
 */

public class AccountManager {

    private static final String TAG = AccountManager.class.getSimpleName();
    private FaceOSClient mClient;
    private AccountCallBack callback;

    public static AccountManager getInstance() {
        return AccountManager.SingletonHandler.accountManager;
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
        mClient.getAccountSender().sendRequestUnifyAuthBotService();
    }

    public interface AccountCallBack {
        void onAccountSuccess(String msg);

        void onAccountFailed(String msg);
    }

    private AbsAccountReceiver mAbsAccountReceiver = new AbsAccountReceiver() {
        @Override
        public void receiveGetAccountData(ExAccountData exAccountData) {
            Lg.getInstance().d(TAG, "receiveGetAccountData : " + exAccountData);
            if (exAccountData != null && exAccountData.isLogin()) {
                if (mClient.isServiceBinded()) {
                    CacheUtils.saveBduss(exAccountData.getBduss());
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
            CacheUtils.saveBduss("");
            callback.onAccountFailed(Constant.ACCOUNT_LOGIN_FAIL);
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
    };
}
