package com.octopusbjsindia.presenter

import android.util.Log
import com.android.volley.VolleyError
import com.google.gson.GsonBuilder
import com.octopusbjsindia.BuildConfig
import com.octopusbjsindia.listeners.APIPresenterListener
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonorApiResponse
import com.octopusbjsindia.request.APIRequestCall
import com.octopusbjsindia.utility.PlatformGson
import com.octopusbjsindia.utility.Urls
import com.octopusbjsindia.view.activities.DonationRecordActivity
import java.lang.ref.WeakReference

class RWBDonationRecordPresenter(activity: DonationRecordActivity): APIPresenterListener {

    private var activityWeakReference: WeakReference<DonationRecordActivity>? = null
    private val TAG = RWBDonationRecordPresenter::class.java.name

    companion object {
        const val GET_DISTRICT_DONOR = "getDistrictDonors"
    }

    init {
        activityWeakReference = WeakReference(activity)
    }

    fun getDistrictDonorsList(districtId: String, structureId: String) {
        val gson = GsonBuilder().create()
        activityWeakReference!!.get()?.showProgressBar()
        val map = HashMap<String, String>()
        map["district_id"] = districtId
        map["structure_id"] = structureId
        val paramJson = gson.toJson(map)
        val getDonorListUrl = (BuildConfig.BASE_URL + String.format(Urls.SSModule.GET_RWB_DONORS))
        Log.d(TAG, "donor list url: $getDonorListUrl")
        val requestCall = APIRequestCall()
        requestCall.setApiPresenterListener(this)
        requestCall.postDataApiCall(
            RWBDonationRecordPresenter.GET_DISTRICT_DONOR,
            paramJson,
            getDonorListUrl
        )
    }

    override fun onFailureListener(requestID: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onErrorListener(requestID: String?, error: VolleyError?) {
        TODO("Not yet implemented")
    }

    override fun onSuccessListener(requestID: String?, response: String?) {
        if (activityWeakReference == null) {
            return
        }
        activityWeakReference?.get()?.hideProgressBar()

        try {
            if (response != null) {
                if (requestID.equals(GET_DISTRICT_DONOR, ignoreCase = true)) {
                    val districtDonorListData = PlatformGson.getPlatformGsonInstance().fromJson(
                        response,
                        RWBDonorApiResponse::class.java
                    )
                    if (districtDonorListData.code == 200) {
                        activityWeakReference?.get()
                            ?.populateDistrictDonorList(requestID, districtDonorListData)
                    } else if (districtDonorListData.code == 400) {
                        activityWeakReference?.get()?.showNoDataMessage()
                    }

                }
            }
        } catch (e: Exception) {
            activityWeakReference?.get()?.onFailureListener(requestID, e.message)
        }

    }

    fun clearData() {
        activityWeakReference = null
    }
}