package com.sasidhar.smaps.payu_money;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String url = "https://test.payu.in/_payment";
//    private String url = "https://secure.payu.in/_payment";

    private String postData;
    private WebView mWebView;

    private HashMap<String, String> params = new HashMap<>();
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        init();

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new PayU(this), "PayU");

        byte[] encodedData = postData.getBytes();
        mWebView.postUrl(url, encodedData);

    }

    private synchronized void init() {

        JSONObject productInfo = new JSONObject();
        JSONArray paymentParts = new JSONArray();
        JSONObject productDetails = new JSONObject();

        try {
            productDetails.put("name", "Test");
            productDetails.put("description", "Test description");
            productDetails.put("value", "9.0");
            productDetails.put("isRequired", "true");
            productDetails.put("settlementEvent", "EmailConfirmation");

            paymentParts.put(productDetails);
            productInfo.put("paymentParts", paymentParts);

            Log.d("Product", paymentParts.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        params.put("key", "JPXUkO");
        params.put("txnid", "" + System.currentTimeMillis());
        params.put("amount", "9.0");
        params.put("productinfo", "pen");
        params.put("firstname", "sasi");
        params.put("email", "sasidhar.678@gmail.com");
        params.put("phone", "9959582678");
        params.put("surl", "https://dl.dropboxusercontent.com/s/rsyajmdygg50ug8/success.html");
        params.put("furl", "https://dl.dropboxusercontent.com/s/haywukd51v4bqlg/failure.html");
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");

        String hashSequence = generateHashSequence(params, "JwXQrETY");
        String hash = hashCal("SHA-512", hashSequence);
        Log.d("HASH SEQUENCE", hashSequence);

        params.put("hash", hash);
        params.put("service_provider", "payu_paisa");

        urlEncodeUTF8(params);
    }

    private synchronized String generateHashSequence(Map<String, String> params, String salt) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s|", params.get("key")));
        sb.append(String.format("%s|", params.get("txnid")));
        sb.append(String.format("%s|", params.get("amount")));
        sb.append(String.format("%s|", params.get("productinfo")));
        sb.append(String.format("%s|", params.get("firstname")));
        sb.append(String.format("%s|", params.get("email")));
        sb.append(String.format("%s|", params.get("udf1")));
        sb.append(String.format("%s|", params.get("udf2")));
        sb.append(String.format("%s|", params.get("udf3")));
        sb.append(String.format("%s|", params.get("udf4")));
        sb.append(String.format("%s||||||", params.get("udf5")));
        sb.append(String.format("%s", salt));

        return sb.toString();
    }

    private void urlEncodeUTF8(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<?, ?> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        postData = sb.toString();
        Log.d("POST DATA", postData);
    }

    private String urlEncodeUTF8(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private synchronized String hashCal(String type, String hashSequence) {
        byte[] hashseq = hashSequence.getBytes();
        StringBuffer hexString = new StringBuffer();

        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return hexString.toString();
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
            Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void onFailure(final String result) {
            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

}
