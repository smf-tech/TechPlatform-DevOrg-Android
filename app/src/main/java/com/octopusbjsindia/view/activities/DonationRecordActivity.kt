package com.octopusbjsindia.view.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.VolleyError
import com.octopusbjsindia.databinding.ActivityDonationRecordBinding
import com.octopusbjsindia.listeners.APIDataListener
import com.octopusbjsindia.listeners.CustomSpinnerListener
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonorApiResponse
import com.octopusbjsindia.presenter.RWBDonationRecordPresenter
import com.octopusbjsindia.utility.Util

class DonationRecordActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDonationRecordBinding

class DonationRecordActivity: AppCompatActivity(), APIDataListener, CustomSpinnerListener {

    private var _binding: ActivityDonationRecordBinding? = null
    private val binding get() = _binding!!
    private var presenter: RWBDonationRecordPresenter? = null
    private var structureDistrictId: String? = null
    private var structureDistrict: String? = null
    private var structureId: String? = null

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

        presenter = RWBDonationRecordPresenter(this)
        presenter?.getDistrictDonorsList(structureDistrictId!!, structureId!!)

    }

    fun populateDistrictDonorList(requestID: String?, data: RWBDonorApiResponse?) {
        if (data != null) {

        } else {
            showNoDataMessage()
        }
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
        TODO("Not yet implemented")
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

    override fun onDestroy() {
        super.onDestroy()
        if (presenter != null) {
            presenter?.clearData()
            presenter = null
        }
    }

    override fun onCustomSpinnerSelection(type: String?) {
        TODO("Not yet implemented")
    }

}