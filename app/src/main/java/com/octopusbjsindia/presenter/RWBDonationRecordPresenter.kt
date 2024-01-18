package com.octopusbjsindia.presenter

import com.android.volley.VolleyError
import com.octopusbjsindia.listeners.APIPresenterListener
import com.octopusbjsindia.view.activities.DonationRecordActivity
import java.lang.ref.WeakReference

class RWBDonationRecordPresenter(activity: DonationRecordActivity): APIPresenterListener {

    private var activityWeakReference: WeakReference<DonationRecordActivity>? = null
    private val TAG = RWBDonationRecordPresenter::class.java

    override fun onFailureListener(requestID: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onErrorListener(requestID: String?, error: VolleyError?) {
        TODO("Not yet implemented")
    }

    override fun onSuccessListener(requestID: String?, response: String?) {
        TODO("Not yet implemented")
    }


}