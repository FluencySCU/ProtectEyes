package cn.fluencycat.protecteyes.Activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cn.fluencycat.protecteyes.R;

public class WebActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        webView = findViewById(R.id.webview_public);
        final ProgressBar bar = findViewById(R.id.progressBar);
        Intent intent = getIntent();
        final String url = intent.getStringExtra("url");
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
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100)
                    bar.setVisibility(View.INVISIBLE);//加载完成，隐藏加载bar
                else {
                    if (bar.getVisibility() == View.INVISIBLE)
                        bar.setVisibility(View.VISIBLE);
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
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
