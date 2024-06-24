package com.example.storie.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storie.core.utils.DateConstant.MONTH_DAY_YEAR_TIME_HOUR_MINUTE
import com.example.storie.core.utils.parseFormatDate
import com.example.storie.data.local.entity.StoryEntity
import com.example.storie.databinding.ItemStoryBinding

class StoriesPagingAdapter :
    PagingDataAdapter<StoryEntity, StoriesPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {
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
        if (item != null) {
            holder.bind(
                item,
                onItemClickListener
            )
        }

    }

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoryEntity, onItemClickListener: OnItemClickListener) {
            binding.apply {
                tvItemName.text = item.name
                tvDescStory.text = item.description
                tvTimestampStory.text =
                    item.createdAt?.parseFormatDate(toFormat = MONTH_DAY_YEAR_TIME_HOUR_MINUTE)
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .into(ivItemPhoto)
                root.setOnClickListener {
                    onItemClickListener.onItemClick(item.id)
                }
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}