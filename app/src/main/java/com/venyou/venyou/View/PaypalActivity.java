package com.venyou.venyou.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.venyou.venyou.Controller.WebViewController;

import c.R;

public class PaypalActivity extends AppCompatActivity {

    private WebView mWebview;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        data = "kupaliwa@syr.edu";
        WebView webView;
        setContentView(R.layout.webviewlayout);
        webView = (WebView)findViewById(R.id.help_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        //file:///android_asset/JsPage.html
        //webView.loadUrl("C:\\Kunal\\Course\\SE\\Venu\\VenYou\\VenYou\\app\\src\\main\\assets");

//        JSInterface = new JavaScriptInterface(this);
//        wv.addJavascriptInterface(JSInterface, "JSInterface");

        webView.loadUrl("file:///android_asset/Paypal.htm");
//        webView.loadDataWithBaseURL("file:///android_asset/Paypal.htm", data, "text/html", "UTF-8", "");
        webView.setWebViewClient(new WebViewController());
    }
}
