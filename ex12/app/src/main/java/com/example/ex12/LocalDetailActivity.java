package com.example.ex12;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LocalDetailActivity extends AppCompatActivity {
    TextView text;
    WebView web;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_detail);

        Intent intent = getIntent();
        String url=intent.getStringExtra("link");
        String title=intent.getStringExtra("title");

        text=(TextView) findViewById(R.id.titleDetail);
        text.setText(Html.fromHtml(title));

        web = (WebView) findViewById(R.id.web);

        progress=new ProgressDialog(this);
        progress.setMessage("페이지를 불러오는 중입니다.");
        progress.show();

        web.setWebViewClient(new MyWebView());
        web.loadUrl(url);

    }

    public class MyWebView extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progress.dismiss();
            super.onPageFinished(view, url);
        }
    }
}