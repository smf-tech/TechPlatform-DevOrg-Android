package com.octopusbjsindia.presenter

import android.util.Log
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.octopusbjsindia.BuildConfig
import com.octopusbjsindia.listeners.APIPresenterListener
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonorApiResponse
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse
import com.octopusbjsindia.request.APIRequestCall
import com.octopusbjsindia.utility.Constants
import com.octopusbjsindia.utility.PlatformGson
import com.octopusbjsindia.utility.Urls
import com.octopusbjsindia.view.activities.CreatePotentialDonor
import org.json.JSONObject
import java.lang.ref.WeakReference

class CreatePotentialDonorActivityPresenter(fragment: CreatePotentialDonor) : APIPresenterListener {

    private var fragmentWeakReference: WeakReference<CreatePotentialDonor>? = null
    private val TAG = CreatePotentialDonorActivityPresenter::class.java.name

    companion object {
        const val KEY_SELECTED_ID = "selected_location_id"
        const val KEY_JURISDICTION_TYPE_ID = "jurisdictionTypeId"
        const val KEY_LEVEL = "jurisdictionLevel"
        const val GET_DISTRICT = "getDistrict"
        const val GET_TALUKA = "getTaluka"
        const val GET_VILLAGE = "getVillage"
    }

    init {
        fragmentWeakReference = WeakReference(fragment)
    }

    fun getLocationData(
        selectedLocationId: String,
        jurisdictionTypeId: String,
        levelName: String
    ) {
        val map = HashMap<String, String>()
        map[KEY_SELECTED_ID] = selectedLocationId
        map[KEY_JURISDICTION_TYPE_ID] = jurisdictionTypeId
        map[KEY_LEVEL] = levelName
        fragmentWeakReference?.get()?.showProgressBar()
        val getLocationUrl = (BuildConfig.BASE_URL + String.format(Urls.Profile.GET_LOCATION_DATA))
        Log.d(TAG, "getLocationUrl: url$getLocationUrl")

        val requestCall = APIRequestCall()
        requestCall.setApiPresenterListener(this)
        if (levelName.equals(Constants.JurisdictionLevelName.DISTRICT_LEVEL, ignoreCase = true)) {
            requestCall.postDataApiCall(
                GET_DISTRICT,
                JSONObject(map as Map<String, String>).toString(),
                getLocationUrl
            )
        } else if (levelName.equals(
                Constants.JurisdictionLevelName.TALUKA_LEVEL,
                ignoreCase = true
            )
        ) {
            requestCall.postDataApiCall(
                GET_TALUKA,
                JSONObject(map as Map<String, String>).toString(),
                getLocationUrl
            )
        } else if (levelName.equals(
                Constants.JurisdictionLevelName.VILLAGE_LEVEL,
                ignoreCase = true
            )
        ) {
            requestCall.postDataApiCall(
                GET_VILLAGE,
                JSONObject(map as Map<String, String>).toString(),
                getLocationUrl
            )
        }
    }

    override fun onFailureListener(requestID: String?, message: String?) {
        if (fragmentWeakReference != null && fragmentWeakReference?.get() != null) {
            fragmentWeakReference?.get()?.hideProgressBar()
            fragmentWeakReference?.get()?.onFailureListener(requestID, message)
        }
    }

    override fun onErrorListener(requestID: String?, error: VolleyError?) {
        if (fragmentWeakReference != null && fragmentWeakReference?.get() != null) {
            fragmentWeakReference?.get()?.hideProgressBar()
            if (error != null) {
                fragmentWeakReference?.get()?.onErrorListener(requestID, error)
            }
        }
    }

    override fun onSuccessListener(requestID: String?, response: String?) {
        if (fragmentWeakReference == null) {
            return
        }
        fragmentWeakReference?.get()?.hideProgressBar()

        try {
            if (response != null) {

                /*val commentResponse = PlatformGson.getPlatformGsonInstance()
                    .fromJson(response, CommentResponse::class.java)
                if (commentResponse.status == 1000) {
                    Util.logOutUser(fragmentWeakReference?.get()?.activity)
                    return
                }*/

               if (requestID.equals(GET_DISTRICT, ignoreCase = true) ||
                    requestID.equals(GET_TALUKA, ignoreCase = true) ||
                    requestID.equals(GET_VILLAGE, ignoreCase = true)) {

                    val jurisdictionLevelResponse = Gson().fromJson(response,
                        JurisdictionLevelResponse::class.java)

                    if (jurisdictionLevelResponse != null &&
                        jurisdictionLevelResponse.data != null &&
                        jurisdictionLevelResponse.data.isNotEmpty() &&
                        jurisdictionLevelResponse.data.size > 0) {
                        if (requestID.equals(GET_DISTRICT, ignoreCase = true)) {
                            fragmentWeakReference?.get()?.showJurisdictionLevel(
                                jurisdictionLevelResponse.data,
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL
                            )
                        } else if (requestID.equals(GET_TALUKA, ignoreCase = true)) {
                            fragmentWeakReference?.get()?.showJurisdictionLevel(
                                jurisdictionLevelResponse.data,
                                Constants.JurisdictionLevelName.TALUKA_LEVEL
                            )
                        } else if (requestID.equals(GET_VILLAGE, ignoreCase = true)) {
                            fragmentWeakReference?.get()?.showJurisdictionLevel(
                                jurisdictionLevelResponse.data,
                                Constants.JurisdictionLevelName.VILLAGE_LEVEL
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            fragmentWeakReference?.get()?.onFailureListener(requestID, e.message)
        }


    }
}