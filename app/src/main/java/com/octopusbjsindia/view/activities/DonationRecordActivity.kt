package com.octopusbjsindia.view.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.VolleyError
import com.octopusbjsindia.R
import com.octopusbjsindia.databinding.ActivityDonationRecordBinding
import com.octopusbjsindia.listeners.APIDataListener
import com.octopusbjsindia.listeners.CustomSpinnerListener
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonorApiResponse
import com.octopusbjsindia.presenter.RWBDonationRecordPresenter
import com.octopusbjsindia.utility.Util
import com.octopusbjsindia.utility.Util.hideKeyboard

class DonationRecordActivity : AppCompatActivity(), APIDataListener, CustomSpinnerListener {

    private lateinit var binding: ActivityDonationRecordBinding
    private var presenter: RWBDonationRecordPresenter? = null

    private var structureDistrictId: String? = null
    private var structureDistrict: String? = null
    private var structureId: String? = null
    private var structureCode: String? = null

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
                lytDonationCommitment.isVisible = !lytDonationCommitment.isVisible
                lytDonationDetail.isVisible = false
            }
            titleDonation.setOnClickListener {
                lytDonationDetail.isVisible = !lytDonationDetail.isVisible
                lytDonationCommitment.isVisible = false
            }

            etDate.setOnClickListener {

            }



            imgTransactionDetailCopy.setOnClickListener {

            }

            btCommitment.setOnClickListener {


            }

            btSubmitDonation.setOnClickListener {


            }


        }
    }

    fun populateDistrictDonorList(requestID: String?, data: RWBDonorApiResponse?) {
        if (data != null) {
            binding.lytDonationCommitment.isVisible = true
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

    }

}