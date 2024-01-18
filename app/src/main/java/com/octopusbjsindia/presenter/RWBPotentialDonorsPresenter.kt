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
import com.octopusbjsindia.view.fragments.PotentialDonorFragment
import java.lang.ref.WeakReference

class RWBPotentialDonorsPresenter(fragment: PotentialDonorFragment) : APIPresenterListener {

    private var fragmentWeakReference: WeakReference<PotentialDonorFragment>? = null
    private val TAG = RWBPotentialDonorsPresenter::class.java.name

    companion object {
        const val GET_POTENTIAL_DONOR = "get_potential_donors"
    }

    init {
        fragmentWeakReference = WeakReference(fragment)
    }

    fun clearData() {
        fragmentWeakReference = null
    }

    fun getDonorsList(donorType: String) {
        val gson = GsonBuilder().create()
        fragmentWeakReference!!.get()?.showProgressBar()
        val map = HashMap<String, String>()
        map["donor_type"] = donorType
        val paramJson = gson.toJson(map)
        val getDonorListUrl = (BuildConfig.BASE_URL + String.format(Urls.SSModule.GET_RWB_DONORS))
        Log.d(TAG, "donor list url: $getDonorListUrl")
        val requestCall = APIRequestCall()
        requestCall.setApiPresenterListener(this)
        requestCall.postDataApiCall(GET_POTENTIAL_DONOR, paramJson, getDonorListUrl)
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

                if (requestID.equals(GET_POTENTIAL_DONOR, ignoreCase = true)) {
                    val donorListData = PlatformGson.getPlatformGsonInstance().fromJson(
                        response,
                        RWBDonorApiResponse::class.java
                    )

                    if (donorListData.code == 200) {
                        fragmentWeakReference?.get()?.
                        populatePotentialDonorList(requestID, donorListData)
                    } else if (donorListData.code == 400) {
                        fragmentWeakReference?.get()?.showNoDataMessage()
                    }
                }
            }
        } catch (e: Exception) {
            fragmentWeakReference?.get()?.onFailureListener(requestID, e.message)
        }


    }
}