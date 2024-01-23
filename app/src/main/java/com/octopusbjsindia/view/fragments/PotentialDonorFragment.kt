package com.octopusbjsindia.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyError
import com.octopusbjsindia.R
import com.octopusbjsindia.databinding.FragmentPotentialDonorBinding
import com.octopusbjsindia.databinding.FragmentProspectDonorBinding
import com.octopusbjsindia.listeners.APIDataListener
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonor
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonorApiResponse
import com.octopusbjsindia.presenter.RWBPotentialDonorsPresenter
import com.octopusbjsindia.presenter.RWBProspectDonorsPresenter
import com.octopusbjsindia.utility.Util
import com.octopusbjsindia.view.activities.DonorsActivity
import com.octopusbjsindia.view.adapters.DonorsListAdapter

class PotentialDonorFragment : Fragment(), DonorsListAdapter.OnItemClickListener, APIDataListener {

    private var _binding: FragmentPotentialDonorBinding? = null
    private val binding get() = _binding!!

    private val donorAdapter: DonorsListAdapter by lazy {
        DonorsListAdapter(this,false)
    }

    private var presenter: RWBPotentialDonorsPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPotentialDonorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = RWBPotentialDonorsPresenter(this)

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = donorAdapter
        }

    }

    override fun onResume() {
        super.onResume()

        presenter?.getDonorsList(DonorsActivity.DONOR_TYPE_POTENTIAL)
    }

    fun populatePotentialDonorList(requestID: String?, data: RWBDonorApiResponse?) {
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
        //do nothing
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}