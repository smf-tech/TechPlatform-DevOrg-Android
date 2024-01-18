package com.octopusbjsindia.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.octopusbjsindia.R
import com.octopusbjsindia.databinding.FragmentProspectDonorBinding

class ProspectDonorFragment : Fragment() {

    private var _binding: FragmentProspectDonorBinding? = null
    private val binding get() = _binding!!

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

        //todo setup recycler view

        binding.fabAdd.setOnClickListener{
            showDonorFormBottomSheet()
        }

    }

    private fun showDonorFormBottomSheet(){
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheet = LayoutInflater.from(context).inflate(R.layout.bs_prospect_donor_form, null)

        val close = bottomSheet.findViewById<ImageView>(R.id.ic_close)
        val submit = bottomSheet.findViewById<MaterialButton>(R.id.bt_submit)
        val etFullName = bottomSheet.findViewById<TextInputEditText>(R.id.et_full_name)

        submit.setOnClickListener {
           //todo
            bottomSheetDialog.dismiss()
        }

        close.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setContentView(bottomSheet)
        bottomSheetDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}