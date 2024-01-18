package com.octopusbjsindia.view.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.octopusbjsindia.databinding.ActivityDonationRecordBinding
import com.octopusbjsindia.presenter.RWBDonationRecordPresenter

class DonationRecordActivity: AppCompatActivity() {

    private var _binding: ActivityDonationRecordBinding? = null
    private val binding get() = _binding!!

    private var presenter: RWBDonationRecordPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDonationRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        presenter = RWBDonationRecordPresenter(this)

    }

}