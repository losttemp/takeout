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
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
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
    private boolean isNeedVoice, isFinish;
    private View login_bg, loadingView, act_back;
    private String oldBudss = null;//记录budss 与上次不同则跳转到地址界面

    private FrameLayout WV_foreground;

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
        Lg.getInstance().e(TAG, "isNeedVoice：" + isNeedVoice);
        if (isNeedVoice) {
            Entry.getInstance().onEvent(Constant.EVENT_OPEN_APP_VOICE, EventType.TOUCH_TYPE);
            AtyContainer.getInstance().finishAllActivity();
        } else {
            Entry.getInstance().onEvent(Constant.EVENT_OPEN_APP_CLICK, EventType.TOUCH_TYPE);
        }
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        init();
        setContentView(R.layout.activity_meituan_login);
        login_bg = findViewById(R.id.login_bg);
        act_back = findViewById(R.id.login_back);
        loadingView = findViewById(R.id.loading_view);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWVMeituan = (WebView) findViewById(R.id.meituan_login);
        WV_foreground = findViewById(R.id.WV_foreground);

        WebSettings webSettings = mWVMeituan.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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

        mWVMeituan.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        mWVMeituan.setWebViewClient(webViewClient);
        mWVMeituan.setWebChromeClient(webChromeClient);
        mWVMeituan.setHorizontalScrollBarEnabled(false);
        mWVMeituan.setVerticalScrollBarEnabled(false);

        networkView = findViewById(R.id.network_view);
        networkView.setBackground(getResources().getDrawable(R.drawable.app_bg));
        findViewById(R.id.no_internet_btn).setOnClickListener(this);
        findViewById(R.id.webview_back).setOnClickListener(this);
        findViewById(R.id.login_back).setOnClickListener(this);
        KeyBoardListener.getInstance(this).init();
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
        act_back.setVisibility(View.VISIBLE);
    }

    private void init() {
        mMeituanAuthReq = new MeituanAuthorizeReq();
        mMeituanAuthReq.setBduss(CacheUtils.getBduss());
    }

    /**
     * 注入js隐藏部分div元素，多个操作用多个js去做才能生效
     */
    private void hideHtmlContent(WebView view) {
        //隐藏元素
        String javascript = "javascript:function hideOther() {" +
                "var headers = document.getElementsByClassName('download');" +
                "for(var i = 0; i < headers.length; i++) {" +
                "headers[i].style.display = 'none';" +
                "}" + "}";
        view.loadUrl(javascript);
        view.loadUrl("javascript:hideOther();");
    }

    private void hideAgreement(WebView view) {
        //隐藏元素
        String javascript = "javascript:function hideAgreement() {" +
                "var aEls = document.getElementsByTagName('a');" +
                "for(var i = 0; i < aEls.length; i++) {" +
                "if (aEls[i].innerText === '查看美团协议与说明') {" +
                "aEls[i].style.display = 'none';" +
                "}" + "}" + "}";
        view.loadUrl(javascript);
        view.loadUrl("javascript:hideAgreement();");
    }


    private WebViewClient webViewClient = new WebViewClient() {
        private boolean isLoginPage = false;
        private boolean isFirstOpenAuth = true;

        @Override
        public void onPageFinished(WebView view, String url) {
            hideHtmlContent(view);
            if (url.contains("https://openapi.waimai.meituan.com/oauth") ||
                    url.contains("https://h5.waimai.meituan.com/authorize")) {
                hideAgreement(view);
            }
            isFinish = url.startsWith("https://h5.waimai.meituan.com/login?back_url");
            view.loadUrl("javascript:window.java_obj.showSource("
                    + "document.getElementsByTagName('html')[0].innerHTML);");
            view.loadUrl("javascript:window.java_obj.showDescription("
                    + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                    + ");");
            progressBar.setVisibility(View.GONE);

            super.onPageFinished(view, url);

            int delayMillis;
            if (isLoginPage) {
                delayMillis = 3000;
            } else {
                delayMillis = 1000;
            }

            if (isFirstOpenAuth) {
                WV_foreground.setVisibility(View.VISIBLE);
            } else {
                loadingView.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingView.setVisibility(View.GONE);
                        WV_foreground.setVisibility(View.GONE);
                    }
                }, delayMillis);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Lg.getInstance().e(TAG, "onPageStarted " + url);

            if (url.contains("h5.waimai.meituan.com/login?back_url")) {
                isLoginPage = true;
                isFirstOpenAuth = false;
            } else {
                isLoginPage = false;
            }

            progressBar.setVisibility(View.VISIBLE);
            if (url.contains("i.waimai.meituan.com/node/account/agreement") ||
                    url.contains("i.waimai.meituan.com/c/rules") ||
                    url.contains("h5.waimai.meituan.com/login?back_url") ||
                    url.contains("openapi.waimai.meituan.com/oauth") ||
                    url.contains("h5.waimai.meituan.com/authorize")) {
                WV_foreground.setVisibility(View.VISIBLE);
            } else {
                WV_foreground.setVisibility(View.GONE);
            }
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
            syncCookie(this, Config.getHost());
            mWVMeituan.clearCache(true);
            mWVMeituan.clearHistory();
            login_bg.setVisibility(View.VISIBLE);
            act_back.setVisibility(View.GONE);
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
            CacheUtils.saveAddrTime(0);
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
        loadingView.setVisibility(View.GONE);
        networkView.setVisibility(View.VISIBLE);
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
            mWVMeituan.removeAllViews();
            mWVMeituan.destroy();
            mWVMeituan = null;
        }
        super.onDestroy();
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
                //快速点击会导致白屏
                if (!allowBack()) return;
                if (!isFinish && mWVMeituan.canGoBack()) {
                    goBack();
                } else {
                    finish();
                }
                break;
            case R.id.login_back:
                finish();
                break;
            default:
                break;
        }
    }

    private long backTime;

    public boolean allowBack() {
        if (System.currentTimeMillis() - backTime > 2000) {
            backTime = System.currentTimeMillis();
            return true;
        }
        return false;
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
                    ToastUtils.show(mContext, "授权失败", Toast.LENGTH_SHORT);
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
            if (html.contains("{\"errno\":10003,")) {
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
