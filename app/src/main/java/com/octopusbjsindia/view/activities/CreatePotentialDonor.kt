package com.octopusbjsindia.view.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.gson.Gson
import com.octopusbjsindia.BuildConfig
import com.octopusbjsindia.R
import com.octopusbjsindia.databinding.ActivityCreatePotentialDonorBinding
import com.octopusbjsindia.listeners.APIDataListener
import com.octopusbjsindia.listeners.CustomSpinnerListener
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonor
import com.octopusbjsindia.models.common.CustomSpinnerObject
import com.octopusbjsindia.models.events.CommonResponse
import com.octopusbjsindia.models.profile.JurisdictionLocationV3
import com.octopusbjsindia.presenter.CreatePotentialDonorActivityPresenter
import com.octopusbjsindia.utility.Constants
import com.octopusbjsindia.utility.Urls
import com.octopusbjsindia.utility.Util
import com.octopusbjsindia.utility.VolleyMultipartRequest
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass
import java.io.UnsupportedEncodingException

class CreatePotentialDonor : AppCompatActivity(), APIDataListener, CustomSpinnerListener {

    private lateinit var binding: ActivityCreatePotentialDonorBinding

    private var presenter: CreatePotentialDonorActivityPresenter? = null

    private val stateList = ArrayList<CustomSpinnerObject>()
    private val districtList = ArrayList<CustomSpinnerObject>()
    private val talukaList = ArrayList<CustomSpinnerObject>()
    private val villageList = ArrayList<CustomSpinnerObject>()

    private var selectedStateId: String? = null
    private var selectedState: String? = null
    private var selectedDistrictId: String? = null
    private var selectedDistrict: String? = null
    private var selectedTalukaId: String? = null
    private var selectedTaluka: String? = null

    private var userStateIds = ""
    private var userDistrictIds = ""
    private var userTalukaIds = ""

    private var isStateFilter = false
    private var isDistrictFilter = false
    private var isTalukaFilter = false

