package com.example.storie.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storie.core.utils.DateConstant.MONTH_DAY_YEAR_TIME_HOUR_MINUTE
import com.example.storie.core.utils.parseFormatDate
import com.example.storie.databinding.ItemStoryBinding
import com.example.storie.domain.model.StoryModel

class StoriesAdapter : ListAdapter<StoryModel, StoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(
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
            onItemClickListener
        )

    }

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoryModel, onItemClickListener: OnItemClickListener) {
            binding.apply {
                tvItemName.text = item.name
                tvDescStory.text = item.description
                tvTimestampStory.text = item.createdAt.parseFormatDate(toFormat = MONTH_DAY_YEAR_TIME_HOUR_MINUTE)
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .into(ivItemPhoto)
                root.setOnClickListener {
                    onItemClickListener.onItemClick(item.id)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryModel,
                newItem: StoryModel,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

}