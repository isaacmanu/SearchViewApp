package com.isaacmanu.tftstatsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.isaacmanu.tftstatsapp.databinding.CardViewBinding
import com.isaacmanu.tftstatsapp.network.model.RecyclerViewItem
/*
ListAdapter which takes a list of RecyclerViewItem
 */
class CardAdapter: ListAdapter<RecyclerViewItem, CardAdapter.CardViewHolder>(DiffCallback) {


    class CardViewHolder(private var binding: CardViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recyclerViewItem: RecyclerViewItem) {
            binding.recyclerViewItem = recyclerViewItem

            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<RecyclerViewItem>() {
        override fun areItemsTheSame(oldItem: RecyclerViewItem, newItem: RecyclerViewItem): Boolean {
            return oldItem.matchData.metadata.match_id == newItem.matchData.metadata.match_id
        }

        override fun areContentsTheSame(oldItem: RecyclerViewItem, newItem: RecyclerViewItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(CardViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val recyclerViewItem = getItem(position)
        holder.bind(recyclerViewItem)
    }
}










