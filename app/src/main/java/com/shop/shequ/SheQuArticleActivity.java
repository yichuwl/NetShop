package com.shop.shequ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.R;

public class SheQuArticleActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    protected WebView mWebArticelcontent;
    protected Button mBtnCommentarticel;
    private String contentHTML;
    private String title;
    public static final String ARTICELID = "articelid";
    String articelId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_she_qu_article);
        Intent intent = getIntent();
        contentHTML = intent.getStringExtra(SheQuFragment.ARTICLE_CONTENT_TAG);
        title = intent.getStringExtra("title");
        articelId = intent.getStringExtra(ARTICELID);
        Log.i("articlecontent", contentHTML);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            onBackPressed();
        } else if (view.getId() == R.id.btn_commentarticel) {
            Intent intent = new Intent(SheQuArticleActivity.this, CommentArticleActivity.class);
            intent.putExtra(ARTICELID, articelId);
            startActivity(intent);
        }
    }

    private void initView() {
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(SheQuArticleActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(TextUtils.isEmpty(title) ? getString(R.string.article_content) : title);
        mWebArticelcontent = (WebView) findViewById(R.id.web_articelcontent);
        initWebViewSetting();
        mBtnCommentarticel = (Button) findViewById(R.id.btn_commentarticel);
        mBtnCommentarticel.setOnClickListener(SheQuArticleActivity.this);
    }

    WebChromeClient webChromeClient;
    WebViewClient webViewClient;

    @SuppressLint("JavascriptInterface")
    private void initWebViewSetting() {
        mWebArticelcontent.addJavascriptInterface(this, "android");//添加js监听 这样html就能调用客户端
        mWebArticelcontent.setWebChromeClient(webChromeClient);
        mWebArticelcontent.setWebViewClient(webViewClient);
        webChromeClient = new WebChromeClient() {

        };
        webViewClient = new WebViewClient() {

        };
        WebSettings webSettings = mWebArticelcontent.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setAllowFileAccess(true);
        if (!TextUtils.isEmpty(contentHTML)) {
            mWebArticelcontent.loadDataWithBaseURL(null, contentHTML, "text/html","UTF-8", null);
           // mWebArticelcontent.loadData(contentHTML, "text/html", "utf-8");
        }
    }
}
