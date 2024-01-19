package com.octopusbjsindia.view.activities

import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.octopusbjsindia.BuildConfig
import com.octopusbjsindia.R
import com.octopusbjsindia.databinding.ActivityDonationRecordBinding
import com.octopusbjsindia.listeners.APIDataListener
import com.octopusbjsindia.listeners.CustomSpinnerListener
import com.octopusbjsindia.models.SujalamSuphalam.RWBDistrictDonor
import com.octopusbjsindia.models.SujalamSuphalam.RWBDistrictDonorsAPIResponse
import com.octopusbjsindia.models.common.CustomSpinnerObject
import com.octopusbjsindia.models.events.CommonResponse
import com.octopusbjsindia.presenter.RWBDonationRecordPresenter
import com.octopusbjsindia.utility.Constants
import com.octopusbjsindia.utility.Permissions
import com.octopusbjsindia.utility.Urls
import com.octopusbjsindia.utility.Util
import com.octopusbjsindia.utility.Util.hideKeyboard
import com.octopusbjsindia.utility.VolleyMultipartRequest
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DonationRecordActivity : AppCompatActivity(), APIDataListener, CustomSpinnerListener {

    private lateinit var binding: ActivityDonationRecordBinding
    private var presenter: RWBDonationRecordPresenter? = null

    private var structureDistrictId: String? = null
    private var structureDistrict: String? = null
    private var structureId: String? = null
    private var structureCode: String? = null

    private var donorsList = ArrayList<RWBDistrictDonor>()
    private var selectedDonor = RWBDistrictDonor()
    private val donorsListCustomObject = ArrayList<CustomSpinnerObject>()
    private var selectedDonorId: String? = null
    private var selectedDonorName: String? = null

    private var transactionPhotoBitmap: Bitmap? = null
    private var currentPhotoPath: String? = null
    private var finalUri: Uri? = null
    private var outputUri: Uri? = null

    private var apiType: String = API_COMMITMENT

    private var rQueue: RequestQueue? = null

    companion object {
        const val API_COMMITMENT = "api_commitment"
        const val API_DONATION = "api_donation"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        structureDistrictId = intent.getStringExtra("districtId")
        structureDistrict = intent.getStringExtra("district")
        structureId = intent.getStringExtra("structureId")
        structureCode = intent.getStringExtra("structureCode")

        presenter = RWBDonationRecordPresenter(this)

        presenter?.getDistrictDonorsList(structureDistrictId!!, structureId!!)

        initViews()
        clickListener()
    }

    private fun initViews() {
        binding.apply {
            tvStructureCode.text = structureCode
            tvDistrict.text = structureDistrict

            //set adapter
            createSetAdapterToAutoCompleteTextView(R.array.payment_mode, binding.etPaymentMode)
            createSetAdapterToAutoCompleteTextView(R.array.payment_type, binding.etPaymentType)
        }
    }

    private fun clickListener() {
        binding.apply {

            titleCommitment.setOnClickListener {
                if (!selectedDonorId.isNullOrBlank()) {
                    lytDonationCommitment.isVisible = !lytDonationCommitment.isVisible
                    lytDonationDetail.isVisible = false
                } else {
                    Snackbar.make(binding.root, "Select Donor first", Snackbar.LENGTH_SHORT).show()
                }
            }
            titleDonation.setOnClickListener {
                if (!selectedDonorId.isNullOrBlank()) {
                    lytDonationDetail.isVisible = !lytDonationDetail.isVisible
                    lytDonationCommitment.isVisible = false
                } else {
                    Snackbar.make(binding.root, "Select Donor first", Snackbar.LENGTH_SHORT).show()
                }

            }

            etSelectDonor.setOnClickListener {
                val csdDonor = CustomSpinnerDialogClass(
                    this@DonationRecordActivity, this@DonationRecordActivity,
                    "Select Donor", donorsListCustomObject, false
                )
                csdDonor.show()
                csdDonor.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            etPaymentMode.setOnClickListener {
                hideKeyboard(it)
            }

            etDate.setOnClickListener {
                showDateDialog(this@DonationRecordActivity, etDate)
            }

            imgTransactionDetailCopy.setOnClickListener {
                onAddImageClick()
            }

            etCommitment.doAfterTextChanged {
                if (it.toString().isNotEmpty()) {
                    setAmountCalculation(it.toString().toInt())
                }
            }

            etPaymentMode.doAfterTextChanged {
                if (it.toString().isNotEmpty()) {
                    if (it.toString().equals("NEFT", true) ||
                        it.toString().equals("RTGS", true)
                    ) {
                        ilTransactionId.isEnabled = true
                        ilChequeNo.isEnabled = false
                    } else {
                        ilTransactionId.isEnabled = false
                        ilChequeNo.isEnabled = true
                    }
                }
            }

            btCommitment.setOnClickListener {
                if (!etCommitment.text.isNullOrBlank()) {
                    val gson = GsonBuilder().create()
                    val map = HashMap<String, String>()
                    map["donor_id"] = selectedDonor.donorId
                    map["structure_id"] = structureId.toString()
                    map["commitment_amount"] = etCommitment.text.toString()
                    val paramJson = gson.toJson(map)

                    apiType = API_COMMITMENT
                    uploadData(
                        paramJson,
                        binding.btCommitment,
                        BuildConfig.BASE_URL + Urls.SSModule.ADD_DONOR_COMMITMENT,
                        0
                    )
                } else {
                    Snackbar.make(
                        binding.root,
                        "Please enter commitment amount",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }

            btSubmitDonation.setOnClickListener {
                if (binding.etDate.text.isNullOrBlank()) {
                    Snackbar.make(
                        binding.root, "Please select transaction date",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (binding.etAmountReceived.text.isNullOrBlank()) {
                    Snackbar.make(
                        binding.root, "Please enter amount received",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (binding.etPaymentMode.text.isNullOrBlank()) {
                    Snackbar.make(
                        binding.root, "Please select payment mode",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (binding.ilChequeNo.isEnabled && binding.etChequeNo.text.isNullOrBlank()) {
                    Snackbar.make(
                        binding.root, "Please enter cheque/DD number",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (binding.ilTransactionId.isEnabled && binding.etTransactionId.text.isNullOrBlank()) {
                    Snackbar.make(
                        binding.root, "Please enter transaction ID",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (binding.etPaymentType.text.isNullOrBlank()) {
                    Snackbar.make(
                        binding.root, "Please select payment type",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (transactionPhotoBitmap == null) {
                    Snackbar.make(
                        binding.root, "Please add transaction detail photo",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val gson = GsonBuilder().create()
                    val map = HashMap<String, String>()
                    map["donor_id"] = selectedDonor.donorId
                    map["structure_id"] = structureId.toString()
                    map["donation_amount"] = etAmountReceived.text.toString()
                    map["transaction_date"] = etDate.text.toString()
                    map["payment_mode"] = etPaymentMode.text.toString()
                    if (ilChequeNo.isEnabled)
                        map["cheque_dd_no"] = etChequeNo.text.toString()
                    if (ilTransactionId.isEnabled)
                        map["trasaction_id"] = etTransactionId.text.toString()
                    map["payment_type"] = etPaymentType.text.toString()
                    val paramJson = gson.toJson(map)

                    apiType = API_DONATION
                    uploadData(
                        paramJson,
                        binding.btSubmitDonation,
                        BuildConfig.BASE_URL + Urls.SSModule.ADD_DONOR_DONATION_DETAIL,
                        1
                    )

                }

            }


        }
    }

    private fun uploadData(
        paramsJson: String,
        btSubmit: MaterialButton,
        url: String,
        imageArraySize: Int
    ) {
        btSubmit.isEnabled = false
        binding.lytProgressBar.isVisible = true
        Log.d("request -", paramsJson)

        val upload_URL = url
        //BuildConfig.BASE_URL + Urls.SSModule.INSERT_RWB_DONORS

        val volleyMultipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(
            Method.POST, upload_URL,
            Response.Listener<NetworkResponse> { response ->
                btSubmit.isEnabled = true
                binding.lytProgressBar.visibility = View.INVISIBLE
                rQueue?.cache?.clear()
                try {
                    val jsonString = String(
                        response.data,
                        charset(HttpHeaderParser.parseCharset(response.headers))
                    )
                    Log.d("response -", jsonString)
                    val commonResponse = Gson().fromJson(jsonString, CommonResponse::class.java)
                    if (commonResponse.status == 200) {
                        Toast.makeText(this, commonResponse.message, Toast.LENGTH_SHORT).show()
                        //todo
                        if (apiType == API_COMMITMENT) {
                            //dibLE edit text and remove submit button
                            binding.etCommitment.isEnabled = false
                            binding.btCommitment.isVisible = false
                            presenter?.getDistrictDonorsList(structureDistrictId!!, structureId!!)
                        } else if (apiType == API_DONATION) {
                            //clear all edit text field
                            finish()
                        }
                    } else {
                        Toast.makeText(this, commonResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                btSubmit.isEnabled = true
                binding.lytProgressBar.visibility = View.INVISIBLE
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["formData"] = paramsJson
                params["imageArraySize"] = imageArraySize.toString() //add string parameters
                Log.d("TAG", "Form params: $params")
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Accept"] = "application/json, text/plain, */*"
                headers["Content-Type"] = bodyContentType
                val loginObj = Util.getLoginObjectFromPref()
                if (loginObj != null && loginObj.loginData != null && loginObj.loginData.accessToken != null) {
                    headers[Constants.Login.AUTHORIZATION] =
                        "Bearer " + loginObj.loginData.accessToken
                    if (Util.getUserObjectFromPref().orgId != null) {
                        headers["orgId"] = Util.getUserObjectFromPref().orgId
                    }
                    if (Util.getUserObjectFromPref().projectIds != null) {
                        headers["projectId"] = Util.getUserObjectFromPref().projectIds[0].id
                    }
                    if (Util.getUserObjectFromPref().roleIds != null) {
                        headers["roleId"] = Util.getUserObjectFromPref().roleIds
                    }
                }
                return headers
            }

            override fun getByteData(): Map<String, DataPart> {
                val params: MutableMap<String, DataPart> = HashMap()
                var drawable: Drawable?

                if (imageArraySize > 0) {
                    drawable = BitmapDrawable(resources, transactionPhotoBitmap)
                    params["attachment0"] =
                        DataPart(
                            "attachment0",
                            Util.getFileDataFromDrawable(drawable),
                            "image/jpeg"
                        )
                }
                Log.d("TAG", "getByteData: $params")
                return params
            }
        }
        volleyMultipartRequest.setRetryPolicy(
            DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        rQueue = Volley.newRequestQueue(this)
        rQueue?.add(volleyMultipartRequest)
    }

    fun populateDistrictDonorList(requestID: String?, data: RWBDistrictDonorsAPIResponse?) {
        if (data != null) {

            donorsList = data.data as ArrayList<RWBDistrictDonor>
            donorsListCustomObject.clear()
            for (i in data.data) {
                val customDonor = CustomSpinnerObject()
                customDonor._id = i.donorId
                customDonor.name = "${i.fullName} | ${i.mobileNumber}"
                donorsListCustomObject.add(customDonor)
            }
        } else {
            showNoDataMessage()
        }
    }

    private fun showDateDialog(context: Context, editText: EditText) {
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]
        val dateDialog = DatePickerDialog(
            context, { picker, year, monthOfYear, dayOfMonth ->
                val date = year.toString() + "-" + Util.getTwoDigit(monthOfYear + 1) + "-" +
                        Util.getTwoDigit(dayOfMonth)
                editText.setText(date)
            }, mYear, mMonth, mDay
        )
        dateDialog.datePicker.maxDate = System.currentTimeMillis()
        dateDialog.show()
    }

    private fun onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(this, this)) {
            showPictureDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                finalUri = Uri.fromFile(File(currentPhotoPath))
                Util.openCropActivityFreeCrop(this, finalUri, finalUri)
            } catch (e: Exception) {
                Log.e("TAG", e.message!!)
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile()
                    outputUri = data.data
                    finalUri = Uri.fromFile(File(currentPhotoPath))
                    Util.openCropActivityFreeCrop(this, outputUri, finalUri)
                } catch (e: Exception) {
                    Log.e("TAG", e.message!!)
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                val resultUri = UCrop.getOutput(data)
                val imageFile = File(resultUri?.path)
                val compressedImageFile = Util.compressFile(imageFile)
                if (Util.isConnected(this)) {
                    if (Util.isValidImageSize(compressedImageFile)) {
                        //selectedIV.setImageURI(resultUri)
                        val bitmap = Util.compressImageToBitmap(imageFile)
                        transactionPhotoBitmap = bitmap
                        binding.imgTransactionDetailCopy.setImageBitmap(bitmap)
                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this)
                    }
                } else {
                    Util.showToast(resources.getString(R.string.msg_no_network), this)
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
        }
    }


    private fun showPictureDialog() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.title_choose_picture))
        val items: Array<String> =
            arrayOf(getString(R.string.label_gallery), getString(R.string.label_camera))
        dialog.setItems(items) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        dialog.show()
    }

    private fun choosePhotoFromGallery() {
        try {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY)
        } catch (e: ActivityNotFoundException) {
            Util.showToast(getString(R.string.msg_error_in_photo_gallery), this)
        }
    }

    private fun takePhotoFromCamera() {
        try {
            val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file: File? = getImageFile() // 1
            val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".file_provider",
                    file!!
                ) else Uri.fromFile(file) // 3
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri) // 4
            startActivityForResult(pictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA)
        } catch (e: ActivityNotFoundException) {
            //display an error message
            Toast.makeText(
                this, resources.getString(R.string.msg_image_capture_not_support),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: SecurityException) {
            Toast.makeText(
                this, resources.getString(R.string.msg_take_photo_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getImageFile(): File? {
        // External sdcard location
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            Constants.Image.IMAGE_STORAGE_DIRECTORY
        )
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file: File
        file = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        currentPhotoPath = file.path
        return file
    }


    fun showNoDataMessage() {
        Util.showToast("No donor available for your/selected district.", this)
    }

    override fun onFailureListener(requestID: String?, message: String?) {
        Util.showToast(message.toString(), this)
    }

    override fun onErrorListener(requestID: String?, error: VolleyError?) {
        Util.showToast(error?.message.toString(), this)
    }

    override fun onSuccessListener(requestID: String?, response: String?) {
    }

    override fun showProgressBar() {
        runOnUiThread {
            binding?.apply {
                lytProgressBar?.apply {
                    isVisible = true
                }
            }
        }
    }

    override fun hideProgressBar() {
        runOnUiThread {
            binding?.apply {
                lytProgressBar?.apply {
                    isVisible = false
                }
            }
        }
    }

    override fun closeCurrentActivity() {
        finish()
    }

    private fun createSetAdapterToAutoCompleteTextView(
        arrayRes: Int,
        autoCompleteTextView: AutoCompleteTextView
    ) {
        val arrayString = resources.getStringArray(arrayRes)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_list_item, arrayString)
        autoCompleteTextView.setAdapter(arrayAdapter)

        autoCompleteTextView.setOnItemClickListener { _, _, _, _ ->
            hideKeyboard(autoCompleteTextView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (presenter != null) {
            presenter = null
        }
    }

    override fun onCustomSpinnerSelection(type: String?) {
        when (type) {
            "Select Donor" -> {
                for (obj in donorsListCustomObject) {
                    if (obj.isSelected) {
                        selectedDonorName = obj.name
                        selectedDonorId = obj._id
                        break
                    }
                }
                binding.lytDonationCommitment.isVisible = true
                binding.etSelectDonor.setText(selectedDonorName)
                binding.lytDonationCommitment.isVisible = true
                binding.lytDonationDetail.isVisible = false

                //get selected donor data
                for (data in donorsList) {
                    if (selectedDonorId == data.donorId) {
                        selectedDonor = data
                    }
                }
                Log.d(
                    "TAG",
                    "Selected Donor Data: ${selectedDonor.fullName}, ${selectedDonor.mobileNumber}, ${selectedDonor.commitment.commitmentAmount}, ${selectedDonor.donorId}"
                )
                //set commitment value if available
                if (!selectedDonor.commitment?.commitmentAmount.isNullOrEmpty()) {
                    binding.apply {
                        etCommitment.setText(selectedDonor.commitment.commitmentAmount)
                        etCommitment.isEnabled = false
                        btCommitment.isVisible = false
                        setAmountCalculation(selectedDonor.commitment.commitmentAmount.toInt())
                    }
                } else {
                    binding.apply {
                        etCommitment.setText("")
                        setAmountCalculation(0)
                        etCommitment.isEnabled = true
                        btCommitment.isVisible = true
                    }
                }

            }
        }
    }

    private fun setAmountCalculation(amount: Int?) {
        binding.apply {
            val workScope = amount?.div(45)
            val backhoeHr = workScope?.div(45)
            val excavatorHr = workScope?.div(100)

            tvWorkScope.text = workScope.toString()
            tvBhHours.text = backhoeHr.toString()
            tvExHours.text = excavatorHr.toString()

        }
    }

}