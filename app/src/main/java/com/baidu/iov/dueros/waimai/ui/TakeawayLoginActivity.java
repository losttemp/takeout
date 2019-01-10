package com.baidu.iov.dueros.waimai.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.KeyEvent;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.Config;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.MeituanAuthorizeReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.MeituanAuthorizeResponse;
import com.baidu.iov.dueros.waimai.presenter.MeituanAuthPresenter;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

import java.util.List;

public class TakeawayLoginActivity extends BaseActivity<MeituanAuthPresenter, MeituanAuthPresenter.MeituanLoginUi> implements
        MeituanAuthPresenter.MeituanLoginUi, View.OnClickListener {

    private static final String TAG = TakeawayLoginActivity.class.getSimpleName();
    private WebView mWVMeituan;
    private ProgressBar progressBar;
    private MeituanAuthorizeReq mMeituanAuthReq;
    private final long SIX_HOUR = 6 * 60 * 60 * 1000;
    Bundle savedInstanceState;
    private View networkView;
    private boolean isNeedVoice;

    @Override
    MeituanAuthPresenter createPresenter() {
        return new MeituanAuthPresenter();
    }

    @Override
    MeituanAuthPresenter.MeituanLoginUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setNoRequestForPermissions(true);
        isNeedVoice = getIntent().getBooleanExtra(VoiceManager.NEED_TTS, false);
        if (isNeedVoice) {
            AtyContainer.getInstance().finishAllActivity();
        }
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        init();
        setContentView(R.layout.activity_meituan_login);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWVMeituan = (WebView) findViewById(R.id.meituan_login);

        WebSettings webSettings = mWVMeituan.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        mWVMeituan.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        mWVMeituan.setWebViewClient(webViewClient);
        mWVMeituan.setWebChromeClient(webChromeClient);
        mWVMeituan.setVisibility(View.GONE);

        networkView = findViewById(R.id.network_view);
        networkView.setBackground(getResources().getDrawable(R.drawable.app_bg));
        findViewById(R.id.no_internet_btn).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtil.getNetWorkState(this)) {
            initPostHttp();
            networkView.setVisibility(View.GONE);
        } else {
            networkView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void init() {
        mMeituanAuthReq = new MeituanAuthorizeReq();
        mMeituanAuthReq.setBduss(CacheUtils.getBduss());
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window.java_obj.showSource("
                    + "document.getElementsByTagName('html')[0].innerHTML);");
            view.loadUrl("javascript:window.java_obj.showDescription("
                    + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                    + ");");
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
            } else {
                view.loadUrl(request.toString());
            }
            return true;
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            result.confirm();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };

    @Override
    public void update(MeituanAuthorizeResponse data) {
        Entry.getInstance().onEvent(Constant.ENTRY_LOGIN_MEITUAN, EventType.TOUCH_TYPE);
        if (data.getIov().getAuthorizedState()) {
            if (CacheUtils.getAuth()) {
                startIntent();
            } else {
                getPresenter().requestAuthInfo();
            }
        } else {
            syncCookie(this, Config.getHost());
            mWVMeituan.setVisibility(View.VISIBLE);
            mWVMeituan.loadUrl(data.getIov().getAuthorizeUrl());
        }
    }

    private void syncCookie(Context context, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, Config.COOKIE_VALUE);
    }

    private void startIntent() {
        long time = CacheUtils.getAddrTime();
        if (time == 0 || (System.currentTimeMillis() - time > SIX_HOUR)) {
            Intent addressIntent = new Intent(this, AddressSelectActivity.class);
            addressIntent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
            startActivity(addressIntent);
            finish();
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void failure(String msg) {
        finish();
    }

    @Override
    public void accountSuccess(String msg) {
        if (Constant.ACCOUNT_LOGIN_SUCCESS.equals(msg)) {
            Lg.getInstance().d(TAG, "account login success");
            getPresenter().requestMeituanAuth(mMeituanAuthReq);
        }
    }

    @Override
    public void accountFailure(String msg) {
        if (Constant.ACCOUNT_LOGIN_FAIL.equals(msg)) {
            Lg.getInstance().d(TAG, "account login fail");
            finish();
        }
    }

    @Override
    public void authSuccess(String msg) {
        if (Constant.ACCOUNT_AUTH_SUCCESS.equals(msg)) {
            Lg.getInstance().d(TAG, "account auth success");
            startIntent();
        }
    }

    @Override
    public void authFailure(String msg) {
        if (Constant.ACCOUNT_AUTH_FAIL.equals(msg)) {
            Lg.getInstance().d(TAG, "account auth fail");
            finish();
        }
    }

    @Override
    public void getAddressListSuccess(List<AddressListBean.IovBean.DataBean> data) {
        Lg.getInstance().d(TAG, "get addresslist success");
        Intent addressIntent = new Intent(this, AddressSelectActivity.class);
        addressIntent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
        startActivity(addressIntent);
        finish();
    }

    @Override
    public void getAddressListFail(String msg) {
        Lg.getInstance().d(TAG, "get addresslist fail");
        ToastUtils.show(this, msg, Toast.LENGTH_LONG);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWVMeituan.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            mWVMeituan.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWVMeituan != null) {
            mWVMeituan.stopLoading();
            mWVMeituan.setWebViewClient(null);
            mWVMeituan.clearHistory();
            mWVMeituan.clearCache(true);
            mWVMeituan.loadUrl("about:blank");
            mWVMeituan.pauseTimers();
            mWVMeituan = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_internet_btn:
                if (NetUtil.getNetWorkState(this)) {
                    initPostHttp();
                } else {
                    ToastUtils.show(this, getResources().getString(R.string.is_network_connected), Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            if (html.contains("errno")||html.contains("error_code")) {
                Lg.getInstance().e(TAG, "webview:" + html);
            }
            if (html.contains("{\"errno\":0,\"err_msg\":\"success\",")
                    && html.contains(",\"meituan\":{\"msg\":\"success\"},")) {
                mWVMeituan.loadUrl("about:blank");
                initPostHttp();
            }
            if (html.contains("error_code") && html.contains("10002")) {
                finish();
            }
        }

        @JavascriptInterface
        public void showDescription(String str) {
        }
    }

    private void initPostHttp() {
        Entry.getInstance().onEvent(Constant.ENTRY_LOGIN_OS, EventType.TOUCH_TYPE);
        getPresenter().requestAccountInfo();
    }
}
