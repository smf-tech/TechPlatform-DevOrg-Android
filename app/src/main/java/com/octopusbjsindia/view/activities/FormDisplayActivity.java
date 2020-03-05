package com.octopusbjsindia.view.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.models.forms.Form;
import com.octopusbjsindia.models.forms.FormData;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.presenter.FormDisplayActivityPresenter;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.ViewPagerAdapter;

import java.util.List;
import java.util.UUID;

public class FormDisplayActivity extends BaseActivity implements APIDataListener {

    private Form formModel;
    private ViewPager vpFormElements;
    private ViewPagerAdapter adapter;
    private List<Elements> formDataArrayList;
    private FormDisplayActivityPresenter presenter;
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

//            Fragment webViewFragment = new FormWebviewFragment();
//            bundle.putString("Weblink", webLink);
//            bundle.putString("FormId", formId);
//            bundle.putSerializable("FormData", formData);
//            webViewFragment.setArguments(bundle);
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fTransaction = fragmentManager.beginTransaction();
//            fTransaction.replace(R.id.viewpager, webViewFragment).addToBackStack(null)
//                    .commit();

//            if (formData == null) {
////                if (Util.isConnected(getContext())) {
////                    formPresenter.getProcessDetails(formId);
////                } else {
////                    view.findViewById(R.id.no_offline_form).setVisibility(View.VISIBLE);
////                    setActionbar("");
////                }
//            } else {
//                formModel = new Form();
//                formModel.setData(formData);
//            }
        }

        webview = findViewById(R.id.webview);
        webview.loadUrl(weblink);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new FormDisplayActivity.WebAppInterface(this), "Android");
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        if (!(Util.isConnected(this)))
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.setWebContentsDebuggingEnabled(true);
        webview.setWebViewClient(new FormDisplayActivity.MyWebViewClient());
        //initView();
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

//    private void setupFormElements(final List<Elements> formDataArrayList, String formId) {
//        for (Elements element : formDataArrayList) {
//            if (element != null && !element.getType().equals("")) {
//                String formDataType = element.getType();
//                Bundle bundle = new Bundle();
//                switch (formDataType) {
//                    case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
//                        Fragment radioButtonFragment = new RadioButtonFragment();
//                        bundle.putSerializable("", element);
//                        radioButtonFragment.setArguments(bundle);
//                        adapter.addFragment(radioButtonFragment, "Question 1");
//                        break;
//
//                    case Constants.FormsFactory.CHECKBOX_TEMPLATE:
//                        Fragment checkboxFragment = new CheckboxFragment();
//                        bundle.putSerializable("", element);
//                        checkboxFragment.setArguments(bundle);
//                        adapter.addFragment(checkboxFragment, "Question 2");
//                        break;
//
//                    case Constants.FormsFactory.MATRIX_DYNAMIC:
//
//                        break;
//                }
//
//
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }

//    public void parseFormSchema(Components components) {
//        if (components == null) {
//            return;
//        }
//        formDataArrayList = components.getPages().get(0).getElements();
//        if (formDataArrayList != null) {
//            setupFormElements(formDataArrayList, formModel.getData().getId());
//        }
//    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }

}
