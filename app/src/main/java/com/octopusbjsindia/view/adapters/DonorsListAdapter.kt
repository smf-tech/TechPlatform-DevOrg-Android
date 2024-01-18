package com.octopusbjsindia.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.octopusbjsindia.databinding.DonorsListItemBinding
import com.octopusbjsindia.models.SujalamSuphalam.RWBDonor

class DonorsListAdapter(
    private val listener: OnItemClickListener,
    private val showOptions: Boolean = true
) : ListAdapter<RWBDonor, DonorsListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DonorsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: DonorsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RWBDonor) {
            binding.apply {

                txtName.text = "${item.fullName} | ${item.talukaName}"
                if (item.emailId.isNullOrBlank()) {
                    txtMobile.text = item.mobileNumber
                } else txtMobile.text = "${item.mobileNumber} | ${item.emailId}"
                textFirm.text = item.companyFirmName

                options.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onOptionBtnClick(item, it)
                    }
                }

                options.isVisible = showOptions
            }
        }
    }

    interface OnItemClickListener {
        fun onOptionBtnClick(item: RWBDonor, view: View)
    }

    class DiffCallback : DiffUtil.ItemCallback<RWBDonor>() {
        override fun areItemsTheSame(oldItem: RWBDonor, newItem: RWBDonor) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RWBDonor, newItem: RWBDonor) =
            oldItem == newItem
    }
}