package com.octopusbjsindia.view.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.octopusbjsindia.databinding.ImagesListItemBinding

class ImageListAdapter(
    private val imageHashList: List<Bitmap>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ImagesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = imageHashList[position]

        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return imageHashList.size
    }

    inner class ViewHolder(private val binding: ImagesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap) {
            binding.apply {

                image.setImageBitmap(bitmap)

                close.setOnClickListener {
                    listener.onRemoveClicked(adapterPosition)
                }

            }
        }
    }

    interface OnItemClickListener {
        fun onRemoveClicked(position: Int)
    }

}