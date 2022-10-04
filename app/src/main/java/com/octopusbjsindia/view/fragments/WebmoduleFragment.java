package com.octopusbjsindia.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.Util;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class WebmoduleFragment extends Fragment {

    private String weblink, webModule_name;
    private WebView webview;
    private View webModuleFragmentView;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private int number = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        webModuleFragmentView = inflater.inflate(R.layout.fragment_webmodule, container, false);
        return webModuleFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            weblink = getArguments().getString("Weblink");
            webModule_name = getArguments().getString("Webmodule_name");
        }
        webview = webModuleFragmentView.findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        //settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
        webview.setWebContentsDebuggingEnabled(true);
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new ChromeClient());

        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setAllowContentAccess(true);
        settings.setPluginState(WebSettings.PluginState.OFF);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.supportZoom();

        webview.loadUrl(weblink);

        //settings.setAppCacheEnabled(true);
        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
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

    public class ChromeClient extends WebChromeClient {

        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath,
                                         WebChromeClient.FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;

//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                // Create the File where the photo should go
//                File photoFile = null;
//                try {
//                    if (Permissions.isCameraPermissionGranted(activity, this)) {
//                        photoFile = createImageFile();
//                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
//                    }
//                } catch (IOException ex) {
//                    // Error occurred while creating the File
//                    Log.e("Error", "Unable to create Image File", ex);
//                }
//
//                // Continue only if the File was successfully created
//                if (photoFile != null) {
//                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(photoFile));
//                } else {
//                    takePictureIntent = null;
//                }
//            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
//            if (takePictureIntent != null) {
//                intentArray = new Intent[]{takePictureIntent};
//            } else {
            intentArray = new Intent[0];
            //}

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;

        }

        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            mUploadMessage = uploadMsg;
            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , "Octopus");

            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }

//            // Create camera captured image file path and name
//            File file = new File(
//                    imageStorageDir + File.separator + "IMG_"
//                            + String.valueOf(System.currentTimeMillis())
//                            + ".jpg");
//
//            mCapturedImageURI = Uri.fromFile(file);


            File file = null;
            try {
                String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/Octopus/Image/picture" + number + ".jpg";
                file = new File(imageFilePath);
                number++;
            } catch (Exception ex) {
                Log.e("Exception", "Unable to create Image File", ex);
            }

            mCapturedImageURI = Uri.fromFile(file);

            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");

            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[]{captureIntent});

            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
        }

        // openFileChooser for Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        //openFileChooser for other Android versions
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {

            openFileChooser(uploadMsg, acceptType);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        Uri[] results = null;
        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                // If there is not data, then we may have taken a photo
                if (mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File imageFile = null;
        try {
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Octopus/Image/picture" + number + ".jpg";
            imageFile = new File(imageFilePath);
            number++;
        } catch (Exception ex) {
            Log.e("Exception", "Unable to create Image File", ex);
        }
        return imageFile;
    }
}

//    private class MyWebViewClient extends WebViewClient {
//        boolean timeout = true;
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            // TODO Auto-generated method stub
//            super.onPageStarted(view, url, favicon);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            timeout = false;
//        }
//
//        @SuppressWarnings("deprecation")
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            final Uri uri = Uri.parse(url);
//            return true;
//        }
//
//        @TargetApi(Build.VERSION_CODES.N)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            view.loadUrl(request.getUrl().toString());
//            return true;
//        }
//
//        @Override
//        public void onReceivedError(WebView view, int errorCode,
//                                    String description, String failingUrl) {
//
//            //Log.i("Error", "GOT Page error : code : " + errorCode + " Desc : " + description);
//            //showError(errorCode);
//            //TODO We can show customized HTML page when page not found/ or server not found error.
//            super.onReceivedError(view, errorCode, description, failingUrl);
//        }
//    }

//    public class ChromeClient extends WebChromeClient {
//        // For Android 5.0
//        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath,
//                                         WebChromeClient.FileChooserParams fileChooserParams) {
//            // Double check that we don't have any existing callbacks
//            if (mFilePathCallback != null) {
//                mFilePathCallback.onReceiveValue(null);
//            }
//            mFilePathCallback = filePath;
//
//            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//            contentSelectionIntent.setType("image/*");
//
//            Intent[] intentArray;
//            intentArray = new Intent[0];
//
//            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//            chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
//
//            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
//
//            return true;
//
//        }
//
//        // openFileChooser for Android 3.0+
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//
//            mUploadMessage = uploadMsg;
//            File imageStorageDir = new File(
//                    Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_PICTURES)
//                    , "Seva");
//
//            if (!imageStorageDir.exists()) {
//                // Create AndroidExampleFolder at sdcard
//                imageStorageDir.mkdirs();
//            }
//
//            File file = null;
//            try {
//                String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
//                        + "/Seva/Image/picture"+number+".jpg";
//                file = new File(imageFilePath);
//                number++;
//            } catch (Exception ex) {
//                Log.e("Exception", "Unable to create Image File", ex);
//            }
//
//            mCapturedImageURI = Uri.fromFile(file);
//
//            // Camera capture image intent
//            final Intent captureIntent = new Intent(
//                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
//
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");
//
//            // Create file chooser intent
//            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
//
//            // Set camera intent to file chooser
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
//                    , new Parcelable[] { captureIntent });
//
//            // On select image call onActivityResult method of activity
//            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
//        }
//
//        // openFileChooser for Android < 3.0
//        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//            openFileChooser(uploadMsg, "");
//        }
//
//        //openFileChooser for other Android versions
//        public void openFileChooser(ValueCallback<Uri> uploadMsg,
//                                    String acceptType,
//                                    String capture) {
//
//            openFileChooser(uploadMsg, acceptType);
//        }
//
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
//                super.onActivityResult(requestCode, resultCode, data);
//                return;
//            }
//            Uri[] results = null;
//            // Check that the response is a good one
//            if (resultCode == RESULT_OK) {
//                if (data == null) {
//                    // If there is not data, then we may have taken a photo
//                    if (mCameraPhotoPath != null) {
//                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
//                    }
//                } else {
//                    String dataString = data.getDataString();
//                    if (dataString != null) {
//                        results = new Uri[]{Uri.parse(dataString)};
//                    }
//                }
//            }
//
//            mFilePathCallback.onReceiveValue(results);
//            mFilePathCallback = null;
//        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
//                super.onActivityResult(requestCode, resultCode, data);
//                return;
//            }
//            if (requestCode == FILECHOOSER_RESULTCODE) {
//                if (null == this.mUploadMessage) {
//                    return;
//                }
//                Uri result = null;
//                try {
//                    if (resultCode != RESULT_OK) {
//                        result = null;
//                    } else {
//                        // retrieve from the private variable if the intent is null
//                        result = data == null ? mCapturedImageURI : data.getData();
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(getActivity(), "activity :" + e,
//                            Toast.LENGTH_LONG).show();
//                }
//                mUploadMessage.onReceiveValue(result);
//                mUploadMessage = null;
//            }
//        }
//        return;
//    }
//}
