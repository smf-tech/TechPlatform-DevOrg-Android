package com.octopusbjsindia.view.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.LocaleData;
import com.octopusbjsindia.models.forms.FormData;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Util;

import java.util.UUID;

public class FormWebviewFragment extends Fragment {

    private String weblink, formId, formName;
    private WebView webview;
    private FormData formData;
    private LocaleData localeData;
    private View webModuleFragmentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        webModuleFragmentView = inflater.inflate(R.layout.fragment_form_webview, container, false);
        return webModuleFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            weblink = getArguments().getString("Weblink");
            formId = getArguments().getString("FormId");
            formData = (FormData) getArguments().getSerializable("FormData");
        }
        webview = webModuleFragmentView.findViewById(R.id.webview);
        webview.loadUrl(weblink);
        WebSettings settings = webview.getSettings();
        //settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new FormWebviewFragment.WebAppInterface(getActivity()), "Android");
        settings.setDomStorageEnabled(true);
        //settings.setAppCacheEnabled(true);
        if (!(Util.isConnected(getActivity())))
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.setWebContentsDebuggingEnabled(true);
        webview.setWebViewClient(new FormWebviewFragment.MyWebViewClient());
    }

    public class WebAppInterface {
        Context mContext;
        String data;

        WebAppInterface(Context ctx) {
            this.mContext = ctx;
        }

        @JavascriptInterface
        public void sendData(String data) {
            //Get the string value to process
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
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

            DatabaseManager.getDBInstance(getActivity()).insertFormResult(result);
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

        //@TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}
