package com.example.skillexchangeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillexchangeapp.R
import com.example.skillexchangeapp.data.local.entity.NeedPost
import com.example.skillexchangeapp.databinding.ItemNeedBinding

class NeedAdapter(private val onItemClick: (NeedPost) -> Unit) :
    ListAdapter<NeedPost, NeedAdapter.NeedViewHolder>(NeedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeedViewHolder {
        val binding = ItemNeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NeedViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class NeedViewHolder(private val binding: ItemNeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NeedPost) {
            val context = binding.root.context
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description.ifBlank { "No description added yet." }
            binding.tvSkill.text = item.skillRequired
            binding.tvHours.text = context.getString(R.string.skill_points_format, item.estimatedHours)
            binding.tvUrgency.text = item.urgencyLevel.ifBlank { "Normal" }
            binding.tvLocation.text = item.location.ifBlank { "Village area" }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class NeedDiffCallback : DiffUtil.ItemCallback<NeedPost>() {
        override fun areItemsTheSame(oldItem: NeedPost, newItem: NeedPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NeedPost, newItem: NeedPost): Boolean {
            return oldItem == newItem
        }
    }
}
