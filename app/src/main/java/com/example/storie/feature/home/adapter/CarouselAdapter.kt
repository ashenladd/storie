package com.example.storie.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storie.databinding.ItemCarouselBinding
import com.example.storie.domain.model.CarouselModel

class CarouselAdapter : ListAdapter<CarouselModel, CarouselAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCarouselBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
        )

    }

    class MyViewHolder(private val binding: ItemCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarouselModel) {
            binding.apply {
                tvHeadline.text = item.title
                ivCarousel.setImageResource(item.imageAsset)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CarouselModel>() {
            override fun areItemsTheSame(oldItem: CarouselModel, newItem: CarouselModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CarouselModel,
                newItem: CarouselModel,
            ): Boolean {
                return oldItem.imageAsset == newItem.imageAsset
            }
        }
    }


}