    private var rQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePotentialDonorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        initView()
        clickListener()
    }

    private fun initView() {
        val roleAccessAPIResponse = Util.getRoleAccessObjectFromPref()
        val roleAccessList = roleAccessAPIResponse.data
        if (roleAccessList != null) {
            val roleAccessObjectList = roleAccessList.roleAccess
            for (roleAccessObject in roleAccessObjectList) {
                if (roleAccessObject.actionCode == Constants.SSModule.ACCESS_CODE_STATE) {
                    isStateFilter = true
                    continue
                } else if (roleAccessObject.actionCode == Constants.SSModule.ACCESS_CODE_DISTRICT) {
                    isDistrictFilter = true
                    continue
                } else if (roleAccessObject.actionCode == Constants.SSModule.ACCESS_CODE_TALUKA) {
                    isTalukaFilter = true
                    continue
                }
            }
        }

        if (Util.getUserObjectFromPref().userLocation.stateId != null) {
            for (i in Util.getUserObjectFromPref().userLocation.stateId.indices) {
                val j = Util.getUserObjectFromPref().userLocation.stateId[i]
                if (i == 0) {
                    userStateIds = j.id
                } else {
                    userStateIds = userStateIds + "," + j.id
                }
            }
        }

        if (Util.getUserObjectFromPref().userLocation.districtIds != null) {
            for (i in Util.getUserObjectFromPref().userLocation.districtIds.indices) {
                val j = Util.getUserObjectFromPref().userLocation.districtIds[i]
                if (i == 0) {
                    userDistrictIds = j.id
                } else {
                    userDistrictIds = userDistrictIds + "," + j.id
                }
            }
        }

        if (Util.getUserObjectFromPref().userLocation.talukaIds != null) {
            for (i in Util.getUserObjectFromPref().userLocation.talukaIds.indices) {
                val j = Util.getUserObjectFromPref().userLocation.talukaIds[i]
                if (i == 0) {
                    userTalukaIds = j.id
                } else {
                    userTalukaIds = userTalukaIds + "," + j.id
                }
            }
        }

        if (Util.getUserObjectFromPref().userLocation.stateId != null &&
            Util.getUserObjectFromPref().userLocation.stateId.size > 0
        ) {
            selectedState = Util.getUserObjectFromPref().userLocation.stateId[0].name
            binding.etState.setText(selectedState)
            selectedStateId = Util.getUserObjectFromPref().userLocation.stateId[0].id
        }
        if (Util.getUserObjectFromPref().userLocation.districtIds != null &&
            Util.getUserObjectFromPref().userLocation.districtIds.size > 0
        ) {
            selectedDistrict = Util.getUserObjectFromPref().userLocation.districtIds[0].name
            binding.etDistrict.setText(selectedDistrict)
            selectedDistrictId = Util.getUserObjectFromPref().userLocation.districtIds[0].id
        }
        if (Util.getUserObjectFromPref().userLocation.talukaIds != null &&
            Util.getUserObjectFromPref().userLocation.talukaIds.size > 0
        ) {
            selectedTaluka = Util.getUserObjectFromPref().userLocation.talukaIds[0].name
            binding.etTaluka.setText(selectedTaluka)
            selectedTalukaId = Util.getUserObjectFromPref().userLocation.talukaIds[0].id
        }

        if (!isStateFilter) {
            if (Util.getUserObjectFromPref().userLocation.stateId.size > 1) {
                stateList.clear()
                for (i in Util.getUserObjectFromPref().userLocation.stateId.indices) {
                    val customState = CustomSpinnerObject()
                    customState._id = Util.getUserObjectFromPref().userLocation.stateId[i].id
                    customState.name = Util.getUserObjectFromPref().userLocation.stateId[i].name
                    stateList.add(customState)
                }
            }
        }
        if (!isDistrictFilter) {
            if (Util.getUserObjectFromPref().userLocation.districtIds.size > 1) {
                districtList.clear()
                for (i in Util.getUserObjectFromPref().userLocation.districtIds.indices) {
                    val customDistrict = CustomSpinnerObject()
                    customDistrict._id = Util.getUserObjectFromPref().userLocation.districtIds[i].id
                    customDistrict.name =
                        Util.getUserObjectFromPref().userLocation.districtIds[i].name
                    districtList.add(customDistrict)
                }
            }
        }
        if (!isTalukaFilter) {
            if (Util.getUserObjectFromPref().userLocation.talukaIds.size > 1) {
                talukaList.clear()
                for (i in Util.getUserObjectFromPref().userLocation.talukaIds.indices) {
                    val customTaluka = CustomSpinnerObject()
                    customTaluka._id = Util.getUserObjectFromPref().userLocation.talukaIds[i].id
                    customTaluka.name = Util.getUserObjectFromPref().userLocation.talukaIds[i].name
                    talukaList.add(customTaluka)
                }
            }
        }

    }

    private fun clickListener() {
        binding.apply {

            etTaluka.setOnClickListener {
                if (talukaList.size > 0) {
                    val csdTaluka = CustomSpinnerDialogClass(
                        this@CreatePotentialDonor, this@CreatePotentialDonor,
                        "Select Taluka", talukaList, false
                    )
                    csdTaluka.show()
                    csdTaluka.window?.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                } else {
                    if (Util.isConnected(this@CreatePotentialDonor)) {
                        presenter?.getLocationData(
                            if (selectedDistrictId?.isNotEmpty() == true) selectedDistrictId!! else userDistrictIds,
                            Util.getUserObjectFromPref().jurisdictionTypeId,
                            Constants.JurisdictionLevelName.TALUKA_LEVEL
                        )
                    } else {
                        Util.showToast(
                            resources.getString(R.string.msg_no_network),
                            this@CreatePotentialDonor
                        )
                    }
                }
            }

            etVillage.setOnClickListener {
                val csdHostVillage = CustomSpinnerDialogClass(
                    this@CreatePotentialDonor, this@CreatePotentialDonor,
                    "Select Village", villageList, false
                )
                csdHostVillage.show()
                csdHostVillage.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

        }
    }

    fun showJurisdictionLevel(data: List<JurisdictionLocationV3>?, levelName: String?) {
        when (levelName) {
            Constants.JurisdictionLevelName.DISTRICT_LEVEL -> if (!data.isNullOrEmpty()) {
                districtList.clear()
                var i = 0
                while (i < data.size) {
                    val location = data[i]
                    val meetCountry = CustomSpinnerObject()
                    meetCountry._id = location.id
                    meetCountry.name = location.name
                    meetCountry.isSelected = false
                    districtList.add(meetCountry)
                    i++
                }
                val csdDisttrict = CustomSpinnerDialogClass(
                    this, this,
                    "Select District", districtList, false
                )
                csdDisttrict.show()
                csdDisttrict.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            Constants.JurisdictionLevelName.TALUKA_LEVEL -> if (!data.isNullOrEmpty()) {
                talukaList.clear()
                var i = 0
                while (i < data.size) {
                    val location = data[i]
                    val meetCountry = CustomSpinnerObject()
                    meetCountry._id = location.id
                    meetCountry.name = location.name
                    meetCountry.isSelected = false
                    talukaList.add(meetCountry)
                    i++
                }
                val csdTaluka = CustomSpinnerDialogClass(
                    this, this,
                    "Select Taluka", talukaList, false
                )
                csdTaluka.show()
                csdTaluka.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            Constants.JurisdictionLevelName.VILLAGE_LEVEL -> if (!data.isNullOrEmpty()) {
                villageList.clear()
                var i = 0
                while (i < data.size) {
                    val location = data[i]
                    val meetCountry = CustomSpinnerObject()
                    meetCountry._id = location.id
                    meetCountry.name = location.name
                    meetCountry.isSelected = false
                    villageList.add(meetCountry)
                    i++
                }
            }
        }
    }

    private fun uploadData(
        donor: RWBDonor,
        btSave: MaterialButton,
        progress: LinearProgressIndicator,
        bottomSheetDialog: BottomSheetDialog
    ) {
        btSave.isEnabled = false
        progress.isVisible = true
        Log.d("request -", Gson().toJson(donor))

        val upload_URL = BuildConfig.BASE_URL + Urls.SSModule.INSERT_RWB_DONORS

        val volleyMultipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(
            Method.POST, upload_URL,
            Response.Listener<NetworkResponse> { response ->
                btSave.isEnabled = true
                progress.visibility = View.INVISIBLE
                rQueue?.cache?.clear()
                try {
                    val jsonString = String(
                        response.data, charset(HttpHeaderParser.parseCharset(response.headers))
                    )
                    val commonResponse = Gson().fromJson(
                        jsonString, CommonResponse::class.java
                    )
                    Log.d("response -", jsonString)
                    if (commonResponse.status == 200) {
                        Toast.makeText(this, commonResponse.message, Toast.LENGTH_SHORT).show()
                        bottomSheetDialog.dismiss()
                    } else {
                        Toast.makeText(this, commonResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                btSave.isEnabled = true
                progress.visibility = View.INVISIBLE
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["formData"] = Gson().toJson(donor)
                params["imageArraySize"] = "0" //add string parameters
                Log.d("TAG", "Donor Form params: $params")
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

            /* override fun getByteData(): Map<String, DataPart> {
                 val params: MutableMap<String, DataPart> = HashMap()
                 var drawable: Drawable? = null
                 val myVeryOwnIterator: Iterator<*> = imageHashmap.keys.iterator()
                 for (i in 0 until imageHashmap.size) {
                     val key = myVeryOwnIterator.next() as String
                     drawable = BitmapDrawable(resources, imageHashmap.get(key))
                     params[key] = DataPart(
                         key, getFileDataFromDrawable(drawable),
                         "image/jpeg"
                     )
                     Log.d("TAG", "getByteData: $params")
                 }
                 return params
             }*/
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

    override fun onCustomSpinnerSelection(type: String?) {
        when (type) {
            /*"Select State" -> {
                for (obj in stateList) {
                    if (obj.isSelected) {
                        selectedState = obj.name
                        selectedStateId = obj._id
                        break
                    }
                }
                etState.setText(selectedState)
                etDistrict.setText("")
                selectedDistrict = ""
                selectedDistrictId = ""
                etTaluka.setText("")
                selectedTaluka = ""
                selectedTalukaId = ""
                districtList.clear()
            }*/

            /* "Select District" -> {
                 for (obj in districtList) {
                     if (obj.isSelected) {
                         selectedDistrict = obj.name
                         selectedDistrictId = obj._id
                         break
                     }
                 }
                 etDistrict.setText(selectedDistrict)
                 etTaluka.setText("")
                 selectedTaluka = ""
                 selectedTalukaId = ""
                 talukaList.clear()
                 villageList.clear()
             }*/

            "Select Taluka" -> {
                for (obj in talukaList) {
                    if (obj.isSelected) {
                        selectedTaluka = obj.name
                        selectedTalukaId = obj._id
                        break
                    }
                }
                binding.etTaluka.setText(selectedTaluka)
                binding.etVillage.setText("")
            }
        }

    }


}