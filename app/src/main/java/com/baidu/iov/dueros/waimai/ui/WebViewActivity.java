package com.baidu.iov.dueros.waimai.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        //https:\/\/openapi.waimai.meituan.com\/oauth\/authorize?app_id=8315367514591523&redirect_uri=http:\/\/sandbox.codriverapi.baidu.com\/iovservice\/waimai\/oauthredirect&response_type=code&scope=&state=5bd1b159ae948
        WebView viewById = (WebView) findViewById(R.id.wv_web);
        viewById.loadUrl("https:\\/\\/openapi.waimai.meituan.com\\/oauth\\/authorize?app_id=8315367514591523&redirect_uri=http:\\/\\/sandbox.codriverapi.baidu.com\\/iovservice\\/waimai\\/oauthredirect&response_type=code&scope=&state=5bd1b159ae948");
    }

//    private void setCookie(){//TODO
//            /* 获取cookie */
//        String key = "key";
//        String value = "value";
//        String jump_url = "http://192.168.0.5:8889/banner/list.pb"
//        //将信息直接存储在cookie中
//        BasicClientCookie newCookie = new BasicClientCookie(key, value);
//        newCookie.setDomain(getDomainByUrl(jump_url));
//        String cookieString = newCookie.getName() + "=" + newCookie.getValue() + "; domain=" + newCookie.getDomain();
//
//        CookieSyncManager.createInstance(this);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();
//        //第一个参数是域名，第二个参数是固定格式的字符串
//        cookieManager.setCookie(getDomainByUrl(jump_url), cookieString);
//        CookieSyncManager.getInstance().sync();
//    }
}
