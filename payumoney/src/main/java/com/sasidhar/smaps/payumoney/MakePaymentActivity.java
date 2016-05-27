package com.sasidhar.smaps.payumoney;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.util.HashMap;

public class MakePaymentActivity extends AppCompatActivity {

    private String postData;
    private WebView mWebView;

    private LinearLayout progressBar;

    private HashMap<String, String> params = new HashMap<>();
    private int ENV = 1;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        Intent intent = getIntent();

        ENV = intent.getIntExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_PRODUCTION);
        params = (HashMap<String, String>) intent.getSerializableExtra(PayUMoney_Constants.PARAMS);

        url = ENV == PayUMoney_Constants.ENV_DEV ? PayUMoney_Constants.test_url : PayUMoney_Constants.production_url;

        progressBar = (LinearLayout) findViewById(R.id.progressBar);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                setProgress(progress * 100);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
        });

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new PayU(this), "PayU");

        postData = Utils.urlEncodeUTF8(params);

        byte[] encodedData = postData.getBytes();
        mWebView.postUrl(url, encodedData);

    }

    public class PayU {
        Context mContext;
        Intent intent;

        PayU(Context c) {
            mContext = c;
            intent = new Intent();
        }

        @JavascriptInterface
        public void onSuccess(final String result) {
            intent.putExtra("result", result);
            setResult(RESULT_OK, intent);
            finish();
        }

        @JavascriptInterface
        public void onFailure(final String result) {
            intent.putExtra("result", result);
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }
}
