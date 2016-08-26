package com.project.wei.zhbj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.project.wei.zhbj.R;
import com.project.wei.zhbj.utils.SharedPrefUtil;

public class NewsDetailsActivity extends Activity implements View.OnClickListener{

    @ViewInject(R.id.ibtn_menu)
    private ImageButton ibtn_menu;
    @ViewInject(R.id.ibtn_back)
    private ImageButton ibtn_back;
    @ViewInject(R.id.ibtn_textsize)
    private ImageButton ibtn_textsize;
    @ViewInject(R.id.ibtn_share)
    private ImageButton ibtn_share;
    @ViewInject(R.id.ll_tools)
    private LinearLayout ll_tools;
    @ViewInject(R.id.wv_news_details)
    private WebView mWebView;
    @ViewInject(R.id.pb_loadinging)
    private ProgressBar pb_loading;
    private String mUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ViewUtils.inject(this);
//        chooseWhich = SharedPrefUtil.getInt(this, "TextSize", 2);  这个有点难，以后再弄吧

        mUrl = getIntent().getStringExtra("url");

        ibtn_menu.setVisibility(View.INVISIBLE);
        ibtn_back.setVisibility(View.VISIBLE);
        ll_tools.setVisibility(View.VISIBLE);

        ibtn_back.setOnClickListener(this);
        ibtn_textsize.setOnClickListener(this);
        ibtn_share.setOnClickListener(this);

       // mWebView.loadUrl("www.baidu.com");
        mWebView.loadUrl(mUrl);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);// 支持js功能，必写，几乎每个网页都会有js
        settings.setBuiltInZoomControls(true);// 显示缩放按钮（wap网页不支持，因为wap已经适配好了手机）
        settings.setUseWideViewPort(true);// 支持双击缩放（wap 网页不支持）

//        mWebView.goBack(); //跳到上个页面
//        mWebView.goForward();//跳到下个页面

        mWebView.setWebViewClient(new WebViewClient(){
            @Override  // 开始加载网页
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //pb_loading.setVisibility(View.VISIBLE);
            }

            @Override  // 网页加载结束
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("tag","网页加载完了");
                //pb_loading.setVisibility(View.INVISIBLE);
            }

            @Override  // 点击网页里所有链接跳转都会走此方法
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;// true表示在此界面的view中跳转
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override  //  进度发生变化  newProgress
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override  //  网页标题  title
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.ibtn_textsize:
                changeTextSize();
                break;
            case R.id.ibtn_share:
                break;
        }
    }
    
    private int tempWhich; // 临时选择的字体大小 ，点击确定之前
    private int chooseWhich = 2; // 点击确定以后，保存的字体大小
    private void changeTextSize() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置字体大小");
        String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
        builder.setSingleChoiceItems(items, chooseWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempWhich = which;  // 使用这里的 which
            }
        });
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {  //这里的 which 是默认值，不会改变，所以不用它
                WebSettings settings = mWebView.getSettings();
                switch (tempWhich) {
                    case 0:
                        settings.setTextZoom(300);
                        break;
                    case 1:
                        settings.setTextZoom(180);
                        break;
                    case 2:
                        settings.setTextZoom(100);
                        break;
                    case 3:
                        settings.setTextZoom(80);
                        break;
                    case 4:
                        settings.setTextZoom(30);
                        break;
                }
                chooseWhich = tempWhich;
                SharedPrefUtil.setInt(getApplicationContext(),"TextSize",chooseWhich);

            }
        });
        builder.setNegativeButton("cancel",null);
        builder.show();
    }
}
