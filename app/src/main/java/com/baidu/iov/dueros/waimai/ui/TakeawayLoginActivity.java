package com.baidu.iov.dueros.waimai.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
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
import com.baidu.iov.dueros.waimai.net.entity.request.MeituanAuthorizeReq;
import com.baidu.iov.dueros.waimai.net.entity.response.MeituanAuthorizeResponse;
import com.baidu.iov.dueros.waimai.presenter.MeituanAuthPresenter;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.KeyBoardListener;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;

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
    private View login_bg, loadingView;
    private String oldBudss = null;//记录budss 与上次不同则跳转到地址界面

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
        isNeedVoice = getIntent().getBooleanExtra(StandardCmdClient.NEED_TTS, false);
        if (isNeedVoice) {
            AtyContainer.getInstance().finishAllActivity();
        }
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        init();
        setContentView(R.layout.activity_meituan_login);
        login_bg = findViewById(R.id.login_bg);
        loadingView = findViewById(R.id.loading_view);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWVMeituan = (WebView) findViewById(R.id.meituan_login);

        mWVMeituan.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        mWVMeituan.setWebViewClient(webViewClient);
        mWVMeituan.setWebChromeClient(webChromeClient);
        mWVMeituan.setHorizontalScrollBarEnabled(false);
        mWVMeituan.setVerticalScrollBarEnabled(false);

        WebSettings webSettings = mWVMeituan.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setAllowContentAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        networkView = findViewById(R.id.network_view);
        networkView.setBackground(getResources().getDrawable(R.drawable.app_bg));
        findViewById(R.id.no_internet_btn).setOnClickListener(this);
        findViewById(R.id.webview_back).setOnClickListener(this);
        KeyBoardListener.getInstance(this).init();
        login_bg.setVisibility(View.GONE);
        oldBudss = CacheUtils.getBduss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtil.getNetWorkState(this)) {
            loadingView.setVisibility(View.VISIBLE);
            initPostHttp();
            networkView.setVisibility(View.GONE);
        } else {
            loadingView.setVisibility(View.GONE);
            networkView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWVMeituan != null && login_bg.getVisibility() == View.VISIBLE) {
            mWVMeituan.loadUrl("about:blank");
            mWVMeituan.clearHistory();
        }
        login_bg.setVisibility(View.GONE);
    }

    private void init() {
        mMeituanAuthReq = new MeituanAuthorizeReq();
        mMeituanAuthReq.setBduss(CacheUtils.getBduss());
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.startsWith("https://h5.waimai.meituan.com/login?back_url")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    String JS_SCRIPT = "var script = document.createElement('script');" +
                            "script.src = 'https://iov-www.cdn.bcebos.com/waimai/index.js';" +
                            "document.head.appendChild(script);";
                    view.evaluateJavascript("javascript:" + JS_SCRIPT, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                }
            }
            view.loadUrl("javascript:window.java_obj.showSource("
                    + "document.getElementsByTagName('html')[0].innerHTML);");
            view.loadUrl("javascript:window.java_obj.showDescription("
                    + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                    + ");");
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

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed();
            handler.cancel();
            super.onReceivedSslError(view, handler, error);
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
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void update(MeituanAuthorizeResponse data) {
        Entry.getInstance().onEvent(Constant.ENTRY_LOGIN_MEITUAN, EventType.TOUCH_TYPE);
        if (data.getIov().getAuthorizedState()) {
            loadingView.setVisibility(View.GONE);
            startIntent();
        } else {
            loadingView.setVisibility(View.GONE);
            syncCookie(this, Config.getHost());
            mWVMeituan.clearCache(true);
            mWVMeituan.clearHistory();
            login_bg.setVisibility(View.VISIBLE);
            mWVMeituan.loadUrl(data.getIov().getAuthorizeUrl());
        }
    }

    private void syncCookie(Context context, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, Config.COOKIE_VALUE);
    }

    private boolean init = false;

    private void startIntent() {
        //接口返回不知道为什么会多次调用,init限制多次跳转界面
        if (init) return;
        init = true;
        //与上次budss 不同则跳转到地址界面
        long time = CacheUtils.getAddrTime();
        if (CacheUtils.getBduss().equals(oldBudss) &&
                time != 0 && System.currentTimeMillis() - time <= SIX_HOUR) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
            intent.putExtra(Constant.START_APP, Constant.START_APP_CODE);
            intent.putExtra(Constant.IS_FROME_TAKEAWAYLOGIN, true);
            startActivity(intent);
        } else {
            Intent addressIntent = new Intent(this, AddressSelectActivity.class);
            addressIntent.putExtra(Constant.IS_NEED_VOICE_FEEDBACK, isNeedVoice);
            addressIntent.putExtra(Constant.START_APP, Constant.START_APP_CODE);
            startActivity(addressIntent);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TakeawayLoginActivity.this.finish();
            }
        }, 500);
    }

    @Override
    public void failure(String msg) {
        Lg.getInstance().e(TAG, "Meituan Auth Fail Activity Finish");
        ToastUtils.show(this, getString(R.string.http_error), Toast.LENGTH_SHORT);
        finish();
    }

    @Override
    public void accountSuccess(String msg) {
        Message message = handler.obtainMessage();
        message.obj = msg;
        message.what = HANDLER_MEITUAN_AUTH;
        handler.sendMessage(message);
    }

    @Override
    public void accountFailure(String msg) {
        Message message = handler.obtainMessage();
        message.obj = msg;
        message.what = HANDLER_POST_FAIL;
        handler.sendMessage(message);
    }

    @Override
    public void authSuccess(String msg) {
        Message message = handler.obtainMessage();
        message.obj = msg;
        message.what = HANDLER_START;
        handler.sendMessage(message);
    }

    @Override
    public void authFailure(String msg) {
        Message message = handler.obtainMessage();
        message.obj = msg;
        message.what = HANDLER_POST_FAIL;
        handler.sendMessage(message);
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (mWVMeituan != null) {
            CookieSyncManager.createInstance(getApplicationContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            CookieSyncManager.getInstance().sync();
            mWVMeituan.loadUrl("about:blank");
            mWVMeituan.clearHistory();
            ((ViewGroup) mWVMeituan.getParent()).removeView(mWVMeituan);
            mWVMeituan.destroy();
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
            case R.id.webview_back:
                if (mWVMeituan.canGoBack()) {
                    goBack();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    public void goBack() {
        WebBackForwardList mWebBackForwardList = mWVMeituan.copyBackForwardList();
        if (mWebBackForwardList.getCurrentIndex() == 1) {
            finish();
        } else {
            mWVMeituan.goBack();
        }
    }

    private final int HANDLER_FINISH_ACT = 0;
    private final int HANDLER_POST_HTTP = 1;
    private final int HANDLER_POST_FAIL = 2;
    private final int HANDLER_START = 3;
    private final int HANDLER_MEITUAN_AUTH = 4;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_FINISH_ACT:
                    finish();
                    break;
                case HANDLER_POST_HTTP:
                    mWVMeituan.setVisibility(View.GONE);
                    mWVMeituan.clearCache(true);
                    mWVMeituan.clearHistory();
                    mWVMeituan.destroy();
                    initPostHttp();
                    break;
                case HANDLER_POST_FAIL:
                    loadingView.setVisibility(View.GONE);
                    if (Constant.ACCOUNT_LOGIN_FAIL.equals(msg.obj)) {
                        Lg.getInstance().d(TAG, "account login fail");
                        finish();
                    }
                    if (Constant.ACCOUNT_AUTH_FAIL.equals(msg.obj)) {
                        Lg.getInstance().d(TAG, "account auth fail");
                        finish();
                    }
                    break;
                case HANDLER_START:
                    loadingView.setVisibility(View.GONE);
                    if (Constant.ACCOUNT_AUTH_SUCCESS.equals(msg.obj)) {
                        Lg.getInstance().d(TAG, "account auth success");
                        startIntent();
                    }
                    break;
                case HANDLER_MEITUAN_AUTH:
                    if (Constant.ACCOUNT_LOGIN_SUCCESS.equals(msg.obj)) {
                        Lg.getInstance().d(TAG, "account login success");
                        if (mMeituanAuthReq != null) {
                            mMeituanAuthReq.setBduss(CacheUtils.getBduss());
                        }
                        getPresenter().requestMeituanAuth(mMeituanAuthReq);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            if (html.contains("errno") || html.contains("error_code")) {
                Lg.getInstance().e(TAG, "webview:" + html);
            }
            if (html.contains("{\"errno\":0,\"err_msg\":\"success\",")) {
                handler.sendEmptyMessage(HANDLER_POST_HTTP);
            }
            if (html.contains("error_code") && html.contains("10002")) {
                handler.sendEmptyMessage(HANDLER_FINISH_ACT);
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
