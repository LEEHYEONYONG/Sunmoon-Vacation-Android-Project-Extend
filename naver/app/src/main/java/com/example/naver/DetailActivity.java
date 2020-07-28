package com.example.naver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String strTitle = intent.getStringExtra("title");
        String strLink = intent.getStringExtra("link");

        TextView title = findViewById(R.id.title);
        title.setText(Html.fromHtml(strTitle));

        WebView web = (WebView)findViewById(R.id.web);

        progress = new ProgressDialog(this);
        progress.setMessage("페이지를 불러오는 중입니다.");
        progress.show();

        web.setWebViewClient(new MyWebView());
        web.loadUrl(strLink);
    }

    public class MyWebView extends WebViewClient{
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