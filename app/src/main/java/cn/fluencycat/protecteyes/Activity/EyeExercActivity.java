package cn.fluencycat.protecteyes.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.fluencycat.protecteyes.R;


public class EyeExercActivity extends Activity{
    WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_exercise);
        webView=findViewById(R.id.webview_eye);
        final String url="https://v.qq.com/x/page/d0543o85mwr.html";
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);
        webView.destroy();
    }
}
