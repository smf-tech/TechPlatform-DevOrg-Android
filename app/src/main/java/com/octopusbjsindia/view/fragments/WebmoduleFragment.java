package com.octopusbjsindia.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.PermissionHelperWebView;
import com.octopusbjsindia.utility.Util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class WebmoduleFragment extends Fragment {

    private String weblink, webModule_name;
    private WebView webview;
    private LinearProgressIndicator progressBar;
    private static final int FILE_PICKER_REQ_CODE = 19;
    private static final int VIDEO_DURATION = 30;
    private static final String FILE_TYPE = "*/*";

    private boolean webViewAudioEnabled = true;
    private boolean webViewCameraEnabled = true;
    private boolean fileChooserEnabled = true;

    private ValueCallback<Uri> filePickerFileMessage;
    private ValueCallback<Uri[]> filePickerFilePath;
    private String filePickerCamMessage;

    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private String[] permissions_33 = new String[]{Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA};

    public String[] storagePermissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = permissions_33;
        } else {
            p = permissions;
        }
        return p;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webmodule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            weblink = getArguments().getString("Weblink");
            webModule_name = getArguments().getString("Webmodule_name");
        }

        webview = view.findViewById(R.id.webview);
        progressBar = view.findViewById(R.id.progressBar);

        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(requireActivity()), "Android");
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.setVerticalScrollBarEnabled(true);
        webview.setScrollbarFadingEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().supportZoom();
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.OFF);

        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new MyWebViewChromeClient());

        progressBar.setProgressCompat(10, true);

        webview.loadUrl(weblink);
        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (webview.canGoBack()) {
                            webview.goBack();
                        } else {
                            requireActivity().onBackPressed();
                        }
                        return true;
                    }
                }
                return true;
            }
        });

    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context ctx) {
            this.mContext = ctx;
        }

        @JavascriptInterface
        public String getUserId() {
            String userId = Util.getUserObjectFromPref().getId();
            return userId;
        }

        @JavascriptInterface
        public String getAppLang() {
            String lang = Locale.getDefault().getLanguage();
            return lang;
        }

    }

    public class MyWebViewChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgressCompat(progress, true);
            if (progress == 100) {
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            PermissionHelperWebView.CheckPermissions(requireContext(), new PermissionHelperWebView.CheckPermissionListener() {
                @Override
                public void onAllGranted(boolean sync) {
                    callback.invoke(origin, true, true);
                }

                @Override
                public void onPartlyGranted(List<String> permissionsDenied, boolean sync) {
                    callback.invoke(origin, false, false);
                }
            }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }

        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            for (String res : request.getResources()) {
                if (res.equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                    if (!webViewAudioEnabled) {
                        request.deny();
                        return;
                    }
                }
                if (res.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                    if (!webViewCameraEnabled) {
                        request.deny();
                        return;
                    }
                }
            }

            PermissionHelperWebView.CheckPermissions(requireContext(), new PermissionHelperWebView.CheckPermissionListener() {
                @Override
                public void onAllGranted(boolean sync) {
                    request.grant(request.getResources());
                }

                @Override
                public void onPartlyGranted(List<String> permissionsDenied, boolean sync) {
                    request.deny();
                }
            }, parsePermission(request.getResources()));
        }

        //Handling input[type="file"] requests for android API 21+
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, final FileChooserParams fileChooserParams) {

            if (!fileChooserEnabled) {
                filePathCallback.onReceiveValue(null);
                return true;
            }
            if (filePickerFilePath != null) {
                filePickerFilePath.onReceiveValue(null);
            }
            filePickerFilePath = filePathCallback;

            //Checking permission for storage and camera for writing and uploading images
            //String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            PermissionHelperWebView.CheckPermissions(requireContext(), new PermissionHelperWebView.CheckPermissionListener() {
                @Override
                public void onAllGranted(boolean sync) {
                    final Intent takePictureIntent = createCameraCaptureIntent(fileChooserParams.getAcceptTypes());
                    if (fileChooserParams.isCaptureEnabled() && fileChooserParams.getMode() == FileChooserParams.MODE_OPEN) {
                        // capture="camera" and without multiple
                        if (takePictureIntent != null) {
                            startActivityForResult(takePictureIntent, FILE_PICKER_REQ_CODE);
                        } else {
                            if (filePickerFilePath != null) {
                                filePickerFilePath.onReceiveValue(null);
                                filePickerFilePath = null;
                            }
                        }
                        return;
                    }
                    final Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType(FILE_TYPE);
                    contentSelectionIntent.putExtra(Intent.EXTRA_MIME_TYPES, fileChooserParams.getAcceptTypes());
                    Intent[] intentArray;
                    if (takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else {
                        intentArray = new Intent[0];
                    }
                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE,"File Chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    startActivityForResult(chooserIntent, FILE_PICKER_REQ_CODE);
                }

                @Override
                public void onPartlyGranted(List<String> permissionsDenied, boolean sync) {
                    if (filePickerFilePath != null) {
                        filePickerFilePath.onReceiveValue(null);
                        filePickerFilePath = null;
                    }
                }
            }, storagePermissions());
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FILE_PICKER_REQ_CODE) {
                if (null == filePickerFilePath) {
                    return;
                }
                if (intent == null || intent.getDataString() == null) {
                    if (filePickerCamMessage != null) {
                        results = new Uri[]{Uri.parse(filePickerCamMessage)};
                    }
                } else {
                    String dataString = intent.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
        }
        filePickerFilePath.onReceiveValue(results);
        filePickerFilePath = null;
    }

    private Intent createCameraCaptureIntent(String[] mimeTypes) {
        boolean isVideo = false;
        if (mimeTypes != null && mimeTypes.length == 1 && mimeTypes[0] != null && mimeTypes[0].startsWith("video")) {
            isVideo = true;
        }
        Intent takePictureIntent = new Intent(isVideo? MediaStore.ACTION_VIDEO_CAPTURE: MediaStore.ACTION_IMAGE_CAPTURE);

        if (isVideo) takePictureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, VIDEO_DURATION);

        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            File imageVideoFile = null;
            try {
                imageVideoFile = createImageOrVideo(isVideo);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (imageVideoFile != null) {
                filePickerCamMessage = "file:" + imageVideoFile.getAbsolutePath();

                Uri photoUri = FileProvider.getUriForFile(requireContext(),
                        requireContext().getPackageName() + ".file_provider",
                        imageVideoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            } else {
                takePictureIntent = null;
            }
        }
        return takePictureIntent;
    }

    //Creating image or video file for upload
    protected File createImageOrVideo(boolean isVideo) throws IOException {
        @SuppressLint("SimpleDateFormat")
        String file_name = new SimpleDateFormat("yyyy_mm_ss").format(new Date());
        String new_name = "file_" + file_name + "_";
        File sd_directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(new_name, isVideo? ".mp4": ".jpg", sd_directory);
    }

    protected String[] parsePermission(String[] resource) {
        List<String> permissions = new ArrayList<>();
        for (String res : resource) {
            if (res.equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (res.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                permissions.add(Manifest.permission.CAMERA);
            }
        }

        String[] result = new String[permissions.size()];
        for (int i = 0; i < permissions.size(); i++) {
            result[i] = permissions.get(i);
        }
        return result;
    }

}
