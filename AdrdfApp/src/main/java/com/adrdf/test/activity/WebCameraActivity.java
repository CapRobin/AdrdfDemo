package com.adrdf.test.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.adrdf.test.R;
import com.adrdf.test.global.AndH5Impl;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.global.RdfConstant;

/**
 * Copyright © CapRobin
 *
 * Name：WebCameraActivity
 * Describe：
 * Date：2018-02-17 18:59:39
 * Author: CapRobin@yeah.net
 *
 */
public class WebCameraActivity extends RdfBaseActivity {

	private WebView mWebView;
	private AndH5Impl impl = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 显示标题的进度条
		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.activity_web_camera);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		toolbar.setTitle(R.string.title_web_camera);
		toolbar.setContentInsetsRelative(0, 0);
		toolbar.setNavigationIcon(R.drawable.ic_back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mWebView = (WebView) findViewById(R.id.webView);
		// 设置支持JavaScript脚本
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置可以支持缩放
		webSettings.setSupportZoom(false);
		// 设置默认缩放方式尺寸是far
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		// 设置出现缩放工具
		webSettings.setBuiltInZoomControls(true);
		// 访问assets目录下的文件
		//String url = "file:///android_asset/index.html";
		String url = "http://www.amsoft.cn/demo/andh5/index_1.php";

		//把本类的一个实例添加到js的全局对象window中
        impl = new AndH5Impl(this);
		mWebView.addJavascriptInterface(impl, "AndH5");
		// String url = "http://www.baidu.com";
		mWebView.loadUrl(url);
		// String summary = "<html><body>Your html code.</body></html>";
		// mWebView.loadData(summary, "text/html", "utf-8");
		// 设置WebViewClient
		mWebView.setWebViewClient(new WebViewClient() {
			// url拦截
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//Toast.makeText(ActivityMain.this, " 拦截到url:" + url,Toast.LENGTH_SHORT).show();
				// 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
				view.loadUrl(url);
				// 相应完成返回true
				return true;
				//return super.shouldOverrideUrlLoading(view, url);
			}

			// 页面开始加载
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				//Toast.makeText(ActivityMain.this, "onPageStarted:" + url,Toast.LENGTH_SHORT).show();
				super.onPageStarted(view, url, favicon);
			}

			// 页面加载完成
			@Override
			public void onPageFinished(WebView view, String url) {
				//Toast.makeText(ActivityMain.this, " onPageFinished:" + url,Toast.LENGTH_SHORT).show();
				super.onPageFinished(view, url);
			}

			// WebView加载的所有资源url
			@Override
			public void onLoadResource(WebView view, String url) {
				//Toast.makeText(ActivityMain.this, " onLoadResource:" + url,Toast.LENGTH_SHORT).show();
				super.onLoadResource(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
				//Toast.makeText(ActivityMain.this,"错误url:" + failingUrl + "," + description,Toast.LENGTH_SHORT).show();
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

		});

		// 设置WebChromeClient
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			// 处理javascript中的alert
			public boolean onJsAlert(WebView view, String url, String message,
									 final JsResult result) {
				//Toast.makeText(ActivityMain.this,"JavaScript的Alert",Toast.LENGTH_SHORT).show();
				return super.onJsAlert(view, url, message, result);
			};

			@Override
			// 处理javascript中的confirm
			public boolean onJsConfirm(WebView view, String url,
									   String message, final JsResult result) {
				//Toast.makeText(ActivityMain.this,"JavaScript的Confirm",Toast.LENGTH_SHORT).show();
				return super.onJsConfirm(view, url, message, result);
			};

			@Override
			// 处理javascript中的prompt
			public boolean onJsPrompt(WebView view, String url, String message,
									  String defaultValue, final JsPromptResult result) {
				//Toast.makeText(ActivityMain.this,"JavaScript的Prompt",Toast.LENGTH_SHORT).show();
				return super.onJsPrompt(view, url, message, defaultValue, result);
			};

			//设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				setProgress(newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}

			//设置程序的Title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			// 返回前一个页面
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (resultCode) {
			case RESULT_OK:
				if(requestCode == RdfConstant.REQUEST_CODE_TAKEPICTURE){
					String  path = intent.getStringExtra("path");
					//Toast.makeText(ActivityMain.this,"照片路径:" + path,Toast.LENGTH_SHORT).show();
					mWebView.loadUrl("javascript:takePictureCallBack('"+path+"')");
				}
				if(requestCode == RdfConstant.REQUEST_CODE_RECODER){
					String  path = intent.getStringExtra("path");
					//Toast.makeText(ActivityMain.this,"照片路径:" + path,Toast.LENGTH_SHORT).show();
					mWebView.loadUrl("javascript:videoRecoderCallback('"+path+"')");
				}
				break;
			default:
				break;
		}
	}

}
