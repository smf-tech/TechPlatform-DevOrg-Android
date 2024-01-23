package com.octopusbjsindia.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.octopusbjsindia.BuildConfig
import com.octopusbjsindia.R
import com.octopusbjsindia.databinding.FragmentProspectDonorBinding
import com.octopusbjsindia.listeners.APIDataListener
import com.octopusbjsindia.listeners.CustomSpinnerListener
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonor
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonorApiResponse
import com.octopusbjsindia.models.common.CustomSpinnerObject
import com.octopusbjsindia.models.events.CommonResponse
import com.octopusbjsindia.models.profile.JurisdictionLocationV3
import com.octopusbjsindia.presenter.RWBProspectDonorsPresenter
import com.octopusbjsindia.utility.Constants
import com.octopusbjsindia.utility.Urls
import com.octopusbjsindia.utility.Util
import com.octopusbjsindia.utility.VolleyMultipartRequest
import com.octopusbjsindia.view.activities.CreatePotentialDonor
import com.octopusbjsindia.view.activities.DonorsActivity.Companion.DONOR_TYPE_PROSPECT
import com.octopusbjsindia.view.adapters.DonorsListAdapter
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass
import java.io.UnsupportedEncodingException

class ProspectDonorFragment : Fragment(), DonorsListAdapter.OnItemClickListener, APIDataListener,
    CustomSpinnerListener {

    private var _binding: FragmentProspectDonorBinding? = null
    private val binding get() = _binding!!

    private val donorAdapter: DonorsListAdapter by lazy {
        DonorsListAdapter(this)
    }

    private var presenter: RWBProspectDonorsPresenter? = null

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

    private var etTaluka: TextInputEditText? = null
    private var etDistrict: TextInputEditText? = null

    private var rQueue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProspectDonorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        presenter = RWBProspectDonorsPresenter(this)

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = donorAdapter

            binding.fabAdd.setOnClickListener {
                showDonorFormBottomSheet()
            }
        }

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
            //etState.setText(selectedState)
            selectedStateId = Util.getUserObjectFromPref().userLocation.stateId[0].id
        }
        if (Util.getUserObjectFromPref().userLocation.districtIds != null &&
            Util.getUserObjectFromPref().userLocation.districtIds.size > 0
        ) {
            selectedDistrict = Util.getUserObjectFromPref().userLocation.districtIds[0].name
            etDistrict?.setText(selectedDistrict)
            selectedDistrictId = Util.getUserObjectFromPref().userLocation.districtIds[0].id
        }
        if (Util.getUserObjectFromPref().userLocation.talukaIds != null &&
            Util.getUserObjectFromPref().userLocation.talukaIds.size > 0
        ) {
            selectedTaluka = Util.getUserObjectFromPref().userLocation.talukaIds[0].name
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

    override fun onResume() {
        super.onResume()
        presenter?.getDonorsList(DONOR_TYPE_PROSPECT)
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
                    activity, this,
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
                    activity, this,
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


    private fun showDonorFormBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheet =
            LayoutInflater.from(context).inflate(R.layout.bs_prospect_donor_form, null)

        val close = bottomSheet.findViewById<ImageView>(R.id.ic_close)
        val submit = bottomSheet.findViewById<MaterialButton>(R.id.bt_submit)
        val progress = bottomSheet.findViewById<LinearProgressIndicator>(R.id.linear_progress)
        val txtState = bottomSheet.findViewById<TextView>(R.id.txtState)
        etDistrict = bottomSheet.findViewById<TextInputEditText>(R.id.et_district)
        etTaluka = bottomSheet.findViewById<TextInputEditText>(R.id.et_taluka)
        val etFullName = bottomSheet.findViewById<TextInputEditText>(R.id.et_full_name)
        val etEmail = bottomSheet.findViewById<TextInputEditText>(R.id.et_email)
        val etFirmName = bottomSheet.findViewById<TextInputEditText>(R.id.et_firm_name)
        val etMobileNumber = bottomSheet.findViewById<TextInputEditText>(R.id.et_mobile_number)

        txtState.text = selectedState
        etDistrict?.setText(selectedDistrict)
        etTaluka?.setText(selectedTaluka)

        //only clcik if has mutiple dist
        if (Util.getUserObjectFromPref().userLocation.districtIds.size > 1) {
            etDistrict?.setOnClickListener {
                if (districtList.size > 0) {
                    val csdDistrict = CustomSpinnerDialogClass(
                        activity, this,
                        "Select District", districtList, false
                    )
                    csdDistrict.show()
                    csdDistrict.window!!.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                } else {
                    if (Util.isConnected(activity)) {
                        presenter!!.getLocationData(
                            (if (!TextUtils.isEmpty(selectedStateId)) selectedStateId else userStateIds)!!,
                            Util.getUserObjectFromPref().jurisdictionTypeId,
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL
                        )
                    } else {
                        Util.showToast(resources.getString(R.string.msg_no_network), this)
                    }
                }
            }
        }

        etTaluka?.setOnClickListener {
            if (talukaList.size > 0) {
                val csdTaluka = CustomSpinnerDialogClass(
                    requireActivity(), this,
                    "Select Taluka", talukaList, false
                )
                csdTaluka.show()
                csdTaluka.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            } else {
                if (Util.isConnected(context)) {
                    presenter?.getLocationData(
                        if (selectedDistrictId?.isNotEmpty() == true) selectedDistrictId!! else userDistrictIds,
                        Util.getUserObjectFromPref().jurisdictionTypeId,
                        Constants.JurisdictionLevelName.TALUKA_LEVEL
                    )
                } else {
                    Util.showToast(resources.getString(R.string.msg_no_network), context)
                }
            }
        }

        submit.setOnClickListener {
            if (selectedTaluka.isNullOrBlank() || selectedTalukaId.isNullOrBlank()) {
                Snackbar.make(
                    bottomSheetDialog.window?.decorView!!,
                    "Please select Taluka",
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(it).show()
            } else if (etFullName.text.isNullOrBlank()) {
                Snackbar.make(
                    bottomSheetDialog.window?.decorView!!,
                    "Please enter full name of donor",
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(it).show()
            } else if (etMobileNumber.text.isNullOrBlank() || etMobileNumber.text.toString().length < 10) {
                Snackbar.make(
                    bottomSheetDialog.window?.decorView!!,
                    "Please enter valid mobile number",
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(it).show()
            } else if (etEmail.text.toString().isNotBlank() &&
                !Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
                Snackbar.make(
                    bottomSheetDialog.window?.decorView!!,
                    "Please enter valid email",
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(it).show()
            } else {
                // submit data
                // multipart post api
                val prospectDonor = RWBDonor()
                prospectDonor.donorType = DONOR_TYPE_PROSPECT
                prospectDonor.stateName = selectedState
                prospectDonor.stateId =
                    Util.getUserObjectFromPref().userLocation.stateId[0].id //todo check this
                prospectDonor.districtName = selectedDistrict
                prospectDonor.districtId = selectedDistrictId
                prospectDonor.talukaName = selectedTaluka
                prospectDonor.talukaId = selectedTalukaId
                prospectDonor.fullName = etFullName.text.toString()
                prospectDonor.emailId = etEmail.text.toString()
                prospectDonor.mobileNumber = etMobileNumber.text.toString()
                prospectDonor.companyFirmName = etFirmName.text.toString()

                uploadData(prospectDonor, submit, progress, bottomSheetDialog)
            }
        }

        close.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setContentView(bottomSheet)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
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
                        Toast.makeText(context, commonResponse.message, Toast.LENGTH_SHORT).show()
                        bottomSheetDialog.dismiss()
                        presenter?.getDonorsList(DONOR_TYPE_PROSPECT)
                    } else {
                        Toast.makeText(context, commonResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                btSave.isEnabled = true
                progress.visibility = View.INVISIBLE
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
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
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        rQueue = Volley.newRequestQueue(context)
        rQueue?.add(volleyMultipartRequest)
    }


    fun populateProspectDonorList(requestID: String?, data: RWBDonorApiResponse?) {
        if (data != null) {
            //sort list alphabetically
            val sortedList : List<RWBDonor> = data.data.sortedBy {
                it.fullName.toString()
            }

            binding.lyNoData.isVisible = false
            donorAdapter.submitList(sortedList)
        } else {
            showNoDataMessage()
        }
    }

    fun showNoDataMessage() {
        donorAdapter.submitList(null)
        binding.lyNoData.isVisible = true
    }


    override fun onDetach() {
        super.onDetach()
        if (presenter != null) {
            presenter?.clearData()
            presenter = null
        }
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
        activity?.runOnUiThread {
            binding?.apply {
                lytProgressBar?.apply {
                    isVisible = true
                }
            }
        }
    }

    override fun hideProgressBar() {
        activity?.runOnUiThread {
            binding?.apply {
                lytProgressBar?.apply {
                    isVisible = false
                }
            }
        }
    }

    override fun closeCurrentActivity() {
        activity?.finish()
    }

    override fun onOptionBtnClick(item: RWBDonor, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_rwb_donors, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_mark_potential_donor) {
                val intent = Intent(activity, CreatePotentialDonor::class.java)
                intent.putExtra("id", item.id)
                intent.putExtra("taluka", item.talukaName)
                intent.putExtra("talukaId", item.talukaId)
                intent.putExtra("name", item.fullName)
                intent.putExtra("email", item.emailId)
                intent.putExtra("mobile", item.mobileNumber)
                intent.putExtra("firm_name", item.companyFirmName)
                activity?.startActivity(intent)
            }
            true
        }
        popupMenu.show()
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

             "Select District" -> {
                 for (obj in districtList) {
                     if (obj.isSelected) {
                         selectedDistrict = obj.name
                         selectedDistrictId = obj._id
                         break
                     }
                 }
                 etDistrict?.setText(selectedDistrict)
                 etTaluka?.setText("")
                 selectedTaluka = ""
                 selectedTalukaId = ""
                 talukaList.clear()
             }

            "Select Taluka" -> {
                for (obj in talukaList) {
                    if (obj.isSelected) {
                        selectedTaluka = obj.name
                        selectedTalukaId = obj._id
                        break
                    }
                }
                etTaluka?.setText(selectedTaluka)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}