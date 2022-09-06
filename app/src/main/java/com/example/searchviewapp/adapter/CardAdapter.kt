package com.example.searchviewapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.searchviewapp.databinding.CardViewBinding
import com.example.searchviewapp.network.model.MatchData

class CardAdapter: ListAdapter<MatchData, CardAdapter.CardViewHolder>(DiffCallback) {


    class CardViewHolder(private var binding: CardViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(matchData: MatchData) {
        binding.matchData = matchData
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<MatchData>() {
        override fun areItemsTheSame(oldItem: MatchData, newItem: MatchData): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: MatchData, newItem: MatchData): Boolean {
            TODO("Not yet implemented")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(CardViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val matchData = getItem(position)
        holder.bind(matchData)
    }
}










