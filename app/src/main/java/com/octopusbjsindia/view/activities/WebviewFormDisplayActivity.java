package com.octopusbjsindia.view.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.forms.FormData;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;

import java.util.UUID;

public class WebviewFormDisplayActivity extends BaseActivity {

    private WebView webview;
    private String weblink, formId;
    private FormData formData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_display);
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            String processId = getIntent().getExtras().getString(Constants.PM.PROCESS_ID);
            formId = getIntent().getExtras().getString(Constants.PM.FORM_ID);
            weblink = getIntent().getExtras().getString("Weblink");
            formData = DatabaseManager.getDBInstance(this).getFormSchema(formId);
            boolean isPartialForm = getIntent().getExtras().getBoolean(Constants.PM.PARTIAL_FORM);
            boolean readOnly = getIntent().getExtras().getBoolean(Constants.PM.EDIT_MODE);
        }

        webview = findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebviewFormDisplayActivity.WebAppInterface(this), "Android");
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // Set cache size to 8 mb by default. should be more than enough
        //settings.setAppCacheMaxSize(1024*1024*8);
        // Load the URLs inside the WebView, not in the external web browser
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        if (!(Util.isConnected(this))) {
//            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        }
        webview.setWebContentsDebuggingEnabled(true);
        webview.setWebViewClient(new WebviewFormDisplayActivity.MyWebViewClient());
        webview.setWebChromeClient(new WebChromeClient() {
            //Other methods for your WebChromeClient here, if needed..

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        //webview.clearCache(false);
        webview.loadUrl(weblink);
        //initView();
    }

//    @Override
//    protected void onDestroy() {
//        if (webview != null) {
//            //webview.clearCache(false);
//            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            ((ViewGroup) webview.getParent()).removeView(webview);
//            webview.destroy();
//            webview = null;
//        }
//        super.onDestroy();
//    }

    public class WebAppInterface {
        Context mContext;
        String data;

        WebAppInterface(Context ctx) {
            this.mContext = ctx;
        }

        @JavascriptInterface
        public void sendData(String data) {
            //Get the string value to process
            Util.snackBarToShowMsg(getWindow().getDecorView().findViewById(android.R.id.content),
                    data, Snackbar.LENGTH_LONG);
            FormResult result = new FormResult();
            result.setFormId(formId);
            result.setFormNameLocale(formData.getName());
            result.setFormCategoryLocale(formData.getCategory().getName());
            result.setCreatedAt(Util.getCurrentTimeStamp());
            String locallySavedFormID = UUID.randomUUID().toString();
            result.set_id(locallySavedFormID);
            result.setFormStatus(SyncAdapterUtils.FormStatus.UN_SYNCED);
            result.setRequestObject(data);

            DatabaseManager.getDBInstance(Platform.getInstance()).insertFormResult(result);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
