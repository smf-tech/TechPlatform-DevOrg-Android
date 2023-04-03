package com.octopusbjsindia.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.*
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.octopusbjsindia.R
import com.octopusbjsindia.utility.PermissionHelperWebView
import com.octopusbjsindia.utility.Util
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class WebModuleFragment : Fragment() {

    companion object {
        const val FILE_PICKER_REQ_CODE = 19
        const val VIDEO_DURATION = 30
    }

    private var webViewAudioEnabled = true
    private var webViewCameraEnabled = true
    private var fileChooserEnabled = true

    private var filePickerFileMessage: ValueCallback<Uri>? = null
    private var filePickerFilePath: ValueCallback<Array<Uri>>? = null
    private var filePickerCamMessage: String? = null
    private var FILE_TYPE = "*/*"

    var permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var permissions_33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.CAMERA
    )

    fun storagePermissions(): Array<String> {
        val p: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions_33
        } else {
            permissions
        }
        return p
    }

    private lateinit var webview: WebView
    private lateinit var progressBar: LinearProgressIndicator
    private var weblink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webmodule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            weblink = requireArguments().getString("Weblink")
        }

        webview = view.findViewById(R.id.webview)
        progressBar = view.findViewById(R.id.progressBar)

        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webview.settings.loadsImagesAutomatically = true
        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(WebAppInterface(requireActivity()), "Android")
        webview.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        webview.settings.loadWithOverviewMode = true
        webview.settings.useWideViewPort = true
        webview.isVerticalScrollBarEnabled = true
        webview.isScrollbarFadingEnabled = true
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.settings.allowFileAccess = true
        webview.settings.setSupportZoom(true)
        webview.settings.allowContentAccess = true
        webview.settings.pluginState = WebSettings.PluginState.OFF

        webview.webViewClient = WebViewClient()
        webview.webChromeClient = MyWebViewChromeClient()

        progressBar.setProgressCompat(10, true)

        webview.loadUrl(weblink!!)
        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webview.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_BACK -> {
                            if (webview.canGoBack()) {
                                webview.goBack()
                            } else {
                                activity?.onBackPressed()
                            }
                            return true
                        }
                    }
                }
                return true
            }
        })
    }

    inner class WebAppInterface internal constructor(var mContext: Context) {

        @get:JavascriptInterface
        val userId: String
            get() = Util.getUserObjectFromPref().id

        @get:JavascriptInterface
        val appLang: String
            get() = Locale.getDefault().language
    }

   /* inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view!!.loadUrl(request!!.url.toString())
            return true
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
        }
    }*/

    inner class MyWebViewChromeClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView, progress: Int) {
            progressBar.setProgressCompat(progress, true)
            if (progress == 100) {
                progressBar.visibility = View.INVISIBLE
            } else {
                progressBar.visibility = View.VISIBLE
            }
        }

        override fun onGeolocationPermissionsShowPrompt(
            origin: String?,
            callback: GeolocationPermissions.Callback
        ) {
            PermissionHelperWebView.CheckPermissions(
                requireContext(),
                object :
                    PermissionHelperWebView.CheckPermissionListener {
                    override fun onAllGranted(sync: Boolean) {
                        callback.invoke(origin, true, true)
                    }

                    override fun onPartlyGranted(permissionsDenied: List<String?>?, sync: Boolean) {
                        callback.invoke(origin, false, false)
                    }
                },
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        override fun onPermissionRequest(request: PermissionRequest) {
            for (res in request.resources) {
                /* if (res == PermissionRequest.RESOURCE_AUDIO_CAPTURE) {
                     if (!webViewAudioEnabled) {
                         request.deny()
                         return
                     }
                 }*/
                if (res == PermissionRequest.RESOURCE_VIDEO_CAPTURE) {
                    if (!webViewCameraEnabled) {
                        request.deny()
                        return
                    }
                }
            }

            PermissionHelperWebView.CheckPermissions(
                requireContext(), object : PermissionHelperWebView.CheckPermissionListener {
                    override fun onAllGranted(sync: Boolean) {
                        request.grant(request.resources)
                    }

                    override fun onPartlyGranted(permissionsDenied: List<String?>?, sync: Boolean) {
                        request.deny()
                    }
                }, *parsePermission(request.resources)
            )
        }

        //Handling input[type="file"] requests for android API 16+
        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
            // handler.sendEmptyMessage(MSG_CLICK_ON_URL)
            if (!fileChooserEnabled) {
                uploadMsg.onReceiveValue(null)
                return
            }
            filePickerFileMessage = uploadMsg
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = acceptType
            startActivityForResult(
                Intent.createChooser(i, "File Chooser"), FILE_PICKER_REQ_CODE
            )
        }

        //Handling input[type="file"] requests for android API 21+
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            if (!fileChooserEnabled) {
                filePathCallback.onReceiveValue(null)
                return true
            }
            filePickerFilePath?.onReceiveValue(null)
            filePickerFilePath = filePathCallback

            //Checking permission for storage and camera for writing and uploading images
            PermissionHelperWebView.CheckPermissions(
                requireContext(),
                object : PermissionHelperWebView.CheckPermissionListener {
                    override fun onAllGranted(sync: Boolean) {
                        val takePictureIntent: Intent =
                            createCameraCaptureIntent(fileChooserParams.acceptTypes)!!
                        if (fileChooserParams.isCaptureEnabled && fileChooserParams.mode == FileChooserParams.MODE_OPEN) {
                            // capture="camera" and without multiple
                            startActivityForResult(takePictureIntent, FILE_PICKER_REQ_CODE)
                            return
                        }
                        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                        contentSelectionIntent.type = FILE_TYPE
                        contentSelectionIntent.putExtra(
                            Intent.EXTRA_MIME_TYPES,
                            fileChooserParams.acceptTypes
                        )
                        val intentArray: Array<Intent?> =
                            arrayOf(takePictureIntent) ?: arrayOfNulls(0)
                        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                        chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser")
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                        startActivityForResult(chooserIntent, FILE_PICKER_REQ_CODE)
                    }

                    override fun onPartlyGranted(permissionsDenied: List<String>, sync: Boolean) {
                        if (filePickerFilePath != null) {
                            filePickerFilePath!!.onReceiveValue(null)
                            filePickerFilePath = null
                        }
                    }
                },
                *storagePermissions()
            )
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        var results: Array<Uri>? = null
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == FILE_PICKER_REQ_CODE) {
                if (null == filePickerFilePath) {
                    return
                }
                if (intent == null || intent.dataString == null) {
                    if (filePickerCamMessage != null) {
                        results = arrayOf(Uri.parse(filePickerCamMessage))
                    }
                } else {
                    val dataString = intent.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
        }
        filePickerFilePath?.onReceiveValue(results)
        filePickerFilePath = null
    }

    private fun createCameraCaptureIntent(mimeTypes: Array<String>?): Intent? {
        var isVideo = false
        if (mimeTypes != null && mimeTypes.isNotEmpty() && mimeTypes[0].startsWith("video")) {
            isVideo = true
        }

        var takePictureIntent: Intent? =
            Intent(if (isVideo) MediaStore.ACTION_VIDEO_CAPTURE else MediaStore.ACTION_IMAGE_CAPTURE)

        if (isVideo) takePictureIntent?.putExtra(MediaStore.EXTRA_DURATION_LIMIT, VIDEO_DURATION);

        if (takePictureIntent!!.resolveActivity(requireContext().packageManager) != null) {
            var imageVideoFile: File? = null
            try {
                imageVideoFile = createImageOrVideo(isVideo)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            if (imageVideoFile != null) {
                filePickerCamMessage = "file:" + imageVideoFile.absolutePath
                val photoUri = FileProvider.getUriForFile(requireContext(), "${context?.packageName}.file_provider", imageVideoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            } else {
                takePictureIntent = null
            }
        }
        return takePictureIntent
    }

    @Throws(IOException::class)
    fun createImageOrVideo(isVideo: Boolean): File? {
        @SuppressLint("SimpleDateFormat")
        val file_name = SimpleDateFormat("yyyy_mm_ss").format(Date())
        val new_name = "file_" + file_name + "_"
        val sd_directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(new_name, if (isVideo) ".mp4" else ".jpg", sd_directory)
    }

    fun parsePermission(resource: Array<String>): Array<String?> {
        val permissions: MutableList<String> = ArrayList()
        for (res in resource) {
            /*if (res == PermissionRequest.RESOURCE_AUDIO_CAPTURE) {
                permissions.add(Manifest.permission.RECORD_AUDIO)
            }*/
            if (res == PermissionRequest.RESOURCE_VIDEO_CAPTURE) {
                permissions.add(Manifest.permission.CAMERA)
            }
        }
        val result = arrayOfNulls<String>(permissions.size)
        for (i in permissions.indices) {
            result[i] = permissions[i]
        }
        return result
    }

}