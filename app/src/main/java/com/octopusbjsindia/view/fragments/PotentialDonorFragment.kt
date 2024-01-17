package com.octopusbjsindia.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.octopusbjsindia.R
import com.octopusbjsindia.databinding.FragmentPotentialDonorBinding
import com.octopusbjsindia.databinding.FragmentProspectDonorBinding

class PotentialDonorFragment : Fragment() {

    private var _binding: FragmentPotentialDonorBinding? = null
    private val binding get() = _binding!!

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

        //todo


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}